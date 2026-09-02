/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.event.impl.game.player.movement.knockback;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.event.EventCancellable;

@Environment(value=EnvType.CLIENT)
public final class VelocityUpdateEvent
extends EventCancellable {
    private double velocityX;
    private double velocityY;
    private double velocityZ;
    private boolean explosion;

    public VelocityUpdateEvent(double velocityX, double velocityY, double velocityZ, boolean explosion) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.velocityZ = velocityZ;
        this.explosion = explosion;
    }

    public boolean isExplosion() {
        return this.explosion;
    }

    public double getVelocityX() {
        return this.velocityX;
    }

    public double getVelocityY() {
        return this.velocityY;
    }

    public double getVelocityZ() {
        return this.velocityZ;
    }

    public void setVelocityX(double velocityX) {
        this.velocityX = velocityX;
    }

    public void setVelocityY(double velocityY) {
        this.velocityY = velocityY;
    }

    public void setVelocityZ(double velocityZ) {
        this.velocityZ = velocityZ;
    }
}

