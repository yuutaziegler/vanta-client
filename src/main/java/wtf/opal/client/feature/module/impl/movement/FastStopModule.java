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
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class FastStopModule
extends Module {
    private final MultipleBooleanProperty conditions = new MultipleBooleanProperty("Conditions", new BooleanProperty("On ground", true), new BooleanProperty("In air", true));
    private final NumberProperty multiplier = new NumberProperty("Multiplier", "x", 0.5, 0.0, 1.0, 0.05);

    public FastStopModule() {
        super("Fast Stop", "Makes you stop moving faster.", ModuleCategory.MOVEMENT);
        this.addProperties(this.conditions, this.multiplier);
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        if (!(Constants.mc.field_1724.method_18798().method_10216() == 0.0 && Constants.mc.field_1724.method_18798().method_10215() == 0.0 || MoveUtility.isMoving())) {
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
            if (Constants.mc.field_1724.field_6235 != 0) {
                return;
            }
            Constants.mc.field_1724.method_18799(Constants.mc.field_1724.method_18798().method_18805(((Double)this.multiplier.getValue()).doubleValue(), 1.0, ((Double)this.multiplier.getValue()).doubleValue()));
        }
    }
}

