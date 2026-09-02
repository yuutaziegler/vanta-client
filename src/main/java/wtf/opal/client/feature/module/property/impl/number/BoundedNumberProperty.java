/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.internal.LinkedTreeMap
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.property.impl.number;

import com.google.gson.internal.LinkedTreeMap;
import com.ibm.icu.impl.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.client.screen.click.dropdown.panel.property.impl.BoundedNumberPropertyComponent;
import wtf.opal.utility.misc.math.MathUtility;
import wtf.opal.utility.misc.math.RandomUtility;

@Environment(value=EnvType.CLIENT)
public final class BoundedNumberProperty
extends Property<Pair<Double, Double>> {
    private final double minValue;
    private final double maxValue;
    private final double increment;
    private String suffix;

    public BoundedNumberProperty(String name, double defaultValue, double defaultValue2, double minValue, double maxValue, double increment) {
        super(name);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
        this.setValue((Pair<Double, Double>)Pair.of((Object)defaultValue, (Object)defaultValue2));
    }

    public BoundedNumberProperty(String name, ModuleMode<?> parent, double defaultValue, double defaultValue2, double minValue, double maxValue, double increment) {
        super(name, parent);
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.increment = increment;
        this.setValue((Pair<Double, Double>)Pair.of((Object)defaultValue, (Object)defaultValue2));
    }

    public BoundedNumberProperty(String name, String suffix, double defaultValue, double defaultValue2, double minValue, double maxValue, double increment) {
        this(name, defaultValue, defaultValue2, minValue, maxValue, increment);
        this.suffix = suffix;
    }

    public BoundedNumberProperty(String name, ModuleMode<?> parent, String suffix, double defaultValue, double defaultValue2, double minValue, double maxValue, double increment) {
        this(name, parent, defaultValue, defaultValue2, minValue, maxValue, increment);
        this.suffix = suffix;
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

    public Double getMidpoint() {
        return ((Double)((Pair)this.getValue()).first + (Double)((Pair)this.getValue()).second) / 2.0;
    }

    public Double getRandomValue() {
        return RandomUtility.getRandomDouble((Double)((Pair)this.getValue()).first, (Double)((Pair)this.getValue()).second);
    }

    @Override
    public void setValue(Pair<Double, Double> value) {
        super.setValue(Pair.of((Object)MathUtility.roundAndClamp((Number)value.first, this.minValue, this.maxValue, this.increment).doubleValue(), (Object)MathUtility.roundAndClamp((Number)value.second, this.minValue, this.maxValue, this.increment).doubleValue()));
    }

    @Override
    public void applyValue(Object propertyValue) {
        if (propertyValue instanceof LinkedTreeMap) {
            LinkedTreeMap jsonProperty = (LinkedTreeMap)propertyValue;
            if (jsonProperty.isEmpty()) {
                return;
            }
            Object value1 = jsonProperty.get((Object)"x");
            Object value2 = jsonProperty.get((Object)"y");
            if (value1 instanceof Double) {
                Double val1 = (Double)value1;
                if (value2 instanceof Double) {
                    Double val2 = (Double)value2;
                    double boundedValue1 = MathUtility.roundAndClamp(val1, this.minValue, this.maxValue, this.increment).doubleValue();
                    double boundedValue2 = MathUtility.roundAndClamp(val2, this.minValue, this.maxValue, this.increment).doubleValue();
                    this.setValue((Pair<Double, Double>)Pair.of((Object)boundedValue1, (Object)boundedValue2));
                }
            }
        }
    }

    @Override
    public PropertyPanel<?> createClickGUIComponent() {
        return new BoundedNumberPropertyComponent(this);
    }
}

