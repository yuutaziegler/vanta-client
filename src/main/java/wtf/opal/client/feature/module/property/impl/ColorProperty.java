/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.property.impl;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.client.screen.click.dropdown.panel.property.impl.ColorPropertyComponent;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public final class ColorProperty
extends Property<Integer> {
    private final float[] hsb = new float[3];

    public ColorProperty(String name, int value) {
        super(name);
        this.setValue(value);
    }

    public ColorProperty(String name, ModuleMode<?> parent, int value) {
        super(name, parent);
        this.setValue(value);
    }

    public float[] getHSB() {
        return this.hsb;
    }

    public void setHue(float hue) {
        this.hsb[0] = hue;
    }

    public void setSaturation(float saturation) {
        this.hsb[1] = saturation;
    }

    public void setBrightness(float brightness) {
        this.hsb[2] = brightness;
    }

    public void updateValue() {
        this.setValue(Color.HSBtoRGB(this.hsb[0], this.hsb[1], this.hsb[2]));
    }

    @Override
    public PropertyPanel<?> createClickGUIComponent() {
        return new ColorPropertyComponent(this);
    }

    @Override
    public void applyValue(Object propertyValue) {
        double colorHex = Double.parseDouble(String.valueOf(propertyValue));
        int[] rgba = ColorUtility.hexToRGB((int)colorHex);
        float[] hsb = Color.RGBtoHSB(rgba[0], rgba[1], rgba[2], null);
        this.setHue(hsb[0]);
        this.setSaturation(hsb[1]);
        this.setBrightness(hsb[2]);
        this.updateValue();
    }
}

