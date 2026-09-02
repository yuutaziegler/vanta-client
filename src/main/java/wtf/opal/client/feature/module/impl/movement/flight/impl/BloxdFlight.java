/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2350$class_2351
 */
package wtf.opal.client.feature.module.impl.movement.flight.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2350;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.module.impl.movement.flight.FlightModule;
import wtf.opal.client.feature.module.impl.movement.physics.PhysicsModule;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.game.player.movement.step.StepEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class BloxdFlight
extends ModuleMode<FlightModule> {
    private final NumberProperty speedProperty = (NumberProperty)((Property)new NumberProperty("Speed", this, 1.0, 0.1, 10.0, 0.1).id("speedBloxd")).hideIf(() -> ((FlightModule)this.module).getActiveMode() != this);
    private final NumberProperty heightProperty = (NumberProperty)((Property)new NumberProperty("Height", this, 1.0, 0.1, 5.0, 0.1).id("heightBloxd")).hideIf(() -> ((FlightModule)this.module).getActiveMode() != this);

    public BloxdFlight(FlightModule module) {
        super(module);
    }

    @Override
    public Enum<?> getEnumValue() {
        return FlightModule.Mode.BLOXD;
    }

    private boolean isVelocityExempted() {
        return !LocalDataWatch.get().velocityStopwatch.hasTimeElapsed(1300L);
    }

    @Subscribe(priority=4)
    public void onPostMove(PostMoveEvent event) {
        PhysicsModule physicsModule;
        if (!this.isVelocityExempted() && (physicsModule = OpalClient.getInstance().getModuleRepository().getModule(PhysicsModule.class)).isEnabled() && Constants.mc.field_1724.field_5976) {
            physicsModule.getPhysics().velocity = LocalDataWatch.get().airTicks <= 1 ? 8.0 : 30.0 * (Double)this.heightProperty.getValue();
        }
    }

    @Subscribe(priority=-1)
    public void onStep(StepEvent event) {
        event.setStepHeight(0.0f);
    }

    @Subscribe
    public void onPostMoveLow(PostMoveEvent event) {
        if (this.isVelocityExempted()) {
            double speed = (Double)this.speedProperty.getValue();
            double motionY = 0.0;
            if (Constants.mc.field_1690.field_1903.method_1434()) {
                motionY = speed;
            } else if (Constants.mc.field_1690.field_1832.method_1434()) {
                motionY = -speed;
            }
            if (MoveUtility.isMoving()) {
                MoveUtility.setSpeed(speed);
            } else {
                MoveUtility.setSpeed(0.0);
            }
            Constants.mc.field_1724.method_18799(Constants.mc.field_1724.method_18798().method_38499(class_2350.class_2351.field_11052, motionY));
        }
    }
}

