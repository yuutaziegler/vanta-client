/*
 * Crosshair Renderer - renders the custom crosshair styles, including the
 * pixel-by-pixel Custom grid. Colors switch when targeting an entity if
 * Dynamic Color is enabled.
 */
package wtf.opal.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_239;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.CustomCrosshairModule;

@Environment(value=EnvType.CLIENT)
public final class CrosshairRenderer {
    private static float smoothedSize = -1.0f;

    private CrosshairRenderer() {
    }

    public static void render() {
        CustomCrosshairModule module = OpalClient.getInstance().getModuleRepository().getModule(CustomCrosshairModule.class);
        if (module == null || !module.isEnabled()) {
            return;
        }

        float centerX = (float)Constants.mc.method_22683().method_4486() / 2.0f;
        float centerY = (float)Constants.mc.method_22683().method_4502() / 2.0f;

        // Dynamic color: switch when the crosshair is over an entity
        int color = module.getColor();
        if (module.isDynamicColor() && isTargetingEntity()) {
            color = module.getDynamicColorTarget();
        }

        float size = effectiveSize(module);
        float thickness = (float)module.getThickness();
        float gap = (float)module.getGap();
        float dotSize = (float)module.getDotSize();

        switch (module.getStyle()) {
            case CROSS:
                renderCross(centerX, centerY, size, thickness, gap, color);
                break;
            case DOT:
                renderDot(centerX, centerY, dotSize, color);
                break;
            case CROSS_DOT:
                renderCross(centerX, centerY, size, thickness, gap, color);
                renderDot(centerX, centerY, dotSize, color);
                break;
            case CIRCLE:
                renderCircle(centerX, centerY, size, thickness, gap, color);
                break;
            case SQUARE:
                renderSquare(centerX, centerY, size, thickness, gap, color);
                break;
            case PLUS:
                renderPlus(centerX, centerY, size, thickness, color);
                break;
            case MINUS:
                renderMinus(centerX, centerY, size, thickness, gap, color);
                break;
            case T_SHAPE:
                renderT(centerX, centerY, size, thickness, gap, color);
                break;
            case CUSTOM:
                renderPixelGrid(module, centerX, centerY, color);
                break;
        }

        if (module.isOutline()) {
            int outlineColor = color & 0xFF000000; // black outline, same alpha
            float outlineThickness = (float)module.getOutlineThickness();
            switch (module.getStyle()) {
                case CROSS:
                case CROSS_DOT:
                case PLUS:
                    renderCrossOutline(centerX, centerY, size, thickness, gap, outlineColor, outlineThickness);
                    break;
                case DOT:
                    renderDotOutline(centerX, centerY, dotSize, outlineColor, outlineThickness);
                    break;
                case CUSTOM:
                    renderPixelGridOutline(module, centerX, centerY, outlineColor, outlineThickness);
                    break;
                default:
                    break;
            }
        }
    }

    /** Smoothed arm size; Shake Reduction slows size changes down to reduce perceived jitter. */
    private static float effectiveSize(CustomCrosshairModule module) {
        float target = (float)module.getSize();
        if (!module.isSmoothAnimation()) {
            smoothedSize = target;
            return target;
        }
        if (smoothedSize < 0.0f) {
            smoothedSize = target;
        }
        float lerpFactor = 0.65f - 0.45f * (float)module.getShakeReduction();
        smoothedSize += (target - smoothedSize) * Math.max(0.05f, lerpFactor);
        return smoothedSize;
    }

    private static boolean isTargetingEntity() {
        return Constants.mc.field_1765 != null
            && Constants.mc.field_1765.method_17783() == class_239.class_240.field_1331;
    }

    private static void renderPixelGrid(CustomCrosshairModule module, float cx, float cy, int color) {
        float px = (float)module.getPixelSize();
        int grid = CustomCrosshairModule.GRID_SIZE;
        float half = (float)grid / 2.0f;
        for (int row = 0; row < grid; ++row) {
            for (int col = 0; col < grid; ++col) {
                if (!module.isPixelOn(row, col)) {
                    continue;
                }
                NVGRenderer.rect(cx + ((float)col - half + 0.5f) * px, cy + ((float)row - half + 0.5f) * px, px, px, color);
            }
        }
    }

