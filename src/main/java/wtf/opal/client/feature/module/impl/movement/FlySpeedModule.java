/*
 * Fly Speed Module - Control fly speed from menu
 */
package wtf.opal.client.feature.module.impl.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2350;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class FlySpeedModule extends Module {
    
    private final BooleanProperty enabled = new BooleanProperty("Enabled", true);
    private final NumberProperty speed = new NumberProperty("Speed", 1.0, 0.1, 10.0, 0.1);
    private final BooleanProperty verticalEnabled = new BooleanProperty("Vertical Movement", true);
    private final NumberProperty verticalSpeed = new NumberProperty("Vertical Speed", 1.0, 0.1, 5.0, 0.1);
    private final BooleanProperty resetOnDisable = new BooleanProperty("Reset Speed On Disable", true);
    
    public FlySpeedModule() {
        super("Fly Speed", "Control your fly speed in creative mode", ModuleCategory.MOVEMENT);
        this.addProperties(
            this.enabled,
            this.speed,
            this.verticalEnabled,
            this.verticalSpeed,
            this.resetOnDisable
        );
    }

    @Override
    protected void onEnable() {
        // Speed is applied in PostMoveEvent
    }

    @Override
    protected void onDisable() {
        if (this.resetOnDisable.getValue() && Constants.mc.field_1724 != null) {
            // Reset to default speed
            double defaultSpeed = MoveUtility.getSwiftnessSpeed(0.221);
            MoveUtility.setSpeed(Math.min(MoveUtility.getSpeed(), defaultSpeed));
        }
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        if (!this.enabled.getValue() || Constants.mc.field_1724 == null) {
            return;
        }
        
        // Only apply when player is in creative or spectator mode
        if (!Constants.mc.field_1724.method_24576() && !Constants.mc.field_1724.method_5833()) {
            return;
        }
        
        double horizontalSpeed = this.speed.getValue();
        double vertSpeed = this.verticalEnabled.getValue() ? this.verticalSpeed.getValue() : 0.0;
        
        double motionY = 0.0;
        if (Constants.mc.field_1690.field_1903.method_1434()) {
            motionY = vertSpeed;
        } else if (Constants.mc.field_1690.field_1832.method_1434()) {
            motionY = -vertSpeed;
        }
        
        if (MoveUtility.isMoving()) {
            MoveUtility.setSpeed(horizontalSpeed);
        } else {
            MoveUtility.setSpeed(0.0);
        }
        
        Constants.mc.field_1724.method_18799(
            Constants.mc.field_1724.method_18798().method_38499(class_2350.class_2351.field_11052, motionY)
        );
    }

    public boolean isEnabled() {
        return this.enabled.getValue();
    }

    public double getSpeed() {
        return this.speed.getValue();
    }

    public boolean isVerticalEnabled() {
        return this.verticalEnabled.getValue();
    }

    public double getVerticalSpeed() {
        return this.verticalSpeed.getValue();
    }
}
