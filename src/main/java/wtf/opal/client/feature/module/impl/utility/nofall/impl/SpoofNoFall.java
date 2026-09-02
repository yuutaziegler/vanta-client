/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.utility.nofall.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.impl.utility.nofall.NoFallModule;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class SpoofNoFall
extends ModuleMode<NoFallModule> {
    private final BooleanProperty noGround = (BooleanProperty)new BooleanProperty("No ground", this, false).hideIf(() -> !((NoFallModule)this.module).mode.is(NoFallModule.Mode.SPOOF));

    public SpoofNoFall(NoFallModule module) {
        super(module);
    }

    @Override
    public Enum<?> getEnumValue() {
        return NoFallModule.Mode.SPOOF;
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        if (this.noGround.getValue().booleanValue()) {
            event.setOnGround(false);
        } else if (((NoFallModule)this.module).getFallDifference() >= (double)PlayerUtility.getMaxFallDistance()) {
            ((NoFallModule)this.module).syncFallDifference();
            event.setOnGround(true);
        }
    }
}

