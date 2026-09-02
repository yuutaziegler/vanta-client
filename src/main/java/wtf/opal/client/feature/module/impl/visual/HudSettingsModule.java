/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;

@Environment(value=EnvType.CLIENT)
public final class HudSettingsModule
extends Module {
    public static HudSettingsModule INSTANCE;
    private final NumberProperty cornerRadius = new NumberProperty("Corner Radius", 10.0, 0.0, 24.0, 0.5);
    private final BooleanProperty glassEffect = new BooleanProperty("Glass Effect", true);
    private final BooleanProperty shadowEnabled = new BooleanProperty("Shadows", true);
    private final NumberProperty glassOpacity = new NumberProperty("Glass Opacity", 0.14, 0.05, 0.4, 0.01);
    private final NumberProperty borderOpacity = new NumberProperty("Border Opacity", 0.18, 0.0, 0.6, 0.01);
    private final BooleanProperty specularHighlight = new BooleanProperty("Specular", true);
    private final BooleanProperty frostedGlass = new BooleanProperty("Frosted Blur", true);
    private final NumberProperty animationSpeed = new NumberProperty("Animation Speed", 0.15, 0.05, 1.0, 0.01);

    public HudSettingsModule() {
        super("HUD Settings", "Configure the visual style of all UI panels.", ModuleCategory.VISUAL);
        INSTANCE = this;
        this.addProperties(this.cornerRadius, this.glassEffect, this.shadowEnabled, this.glassOpacity, this.borderOpacity, this.specularHighlight, this.frostedGlass, this.animationSpeed);
        this.setEnabled(true);
    }

    public float getCornerRadius() {
        return ((Double)this.cornerRadius.getValue()).floatValue();
    }

    public boolean isGlassEffect() {
        return this.glassEffect.getValue();
    }

    public boolean isShadowEnabled() {
        return this.shadowEnabled.getValue();
    }

    public float getGlassOpacity() {
        return ((Double)this.glassOpacity.getValue()).floatValue();
    }

    public float getBorderOpacity() {
        return ((Double)this.borderOpacity.getValue()).floatValue();
    }

    public boolean isSpecularHighlight() {
        return this.specularHighlight.getValue();
    }

    public boolean isFrostedGlass() {
        return this.frostedGlass.getValue();
    }

    public float getAnimationSpeed() {
        return ((Double)this.animationSpeed.getValue()).floatValue();
    }
}

