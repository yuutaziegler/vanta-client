/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_243
 *  net.minecraft.class_2708
 *  net.minecraft.class_2828
 *  net.minecraft.class_746
 */
package wtf.opal.client.feature.module.impl.utility;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_243;
import net.minecraft.class_2708;
import net.minecraft.class_2828;
import net.minecraft.class_746;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder.BlockHolder;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.OutboundNetworkBlockage;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.movement.flight.FlightModule;
import wtf.opal.client.feature.module.impl.movement.longjump.LongJumpModule;
import wtf.opal.client.feature.module.impl.utility.nofall.NoFallModule;
import wtf.opal.client.feature.module.repository.ModuleRepository;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.mixin.ClientPlayerEntityAccessor;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class AntiVoidModule
extends Module {
    private final BlockHolder blockHolder = new BlockHolder(OutboundNetworkBlockage.get());
    private GroundStates overGroundStates;
    private boolean blinked;
    private boolean failed;
    private double startingY;

    public AntiVoidModule() {
        super("Anti Void", "Makes it impossible to fall into the void.", ModuleCategory.UTILITY);
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        boolean shouldRun;
        if (Constants.mc.field_1724 == null) {
            return;
        }
        ModuleRepository moduleRepository = OpalClient.getInstance().getModuleRepository();
        LongJumpModule longJumpModule = moduleRepository.getModule(LongJumpModule.class);
        boolean bl = shouldRun = !longJumpModule.isEnabled() && !moduleRepository.getModule(FlightModule.class).isEnabled() && !Constants.mc.field_1724.method_31549().field_7478 && !Constants.mc.field_1724.method_31549().field_7479;
        if (!shouldRun) {
            this.blockHolder.release();
            this.overGroundStates = null;
            this.failed = true;
            return;
        }
        if (PlayerUtility.isOverVoid()) {
            if (!this.failed) {
                if (Constants.mc.field_1724.method_23318() - this.startingY <= -6.0 && this.overGroundStates != null) {
                    this.overGroundStates.restoreStates(Constants.mc.field_1724);
                    NoFallModule noFallModule = OpalClient.getInstance().getModuleRepository().getModule(NoFallModule.class);
                    if (noFallModule.isEnabled()) {
                        noFallModule.syncFallDifference();
                    }
                    this.blockHolder.setPacketTransformer(p -> {
                        if (p instanceof class_2828) {
                            return null;
                        }
                        return p;
                    });
                    this.startingY = Constants.mc.field_1724.method_23318();
                    this.blockHolder.release();
                } else {
                    this.blockHolder.block();
                }
            }
            this.blinked = true;
        } else {
            ClientPlayerEntityAccessor accessor = (ClientPlayerEntityAccessor)Constants.mc.field_1724;
            this.overGroundStates = new GroundStates(Constants.mc.field_1724.method_73189(), new class_243(Constants.mc.field_1724.field_6014, Constants.mc.field_1724.field_6036, Constants.mc.field_1724.field_5969), Constants.mc.field_1724.method_18798(), accessor.getLastXClient(), accessor.getLastYClient(), accessor.getLastZClient(), accessor.getLastYawClient(), accessor.getLastPitchClient(), accessor.isLastOnGround(), accessor.getTicksSinceLastPositionPacketSent(), LocalDataWatch.get().airTicks, LocalDataWatch.get().groundTicks);
            this.startingY = Constants.mc.field_1724.method_23318();
            this.failed = false;
            if (this.blinked) {
                this.blockHolder.release();
                this.blinked = false;
            }
        }
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        if (event.getPacket() instanceof class_2708 && this.overGroundStates != null) {
            this.blockHolder.release();
            this.overGroundStates = null;
            this.failed = true;
        }
    }

    @Override
    protected void onDisable() {
        this.blockHolder.release();
    }

    @Environment(value=EnvType.CLIENT)
    private record GroundStates(class_243 position, class_243 lastPosition, class_243 velocity, double lastX, double lastBaseY, double lastZ, float lastYaw, float lastPitch, boolean lastOnGround, int ticksSinceLastPositionPacketSent, int airTicks, int groundTicks) {
        public void restoreStates(class_746 localPlayer) {
            ClientPlayerEntityAccessor accessor = (ClientPlayerEntityAccessor)localPlayer;
            accessor.setLastXClient(this.lastX);
            accessor.setLastYClient(this.lastBaseY);
            accessor.setLastZClient(this.lastZ);
            accessor.setLastYawClient(this.lastYaw);
            accessor.setLastPitchClient(this.lastPitch);
            accessor.setLastOnGround(this.lastOnGround);
            accessor.setTicksSinceLastPositionPacketSent(this.ticksSinceLastPositionPacketSent);
            localPlayer.method_33574(this.position);
            localPlayer.field_6014 = this.lastPosition.method_10216();
            localPlayer.field_6036 = this.lastPosition.method_10214();
            localPlayer.field_5969 = this.lastPosition.method_10215();
            localPlayer.method_18800(0.0, this.velocity.method_10214(), 0.0);
            LocalDataWatch.get().airTicks = this.airTicks;
            LocalDataWatch.get().groundTicks = this.groundTicks;
        }
    }
}

