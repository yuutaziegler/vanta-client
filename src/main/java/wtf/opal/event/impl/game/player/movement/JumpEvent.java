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
public final class JumpEvent
extends EventCancellable {
    private boolean sprinting;

    public JumpEvent(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }
}

