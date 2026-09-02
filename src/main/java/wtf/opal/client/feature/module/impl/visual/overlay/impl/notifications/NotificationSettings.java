/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.notifications;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;

@Environment(value=EnvType.CLIENT)
public final class NotificationSettings {
    private final BooleanProperty enabled = new BooleanProperty("Enabled", true);
    private final BooleanProperty moduleToggleNotifications = new BooleanProperty("On module toggle", false);

    NotificationSettings(OverlayModule module) {
        module.addProperties(new GroupProperty("Notifications", this.moduleToggleNotifications));
    }

    public boolean isEnabled() {
        return this.enabled.getValue();
    }

    public boolean isModuleToggleNotifications() {
        return this.moduleToggleNotifications.getValue();
    }
}

