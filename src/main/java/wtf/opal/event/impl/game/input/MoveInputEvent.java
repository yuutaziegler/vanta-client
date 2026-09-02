/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.event.impl.game.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class MoveInputEvent {
    private float forward;
    private float sideways;
    private boolean jump;
    private boolean sneak;

    public MoveInputEvent(float forward, float sideways, boolean jump, boolean sneak) {
        this.forward = forward;
        this.sideways = sideways;
        this.jump = jump;
        this.sneak = sneak;
    }

    public float getForward() {
        return this.forward;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public boolean isJump() {
        return this.jump;
    }

    public void setForward(float forward) {
        this.forward = forward;
    }

    public float getSideways() {
        return this.sideways;
    }

    public void setSideways(float sideways) {
        this.sideways = sideways;
    }

    public boolean isSneak() {
        return this.sneak;
    }

    public void setSneak(boolean sneak) {
        this.sneak = sneak;
    }
}

