/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2596
 *  net.minecraft.class_2743
 */
package wtf.opal.client.feature.module.impl.combat.velocity.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;
import net.minecraft.class_2743;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder.BlockHolder;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.InboundNetworkBlockage;
import wtf.opal.client.feature.module.impl.combat.velocity.VelocityMode;
import wtf.opal.client.feature.module.impl.combat.velocity.VelocityModule;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MoveInputEvent;
import wtf.opal.event.impl.game.packet.InstantaneousReceivePacketEvent;
import wtf.opal.event.impl.game.player.movement.knockback.VelocityUpdateEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.mixin.LivingEntityAccessor;
import wtf.opal.utility.misc.time.Stopwatch;

@Environment(value=EnvType.CLIENT)
public final class NormalVelocity
extends VelocityMode {
    private final NumberProperty horizontal = (NumberProperty)new NumberProperty("Horizontal", "%", 0.0, 0.0, 100.0, 1.0).hideIf(() -> ((VelocityModule)this.module).getActiveMode() != this);
    private final NumberProperty vertical = (NumberProperty)new NumberProperty("Vertical", "%", 100.0, 0.0, 100.0, 1.0).hideIf(() -> ((VelocityModule)this.module).getActiveMode() != this);
    private final BooleanProperty onlyWhileTargeting = (BooleanProperty)new BooleanProperty("Only while targeting", false).hideIf(() -> ((VelocityModule)this.module).getActiveMode() != this);
    private final BooleanProperty delayUntilGround = (BooleanProperty)((Property)new BooleanProperty("Delay until ground", false).id("delayUntilGroundNormal")).hideIf(() -> ((VelocityModule)this.module).getActiveMode() != this);
    private final BooleanProperty jumpOnGround = (BooleanProperty)new BooleanProperty("Jump on ground", false).hideIf(() -> ((VelocityModule)this.module).getActiveMode() != this);
    private final BlockHolder blockHolder = new BlockHolder(InboundNetworkBlockage.get());
    private final Stopwatch blockStopwatch = new Stopwatch();
    private boolean jump;

    public NormalVelocity(VelocityModule module) {
        super(module);
        module.addProperties(this.horizontal, this.vertical, this.onlyWhileTargeting, this.delayUntilGround, this.jumpOnGround);
    }

    @Override
    public String getSuffix() {
        return ((Double)this.horizontal.getValue()).intValue() + " " + ((Double)this.vertical.getValue()).intValue();
    }

    @Subscribe
    public void onInstantaneousReceivePacket(InstantaneousReceivePacketEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2743) {
            class_2743 velocity = (class_2743)class_25962;
            if (Constants.mc.field_1724 == null || velocity.method_11818() != Constants.mc.field_1724.method_5628() || !this.delayUntilGround.getValue().booleanValue()) {
                return;
            }
            if (Constants.mc.field_1724.method_24828()) {
                return;
            }
            this.blockHolder.block();
            this.blockStopwatch.reset();
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

    public NumberProperty getVertical() {
        return this.vertical;
    }

    public NumberProperty getHorizontal() {
        return this.horizontal;
    }

    @Subscribe
    public void onVelocityUpdate(VelocityUpdateEvent event) {
        if (((VelocityModule)this.module).isInvalid()) {
            return;
        }
        double horizontal = (Double)this.horizontal.getValue() / 100.0;
        double vertical = (Double)this.vertical.getValue() / 100.0;
        event.setCancelled();
        if (!event.isExplosion() && horizontal == 0.0 && vertical == 0.0) {
            return;
        }
        double velocityX = event.getVelocityX() * horizontal;
        double velocityY = event.getVelocityY() * vertical;
        double velocityZ = event.getVelocityZ() * horizontal;
        if (Constants.mc.field_1724.method_24828() && this.jumpOnGround.getValue().booleanValue()) {
            this.jump = true;
        }
        if (horizontal != 0.0) {
            Constants.mc.field_1724.method_18800(velocityX, Constants.mc.field_1724.method_18798().method_10214(), velocityZ);
        }
        if (vertical != 0.0) {
            Constants.mc.field_1724.method_18800(Constants.mc.field_1724.method_18798().method_10216(), velocityY, Constants.mc.field_1724.method_18798().method_10215());
        }
    }

    @Subscribe
    public void onScheduledExecutables(PreGameTickEvent event) {
        if (this.blockHolder.isBlocking() && (Constants.mc.field_1724 == null || Constants.mc.field_1724.method_24828() || Constants.mc.field_1724.method_52535() || Constants.mc.field_1724.method_6101() || this.blockStopwatch.hasTimeElapsed(1000L))) {
            this.blockHolder.release();
        }
    }

    @Override
    public void onDisable() {
        this.blockHolder.release();
        super.onDisable();
    }

    public BooleanProperty getDelayUntilGround() {
        return this.delayUntilGround;
    }

    @Override
    public Enum<?> getEnumValue() {
        return VelocityModule.Mode.NORMAL;
    }
}

