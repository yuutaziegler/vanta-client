/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.event.impl.game.player.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.event.EventCancellable;

@Environment(value=EnvType.CLIENT)
public final class SlowdownEvent
extends EventCancellable {
    private float slowdown;

    public SlowdownEvent(float slowdown) {
        this.slowdown = slowdown;
    }

    public void setSlowdown(float slowdown) {
        this.slowdown = slowdown;
    }

    public float getSlowdown() {
        return this.slowdown;
    }
}