    private static void renderPixelGridOutline(CustomCrosshairModule module, float cx, float cy, int color, float outlineThickness) {
        float px = (float)module.getPixelSize();
        int grid = CustomCrosshairModule.GRID_SIZE;
        float half = (float)grid / 2.0f;
        for (int row = 0; row < grid; ++row) {
            for (int col = 0; col < grid; ++col) {
                if (!module.isPixelOn(row, col)) {
                    continue;
                }
                float x = cx + ((float)col - half + 0.5f) * px;
                float y = cy + ((float)row - half + 0.5f) * px;
                NVGRenderer.rectOutline(x - outlineThickness, y - outlineThickness, px + outlineThickness * 2.0f, px + outlineThickness * 2.0f, outlineThickness, color);
            }
        }
    }

    private static void renderCross(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        NVGRenderer.rect(cx - halfThick, cy - halfSize - gap, thickness, halfSize, color);
        NVGRenderer.rect(cx - halfThick, cy + gap, thickness, halfSize, color);
        NVGRenderer.rect(cx - halfSize - gap, cy - halfThick, halfSize, thickness, color);
        NVGRenderer.rect(cx + gap, cy - halfThick, halfSize, thickness, color);
    }

    private static void renderCrossOutline(float cx, float cy, float size, float thickness, float gap, int color, float outlineThickness) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        NVGRenderer.rectOutline(cx - halfThick - outlineThickness, cy - halfSize - gap - outlineThickness, thickness + outlineThickness * 2.0f, halfSize + outlineThickness * 2.0f, outlineThickness, color);
        NVGRenderer.rectOutline(cx - halfThick - outlineThickness, cy + gap - outlineThickness, thickness + outlineThickness * 2.0f, halfSize + outlineThickness * 2.0f, outlineThickness, color);
        NVGRenderer.rectOutline(cx - halfSize - gap - outlineThickness, cy - halfThick - outlineThickness, halfSize + outlineThickness * 2.0f, thickness + outlineThickness * 2.0f, outlineThickness, color);
        NVGRenderer.rectOutline(cx + gap - outlineThickness, cy - halfThick - outlineThickness, halfSize + outlineThickness * 2.0f, thickness + outlineThickness * 2.0f, outlineThickness, color);
    }

    private static void renderDot(float cx, float cy, float size, int color) {
        NVGRenderer.roundedRect(cx - size / 2.0f, cy - size / 2.0f, size, size, size / 2.0f, color);
    }

    private static void renderDotOutline(float cx, float cy, float size, int color, float outlineThickness) {
        NVGRenderer.roundedRectOutline(cx - size / 2.0f - outlineThickness, cy - size / 2.0f - outlineThickness, size + outlineThickness * 2.0f, size + outlineThickness * 2.0f, size / 2.0f + outlineThickness, outlineThickness, color);
    }

    private static void renderCircle(float cx, float cy, float size, float thickness, float gap, int color) {
        float radius = size / 2.0f;
        NVGRenderer.roundedRectOutline(cx - radius - gap, cy - radius - gap, radius * 2.0f, radius * 2.0f, radius, thickness, color);
    }

    private static void renderSquare(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfGap = gap / 2.0f;
        NVGRenderer.rectOutline(cx - halfSize - halfGap, cy - halfSize - halfGap, size, size, thickness, color);
    }

    private static void renderPlus(float cx, float cy, float size, float thickness, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        NVGRenderer.rect(cx - halfThick, cy - halfSize, thickness, size, color);
        NVGRenderer.rect(cx - halfSize, cy - halfThick, size, thickness, color);
    }

    private static void renderMinus(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        NVGRenderer.rect(cx - halfSize - gap, cy - halfThick, size + gap * 2.0f, thickness, color);
    }

    private static void renderT(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        NVGRenderer.rect(cx - halfSize - gap, cy - halfSize - halfThick, size + gap * 2.0f, thickness, color);
        NVGRenderer.rect(cx - halfThick, cy - halfSize + halfThick, thickness, size, color);
    }
}
