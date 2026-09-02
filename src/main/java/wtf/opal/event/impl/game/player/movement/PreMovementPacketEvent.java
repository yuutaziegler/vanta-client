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
public final class PreMovementPacketEvent
extends EventCancellable {
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean onGround;
    private boolean sprinting;
    private boolean horizontalCollision;
    private boolean forceInput;

    public PreMovementPacketEvent(double x, double y, double z, float yaw, float pitch, boolean onGround, boolean sprinting, boolean horizontalCollision) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.onGround = onGround;
        this.sprinting = sprinting;
        this.horizontalCollision = horizontalCollision;
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

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public float getYaw() {
        return this.yaw;
    }

    public float getPitch() {
        return this.pitch;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public boolean isOnGround() {
        return this.onGround;
    }

    public boolean isSprinting() {
        return this.sprinting;
    }

    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }

    public boolean isHorizontalCollision() {
        return this.horizontalCollision;
    }

    public void setHorizontalCollision(boolean horizontalCollision) {
        this.horizontalCollision = horizontalCollision;
    }

    public boolean isForceInput() {
        return this.forceInput;
    }

    public void setForceInput(boolean forceInput) {
        this.forceInput = forceInput;
    }
}

