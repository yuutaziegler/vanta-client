/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.movement.noslow.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.impl.movement.noslow.NoSlowModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.event.impl.game.player.movement.SlowdownEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class VanillaNoSlow
extends ModuleMode<NoSlowModule> {
    public VanillaNoSlow(NoSlowModule module) {
        super(module);
    }

    @Subscribe
    public void onSlowdown(SlowdownEvent event) {
        event.setCancelled();
    }

    @Override
    public Enum<?> getEnumValue() {
        return NoSlowModule.Mode.VANILLA;
    }
}

