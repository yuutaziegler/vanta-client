/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.hypixel.data.type.GameType
 *  net.minecraft.class_1657
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 */
package wtf.opal.client.feature.module.impl.utility.nofall.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hypixel.data.type.GameType;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder.BlockHolder;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.InboundNetworkBlockage;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.OutboundNetworkBlockage;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.impl.utility.nofall.NoFallModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.feature.simulation.PlayerSimulation;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.game.player.movement.PostMovementPacketEvent;
import wtf.opal.event.impl.game.player.movement.PreMoveEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.mixin.ClientPlayerEntityAccessor;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class WatchdogNoFall
extends ModuleMode<NoFallModule> {
    private final BlockHolder inboundHolder = new BlockHolder(InboundNetworkBlockage.get());
    private final BlockHolder outboundHolder = new BlockHolder(OutboundNetworkBlockage.get());
    private class_243 prevMotion;
    private class_243 nextPos;
    private boolean blocked;

    public WatchdogNoFall(NoFallModule module) {
        super(module);
    }

    private void block() {
        this.inboundHolder.block();
        this.outboundHolder.block();
        this.blocked = true;
    }

    private void release() {
        this.inboundHolder.release();
        this.outboundHolder.release();
    }

    @Subscribe
    public void onPreMove(PreMoveEvent event) {
        HypixelServer.ModAPI.Location currentLocation;
        if (this.nextPos != null) {
            Constants.mc.field_1724.method_23327(this.nextPos.method_10216(), this.nextPos.method_10214(), this.nextPos.method_10215());
            this.nextPos = null;
            return;
        }
        if (LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer && (currentLocation = HypixelServer.ModAPI.get().getCurrentLocation()) != null && (currentLocation.isLobby() || currentLocation.serverType() == GameType.PIT || currentLocation.serverType() == GameType.WOOL_GAMES || currentLocation.serverType() == GameType.MURDER_MYSTERY)) {
            return;
        }
        if (this.isGoingToFall()) {
            return;
        }
        double fallDistance = ((NoFallModule)this.module).getFallDifference() - (Constants.mc.field_1724.method_18798().method_10214() - 0.08) * (double)0.98f;
        if (fallDistance >= (double)PlayerUtility.getMaxFallDistance()) {
            this.prevMotion = Constants.mc.field_1724.method_18798();
        }
    }

    private boolean isGoingToFall() {
        if (Constants.mc.field_1724.method_24828()) {
            return true;
        }
        if (PlayerUtility.isOverVoid()) {
            PlayerSimulation simulation = new PlayerSimulation((class_1657)Constants.mc.field_1724);
            for (int i = 0; i < 14; ++i) {
                simulation.simulateTick();
                if (PlayerUtility.isOverVoid(simulation.getSimulatedEntity().method_5829())) continue;
                return false;
            }
            return true;
        }
        return false;
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        if (this.prevMotion != null) {
            class_243 velocity = Constants.mc.field_1724.method_18798().method_1021(0.5);
            if (PlayerUtility.isBoxEmpty(Constants.mc.field_1724.method_5829().method_989(velocity.method_10216(), velocity.method_10214(), velocity.method_10215()))) {
                this.nextPos = Constants.mc.field_1724.method_73189().method_1019(velocity);
            }
            Constants.mc.field_1724.method_18800(0.0, 0.0, 0.0);
        }
    }

    @Subscribe(priority=-5)
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        if (this.prevMotion != null) {
            double diffZ;
            double diffY;
            boolean moved;
            ClientPlayerEntityAccessor accessor = (ClientPlayerEntityAccessor)Constants.mc.field_1724;
            double diffX = event.getX() - accessor.getLastXClient();
            boolean bl = moved = class_3532.method_41190((double)diffX, (double)(diffY = event.getY() - accessor.getLastYClient()), (double)(diffZ = event.getZ() - accessor.getLastZClient())) > class_3532.method_33723((double)2.0E-4);
            if (!moved) {
                int ticksSinceLastPositionPacketSent = accessor.getTicksSinceLastPositionPacketSent();
                if (ticksSinceLastPositionPacketSent >= 20) {
                    accessor.setTicksSinceLastPositionPacketSent(18);
                }
                event.setOnGround(true);
                ((NoFallModule)this.module).syncFallDifference();
                this.block();
            }
            Constants.mc.field_1724.method_18799(this.prevMotion);
            this.prevMotion = null;
        }
    }

    @Subscribe
    public void onPostMovementPacket(PostMovementPacketEvent event) {
        if (this.nextPos != null) {
            class_243 nextPos = this.nextPos;
            this.nextPos = Constants.mc.field_1724.method_73189();
            Constants.mc.field_1724.method_23327(nextPos.method_10216(), nextPos.method_10214(), nextPos.method_10215());
        }
        if (this.blocked) {
            this.blocked = false;
        } else {
            this.release();
        }
    }

    @Override
    public void onDisable() {
        this.release();
        this.blocked = false;
        this.nextPos = null;
        super.onDisable();
    }

    @Override
    public Enum<?> getEnumValue() {
        return NoFallModule.Mode.WATCHDOG;
    }
}

