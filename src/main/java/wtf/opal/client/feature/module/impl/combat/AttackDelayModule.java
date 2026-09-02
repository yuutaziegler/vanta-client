/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.combat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.player.interaction.AttackDelayEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class AttackDelayModule
extends Module {
    private final NumberProperty maxCooldown = new NumberProperty("Max cooldown", 0.0, 0.0, 9.0, 1.0);

    public AttackDelayModule() {
        super("Attack Delay", "Removes the delay after missing an attack.", ModuleCategory.COMBAT);
        this.addProperties(this.maxCooldown);
    }

    @Subscribe
    public void onAttackCooldown(AttackDelayEvent event) {
        event.setDelay(((Double)this.maxCooldown.getValue()).intValue());
    }
}

