/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.webhook;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class WebhookHelper {
    private static String webhookUrl = "";
    private static boolean enabled = false;

    public static void setWebhookUrl(String url) {
        webhookUrl = url;
        enabled = url != null && !url.isEmpty() && url.startsWith("https://discord.com/api/webhooks/");
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public static void sendEvent(String title, String description, Color color) {
        if (!enabled) {
            return;
        }
        new Thread(() -> {
            try {
                JsonObject embed = new JsonObject();
                embed.addProperty("title", title);
                embed.addProperty("description", description);
                embed.addProperty("color", (Number)(color.getRGB() & 0xFFFFFF));
                embed.addProperty("timestamp", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(new Date()));
                JsonObject footer = new JsonObject();
                footer.addProperty("text", "TerentX Client");
                embed.add("footer", (JsonElement)footer);
                JsonObject payload = new JsonObject();
                payload.add("embeds", (JsonElement)new JsonArray());
                payload.getAsJsonArray("embeds").add((JsonElement)embed);
                URL url = new URL(webhookUrl);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                try (OutputStream os = conn.getOutputStream();){
                    byte[] input = payload.toString().getBytes(StandardCharsets.UTF_8);
                    os.write(input, 0, input.length);
                }
                conn.getResponseCode();
                conn.disconnect();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }).start();
    }

    public static void logKill(String playerName) {
        WebhookHelper.sendEvent("\ud83d\udc80 Kill", "Killed player: **" + playerName + "**", new Color(255, 0, 0));
    }

    public static void logDeath(String killer) {
        WebhookHelper.sendEvent("\u2620\ufe0f Death", "Killed by: **" + killer + "**", new Color(139, 0, 0));
    }

    public static void logServerJoin(String serverAddress) {
        WebhookHelper.sendEvent("\ud83c\udf10 Server Join", "Connected to: **" + serverAddress + "**", new Color(0, 255, 0));
    }

    public static void logServerLeave(String serverAddress) {
        WebhookHelper.sendEvent("\ud83d\udeaa Server Leave", "Disconnected from: **" + serverAddress + "**", new Color(255, 165, 0));
    }

    public static void logChat(String playerName, String message) {
        WebhookHelper.sendEvent("\ud83d\udcac Chat", "**" + playerName + "**: " + message, new Color(100, 149, 237));
    }

    public static void logDamage(String attacker, float damage) {
        WebhookHelper.sendEvent("\u2694\ufe0f Damage", "Damaged by **" + attacker + "** for **" + String.format("%.1f", Float.valueOf(damage)) + "** HP", new Color(255, 69, 0));
    }

    public static void logItemPickup(String itemName, int count) {
        WebhookHelper.sendEvent("\ud83d\udce6 Item Pickup", "Picked up **" + count + "x " + itemName + "**", new Color(255, 215, 0));
    }

    public static void logModuleToggle(String moduleName, boolean enabled) {
        WebhookHelper.sendEvent("\u2699\ufe0f Module", "**" + moduleName + "** " + (enabled ? "enabled" : "disabled"), new Color(138, 43, 226));
    }
}

