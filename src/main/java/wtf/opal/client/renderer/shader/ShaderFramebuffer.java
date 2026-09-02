/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_276
 *  net.minecraft.class_6364
 */
package wtf.opal.client.renderer.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_276;
import net.minecraft.class_6364;

@Environment(value=EnvType.CLIENT)
public final class ShaderFramebuffer {
    private static class_276 blurFramebuffer;
    private static class_276 glowFramebuffer;

    public static void applyBlurToFullScreen() {
    }

    public static void applyGlowToNVGObjects() {
        if (glowFramebuffer != null) {
            RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(glowFramebuffer.method_30277(), 0, glowFramebuffer.method_30278(), 1.0);
        }
    }

    public static void onResized(int width, int height) {
        if (blurFramebuffer != null) {
            blurFramebuffer.method_1238();
        }
        if (glowFramebuffer != null) {
            glowFramebuffer.method_1238();
        }
        blurFramebuffer = new class_6364(width, height);
        RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(blurFramebuffer.method_30277(), 0, blurFramebuffer.method_30278(), 1.0);
        glowFramebuffer = new class_6364(width, height);
        RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(glowFramebuffer.method_30277(), 0, glowFramebuffer.method_30278(), 1.0);
    }

    public static class_276 getGlowFramebuffer() {
        return glowFramebuffer;
    }
}

