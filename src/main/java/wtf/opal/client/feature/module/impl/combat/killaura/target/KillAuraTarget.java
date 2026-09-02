/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.client.feature.module.impl.combat.killaura.target;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.target.impl.TargetLivingEntity;
import wtf.opal.client.feature.module.impl.combat.killaura.target.LastAttackData;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class KillAuraTarget {
    private final TargetLivingEntity target;
    @Nullable
    private LastAttackData lastAttackData;

    public KillAuraTarget(TargetLivingEntity target) {
        this.target = target;
    }

    public TargetLivingEntity getTarget() {
        return this.target;
    }

    public void onAttack(boolean reset) {
        double damage = this.getDamage();
        if (this.lastAttackData == null) {
            this.lastAttackData = new LastAttackData(damage);
        } else {
            this.lastAttackData.reset(reset, damage);
        }
    }

    public boolean isAttackAvailable() {
        if (this.lastAttackData == null) {
            return true;
        }
        double damage = this.getDamage();
        if ((double)this.target.getFullHealth() <= damage) {
            return true;
        }
        return this.lastAttackData.getTime() >= 470L || damage > this.lastAttackData.getDamage();
    }

    public double getDamage() {
        double damage = PlayerUtility.getStackAttackDamage(Constants.mc.field_1724.method_6047());
        if (damage < 0.5) {
            damage = 0.5;
        }
        if (PlayerUtility.isCriticalHitAvailable() && Constants.mc.field_1724.field_6017 > 0.0) {
            damage *= 1.5;
        }
        return damage;
    }

    @Nullable
    public LastAttackData getLastAttackData() {
        return this.lastAttackData;
    }
}

