/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderPass
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1041
 *  net.minecraft.class_10799
 *  net.minecraft.class_276
 *  org.lwjgl.nanovg.NVGColor
 *  org.lwjgl.nanovg.NVGPaint
 *  org.lwjgl.nanovg.NanoVG
 *  org.lwjgl.nanovg.NanoVGGL3
 *  org.lwjgl.opengl.GL33C
 */
package wtf.opal.client.renderer;

import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_10799;
import net.minecraft.class_276;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import org.lwjgl.opengl.GL33C;
import wtf.opal.client.Constants;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.GLUtility;
import wtf.opal.utility.render.ScreenPosition;

@Environment(value=EnvType.CLIENT)
public final class NVGRenderer {
    private static final long VG = NanoVGGL3.nvgCreate((int)3);
    public static final NVGPaint NVG_PAINT = NVGPaint.create();
    public static final NVGPaint BLUR_PAINT = NVGPaint.create();
    public static final NVGPaint GLOW_PAINT = NVGPaint.create();
    public static final NVGColor NVG_COLOR_1 = NVGColor.create();
    public static final NVGColor NVG_COLOR_2 = NVGColor.create();
    private static boolean frameStarted;
    public static float globalAlpha;
    private static final List<ScreenPosition> scissors;

    public static boolean beginFrame() {
        class_1041 window = Constants.mc.method_22683();
        float scaleFactor = window.method_4495();
        if (!frameStarted) {
            GLUtility.setup();
            GLUtility.push();
            NanoVG.nvgBeginFrame((long)VG, (float)((float)window.method_4489() / scaleFactor), (float)((float)window.method_4506() / scaleFactor), (float)scaleFactor);
            if (!scissors.isEmpty()) {
                NVGRenderer.useCurrentScissors();
            }
            frameStarted = true;
            return true;
        }
        return false;
    }

    public static void endFrameAndReset(boolean createRenderPass) {
        NVGRenderer.endFrame(createRenderPass);
        NVGRenderer.clearScissors();
    }

