/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.notification;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public enum NotificationType {
    SUCCESS("\ue5ca", -13710223),
    ERROR("\ue5cd", -1618884),
    WARN("\ue002", -142795),
    INFO("\ue88f", -9398321);

    private final String icon;
    private final int iconColor;

    private NotificationType(String icon, int iconColor) {
        this.icon = icon;
        this.iconColor = iconColor;
    }

    public String getIcon() {
        return this.icon;
    }

    public int getIconColor() {
        return this.iconColor;
    }
}

