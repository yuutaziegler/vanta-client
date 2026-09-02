/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.movement.speed.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.module.impl.movement.speed.SpeedModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.event.impl.game.input.MoveInputEvent;
import wtf.opal.event.impl.game.player.movement.JumpEvent;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class MushMCSpeed
extends ModuleMode<SpeedModule> {
    private int offset;
    private double speed;

    public MushMCSpeed(SpeedModule module) {
        super(module);
    }

    @Override
    public Enum<?> getEnumValue() {
        return SpeedModule.Mode.MUSHMC;
    }

    @Subscribe
    public void onJump(JumpEvent event) {
        if (this.offset > 0) {
            event.setCancelled();
        } else {
            event.setSprinting(true);
        }
    }

    @Subscribe
    public void onMoveInput(MoveInputEvent event) {
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        if (MoveUtility.isMoving()) {
            double speed = MoveUtility.getSpeed();
            if (Constants.mc.field_1724.method_24828()) {
                if (Constants.mc.field_1724.method_18798().method_10214() < 0.0) {
                    if (this.offset == 0 && LocalDataWatch.get().groundTicks > 1) {
                        speed = this.speed;
                        this.speed = Math.min(this.speed + 0.05, 0.5);
                        this.offset = 2;
                    }
                } else {
                    Constants.mc.field_1724.method_18799(Constants.mc.field_1724.method_18798().method_1023(0.0, 0.02, 0.0));
                    this.resetSpeed();
                }
            } else {
                int airTicks = LocalDataWatch.get().airTicks;
                if (airTicks == 4) {
                    Constants.mc.field_1724.method_18799(Constants.mc.field_1724.method_18798().method_1023(0.0, 0.2, 0.0));
                } else if (airTicks == 5) {
                    Constants.mc.field_1724.method_18799(Constants.mc.field_1724.method_18798().method_1023(0.0, 0.13, 0.0));
                }
                this.resetSpeed();
            }
            if (speed < 0.29) {
                speed = 0.29;
            }
            MoveUtility.setSpeed(speed);
        } else {
            MoveUtility.setSpeed(0.0);
            this.resetSpeed();
        }
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        if (this.offset > 0) {
            if (Constants.mc.field_1724.method_24828()) {
                if (this.offset == 2) {
                    event.setY(event.getY() + 0.0625);
                    event.setOnGround(false);
                }
                --this.offset;
            } else {
                this.offset = 0;
            }
        }
    }

    @Override
    public void onEnable() {
        this.resetSpeed();
        super.onEnable();
    }

    private void resetSpeed() {
        this.speed = Math.max(0.29, Constants.mc.field_1724 == null ? 0.0 : MoveUtility.getSpeed());
    }
}

