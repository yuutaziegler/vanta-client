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
import wtf.opal.client.feature.module.property.impl.ColorProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;

@Environment(value=EnvType.CLIENT)
public final class CustomCrosshairModule extends Module {
    
    // Basic settings
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
            this.style,
            this.color,
            new GroupProperty("Dynamic", this.dynamicColor, this.dynamicColorTarget),
            new GroupProperty("Size", this.size, this.thickness, this.gap),
            new GroupProperty("Dot", this.showDot, this.dotSize),
            new GroupProperty("Outline", this.outline, this.outlineThickness),
            new GroupProperty("Advanced", this.shakeReduction, this.smoothAnimation)
        );
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

    public double getSize() {
        return this.size.getValue();
    }

    public double getThickness() {
        return this.thickness.getValue();
    }

    public double getGap() {
        return this.gap.getValue();
    }

    public boolean isShowDot() {
        return this.showDot.getValue();
    }

    public double getDotSize() {
        return this.dotSize.getValue();
    }

    public boolean isOutline() {
        return this.outline.getValue();
    }

    public double getOutlineThickness() {
        return this.outlineThickness.getValue();
    }

    public double getShakeReduction() {
        return this.shakeReduction.getValue();
    }

    public boolean isSmoothAnimation() {
        return this.smoothAnimation.getValue();
    }

    @Environment(value=EnvType.CLIENT)
    public enum CrosshairStyle {
        CROSS("Cross"),
        DOT("Dot"),
        CROSS_DOT("Cross + Dot"),
        CIRCLE("Circle"),
        SQUARE("Square"),
        PLUS("Plus"),
        MINUS("Minus"),
        SWASTIKA("Swastika"),
        CUSTOM("Custom");

        private final String name;

        CrosshairStyle(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }
    }
}
