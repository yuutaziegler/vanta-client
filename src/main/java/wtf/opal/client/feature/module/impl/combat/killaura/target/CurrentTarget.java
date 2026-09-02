/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 */
package wtf.opal.client.feature.module.impl.combat.killaura.target;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import wtf.opal.client.feature.module.impl.combat.killaura.target.KillAuraTarget;
import wtf.opal.utility.player.RaytracedRotation;

@Environment(value=EnvType.CLIENT)
public final class CurrentTarget {
    private final KillAuraTarget target;
    private final RaytracedRotation rotations;

    public CurrentTarget(KillAuraTarget target, RaytracedRotation rotations) {
        this.target = target;
        this.rotations = rotations;
    }

    public KillAuraTarget getKillAuraTarget() {
        return this.target;
    }

    public class_1309 getEntity() {
        return this.getKillAuraTarget().getTarget().getEntity();
    }

    public RaytracedRotation getRotations() {
        return this.rotations;
    }
}

