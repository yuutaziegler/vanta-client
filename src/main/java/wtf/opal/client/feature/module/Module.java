/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 *  com.google.gson.annotations.SerializedName
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.OpalClient;
import wtf.opal.client.binding.IBindable;
import wtf.opal.client.feature.helper.impl.render.ScreenPositionManager;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.misc.WebhookLoggerModule;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.notifications.NotificationSettings;
import wtf.opal.client.feature.module.property.IPropertyListProvider;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.notification.NotificationType;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.client.ModuleToggleEvent;
import wtf.opal.event.subscriber.IEventSubscriber;

@Environment(value=EnvType.CLIENT)
public class Module
implements IBindable,
IPropertyListProvider,
IEventSubscriber {
    private final String name;
    private final String description;
    @Expose
    @SerializedName(value="name")
    private final String id;
    private final ModuleCategory category;
    @Expose
    @SerializedName(value="enabled")
    private boolean enabled;
    @Expose
    @SerializedName(value="visible")
    private boolean visible = true;
    @Expose
    @SerializedName(value="properties")
    private final List<Property<?>> propertyList = new ArrayList();
    private final List<ModuleMode<?>> moduleModeList = new ArrayList();
    private ModeProperty<?> modeProperty;
    private boolean expanded;
    private int propertyIndex;

    protected Module(String name, String description, ModuleCategory category) {
        this.name = name;
        this.id = name.toLowerCase().replace(' ', '_');
        this.description = description;
        this.category = category;
        EventDispatcher.subscribe(this);
    }

    public final void setEnabled(boolean enabled) {
        if (this.enabled == enabled) {
            return;
        }
        ModuleToggleEvent event = new ModuleToggleEvent(this, enabled);
        EventDispatcher.dispatch(event);
        if (event.isCancelled()) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
        try {
            WebhookLoggerModule.onModuleToggle(this.name, enabled);
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public final void toggle() {
        NotificationSettings notificationSettings;
        this.setEnabled(!this.isEnabled());
        OverlayModule overlayModule = OpalClient.getInstance().getModuleRepository().getModule(OverlayModule.class);
        if (overlayModule.isEnabled() && (notificationSettings = overlayModule.getNotifications().getSettings()).isEnabled() && notificationSettings.isModuleToggleNotifications()) {
            OpalClient.getInstance().getNotificationManager().builder(NotificationType.INFO).duration(1000).title(this.name).description("Module " + (this.enabled ? "enabled." : "disabled.")).buildAndPublish();
        }
    }

    protected void onEnable() {
        if (this.getActiveMode() != null) {
            this.getActiveMode().onEnable();
        }
    }

    protected void onDisable() {
        if (this.getActiveMode() != null) {
            this.getActiveMode().onDisable();
        }
    }

    public final String getName() {
        return this.name;
    }

    public final String getId() {
        return this.id;
    }

    public final String getDescription() {
        return this.description;
    }

    public final ModuleCategory getCategory() {
        return this.category;
    }

    public final boolean isEnabled() {
        return this.enabled;
    }

    public final boolean isVisible() {
        return this.visible;
    }

    public final void setVisible(boolean visible) {
        this.visible = visible;
    }

    public final void addProperties(Property<?> ... properties) {
        for (Property<?> property : properties) {
            if (property == null) continue;
            this.propertyList.add(property);
            if (property instanceof ScreenPositionProperty) {
                ScreenPositionProperty screenPositionProperty = (ScreenPositionProperty)property;
                ScreenPositionManager.getInstance().register(this, screenPositionProperty);
                continue;
            }
            if (!(property instanceof GroupProperty)) continue;
            GroupProperty group = (GroupProperty)property;
            for (Property<?> groupProperty : group.getPropertyList()) {
                if (!(groupProperty instanceof ScreenPositionProperty)) continue;
                ScreenPositionProperty screenPositionProperty = (ScreenPositionProperty)groupProperty;
                ScreenPositionManager.getInstance().register(this, screenPositionProperty);
            }
        }
    }

    @SafeVarargs
    public final <T extends Module> void addModuleModes(ModeProperty<?> modeProperty, ModuleMode<T> ... modes) {
        this.modeProperty = modeProperty;
        Collections.addAll(this.moduleModeList, modes);
    }

    public final List<ModuleMode<?>> getModuleModes() {
        return this.moduleModeList;
    }

    public final ModuleMode<?> getActiveMode() {
        return this.moduleModeList.stream().filter(m -> m.getEnumValue().equals(this.modeProperty.getValue())).findFirst().orElse(null);
    }

    public final ModeProperty<?> getModeProperty() {
        return this.modeProperty;
    }

    public final void setModeProperty(ModeProperty<?> modeProperty) {
        this.modeProperty = modeProperty;
    }

    public String getSuffix() {
        return null;
    }

    public final boolean isExpanded() {
        return this.expanded;
    }

    public final int getPropertyIndex() {
        return this.propertyIndex;
    }

    public final void setPropertyIndex(int propertyIndex) {
        this.propertyIndex = propertyIndex;
    }

    public final void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    @Override
    public final List<Property<?>> getPropertyList() {
        return this.propertyList;
    }

    @Override
    public final void onBindingInteraction() {
        this.toggle();
    }

    @Override
    public final boolean isHandlingEvents() {
        return this.enabled;
    }
}