    public static void endFrame(boolean createRenderPass) {
        if (frameStarted) {
            if (createRenderPass) {
                class_276 framebuffer = Constants.mc.method_1522();
                try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "terentx/nvg", framebuffer.method_71639(), OptionalInt.empty(), framebuffer.field_1478 ? framebuffer.method_71640() : null, OptionalDouble.empty());){
                    renderPass.setPipeline(class_10799.field_56879);
                    NanoVG.nvgEndFrame((long)VG);
                }
            } else {
                NanoVG.nvgEndFrame((long)VG);
            }
            GLUtility.pop();
            GL33C.glViewport((int)0, (int)0, (int)Constants.mc.method_22683().method_4489(), (int)Constants.mc.method_22683().method_4506());
            frameStarted = false;
        }
    }

    public static void clearScissors() {
        scissors.clear();
    }

    public static void globalAlpha(float alpha) {
        globalAlpha = alpha;
        NanoVG.nvgGlobalAlpha((long)VG, (float)alpha);
    }

    public static void rect(float x, float y, float width, float height, int color) {
        NVGRenderer.applyColor(color, NVG_COLOR_1);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillColor((long)VG, (NVGColor)NVG_COLOR_1);
        NanoVG.nvgRect((long)VG, (float)x, (float)y, (float)width, (float)height);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void rect(float x, float y, float width, float height, NVGPaint nvgPaint) {
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillPaint((long)VG, (NVGPaint)nvgPaint);
        NanoVG.nvgRect((long)VG, (float)x, (float)y, (float)width, (float)height);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void scale(float factor, float x, float y, float width, float height, Runnable content) {
        float translateX = x + width / 2.0f;
        float translateY = y + height / 2.0f;
        NanoVG.nvgSave((long)VG);
        NanoVG.nvgTranslate((long)VG, (float)translateX, (float)translateY);
        NanoVG.nvgScale((long)VG, (float)factor, (float)factor);
        NanoVG.nvgTranslate((long)VG, (float)(-translateX), (float)(-translateY));
        content.run();
        NanoVG.nvgRestore((long)VG);
    }

    public static void rectStroke(float x, float y, float width, float height, float strokeThickness, int color, int strokeColor) {
        NVGRenderer.rect(x - strokeThickness, y - strokeThickness, width + strokeThickness * 2.0f, height + strokeThickness * 2.0f, strokeColor);
        NVGRenderer.rect(x, y, width, height, color);
    }

    public static void rotate(double degrees, float x, float y, float width, float height, Runnable content) {
        float translateX = x + width / 2.0f;
        float translateY = y + height / 2.0f;
        NanoVG.nvgSave((long)VG);
        NanoVG.nvgTranslate((long)VG, (float)translateX, (float)translateY);
        NanoVG.nvgRotate((long)VG, (float)((float)Math.toRadians(degrees)));
        content.run();
        NanoVG.nvgRestore((long)VG);
    }

    public static void rectOutlineStroke(float x, float y, float width, float height, float outlineThickness, float strokeThickness, int outlineColor, int strokeColor) {
        NVGRenderer.rectOutline(x - outlineThickness, y - outlineThickness, width + outlineThickness * 2.0f, height + outlineThickness * 2.0f, strokeThickness, strokeColor);
        NVGRenderer.rectOutline(x, y, width, height, outlineThickness, outlineColor);
    }

    public static void rectOutline(float x, float y, float width, float height, float thickness, int color) {
        NVGRenderer.applyColor(color, NVG_COLOR_1);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillColor((long)VG, (NVGColor)NVG_COLOR_1);
        NanoVG.nvgRect((long)VG, (float)x, (float)y, (float)width, (float)thickness);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillColor((long)VG, (NVGColor)NVG_COLOR_1);
        NanoVG.nvgRect((long)VG, (float)(x + width - thickness), (float)(y + thickness), (float)thickness, (float)(height - thickness));
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillColor((long)VG, (NVGColor)NVG_COLOR_1);
        NanoVG.nvgRect((long)VG, (float)x, (float)(y + height - thickness), (float)(width - thickness), (float)thickness);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillColor((long)VG, (NVGColor)NVG_COLOR_1);
        NanoVG.nvgRect((long)VG, (float)x, (float)(y + thickness), (float)thickness, (float)(height - thickness));
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void rainbowRect(float x, float y, float width, float height) {
        for (float i = y; i < y + height; i += 0.5f) {
            float hue = (i - y) / height;
            int rgbColor = Color.HSBtoRGB(hue, 1.0f, 1.0f);
            float segmentHeight = Math.min(0.5f, y + height - i);
            NanoVG.nvgShapeAntiAlias((long)VG, (boolean)false);
            NVGRenderer.rect(x, i, width, segmentHeight, rgbColor);
            NanoVG.nvgShapeAntiAlias((long)VG, (boolean)true);
        }
    }

    public static void roundedRectOutline(float x, float y, float width, float height, float radius, float thickness, int color) {
        NVGRenderer.applyColor(color, NVG_COLOR_1);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgStrokeColor((long)VG, (NVGColor)NVG_COLOR_1);
        NanoVG.nvgStrokeWidth((long)VG, (float)thickness);
        NanoVG.nvgRoundedRect((long)VG, (float)x, (float)y, (float)width, (float)height, (float)radius);
        NanoVG.nvgStroke((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void scissor(float x, float y, float width, float height, Runnable content) {
        ScreenPosition scissor = new ScreenPosition(x, y, width, height);
        scissors.add(scissor);
        NanoVG.nvgIntersectScissor((long)VG, (float)x, (float)y, (float)width, (float)height);
        content.run();
        NanoVG.nvgResetScissor((long)VG);
        scissors.remove(scissor);
        NVGRenderer.useCurrentScissors();
    }

    private static void useCurrentScissors() {
        for (ScreenPosition scissor : scissors) {
            NanoVG.nvgIntersectScissor((long)VG, (float)scissor.getX(), (float)scissor.getY(), (float)scissor.getWidth(), (float)scissor.getHeight());
        }
    }

    public static void roundedRect(float x, float y, float width, float height, float radius, int color) {
        NVGRenderer.applyColor(color, NVG_COLOR_1);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillColor((long)VG, (NVGColor)NVG_COLOR_1);
        NanoVG.nvgRoundedRect((long)VG, (float)x, (float)y, (float)width, (float)height, (float)radius);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void roundedRectGradient(float x, float y, float width, float height, float radius, int color1, int color2, float angleDegrees) {
        NVGRenderer.applyColor(color1, NVG_COLOR_1);
        NVGRenderer.applyColor(color2, NVG_COLOR_2);
        float angleRadians = (float)Math.toRadians(angleDegrees);
        float dx = (float)Math.cos(angleRadians);
        float dy = (float)Math.sin(angleRadians);
        NanoVG.nvgLinearGradient((long)VG, (float)(x + width * 0.5f - dx * width * 0.5f), (float)(y + height * 0.5f - dy * height * 0.5f), (float)(x + width * 0.5f + dx * width * 0.5f), (float)(y + height * 0.5f + dy * height * 0.5f), (NVGColor)NVG_COLOR_1, (NVGColor)NVG_COLOR_2, (NVGPaint)NVG_PAINT);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillPaint((long)VG, (NVGPaint)NVG_PAINT);
        NanoVG.nvgRoundedRect((long)VG, (float)x, (float)y, (float)width, (float)height, (float)radius);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void rectGradient(float x, float y, float width, float height, int color1, int color2, float angleDegrees) {
        NVGRenderer.applyColor(color1, NVG_COLOR_1);
        NVGRenderer.applyColor(color2, NVG_COLOR_2);
        float angleRadians = (float)Math.toRadians(angleDegrees);
        float dx = (float)Math.cos(angleRadians);
        float dy = (float)Math.sin(angleRadians);
        NanoVG.nvgLinearGradient((long)VG, (float)(x + width * 0.5f - dx * width * 0.5f), (float)(y + height * 0.5f - dy * height * 0.5f), (float)(x + width * 0.5f + dx * width * 0.5f), (float)(y + height * 0.5f + dy * height * 0.5f), (NVGColor)NVG_COLOR_1, (NVGColor)NVG_COLOR_2, (NVGPaint)NVG_PAINT);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillPaint((long)VG, (NVGPaint)NVG_PAINT);
        NanoVG.nvgRect((long)VG, (float)x, (float)y, (float)width, (float)height);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void roundedRect(float x, float y, float width, float height, float radius, NVGPaint nvgPaint) {
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillPaint((long)VG, (NVGPaint)nvgPaint);
        NanoVG.nvgRoundedRect((long)VG, (float)x, (float)y, (float)width, (float)height, (float)radius);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void roundedRectVarying(float x, float y, float width, float height, float radiusTopLeft, float radiusTopRight, float radiusBottomRight, float radiusBottomLeft, int color) {
        NVGRenderer.applyColor(color, NVG_COLOR_1);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillColor((long)VG, (NVGColor)NVG_COLOR_1);
        NanoVG.nvgRoundedRectVarying((long)VG, (float)x, (float)y, (float)width, (float)height, (float)radiusTopLeft, (float)radiusTopRight, (float)radiusBottomRight, (float)radiusBottomLeft);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void roundedRectVaryingGradient(float x, float y, float width, float height, float radiusTopLeft, float radiusTopRight, float radiusBottomRight, float radiusBottomLeft, int color1, int color2, float angleDegrees) {
        NVGRenderer.applyColor(color1, NVG_COLOR_1);
        NVGRenderer.applyColor(color2, NVG_COLOR_2);
        float angleRadians = (float)Math.toRadians(angleDegrees);
        float dx = (float)Math.cos(angleRadians);
        float dy = (float)Math.sin(angleRadians);
        NanoVG.nvgLinearGradient((long)VG, (float)(x + width * 0.5f - dx * width * 0.5f), (float)(y + height * 0.5f - dy * height * 0.5f), (float)(x + width * 0.5f + dx * width * 0.5f), (float)(y + height * 0.5f + dy * height * 0.5f), (NVGColor)NVG_COLOR_1, (NVGColor)NVG_COLOR_2, (NVGPaint)NVG_PAINT);
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillPaint((long)VG, (NVGPaint)NVG_PAINT);
        NanoVG.nvgRoundedRectVarying((long)VG, (float)x, (float)y, (float)width, (float)height, (float)radiusTopLeft, (float)radiusTopRight, (float)radiusBottomRight, (float)radiusBottomLeft);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void roundedRectVarying(float x, float y, float width, float height, float radiusTopLeft, float radiusTopRight, float radiusBottomRight, float radiusBottomLeft, NVGPaint nvgPaint) {
        NanoVG.nvgBeginPath((long)VG);
        NanoVG.nvgFillPaint((long)VG, (NVGPaint)nvgPaint);
        NanoVG.nvgRoundedRectVarying((long)VG, (float)x, (float)y, (float)width, (float)height, (float)radiusTopLeft, (float)radiusTopRight, (float)radiusBottomRight, (float)radiusBottomLeft);
        NanoVG.nvgFill((long)VG);
        NanoVG.nvgClosePath((long)VG);
    }

    public static void applyColor(int color, NVGColor nvgColor) {
        int[] rgba = ColorUtility.hexToRGBA(color);
        NanoVG.nvgRGBAf((float)((float)rgba[0] / 255.0f), (float)((float)rgba[1] / 255.0f), (float)((float)rgba[2] / 255.0f), (float)((float)rgba[3] / 255.0f), (NVGColor)nvgColor);
    }

    public static void createNVGPaintFromTex(int width, int height, int glTex, NVGPaint nvgPaint) {
        int imageHandle = NanoVGGL3.nvglCreateImageFromHandle((long)VG, (int)glTex, (int)width, (int)height, (int)9);
        NanoVG.nvgImagePattern((long)VG, (float)0.0f, (float)0.0f, (float)Constants.mc.method_22683().method_4486(), (float)Constants.mc.method_22683().method_4502(), (float)0.0f, (int)imageHandle, (float)1.0f, (NVGPaint)nvgPaint);
    }

    public static long getContext() {
        return VG;
    }

    static {
        globalAlpha = 1.0f;
        scissors = new ArrayList<ScreenPosition>();
    }
}

