/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.property.impl.mode;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.client.screen.click.dropdown.panel.property.impl.ModePropertyComponent;

@Environment(value=EnvType.CLIENT)
public final class ModeProperty<T extends Enum<T>>
extends Property<T> {
    private final T[] values;
    private Module module;
    private boolean theme;

    public ModeProperty(String name, T value) {
        super(name);
        this.setValue(value);
        this.values = this.getEnumConstants();
    }

    public ModeProperty(String name, T value, T[] values) {
        super(name);
        this.setValue(value);
        this.values = values;
    }

    public ModeProperty(String name, T value, boolean theme) {
        super(name);
        this.setValue(value);
        this.values = this.getEnumConstants();
        this.theme = theme;
    }

    public ModeProperty(String name, ModuleMode<?> parent, T value) {
        super(name, parent);
        this.setValue(value);
        this.values = this.getEnumConstants();
    }

    public ModeProperty(String name, Module module, T value) {
        super(name);
        this.setValue(value);
        this.values = this.getEnumConstants();
        this.module = module;
        module.setModeProperty(this);
    }

    private T[] getEnumConstants() {
        return (Enum[])((Enum)this.getValue()).getClass().getEnumConstants();
    }

    public T[] getValues() {
        return this.values;
    }

    public void setValueOrdinal(int value) {
        if (this.module != null && this.module.isEnabled()) {
            this.module.getModuleModes().forEach(ModuleMode::onDisable);
        }
        this.setValue(this.values[value]);
        if (this.module != null) {
            for (ModuleMode<?> mode : this.module.getModuleModes()) {
                if (mode.getEnumValue().ordinal() != value || !this.module.isEnabled()) continue;
                mode.onEnable();
                break;
            }
        }
    }

    public void cycle(boolean forwards) {
        int currentIndex = ((Enum)this.getValue()).ordinal();
        int nextIndex = (currentIndex + (forwards ? 1 : this.values.length - 1)) % this.values.length;
        this.setValueOrdinal(nextIndex);
    }

    public boolean isTheme() {
        return this.theme;
    }

    public boolean is(T value) {
        return this.getValue() == value;
    }

    @Override
    public PropertyPanel<?> createClickGUIComponent() {
        return new ModePropertyComponent(this);
    }

    @Override
    public void applyValue(Object propertyValue) {
        if (propertyValue instanceof String) {
            String valueString = (String)propertyValue;
            for (T possibleValue : this.values) {
                if (!((Enum)possibleValue).name().equals(valueString)) continue;
                this.setValueOrdinal(((Enum)possibleValue).ordinal());
                break;
            }
        }
    }
}

