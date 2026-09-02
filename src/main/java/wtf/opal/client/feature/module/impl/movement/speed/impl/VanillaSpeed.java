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
import wtf.opal.client.feature.module.impl.movement.speed.SpeedModule;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.input.MoveInputEvent;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class VanillaSpeed
extends ModuleMode<SpeedModule> {
    private final NumberProperty speedProperty = (NumberProperty)new NumberProperty("Speed", this, 1.0, 0.1, 10.0, 0.1).hideIf(() -> ((SpeedModule)this.module).getActiveMode() != this);
    private final BooleanProperty autoJump = (BooleanProperty)new BooleanProperty("Auto jump", this, true).hideIf(() -> ((SpeedModule)this.module).getActiveMode() != this);

    public VanillaSpeed(SpeedModule module) {
        super(module);
    }

    @Override
    public Enum<?> getEnumValue() {
        return SpeedModule.Mode.VANILLA;
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        double speed = (Double)this.speedProperty.getValue();
        if (MoveUtility.isMoving()) {
            MoveUtility.setSpeed(speed);
        } else {
            MoveUtility.setSpeed(0.0);
        }
    }

    @Subscribe
    public void onMoveInput(MoveInputEvent event) {
        if (this.autoJump.getValue().booleanValue() && MoveUtility.isMoving()) {
            event.setJump(true);
        }
    }

    @Override
    public void onDisable() {
        super.onDisable();
        if (Constants.mc.field_1724 == null) {
            return;
        }
        double maxSpeed = MoveUtility.getSwiftnessSpeed(0.221);
        MoveUtility.setSpeed(Math.min(MoveUtility.getSpeed(), maxSpeed));
    }
}

