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
import wtf.opal.client.notification.NotificationType;
import wtf.opal.utility.misc.time.Stopwatch;

@Environment(value=EnvType.CLIENT)
public final class Notification {
    private final Stopwatch timer = new Stopwatch();
    private final NotificationType type;
    private final String title;
    private final String description;
    private final int duration;

    Notification(NotificationType type, String title, String description, int duration) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.duration = duration;
    }

    public NotificationType getType() {
        return this.type;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean hasExpired() {
        return this.timer.hasTimeElapsed(this.duration, false);
    }

    public long getTime() {
        return this.timer.getTime();
    }
}

