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
        
        float size = (float)module.getSize();
        float thickness = (float)module.getThickness();
        float gap = (float)module.getGap();
        float dotSize = (float)module.getDotSize();
        
        int color = module.getColor();
        
        // Apply shake reduction
        float shakeReduction = (float)module.getShakeReduction();
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
                renderCross(centerX, centerY, size, thickness, gap, color);
                break;
        }
        
        // Draw outline if enabled
        if (module.isOutline()) {
            int outlineColor = 0xFF000000 | (color & 0x00FFFFFF);
            float outlineThickness = (float)module.getOutlineThickness();
            
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
        
        // Top
        NVGRenderer.rect(cx - halfThick, cy - halfSize - gap, thickness, halfSize, color);
        // Bottom
        NVGRenderer.rect(cx - halfThick, cy + gap, thickness, halfSize, color);
        // Left
        NVGRenderer.rect(cx - halfSize - gap, cy - halfThick, halfSize, thickness, color);
        // Right
        NVGRenderer.rect(cx + gap, cy - halfThick, halfSize, thickness, color);
    }
    
    private static void renderCrossOutline(float cx, float cy, float size, float thickness, float gap, int color, float outlineThickness) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        
        // Top outline
        NVGRenderer.rectOutline(cx - halfThick - outlineThickness, cy - halfSize - gap - outlineThickness, 
                thickness + outlineThickness * 2, halfSize + outlineThickness * 2, outlineThickness, color);
        // Bottom outline
        NVGRenderer.rectOutline(cx - halfThick - outlineThickness, cy + gap - outlineThickness, 
                thickness + outlineThickness * 2, halfSize + outlineThickness * 2, outlineThickness, color);
        // Left outline
        NVGRenderer.rectOutline(cx - halfSize - gap - outlineThickness, cy - halfThick - outlineThickness, 
                halfSize + outlineThickness * 2, thickness + outlineThickness * 2, outlineThickness, color);
        // Right outline
        NVGRenderer.rectOutline(cx + gap - outlineThickness, cy - halfThick - outlineThickness, 
                halfSize + outlineThickness * 2, thickness + outlineThickness * 2, outlineThickness, color);
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
        NVGRenderer.roundedRectOutline(cx - radius - gap, cy - radius - gap, radius * 2, radius * 2, radius, thickness, color);
    }
    
    private static void renderSquare(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfGap = gap / 2.0f;
        NVGRenderer.roundedRectOutline(cx - halfSize - halfGap, cy - halfSize - halfGap, size, size, 2.0f, thickness, color);
    }
    
    private static void renderPlus(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        float halfGap = gap / 2.0f;
        
        NVGRenderer.rect(cx - halfThick, cy - halfSize - halfGap, thickness, size, color);
        NVGRenderer.rect(cx - halfSize - halfGap, cy - halfThick, size, thickness, color);
    }
    
    private static void renderMinus(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        NVGRenderer.rect(cx - halfSize - gap, cy - halfThick, size, thickness, color);
    }
    
    private static void renderSwastika(float cx, float cy, float size, float thickness, float gap, int color) {
        float halfSize = size / 2.0f;
        float halfThick = thickness / 2.0f;
        
        // Center cross
        NVGRenderer.rect(cx - halfThick, cy - halfSize, thickness, size * 2, color);
        NVGRenderer.rect(cx - halfSize, cy - halfThick, size * 2, thickness, color);
        
        // Arms
        NVGRenderer.rect(cx + halfThick, cy - halfSize, halfSize, thickness, color); // Top-right
        NVGRenderer.rect(cx - halfThick - halfSize, cy + halfSize - thickness, halfSize, thickness, color); // Bottom-left
        NVGRenderer.rect(cx - halfSize, cy - halfThick - halfSize, thickness, halfSize, color); // Left-top
        NVGRenderer.rect(cx + halfSize - thickness, cy + halfThick, thickness, halfSize, color); // Right-bottom
    }
}
