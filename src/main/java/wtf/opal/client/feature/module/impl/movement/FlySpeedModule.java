/*
 * Fly Speed Module - control creative/spectator fly speed from the menu.
 * Works by scaling the player abilities fly speed (the value vanilla uses
 * for creative flight), so it only affects actual flying - never walking.
 */
package wtf.opal.client.feature.module.impl.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1656;
import net.minecraft.class_2350;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class FlySpeedModule extends Module {

    private static final float DEFAULT_FLY_SPEED = 0.05f;

    private final NumberProperty speed = new NumberProperty("Fly Speed", 1.0, 0.1, 10.0, 0.1);
    private final BooleanProperty verticalEnabled = new BooleanProperty("Vertical Movement", true);
    private final NumberProperty verticalSpeed = new NumberProperty("Vertical Speed", 1.0, 0.1, 5.0, 0.1);
    private final BooleanProperty resetOnDisable = new BooleanProperty("Reset Speed On Disable", true);

    public FlySpeedModule() {
        super("Fly Speed", "Change how fast you fly (creative/spectator) right from the menu", ModuleCategory.MOVEMENT);
        this.addProperties(
            this.speed,
            this.verticalEnabled,
            this.verticalSpeed,
            this.resetOnDisable
        );
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        if (this.resetOnDisable.getValue()) {
            this.resetFlySpeed();
        }
    }

    private void resetFlySpeed() {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        class_1656 abilities = Constants.mc.field_1724.method_31549();
        if (abilities != null) {
            abilities.field_7481 = DEFAULT_FLY_SPEED;
        }
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        if (!this.isEnabled() || Constants.mc.field_1724 == null) {
            return;
        }
        class_1656 abilities = Constants.mc.field_1724.method_31549();
        if (abilities == null) {
            return;
        }
        if (!abilities.field_7479) {
            // Not flying (e.g. creative flight off): keep vanilla behaviour.
            if (abilities.field_7481 != DEFAULT_FLY_SPEED) {
                abilities.field_7481 = DEFAULT_FLY_SPEED;
            }
            return;
        }
        // Scale creative fly speed (vanilla default is 0.05).
        abilities.field_7481 = (float)(0.05 * this.speed.getValue());
        // Optional faster vertical movement while flying.
        if (this.verticalEnabled.getValue()) {
            double motionY = 0.0;
            if (Constants.mc.field_1690.field_1903.method_1434()) {
                motionY = this.verticalSpeed.getValue();
            } else if (Constants.mc.field_1690.field_1832.method_1434()) {
                motionY = -this.verticalSpeed.getValue();
            }
            if (motionY != 0.0) {
                Constants.mc.field_1724.method_18799(
                    Constants.mc.field_1724.method_18798().method_38499(class_2350.class_2351.field_11052, motionY)
                );
            }
        }
    }

    public double getSpeed() {
        return this.speed.getValue();
    }
}
