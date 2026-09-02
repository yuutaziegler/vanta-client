/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.internal.LinkedTreeMap
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.property.impl.bool;

import com.google.gson.internal.LinkedTreeMap;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.client.screen.click.dropdown.panel.property.impl.MultipleBooleanPropertyComponent;

@Environment(value=EnvType.CLIENT)
public final class MultipleBooleanProperty
extends Property<List<BooleanProperty>> {
    private int subPropertyIndex;

    public MultipleBooleanProperty(String name, BooleanProperty ... booleanProperties) {
        super(name);
        this.setValue(Arrays.asList(booleanProperties));
    }

    public MultipleBooleanProperty(String name, ModuleMode<?> parent, BooleanProperty ... booleanProperties) {
        super(name, parent);
        this.setValue(Arrays.asList(booleanProperties));
    }

    public BooleanProperty getProperty(String name) {
        return ((List)this.getValue()).stream().filter(booleanProperty -> booleanProperty.getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public void applyValue(Object propertyValue) {
        if (propertyValue instanceof List) {
            List jsonProperties = (List)propertyValue;
            for (Object jsonPropertyObj : jsonProperties) {
                LinkedTreeMap jsonProperty = (LinkedTreeMap)jsonPropertyObj;
                String propertyName = (String)jsonProperty.get((Object)"name");
                Object propertyVal = jsonProperty.get((Object)"value");
                for (BooleanProperty booleanProperty : (List)this.getValue()) {
                    if (!propertyName.equals(booleanProperty.getId())) continue;
                    booleanProperty.setValue(Boolean.parseBoolean(String.valueOf(propertyVal)));
                }
            }
        }
    }

    public int getSubPropertyIndex() {
        return this.subPropertyIndex;
    }

    public void setSubPropertyIndex(int subPropertyIndex) {
        this.subPropertyIndex = subPropertyIndex;
    }

    public void cycleSubPropertyIndex() {
        if (!((List)this.getValue()).isEmpty()) {
            this.subPropertyIndex = (this.subPropertyIndex + 1) % ((List)this.getValue()).size();
        }
    }

    public BooleanProperty getSelectedSubProperty() {
        return (BooleanProperty)((List)this.getValue()).get(this.subPropertyIndex);
    }

    @Override
    public PropertyPanel<?> createClickGUIComponent() {
        return new MultipleBooleanPropertyComponent(this);
    }
}

