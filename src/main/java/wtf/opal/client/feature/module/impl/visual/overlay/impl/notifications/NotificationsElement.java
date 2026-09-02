/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1041
 *  net.minecraft.class_332
 *  org.lwjgl.nanovg.NanoVG
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.notifications;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_332;
import org.lwjgl.nanovg.NanoVG;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.notifications.NotificationSettings;
import wtf.opal.client.notification.Notification;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class NotificationsElement
implements IOverlayElement {
    private static final NVGTextRenderer ICON_FONT = FontRepository.getFont("materialicons-regular");
    private static final NVGTextRenderer TITLE_FONT = FontRepository.getFont("productsans-bold");
    private static final NVGTextRenderer DESCRIPTION_FONT = FontRepository.getFont("productsans-medium");
    private final Map<Notification, Animation> animations = new HashMap<Notification, Animation>();
    private final NotificationSettings settings;

    public NotificationsElement(OverlayModule module) {
        this.settings = new NotificationSettings(module);
    }

    public NotificationSettings getSettings() {
        return this.settings;
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        List<Notification> notifications = OpalClient.getInstance().getNotificationManager().getNotifications();
        float padding = 3.0f;
        float height = 21.0f;
        float iconSize = 14.0f;
        float iconOffset = 17.0f;
        class_1041 window = Constants.mc.method_22683();
        float scaledWidth = window.method_4486();
        float scaledHeight = window.method_4502();
        for (int i = 0; i < notifications.size(); ++i) {
            Notification notification = notifications.get(i);
            Animation animation = this.animations.computeIfAbsent(notification, n -> new Animation(Easing.EASE_OUT_EXPO, 400L));
            float width = Math.max(100.0f, 17.0f + Math.max(TITLE_FONT.getStringWidth(notification.getTitle(), 7.0f) + 12.0f, DESCRIPTION_FONT.getStringWidth(notification.getDescription(), 7.5f)));
            float endX = scaledWidth - width - 3.0f;
            if (!notification.hasExpired()) {
                animation.setStartValue(scaledWidth);
            }
            animation.run(notification.hasExpired() ? scaledWidth : endX);
            float x = animation.getValue();
            float y = scaledHeight - 6.0f - (float)(i + 1) * 24.0f;
            float progress = (float)notification.getTime() / (float)notification.getDuration();
            int iconColor = notification.getType().getIconColor();
            NVGRenderer.roundedRect(x, y, width, 21.0f, 4.0f, NVGRenderer.BLUR_PAINT);
            NVGRenderer.roundedRect(x, y, width, 21.0f, 4.0f, -2146891511);
            NanoVG.nvgShapeAntiAlias((long)Constants.VG, (boolean)false);
            NVGRenderer.roundedRectVaryingGradient(x + 0.5f, y + 21.0f - 4.0f, (width - 0.5f) * progress, 4.0f, 0.0f, 0.0f, progress > 0.95f ? 4.0f : 0.0f, 4.0f, 2, ColorUtility.applyOpacity(iconColor, 0.25f), 90.0f);
            NanoVG.nvgShapeAntiAlias((long)Constants.VG, (boolean)true);
            NVGRenderer.roundedRect(x + 3.0f - 0.5f, y + 1.5f + 0.5f, 17.0f, 17.0f, 2.75f, ColorUtility.applyOpacity(ColorUtility.darker(iconColor, 0.6f), 0.5f));
            ICON_FONT.drawString(notification.getType().getIcon(), x + 3.0f + 1.25f, y + 9.0f + 8.5f, 14.0f, iconColor);
            TITLE_FONT.drawString(notification.getTitle(), x + 6.0f + 17.0f, y + 9.0f, 7.0f, -1);
            DESCRIPTION_FONT.drawString(notification.getDescription(), x + 6.0f + 17.0f, y + 9.0f + 7.5f, 6.5f, -5592406);
            if (!notification.hasExpired() || animation.getValue() != scaledWidth) continue;
            notifications.remove(notification);
            this.animations.remove(notification);
        }
    }

    @Override
    public boolean isActive() {
        return !Constants.mc.method_53526().method_53536() && this.settings.isEnabled();
    }

    @Override
    public boolean isBloom() {
        return true;
    }
}

