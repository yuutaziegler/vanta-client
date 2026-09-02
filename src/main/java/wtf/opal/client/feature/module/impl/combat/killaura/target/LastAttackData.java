/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.combat.killaura.target;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.utility.misc.time.Stopwatch;

@Environment(value=EnvType.CLIENT)
public final class LastAttackData {
    private final Stopwatch stopwatch = new Stopwatch();
    private double damage;

    public LastAttackData(double damage) {
        this.reset(false, damage);
    }

    public void reset(boolean reset, double damage) {
        if (reset) {
            this.stopwatch.reset();
        }
        this.damage = damage;
    }

    public long getTime() {
        return this.stopwatch.getTime();
    }

    public double getDamage() {
        return this.damage;
    }
}

