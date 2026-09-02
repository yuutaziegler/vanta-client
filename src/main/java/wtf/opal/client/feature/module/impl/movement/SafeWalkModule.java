/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1747
 */
package wtf.opal.client.feature.module.impl.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1747;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.event.impl.game.player.movement.ClipAtLedgeEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class SafeWalkModule
extends Module {
    private final BooleanProperty holdingBlockCheck = new BooleanProperty("Holding block check", false);
    private final BooleanProperty directionCheck = new BooleanProperty("Direction check", false);

    public SafeWalkModule() {
        super("Safe Walk", "Allows you to move around without falling off blocks.", ModuleCategory.MOVEMENT);
        this.addProperties(this.holdingBlockCheck, this.directionCheck);
    }

    @Subscribe
    public void onClipAtLedge(ClipAtLedgeEvent event) {
        boolean directionCondition;
        boolean holdingBlockItem = Constants.mc.field_1724.method_31548().method_7391().method_7909() instanceof class_1747;
        boolean holdingCondition = this.holdingBlockCheck.getValue() == false || holdingBlockItem;
        boolean bl = directionCondition = this.directionCheck.getValue() == false || Constants.mc.field_1690.field_1881.method_1434();
        if (holdingCondition && directionCondition) {
            event.setClip(true);
        }
    }
}

