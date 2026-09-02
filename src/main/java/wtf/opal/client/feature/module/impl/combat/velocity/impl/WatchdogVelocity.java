/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2596
 *  net.minecraft.class_2708
 *  net.minecraft.class_2743
 *  net.minecraft.class_3532
 */
package wtf.opal.client.feature.module.impl.combat.velocity.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_3532;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder.BlockHolder;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.InboundNetworkBlockage;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.impl.combat.velocity.VelocityMode;
import wtf.opal.client.feature.module.impl.combat.velocity.VelocityModule;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MoveInputEvent;
import wtf.opal.event.impl.game.packet.InstantaneousReceivePacketEvent;
import wtf.opal.event.impl.game.player.movement.knockback.VelocityUpdateEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.mixin.LivingEntityAccessor;
import wtf.opal.utility.misc.time.Stopwatch;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class WatchdogVelocity
extends VelocityMode {
    private final BooleanProperty delayUntilGround = (BooleanProperty)((Property)new BooleanProperty("Delay until ground", true).id("delayUntilGroundWatchdog")).hideIf(() -> ((VelocityModule)this.module).getActiveMode() != this);
    private final BlockHolder blockHolder = new BlockHolder(InboundNetworkBlockage.get());
    private final Stopwatch blockStopwatch = new Stopwatch();
    private boolean jump;
    private int sprintResetTicks;

    public WatchdogVelocity(VelocityModule module) {
        super(module);
        module.addProperties(this.delayUntilGround);
    }

    @Override
    public String getSuffix() {
        return "Watchdog";
    }

    public BooleanProperty getDelayUntilGround() {
        return this.delayUntilGround;
    }

    @Subscribe
    public void onInstantaneousReceivePacket(InstantaneousReceivePacketEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2743) {
            class_2743 velocity = (class_2743)class_25962;
            if (Constants.mc.field_1724 == null || velocity.method_11818() != Constants.mc.field_1724.method_5628() || !this.delayUntilGround.getValue().booleanValue()) {
                return;
            }
            if (((VelocityModule)this.getModule()).isInvalid()) {
                return;
            }
            this.blockHolder.block(null, InboundNetworkBlockage.VISUAL_VALIDATOR);
            this.blockStopwatch.reset();
        } else if (event.getPacket() instanceof class_2708) {
            this.blockHolder.release();
        }
    }

    @Subscribe
    public void onMoveInput(MoveInputEvent event) {
        if (this.jump) {
            ((LivingEntityAccessor)Constants.mc.field_1724).setJumpingCooldown(0);
            event.setJump(true);
            this.jump = false;
        }
    }

    @Subscribe
    public void onVelocityUpdate(VelocityUpdateEvent event) {
        if (((VelocityModule)this.module).isInvalid()) {
            return;
        }
        double velocityY = event.getVelocityY();
        if (event.isExplosion()) {
            this.blockHolder.release();
            return;
        }
        if (Constants.mc.field_1724.method_24828() && this.delayUntilGround.getValue().booleanValue() && LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer) {
            LivingEntityAccessor accessor = (LivingEntityAccessor)Constants.mc.field_1724;
            if (Constants.mc.field_1724.field_3913.field_54155.comp_3163() || (double)accessor.callGetJumpVelocity() < velocityY) {
                this.jump = true;
            }
        }
        this.sprintResetTicks = 10;
    }

    public boolean isSprintReset() {
        return this.sprintResetTicks > 0 && class_3532.method_15356((float)MoveUtility.getMoveYaw(), (float)MoveUtility.getDirectionDegrees()) >= 70.0f;
    }

    @Subscribe
    public void onScheduledExecutables(PreGameTickEvent event) {
        if (this.blockHolder.isBlocking() && (Constants.mc.field_1724 == null || Constants.mc.field_1724.method_24828() || Constants.mc.field_1724.method_6101() || Constants.mc.field_1724.method_52535() || this.blockStopwatch.hasTimeElapsed(1000L))) {
            this.blockHolder.release();
        }
        if (this.sprintResetTicks > 0) {
            --this.sprintResetTicks;
        }
    }

    @Override
    public void onDisable() {
        this.blockHolder.release();
        super.onDisable();
    }

    @Override
    public Enum<?> getEnumValue() {
        return VelocityModule.Mode.WATCHDOG;
    }
}

