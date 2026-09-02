/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.jetbrains.annotations.NotNull
 */
package wtf.opal.client.feature.module.property.impl.number;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.client.screen.click.dropdown.panel.property.impl.NumberPropertyComponent;
import wtf.opal.utility.misc.math.MathUtility;

@Environment(value=EnvType.CLIENT)
public final class NumberProperty
extends Property<Double> {
    private final Double minValue;
    private final Double maxValue;
    private final Double increment;
    private String suffix;

    public NumberProperty(String name, double defaultValue, double minValue, double maxValue, double increment) {
        super(name);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
        this.setValue(defaultValue);
    }

    public NumberProperty(String name, ModuleMode<?> parent, double defaultValue, double minValue, double maxValue, double increment) {
        super(name, parent);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
        this.setValue(defaultValue);
    }

    public NumberProperty(String name, String suffix, double defaultValue, double minValue, double maxValue, double increment) {
        this(name, defaultValue, minValue, maxValue, increment);
        this.suffix = suffix;
    }

    public NumberProperty(String name, String suffix, ModuleMode<?> parent, double defaultValue, double minValue, double maxValue, double increment) {
        this(name, parent, defaultValue, minValue, maxValue, increment);
        this.suffix = suffix;
    }

    public double getIncrement() {
        return this.increment;
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    public double getMinValue() {
        return this.minValue;
    }

    public String getSuffix() {
        return this.suffix;
    }

    @Override
    public void setValue(@NotNull Double value) {
        super.setValue(MathUtility.roundAndClamp(value, this.minValue, this.maxValue, this.increment).doubleValue());
    }

    @Override
    public PropertyPanel<?> createClickGUIComponent() {
        return new NumberPropertyComponent(this);
    }

    @Override
    public void applyValue(Object propertyValue) {
        this.setValue(Double.parseDouble(String.valueOf(propertyValue)));
    }
}

