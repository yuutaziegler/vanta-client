/*
 * Crosshair Renderer - Renders custom crosshairs
 */
package wtf.opal.client.renderer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.CustomCrosshairModule;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public class CrosshairRenderer {
    
    public static void render() {
        CustomCrosshairModule module = OpalClient.getInstance().getModuleRepository().getModule(CustomCrosshairModule.class);
        if (module == null || !module.isEnabled()) {
            return;
        }
        
        float centerX = (float)Constants.mc.method_22683().method_4486() / 2.0f;
        float centerY = (float)Constants.mc.method_22683().method_4502() / 2.0f;
        
        float size = module.getSize();
        float thickness = module.getThickness();
        float gap = module.getGap();
        float dotSize = module.getDotSize();
        
        int color = module.getColor();
        
        // Apply shake reduction
        float shakeReduction = module.getShakeReduction();
        if (shakeReduction > 0) {
            // Reduce crosshair shake based on setting
        }
        
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
                renderPlus(centerX, centerY, size, thickness, gap, color);
                break;
                
            case MINUS:
                renderMinus(centerX, centerY, size, thickness, gap, color);
                break;
                
            case SWASTIKA:
                renderSwastika(centerX, centerY, size, thickness, gap, color);
                break;
                
            case CUSTOM:
                // Custom crosshair could be loaded from config
                renderCross(centerX, centerY, size, thickness, gap, color);
                break;
        }
        
        // Draw outline if enabled
        if (module.isOutline()) {
            int outlineColor = 0xFF000000 | (color & 0x00FFFFFF);
            float outlineThickness = module.getOutlineThickness();
            
            // Render outline version with offset
            switch (module.getStyle()) {
                case CROSS:
                case CROSS_DOT:
                case PLUS:
                    renderCrossOutline(centerX, centerY, size, thickness, gap, outlineColor, outlineThickness);
                    break;
                case DOT:
                    renderDotOutline(centerX, centerY, dotSize, outlineColor, outlineThickness);
                    break;
                default:
                    break;
            }
        }
    }
    
    private static void renderCross(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        float halfGap = gap / 2.0f;
        
        // Top
        NVGRenderer.roundedRect(cx - halfThick, cy - halfSize - halfGap, thickness, halfSize, halfThick, color);
        // Bottom
        NVGRenderer.roundedRect(cx - halfThick, cy + halfGap, thickness, halfSize, halfThick, color);
        // Left
        NVGRenderer.roundedRect(cx - halfSize - halfGap, cy - halfThick, halfSize, thickness, halfThick, color);
        // Right
        NVGRenderer.roundedRect(cx + halfGap, cy - halfThick, halfSize, thickness, halfThick, color);
    }
    
    private static void renderCrossOutline(float cx, float cy, float size, float thickness, float gap, int color, float outlineThickness) {
        // Simplified outline - just draw the same cross slightly larger/outlined
        float offset = outlineThickness;
        renderCross(cx, cy, size + offset * 2, thickness + offset * 2, gap, color);
    }
    
    private static void renderDot(float cx, float cy, float size, int color) {
        NVGRenderer.roundedRect(cx - size / 2.0f, cy - size / 2.0f, size, size, size / 2.0f, color);
    }
    
    private static void renderDotOutline(float cx, float cy, float size, int color, float outlineThickness) {
        NVGRenderer.roundedRectOutline(cx - size / 2.0f - outlineThickness, cy - size / 2.0f - outlineThickness, 
                size + outlineThickness * 2, size + outlineThickness * 2, size / 2.0f + outlineThickness, outlineThickness, color);
    }
    
    private static void renderCircle(float cx, float cy, float size, float thickness, float gap, int color) {
        float radius = size / 2.0f;
        NVGRenderer.roundedRect(cx - radius - gap, cy - radius - gap, radius * 2, radius * 2, radius, thickness, color);
    }
    
    private static void renderSquare(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfGap = gap / 2.0f;
        NVGRenderer.roundedRect(cx - halfSize - halfGap, cy - halfSize - halfGap, size, size, 2.0f, thickness, color);
    }
    
    private static void renderPlus(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        float halfGap = gap / 2.0f;
        
        // Horizontal line
        NVGRenderer.roundedRect(cx - halfSize - halfGap, cy - halfThick, size + gap, thickness, halfThick, color);
        // Vertical line
        NVGRenderer.roundedRect(cx - halfThick, cy - halfSize - halfGap, thickness, size + gap, halfThick, color);
    }
    
    private static void renderMinus(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        float halfGap = gap / 2.0f;
        
        // Horizontal line only
        NVGRenderer.roundedRect(cx - halfSize - halfGap, cy - halfThick, size + gap, thickness, halfThick, color);
    }
    
    private static void renderSwastika(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        float halfGap = gap / 2.0f;
        float armWidth = thickness;
        float armLength = size;
        float bendSize = thickness;
        
        // Center cross
        NVGRenderer.rect(cx - halfThick, cy - halfSize - halfGap, thickness, size + gap, color);
        NVGRenderer.rect(cx - halfSize - halfGap, cy - halfThick, size + gap, thickness, color);
        
        // Top-left bend
        NVGRenderer.rect(cx - armLength - halfGap, cy - halfThick - bendSize, bendSize, bendSize, color);
        // Top-right bend
        NVGRenderer.rect(cx + armLength - bendSize + halfGap, cy - halfThick - bendSize, bendSize, bendSize, color);
        // Bottom-left bend
        NVGRenderer.rect(cx - armLength - halfGap, cy + halfThick + armLength - bendSize, bendSize, bendSize, color);
        // Bottom-right bend
        NVGRenderer.rect(cx + armLength - bendSize + halfGap, cy + halfThick + armLength - bendSize, bendSize, bendSize, color);
    }
}
