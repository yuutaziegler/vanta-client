/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.loader.impl.launch.knot.Knot
 */
package wtf.opal.client.renderer.repository;

import java.io.InputStream;
import java.util.HashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.launch.knot.Knot;
import wtf.opal.client.renderer.text.NVGTextRenderer;

@Environment(value=EnvType.CLIENT)
public final class FontRepository {
    private static final HashMap<String, NVGTextRenderer> TEXT_RENDERER_MAP = new HashMap();

    public static NVGTextRenderer getFont(String name) {
        if (TEXT_RENDERER_MAP.containsKey(name)) {
            return TEXT_RENDERER_MAP.get(name);
        }
        InputStream pathURL = Knot.getLauncher().getTargetClassLoader().getResourceAsStream("assets/terentx/fonts/" + name + ".ttf");
        if (pathURL != null) {
            TEXT_RENDERER_MAP.put(name, new NVGTextRenderer(name, pathURL));
            return TEXT_RENDERER_MAP.get(name);
        }
        throw new RuntimeException("Font not found: " + name);
    }
}

