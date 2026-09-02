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
public final class ClipAtLedgeEvent {
    private boolean updated;
    private boolean clip;

    public boolean isUpdated() {
        return this.updated;
    }

    public boolean isClip() {
        return this.clip;
    }

    public void setClip(boolean clip) {
        this.updated = true;
        this.clip = clip;
    }
}

