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
import wtf.opal.client.feature.module.impl.movement.flight.FlightModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.input.MoveInputEvent;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class VanillaFlight
extends ModuleMode<FlightModule> {
    private final NumberProperty speedProperty = (NumberProperty)new NumberProperty("Speed", this, 1.0, 0.1, 10.0, 0.1).hideIf(() -> ((FlightModule)this.module).getActiveMode() != this);

    public VanillaFlight(FlightModule module) {
        super(module);
    }

    @Override
    public Enum<?> getEnumValue() {
        return FlightModule.Mode.VANILLA;
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
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

    @Subscribe
    public void onMoveInput(MoveInputEvent event) {
        event.setSneak(false);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (Constants.mc.field_1724 == null) {
            return;
        }
        double maxSpeed = MoveUtility.getSwiftnessSpeed(0.221);
        MoveUtility.setSpeed(Math.min(MoveUtility.getSpeed(), maxSpeed));
        Constants.mc.field_1724.method_18799(Constants.mc.field_1724.method_18798().method_38499(class_2350.class_2351.field_11052, 0.0));
    }
}

