/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.property.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.client.screen.click.dropdown.panel.property.impl.StringPropertyComponent;

@Environment(value=EnvType.CLIENT)
public final class StringProperty
extends Property<String> {
    public StringProperty(String name, String value) {
        super(name);
        this.setValue(value);
    }

    public StringProperty(String name, ModuleMode<?> parent, String value) {
        super(name, parent);
        this.setValue(value);
    }

    @Override
    public void applyValue(Object propertyValue) {
        this.setValue(String.valueOf(propertyValue));
    }

    @Override
    public PropertyPanel<?> createClickGUIComponent() {
        return new StringPropertyComponent(this);
    }
}

