/*
 * Custom Crosshair Module for TerentX Client
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.color.ColorProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.render.RenderCrosshairEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class CustomCrosshairModule extends Module {
    
    // Basic settings
    private final BooleanProperty enabled = new BooleanProperty("Enabled", true);
    private final ModeProperty<CrosshairStyle> style = new ModeProperty<CrosshairStyle>("Style", CrosshairStyle.CROSS);
    private final ColorProperty color = new ColorProperty("Color", 0xFFFFFFFF);
    private final BooleanProperty dynamicColor = new BooleanProperty("Dynamic Color", false);
    private final ColorProperty dynamicColorTarget = new ColorProperty("Target Color", 0xFFFF5555);
    
    // Size settings
    private final NumberProperty size = new NumberProperty("Size", 10.0, 5.0, 30.0, 1.0);
    private final NumberProperty thickness = new NumberProperty("Thickness", 2.0, 1.0, 6.0, 0.5);
    private final NumberProperty gap = new NumberProperty("Gap", 4.0, 0.0, 15.0, 0.5);
    
    // Dot settings
    private final BooleanProperty showDot = new BooleanProperty("Show Dot", true);
    private final NumberProperty dotSize = new NumberProperty("Dot Size", 2.0, 1.0, 6.0, 0.5);
    
    // Outline settings
    private final BooleanProperty outline = new BooleanProperty("Outline", false);
    private final NumberProperty outlineThickness = new NumberProperty("Outline Width", 1.0, 1.0, 3.0, 0.5);
    
    // Advanced
    private final NumberProperty shakeReduction = new NumberProperty("Shake Reduction", 0.0, 0.0, 1.0, 0.1);
    private final BooleanProperty smoothAnimation = new BooleanProperty("Smooth", true);
    
    public CustomCrosshairModule() {
        super("Custom Crosshair", "Customize your crosshair with pixel-perfect control", ModuleCategory.VISUAL);
        this.addProperties(
            this.enabled,
            this.style,
            this.color,
            new GroupProperty("Dynamic", this.dynamicColor, this.dynamicColorTarget),
            new GroupProperty("Size", this.size, this.thickness, this.gap),
            new GroupProperty("Dot", this.showDot, this.dotSize),
            new GroupProperty("Outline", this.outline, this.outlineThickness),
            new GroupProperty("Advanced", this.shakeReduction, this.smoothAnimation)
        );
    }

    public boolean isEnabled() {
        return this.enabled.getValue();
    }

    public CrosshairStyle getStyle() {
        return this.style.getValue();
    }

    public int getColor() {
        return this.color.getValue();
    }

    public boolean isDynamicColor() {
        return this.dynamicColor.getValue();
    }

    public int getDynamicColorTarget() {
        return this.dynamicColorTarget.getValue();
    }

    public float getSize() {
        return this.size.getValue().floatValue();
    }

    public float getThickness() {
        return this.thickness.getValue().floatValue();
    }

    public float getGap() {
        return this.gap.getValue().floatValue();
    }

    public boolean isShowDot() {
        return this.showDot.getValue();
    }

    public float getDotSize() {
        return this.dotSize.getValue().floatValue();
    }

    public boolean isOutline() {
        return this.outline.getValue();
    }

    public float getOutlineThickness() {
        return this.outlineThickness.getValue().floatValue();
    }

    public float getShakeReduction() {
        return this.shakeReduction.getValue().floatValue();
    }

    public boolean isSmoothAnimation() {
        return this.smoothAnimation.getValue();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum CrosshairStyle {
        CROSS("Cross"),
        DOT("Dot"),
        CROSS_DOT("Cross + Dot"),
        CIRCLE("Circle"),
        SQUARE("Square"),
        SWASTIKA("Swastika"),
        PLUS("Plus"),
        MINUS("Minus"),
        CUSTOM("Custom");

        private final String name;

        private CrosshairStyle(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}
