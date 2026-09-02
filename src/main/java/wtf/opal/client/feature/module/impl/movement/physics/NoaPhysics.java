/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.movement.physics;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class NoaPhysics {
    public static final double TICK_DELTA = 0.03333333333333333;
    public double impulse;
    public double force;
    public double velocity;
    public double gravity = -10.0;
    private final double mass = 1.0;

    public double getMotionForTick() {
        double massDiv = 1.0 / this.mass;
        this.force *= massDiv;
        this.force += this.gravity;
        this.force *= 2.0;
        this.impulse *= massDiv;
        this.force *= 0.03333333333333333;
        this.impulse += this.force;
        this.velocity += this.impulse;
        this.force = 0.0;
        this.impulse = 0.0;
        return this.velocity;
    }
}

