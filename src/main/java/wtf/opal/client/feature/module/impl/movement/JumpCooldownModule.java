/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.player.movement.JumpingCooldownEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class JumpCooldownModule
extends Module {
    private final NumberProperty maxCooldown = new NumberProperty("Max cooldown", 0.0, 0.0, 9.0, 1.0);

    public JumpCooldownModule() {
        super("Jump Cooldown", "Modifies the vanilla jump cooldown.", ModuleCategory.MOVEMENT);
        this.addProperties(this.maxCooldown);
    }

    @Subscribe
    public void onJumpingCooldown(JumpingCooldownEvent event) {
        event.setCooldown(((Double)this.maxCooldown.getValue()).intValue());
    }
}

