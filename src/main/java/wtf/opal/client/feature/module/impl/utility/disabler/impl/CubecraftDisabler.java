/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2670
 *  net.minecraft.class_6373
 */
package wtf.opal.client.feature.module.impl.utility.disabler.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2670;
import net.minecraft.class_6373;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder.BlockHolder;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.InboundNetworkBlockage;
import wtf.opal.client.feature.module.impl.utility.disabler.DisablerModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.event.impl.game.JoinWorldEvent;
import wtf.opal.event.impl.game.packet.InstantaneousReceivePacketEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.impl.game.player.teleport.PreTeleportEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.mixin.CommonPingS2CPacketAccessor;
import wtf.opal.utility.misc.time.Stopwatch;

@Environment(value=EnvType.CLIENT)
public final class CubecraftDisabler
extends ModuleMode<DisablerModule> {
    private final BlockHolder blockHolder = new BlockHolder(InboundNetworkBlockage.get());
    private final Stopwatch flagStopwatch = new Stopwatch();
    private boolean cancel;

    public CubecraftDisabler(DisablerModule module) {
        super(module);
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        if (this.flagStopwatch.hasTimeElapsed(200L)) {
            this.blockHolder.block(p -> p, p -> p instanceof class_6373);
        } else {
            this.blockHolder.release();
        }
        if (this.flagStopwatch.hasTimeElapsed(3000L)) {
            event.setY(event.getY() + 11.0);
        }
    }

    @Subscribe
    public void onInstantaneousReceivePacket(InstantaneousReceivePacketEvent event) {
        if (event.getPacket() instanceof class_2670) {
            event.setCancelled();
        } else {
            Object object = event.getPacket();
            if (object instanceof class_6373) {
                class_6373 ping = (class_6373)object;
                object = (CommonPingS2CPacketAccessor)ping;
            }
        }
    }

    @Subscribe
    public void onPreTeleport(PreTeleportEvent event) {
        this.blockHolder.release();
        this.flagStopwatch.reset();
        this.cancel = true;
    }

    @Subscribe
    public void onJoinWorld(JoinWorldEvent event) {
        this.blockHolder.release();
        this.cancel = true;
    }

    @Override
    public void onDisable() {
        this.blockHolder.release();
        super.onDisable();
    }

    @Override
    public Enum<?> getEnumValue() {
        return DisablerModule.Mode.CUBECRAFT;
    }
}

