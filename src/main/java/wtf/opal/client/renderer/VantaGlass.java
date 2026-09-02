/*
 * VantaGlass - self-contained "Liquid Glass" rendering (ReGlass style)
 *
 * Built on NanoVG only, so it works on every platform the client supports
 * and needs no extra shaders/framebuffers.  Produces the frosted-glass look
 * ReGlass is known for: soft drop shadow, translucent frosted body,
 * inner/outer rim light, top specular sheen and a tinted hover glow.
 */
package wtf.opal.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import wtf.opal.utility.render.ColorUtility;

@Environment(value = EnvType.CLIENT)
public final class VantaGlass {
    private static final NVGColor C1 = NVGColor.create();
    private static final NVGColor C2 = NVGColor.create();
    private static final NVGPaint PAINT = NVGPaint.create();

    private VantaGlass() {
    }

    /* Theme ---------------------------------------------------------------- */
    public static int accent() {
        return 0xFF4C8DFF;
    }

    /**
     * Draws a full liquid-glass panel (shadow + frosted body + rim + sheen).
     * Safe to call inside an already-started NVG frame.
     */
    public static void panel(float x, float y, float w, float h, float radius) {
        panel(x, y, w, h, radius, 0.16f);
    }

    public static void panel(float x, float y, float w, float h, float radius, float frostAlpha) {
        long vg = NVGRenderer.getContext();

        // 1. Soft drop shadow underneath the panel
        applyColor(0x000000, 0.35f, C1);
        applyColor(0x000000, 0.0f, C2);
        NanoVG.nvgBoxGradient(vg, x - 10.0f, y - 4.0f, w + 20.0f, h + 24.0f, radius * 2.0f, 28.0f, C1, C2, PAINT);
        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRect(vg, x - 40.0f, y - 40.0f, w + 80.0f, h + 90.0f);
        NanoVG.nvgFillPaint(vg, PAINT);
        NanoVG.nvgFill(vg);
        NanoVG.nvgClosePath(vg);

        // 2. Frosted glass body (two stacked translucent fills for depth)
        NVGRenderer.roundedRect(x, y, w, h, radius, ColorUtility.applyOpacity(0xFFFFFF, frostAlpha));
        NVGRenderer.roundedRect(x, y, w, h, radius, ColorUtility.applyOpacity(0xDCE6FF, frostAlpha * 0.45f));

        // 3. Vertical body shading: lighter at top, slightly darker at bottom
        applyColor(0xFFFFFF, 0.10f, C1);
        applyColor(0x8FA8D8, 0.08f, C2);
        NanoVG.nvgLinearGradient(vg, x, y, x, y + h, C1, C2, PAINT);
        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRoundedRect(vg, x, y, w, h, radius);
        NanoVG.nvgFillPaint(vg, PAINT);
        NanoVG.nvgFill(vg);
        NanoVG.nvgClosePath(vg);

        // 4. Outer rim light
        NVGRenderer.roundedRectOutline(x + 0.5f, y + 0.5f, w - 1.0f, h - 1.0f, radius, 1.0f, ColorUtility.applyOpacity(0xFFFFFF, 0.35f));

        // 5. Top specular sheen (the "glass reflection")
        sheen(x, y, w, h, radius);
    }

    /** Bright reflection across the top third of a glass surface. */
    public static void sheen(float x, float y, float w, float h, float radius) {
        long vg = NVGRenderer.getContext();
        applyColor(0xFFFFFF, 0.22f, C1);
        applyColor(0xFFFFFF, 0.0f, C2);
        NanoVG.nvgLinearGradient(vg, x, y, x, y + h * 0.45f, C1, C2, PAINT);
        // clip to the top 45% of the panel, then fill the rounded rect so the
        // gradient naturally fades to transparent before reaching the slots.
        NVGRenderer.scissor(x, y, w, Math.max(radius + 2.0f, h * 0.45f), () -> {
            NanoVG.nvgBeginPath(vg);
            NanoVG.nvgRoundedRect(vg, x, y, w, h, radius);
            NanoVG.nvgFillPaint(vg, PAINT);
            NanoVG.nvgFill(vg);
            NanoVG.nvgClosePath(vg);
        });
        // Crisp 1px highlight on the very top edge
        NVGRenderer.roundedRectOutline(x + 1.0f, y + 0.75f, w - 2.0f, Math.min(h - 2.0f, radius + 2.0f), radius, 1.2f, ColorUtility.applyOpacity(0xFFFFFF, 0.55f));
    }

