/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.renderer;

import java.util.LinkedList;
import java.util.Queue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class MinecraftRenderer {
    private static final Queue<Runnable> RENDER_QUEUE = new LinkedList<Runnable>();

    private MinecraftRenderer() {
    }

    public static void addToQueue(Runnable runnable) {
        RENDER_QUEUE.add(runnable);
    }

    public static void render() {
        while (!RENDER_QUEUE.isEmpty()) {
            RENDER_QUEUE.poll().run();
        }
    }
}

