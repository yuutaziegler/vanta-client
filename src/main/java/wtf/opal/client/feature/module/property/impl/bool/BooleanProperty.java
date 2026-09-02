/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.property.impl.bool;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.client.screen.click.dropdown.panel.property.impl.BooleanPropertyComponent;

@Environment(value=EnvType.CLIENT)
public final class BooleanProperty
extends Property<Boolean> {
    public BooleanProperty(String name, boolean value) {
        super(name);
        this.setValue(value);
    }

    public BooleanProperty(String name, ModuleMode<?> parent, boolean value) {
        super(name, parent);
        this.setValue(value);
    }

    public void toggle() {
        this.setValue(this.getValue() == false);
    }

    @Override
    public Boolean getValue() {
        return (Boolean)super.getValue() != false && !this.isHidden();
    }

    @Override
    public PropertyPanel<?> createClickGUIComponent() {
        return new BooleanPropertyComponent(this);
    }

    @Override
    public void applyValue(Object propertyValue) {
        this.setValue(Boolean.parseBoolean(String.valueOf(propertyValue)));
    }
}

