/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.movement.noslow.NoSlowModule;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.math.RandomUtility;
import wtf.opal.utility.player.MoveUtility;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class StrafeModule
extends Module {
    private final MultipleBooleanProperty conditions = new MultipleBooleanProperty("Conditions", new BooleanProperty("On ground", true), new BooleanProperty("In air", true));
    private final BooleanProperty ncpMax = new BooleanProperty("NCP Max", false);
    private final NumberProperty strength = new NumberProperty("Strength", "%", 100.0, 1.0, 100.0, 1.0);

    public StrafeModule() {
        super("Strafe", "Makes you strafe more.", ModuleCategory.MOVEMENT);
        this.addProperties(this.conditions, this.strength, this.ncpMax);
    }

    @Subscribe(priority=999)
    public void onPostMove(PostMoveEvent event) {
        if (Constants.mc.field_1724 != null && MoveUtility.isMoving()) {
            boolean allowOnGround = this.conditions.getProperty("On ground").getValue();
            boolean allowInAir = this.conditions.getProperty("In air").getValue();
            if (!allowOnGround && !allowInAir) {
                return;
            }
            if (!allowOnGround && Constants.mc.field_1724.method_24828()) {
                return;
            }
            if (!allowInAir && !Constants.mc.field_1724.method_24828()) {
                return;
            }
            boolean itemSlowdown = Constants.mc.field_1724.method_6115() && !OpalClient.getInstance().getModuleRepository().getModule(NoSlowModule.class).isEnabled();
            double speed = MoveUtility.getSpeed();
            if (!(!this.ncpMax.getValue().booleanValue() || Constants.mc.field_1724.method_52535() || Constants.mc.field_1724.method_5715() || LocalDataWatch.get().ticksSinceTeleport <= 5 || PlayerUtility.isInsideBlock() || itemSlowdown)) {
                speed = Math.max(speed, MoveUtility.getSwiftnessSpeed(0.2873) - 1.0E-4 * RandomUtility.RANDOM.nextDouble());
            }
            MoveUtility.setSpeed(speed, (Double)this.strength.getValue());
        }
    }
}

