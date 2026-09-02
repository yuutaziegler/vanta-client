/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10182
 *  net.minecraft.class_2709
 */
package wtf.opal.event.impl.game.player.teleport;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10182;
import net.minecraft.class_2709;
import wtf.opal.event.EventCancellable;

@Environment(value=EnvType.CLIENT)
public final class PreTeleportEvent
extends EventCancellable {
    private final int teleportId;
    private class_10182 change;
    private final Set<class_2709> relatives;

    public PreTeleportEvent(int teleportId, class_10182 change, Set<class_2709> relatives) {
        this.teleportId = teleportId;
        this.change = change;
        this.relatives = relatives;
    }

    public int getTeleportId() {
        return this.teleportId;
    }

    public class_10182 getChange() {
        return this.change;
    }

    public void setChange(class_10182 change) {
        this.change = change;
    }

    public Set<class_2709> getRelatives() {
        return this.relatives;
    }
}