    /**
     * Draws a glass frame around a rectangle (used for vanilla container
     * screens): frosted border + sheen, without covering the inner slots.
     */
    public static void frame(float x, float y, float w, float h, float radius) {
        long vg = NVGRenderer.getContext();

        // drop shadow
        applyColor(0x000000, 0.30f, C1);
        applyColor(0x000000, 0.0f, C2);
        NanoVG.nvgBoxGradient(vg, x - 12.0f, y - 8.0f, w + 24.0f, h + 28.0f, radius * 2.0f, 30.0f, C1, C2, PAINT);
        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRect(vg, x - 44.0f, y - 44.0f, w + 88.0f, h + 96.0f);
        NanoVG.nvgFillPaint(vg, PAINT);
        NanoVG.nvgFill(vg);
        NanoVG.nvgClosePath(vg);

        // frosted body
        NVGRenderer.roundedRect(x, y, w, h, radius, ColorUtility.applyOpacity(0xFFFFFF, 0.10f));
        NVGRenderer.roundedRect(x, y, w, h, radius, ColorUtility.applyOpacity(0xC8D8FF, 0.06f));

        // top sheen
        sheen(x, y, w, h, radius);

        // rim
        NVGRenderer.roundedRectOutline(x + 0.5f, y + 0.5f, w - 1.0f, h - 1.0f, radius, 1.4f, ColorUtility.applyOpacity(0xFFFFFF, 0.45f));
        NVGRenderer.roundedRectOutline(x - 0.5f, y - 0.5f, w + 1.0f, h + 1.0f, radius, 1.0f, ColorUtility.applyOpacity(0x8FA8D8, 0.25f));

        // accent glow line along the top edge
        applyColor(accent(), 0.55f, C1);
        applyColor(accent(), 0.0f, C2);
        NanoVG.nvgBoxGradient(vg, x + 6.0f, y - 1.5f, w - 12.0f, 6.0f, 3.0f, 6.0f, C1, C2, PAINT);
        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRoundedRect(vg, x + 6.0f, y - 1.5f, w - 12.0f, 6.0f, 3.0f);
        NanoVG.nvgFillPaint(vg, PAINT);
        NanoVG.nvgFill(vg);
        NanoVG.nvgClosePath(vg);
    }

    /** Hover/selection glow for interactive elements. */
    public static void glow(float x, float y, float w, float h, float radius, int color, float strength) {
        if (strength <= 0.01f) {
            return;
        }
        long vg = NVGRenderer.getContext();
        applyColor(color, 0.35f * strength, C1);
        applyColor(color, 0.0f, C2);
        NanoVG.nvgBoxGradient(vg, x, y, w, h, radius, 18.0f * strength, C1, C2, PAINT);
        NanoVG.nvgBeginPath(vg);
        NanoVG.nvgRoundedRect(vg, x, y, w, h, radius);
        NanoVG.nvgFillPaint(vg, PAINT);
        NanoVG.nvgFill(vg);
        NanoVG.nvgClosePath(vg);
        NVGRenderer.roundedRectOutline(x, y, w, h, radius, 1.0f, ColorUtility.applyOpacity(color, 0.6f * strength));
    }

    private static void applyColor(int rgb, float alpha, NVGColor out) {
        out.r((float) ((rgb >> 16) & 0xFF) / 255.0f);
        out.g((float) ((rgb >> 8) & 0xFF) / 255.0f);
        out.b((float) (rgb & 0xFF) / 255.0f);
        out.a(alpha);
    }
}
