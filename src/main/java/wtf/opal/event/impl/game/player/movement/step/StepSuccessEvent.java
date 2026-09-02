/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_243
 */
package wtf.opal.event.impl.game.player.movement.step;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_243;
import wtf.opal.event.EventCancellable;

@Environment(value=EnvType.CLIENT)
public final class StepSuccessEvent
extends EventCancellable {
    private class_243 movement;
    private class_243 adjustedVec;

    public StepSuccessEvent(class_243 movement, class_243 adjustedVec) {
        this.movement = movement;
        this.adjustedVec = adjustedVec;
    }

    public class_243 getMovement() {
        return this.movement;
    }

    public void setAdjustedVec(class_243 adjustedVec) {
        this.adjustedVec = adjustedVec;
    }

    public void setMovement(class_243 movement) {
        this.movement = movement;
    }

    public class_243 getAdjustedVec() {
        return this.adjustedVec;
    }
}

