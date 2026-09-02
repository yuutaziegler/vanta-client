/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.lwjgl.nanovg.NVGColor
 *  org.lwjgl.nanovg.NVGPaint
 *  org.lwjgl.nanovg.NanoVG
 */
package wtf.opal.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.HudSettingsModule;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public class LiquidGlassRenderer {
    private static final NVGPaint SHADOW_PAINT = NVGPaint.create();
    private static final NVGPaint REFLECTION_PAINT = NVGPaint.create();
    private static final NVGColor COLOR_1 = NVGColor.create();
    private static final NVGColor COLOR_2 = NVGColor.create();

    public static void drawGlassPanel(float x, float y, float width, float height, float radius) {
        long vg = NVGRenderer.getContext();
        HudSettingsModule hudSettings = OpalClient.getInstance().getModuleRepository().getModule(HudSettingsModule.class);
        if (hudSettings == null || !hudSettings.isEnabled() || hudSettings.isShadowEnabled()) {
            NVGRenderer.applyColor(0x30000000, COLOR_1);
            NVGRenderer.applyColor(0, COLOR_2);
            NanoVG.nvgBoxGradient((long)vg, (float)x, (float)(y + 10.0f), (float)width, (float)height, (float)(radius * 2.0f), (float)35.0f, (NVGColor)COLOR_1, (NVGColor)COLOR_2, (NVGPaint)SHADOW_PAINT);
            NanoVG.nvgBeginPath((long)vg);
            NanoVG.nvgRect((long)vg, (float)(x - 50.0f), (float)(y - 50.0f), (float)(width + 100.0f), (float)(height + 100.0f));
            NanoVG.nvgFillPaint((long)vg, (NVGPaint)SHADOW_PAINT);
            NanoVG.nvgFill((long)vg);
            NanoVG.nvgClosePath((long)vg);
        }
        if (hudSettings == null || !hudSettings.isEnabled() || hudSettings.isGlassEffect()) {
            float glassOpacity = hudSettings != null && hudSettings.isEnabled() ? hudSettings.getGlassOpacity() : 0.14f;
            float frostOpacity = hudSettings != null && hudSettings.isEnabled() ? hudSettings.getGlassOpacity() * 0.5f : 0.08f;
            NVGRenderer.roundedRect(x, y, width, height, radius, ColorUtility.applyOpacity(-1, glassOpacity));
            if (hudSettings == null || !hudSettings.isEnabled() || hudSettings.isFrostedGlass()) {
                NVGRenderer.roundedRect(x, y, width, height, radius, ColorUtility.applyOpacity(-1, frostOpacity));
            }
            NVGRenderer.applyColor(ColorUtility.applyOpacity(-1, 0.15f), COLOR_1);
            NVGRenderer.applyColor(ColorUtility.applyOpacity(-1, 0.0f), COLOR_2);
            NanoVG.nvgLinearGradient((long)vg, (float)x, (float)y, (float)x, (float)(y + height * 0.4f), (NVGColor)COLOR_1, (NVGColor)COLOR_2, (NVGPaint)REFLECTION_PAINT);
            NanoVG.nvgBeginPath((long)vg);
            NanoVG.nvgRoundedRect((long)vg, (float)x, (float)y, (float)width, (float)height, (float)radius);
            NanoVG.nvgFillPaint((long)vg, (NVGPaint)REFLECTION_PAINT);
            NanoVG.nvgFill((long)vg);
            NanoVG.nvgClosePath((long)vg);
            NVGRenderer.applyColor(ColorUtility.applyOpacity(-1, 0.1f), COLOR_1);
            NVGRenderer.applyColor(ColorUtility.applyOpacity(-1, 0.0f), COLOR_2);
            NanoVG.nvgBoxGradient((long)vg, (float)x, (float)y, (float)width, (float)height, (float)radius, (float)10.0f, (NVGColor)COLOR_1, (NVGColor)COLOR_2, (NVGPaint)SHADOW_PAINT);
            NanoVG.nvgBeginPath((long)vg);
            NanoVG.nvgRoundedRect((long)vg, (float)x, (float)y, (float)width, (float)height, (float)radius);
            NanoVG.nvgFillPaint((long)vg, (NVGPaint)SHADOW_PAINT);
            NanoVG.nvgFill((long)vg);
            NanoVG.nvgClosePath((long)vg);
            float borderOpacity = hudSettings != null && hudSettings.isEnabled() ? hudSettings.getBorderOpacity() : 0.18f;
            NVGRenderer.roundedRectOutline(x, y, width, height, radius, 1.0f, ColorUtility.applyOpacity(-1, borderOpacity));
            if (hudSettings == null || !hudSettings.isEnabled() || hudSettings.isSpecularHighlight()) {
                NVGRenderer.scissor(x, y, width, height / 4.0f, () -> NVGRenderer.roundedRectOutline(x, y, width, height, radius, 1.5f, ColorUtility.applyOpacity(-1, 0.6f)));
            }
        } else {
            NVGRenderer.roundedRect(x, y, width, height, radius, -804253680);
        }
    }

    public static void drawGlassHighlight(float x, float y, float width, float height, float radius, int highlightColor, float hoverFactor) {
        long vg = NVGRenderer.getContext();
        HudSettingsModule hudSettings = OpalClient.getInstance().getModuleRepository().getModule(HudSettingsModule.class);
        if (hoverFactor > 0.01f) {
            NVGRenderer.applyColor(ColorUtility.applyOpacity(highlightColor, 0.4f * hoverFactor), COLOR_1);
            NVGRenderer.applyColor(ColorUtility.applyOpacity(highlightColor, 0.0f), COLOR_2);
            NanoVG.nvgBoxGradient((long)vg, (float)x, (float)y, (float)width, (float)height, (float)radius, (float)(15.0f * hoverFactor), (NVGColor)COLOR_1, (NVGColor)COLOR_2, (NVGPaint)SHADOW_PAINT);
            NanoVG.nvgBeginPath((long)vg);
            NanoVG.nvgRoundedRect((long)vg, (float)x, (float)y, (float)width, (float)height, (float)radius);
            NanoVG.nvgFillPaint((long)vg, (NVGPaint)SHADOW_PAINT);
            NanoVG.nvgFill((long)vg);
            NanoVG.nvgClosePath((long)vg);
            NVGRenderer.applyColor(ColorUtility.applyOpacity(highlightColor, 0.2f * hoverFactor), COLOR_1);
            NVGRenderer.applyColor(ColorUtility.applyOpacity(highlightColor, 0.0f), COLOR_2);
            NanoVG.nvgBoxGradient((long)vg, (float)(x - 2.0f), (float)(y - 2.0f), (float)(width + 4.0f), (float)(height + 4.0f), (float)(radius * 1.5f), (float)(10.0f * hoverFactor), (NVGColor)COLOR_1, (NVGColor)COLOR_2, (NVGPaint)SHADOW_PAINT);
            NanoVG.nvgBeginPath((long)vg);
            NanoVG.nvgRect((long)vg, (float)(x - 20.0f), (float)(y - 20.0f), (float)(width + 40.0f), (float)(height + 40.0f));
            NanoVG.nvgFillPaint((long)vg, (NVGPaint)SHADOW_PAINT);
            NanoVG.nvgFill((long)vg);
            NanoVG.nvgClosePath((long)vg);
        }
    }
}

