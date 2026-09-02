/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.notification;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.notification.Notification;
import wtf.opal.client.notification.NotificationType;

@Environment(value=EnvType.CLIENT)
public final class NotificationManager {
    private final List<Notification> notifications = new ArrayList<Notification>();

    public List<Notification> getNotifications() {
        return this.notifications;
    }

    public NotificationBuilder builder(NotificationType type) {
        return new NotificationBuilder(this, type);
    }

    private Notification publish(Notification notification) {
        System.out.println(notification.getTitle() + ": " + notification.getDescription());
        this.notifications.add(notification);
        return notification;
    }

    public void remove(Notification notification) {
        this.notifications.remove(notification);
    }

    @Environment(value=EnvType.CLIENT)
    public static class NotificationBuilder {
        private final NotificationManager dispatcher;
        private final NotificationType type;
        private String title;
        private String description;
        private int duration;

        private NotificationBuilder(NotificationManager dispatcher, NotificationType type) {
            this.dispatcher = dispatcher;
            this.type = type;
            this.title = "Notification";
            this.duration = 2000;
        }

        public NotificationBuilder title(String title) {
            this.title = title;
            return this;
        }

        public NotificationBuilder description(String description) {
            this.description = description;
            return this;
        }

        public NotificationBuilder duration(int duration) {
            this.duration = duration;
            return this;
        }

        public Notification buildAndPublish() {
            return this.dispatcher.publish(new Notification(this.type, this.title, this.description, this.duration));
        }
    }
}

