/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.loader.api.FabricLoader
 */
package wtf.opal.client.renderer.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.HashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import wtf.opal.client.renderer.image.NVGImageRenderer;

@Environment(value=EnvType.CLIENT)
public final class ImageRepository {
    private static final HashMap<String, NVGImageRenderer> imageMap = new HashMap();

    public static NVGImageRenderer getImage(Path path, int flags) {
        String pathString = path.toString();
        if (imageMap.containsKey(pathString)) {
            return imageMap.get(pathString);
        }
        try {
            imageMap.put(pathString, new NVGImageRenderer(Files.newInputStream(path, new OpenOption[0]), flags));
            return imageMap.get(pathString);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NVGImageRenderer getImage(String path, int flags) {
        if (imageMap.containsKey(path)) {
            return imageMap.get(path);
        }
        Path pathURL = FabricLoader.getInstance().getModContainer("terentx").flatMap(c -> c.findPath("assets/terentx/" + path)).orElse(null);
        if (pathURL == null) {
            return null;
        }
        try {
            imageMap.put(path, new NVGImageRenderer(Files.newInputStream(pathURL, new OpenOption[0]), flags));
            return imageMap.get(path);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static NVGImageRenderer getImage(String path) {
        if (imageMap.containsKey(path)) {
            return imageMap.get(path);
        }
        Path pathURL = FabricLoader.getInstance().getModContainer("terentx").flatMap(c -> c.findPath("assets/terentx/" + path)).orElse(null);
        if (pathURL == null) {
            return null;
        }
        try {
            imageMap.put(path, new NVGImageRenderer(Files.newInputStream(pathURL, new OpenOption[0])));
            return imageMap.get(path);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

