/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.internal.LinkedTreeMap
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.property.impl;

import com.google.gson.internal.LinkedTreeMap;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.property.IPropertyListProvider;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.client.screen.click.dropdown.panel.property.impl.GroupPropertyComponent;

@Environment(value=EnvType.CLIENT)
public final class GroupProperty
extends Property<List<Property<?>>>
implements IPropertyListProvider {
    private final BooleanSupplier enabledSupplier;

    public GroupProperty(String name, BooleanSupplier enabledSupplier, Property<?> ... children) {
        super(name);
        this.enabledSupplier = enabledSupplier;
        this.setValue(Arrays.stream(children).filter(Objects::nonNull).toList());
    }

    public GroupProperty(String name, Property<?> ... children) {
        this(name, (BooleanSupplier)null, children);
    }

    @Override
    public List<Property<?>> getPropertyList() {
        return (List)this.getValue();
    }

    @Override
    public PropertyPanel<?> createClickGUIComponent() {
        return new GroupPropertyComponent(this);
    }

    public boolean isEnabled() {
        return this.enabledSupplier != null && this.enabledSupplier.getAsBoolean();
    }

    @Override
    public void applyValue(Object propertyValue) {
        if (propertyValue instanceof List) {
            List groupValues = (List)propertyValue;
            for (Object jsonGroupPropObj : groupValues) {
                LinkedTreeMap jsonGroupProp = (LinkedTreeMap)jsonGroupPropObj;
                String groupName = (String)jsonGroupProp.get((Object)"name");
                Object groupValue = jsonGroupProp.get((Object)"value");
                for (Property<?> clientGroupProp : this.getPropertyList()) {
                    if (!groupName.equals(clientGroupProp.getName())) continue;
                    clientGroupProp.applyValue(groupValue);
                }
            }
        }
    }
}

