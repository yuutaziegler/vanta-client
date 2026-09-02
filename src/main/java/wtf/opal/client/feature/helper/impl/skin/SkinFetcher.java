/*
 * SkinFetcher - resolves any player skin/cape by username.
 *
 * Flow: name -> Mojang profile lookup -> session profile -> base64 texture payload
 *       -> download skin/cape png -> decode into a NativeImage -> register as a
 *         dynamic texture on the render thread.
 *
 * The render-thread registrations are queued and drained from PlayerListEntryMixin
 * (which always runs on the render thread while players are rendered).
 */
package wtf.opal.client.feature.helper.impl.skin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1011;
import net.minecraft.class_1043;
import net.minecraft.class_2960;
import wtf.opal.client.Constants;

@Environment(value=EnvType.CLIENT)
public final class SkinFetcher {

    public static final class SkinData {
        public volatile class_2960 skinTexture;
        public volatile class_2960 capeTexture;
        public volatile boolean failed;
    }

    private static final Map<String, SkinData> CACHE = new ConcurrentHashMap<>();
    private static final Map<String, Long> LAST_ATTEMPT = new ConcurrentHashMap<>();
    private static final ConcurrentLinkedQueue<Runnable> PENDING_GPU_WORK = new ConcurrentLinkedQueue<Runnable>();
    private static final long RETRY_COOLDOWN_MS = 60_000L;

    private SkinFetcher() {
    }

    /** Returns (and lazily starts resolving) the skin data for a player name. */
    public static SkinData request(String rawName) {
        String name = sanitize(rawName);
        if (name.isEmpty()) {
            return null;
        }
        SkinData data = CACHE.get(name);
        if (data == null) {
            data = new SkinData();
            SkinData previous = CACHE.putIfAbsent(name, data);
            if (previous != null) {
                data = previous;
            } else {
                startFetch(name, data);
            }
        } else if (data.failed) {
            Long last = LAST_ATTEMPT.get(name);
            if (last == null || System.currentTimeMillis() - last.longValue() > RETRY_COOLDOWN_MS) {
                data.failed = false;
                startFetch(name, data);
            }
        }
        return data;
    }

    public static class_2960 getSkinTexture(String rawName) {
        SkinData data = CACHE.get(sanitize(rawName));
        return data == null ? null : data.skinTexture;
    }

    public static class_2960 getCapeTexture(String rawName) {
        SkinData data = CACHE.get(sanitize(rawName));
        return data == null ? null : data.capeTexture;
    }

    /** Must be called on the render thread; registers queued textures. */
    public static void drainPending() {
        Runnable work;
        int budget = 4;
        while (budget-- > 0 && (work = PENDING_GPU_WORK.poll()) != null) {
            try {
                work.run();
            } catch (Throwable ignored) {
                // never let texture registration break rendering
            }
        }
    }

    private static String sanitize(String name) {
        if (name == null) {
            return "";
        }
        return name.trim().toLowerCase(Locale.ROOT);
    }

    private static void startFetch(final String name, final SkinData data) {
        LAST_ATTEMPT.put(name, Long.valueOf(System.currentTimeMillis()));
        Thread thread = new Thread(() -> {
            try {
                String uuid = lookupUuid(name);
                if (uuid == null) {
                    data.failed = true;
                    return;
                }
                JsonObject textures = lookupTextures(uuid);
                if (textures == null) {
                    data.failed = true;
                    return;
                }
                String skinUrl = textureUrl(textures, "SKIN");
                String capeUrl = textureUrl(textures, "CAPE");
                if (skinUrl == null && capeUrl == null) {
                    data.failed = true;
                    return;
                }
                if (skinUrl != null) {
                    final byte[] skinBytes = download(skinUrl);
                    if (skinBytes != null) {
                        queueTextureRegistration("skin-" + name, skinBytes, data, true);
                    }
                }
                if (capeUrl != null) {
                    final byte[] capeBytes = download(capeUrl);
                    if (capeBytes != null) {
                        queueTextureRegistration("cape-" + name, capeBytes, data, false);
                    }
                }
            } catch (Throwable t) {
                data.failed = true;
            }
        }, "TerentX-SkinFetcher");
        thread.setDaemon(true);
        thread.start();
    }

    private static void queueTextureRegistration(final String key, final byte[] pngBytes, final SkinData data, final boolean isSkin) {
        PENDING_GPU_WORK.add(() -> {
            try {
                class_1011 image = class_1011.method_4303(class_1011.class_1012.field_4997, ByteBuffer.wrap(pngBytes));
                class_1043 texture = new class_1043(() -> "terentx/" + key, image);
                final class_2960 id = class_2960.method_60655("terentx", "skins/" + key + ".png");
                Constants.mc.method_1531().method_4616(id, texture);
                if (isSkin) {
                    data.skinTexture = id;
                } else {
                    data.capeTexture = id;
                }
            } catch (Throwable t) {
                data.failed = true;
            }
        });
    }

    private static String lookupUuid(String name) throws Exception {
        JsonObject json = getJson("https://api.mojang.com/users/profiles/minecraft/" + name);
        if (json == null || !json.has("id")) {
            return null;
        }
        return json.get("id").getAsString();
    }

    private static JsonObject lookupTextures(String uuid) throws Exception {
        JsonObject json = getJson("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid);
        if (json == null || !json.has("properties")) {
            return null;
        }
        for (JsonElement element : json.getAsJsonArray("properties")) {
            JsonObject property = element.getAsJsonObject();
            if (!"textures".equals(property.get("name").getAsString())) {
                continue;
            }
            String decoded = new String(Base64.getDecoder().decode(property.get("value").getAsString()), StandardCharsets.UTF_8);
            JsonObject payload = JsonParser.parseString(decoded).getAsJsonObject();
            if (payload.has("textures")) {
                return payload.getAsJsonObject("textures");
            }
        }
        return null;
    }

    private static String textureUrl(JsonObject textures, String key) {
        if (!textures.has(key)) {
            return null;
        }
        JsonObject entry = textures.getAsJsonObject(key);
        return entry.has("url") ? entry.get("url").getAsString() : null;
    }

    private static JsonObject getJson(String url) throws Exception {
        byte[] bytes = download(url);
        if (bytes == null) {
            return null;
        }
        try {
            return JsonParser.parseString(new String(bytes, StandardCharsets.UTF_8)).getAsJsonObject();
        } catch (Throwable t) {
            return null;
        }
    }

    private static byte[] download(String url) {
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setRequestProperty("User-Agent", "TerentX-Client");
            if (connection.getResponseCode() != 200) {
                return null;
            }
            InputStream stream = connection.getInputStream();
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            byte[] chunk = new byte[8192];
            int read;
            while ((read = stream.read(chunk)) != -1) {
                buffer.write(chunk, 0, read);
                if (buffer.size() > 8 * 1024 * 1024) {
                    return null;
                }
            }
            stream.close();
            return buffer.toByteArray();
        } catch (Throwable t) {
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
}
