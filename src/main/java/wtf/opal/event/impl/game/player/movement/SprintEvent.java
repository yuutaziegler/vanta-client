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

@Environment(value=EnvType.CLIENT)
public final class SprintEvent {
    private boolean canStartSprinting;

    public SprintEvent(boolean canStartSprinting) {
        this.canStartSprinting = canStartSprinting;
    }

    public boolean isCanStartSprinting() {
        return this.canStartSprinting;
    }

    public void setCanStartSprinting(boolean canStartSprinting) {
        this.canStartSprinting = canStartSprinting;
    }
}

