/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 *  com.google.gson.annotations.SerializedName
 *  com.mojang.logging.LogUtils
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.property;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mojang.logging.LogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.property.IPropertyUpdateListener;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.client.PropertyUpdateEvent;

@Environment(value=EnvType.CLIENT)
public class Property<T> {
    private final String name;
    @Expose
    @SerializedName(value="name")
    private String id;
    @Expose
    @SerializedName(value="value")
    private T value;
    private boolean focused;
    private BooleanSupplier hiddenSupplier;
    private List<IPropertyUpdateListener<T>> propertyUpdateListeners;

    protected Property(String name) {
        this.name = name;
        this.id = name;
    }

    protected Property(String name, ModuleMode<?> parent) {
        this.name = name;
        this.id = name;
        Object module = parent.getModule();
        ModeProperty<?> modeProperty = ((Module)module).getModeProperty();
        if (modeProperty == null) {
            LogUtils.getLogger().error(((Module)module).getName() + " does not have a mode property.");
            return;
        }
        for (Enum e : modeProperty.getValues()) {
            if (!e.equals(parent.getEnumValue())) continue;
            ((Module)module).addProperties(this);
            break;
        }
    }

    public final <R extends Property<T>> R hideIf(BooleanSupplier hiddenSupplier) {
        this.hiddenSupplier = hiddenSupplier;
        return (R)this;
    }

    public final String getName() {
        return this.name;
    }

    public final String getId() {
        return this.id;
    }

    public final <R extends Property<T>> R id(String id) {
        this.id = id;
        return (R)this;
    }

    public T getValue() {
        return this.value;
    }

    public void setValue(T value) {
        T prevValue = this.value;
        this.value = value;
        EventDispatcher.dispatch(new PropertyUpdateEvent(this));
        this.callChangeListeners(prevValue, value);
    }

    public final boolean isHidden() {
        return this.hiddenSupplier != null && this.hiddenSupplier.getAsBoolean();
    }

    public PropertyPanel<?> createClickGUIComponent() {
        return null;
    }

    public final <P extends Property<T>> P addUpdateListener(IPropertyUpdateListener<T> listener) {
        if (this.propertyUpdateListeners == null) {
            this.propertyUpdateListeners = new ArrayList<IPropertyUpdateListener<T>>(1);
        }
        this.propertyUpdateListeners.add(listener);
        return (P)this;
    }

    public final <P extends Property<T>> P addUpdateListener(Runnable listener) {
        if (this.propertyUpdateListeners == null) {
            this.propertyUpdateListeners = new ArrayList<IPropertyUpdateListener<T>>(1);
        }
        this.propertyUpdateListeners.add((p, v) -> listener.run());
        return (P)this;
    }

    public final void callChangeListeners(T prevValue, T value) {
        if (this.propertyUpdateListeners != null) {
            this.propertyUpdateListeners.forEach(l -> l.onChange(prevValue, value));
        }
    }

    public final boolean isFocused() {
        return this.focused;
    }

    public final void setFocused(boolean focused) {
        this.focused = focused;
    }

    public void applyValue(Object propertyValue) {
    }
}

