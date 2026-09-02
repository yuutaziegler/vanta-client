/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_3414
 */
package wtf.opal.event.impl.game.world;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3414;
import wtf.opal.event.EventCancellable;

@Environment(value=EnvType.CLIENT)
public final class PlaySoundEvent
extends EventCancellable {
    private final class_3414 soundEvent;
    private final double x;
    private final double y;
    private final double z;

    public PlaySoundEvent(class_3414 soundEvent, double x, double y, double z) {
        this.soundEvent = soundEvent;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public class_3414 getSoundEvent() {
        return this.soundEvent;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

    public double getZ() {
        return this.z;
    }
}

