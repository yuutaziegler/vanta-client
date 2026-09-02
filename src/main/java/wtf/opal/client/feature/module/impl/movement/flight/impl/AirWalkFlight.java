/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2338
 *  net.minecraft.class_259
 */
package wtf.opal.client.feature.module.impl.movement.flight.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_259;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.impl.movement.flight.FlightModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.event.impl.game.world.BlockShapeEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class AirWalkFlight
extends ModuleMode<FlightModule> {
    public AirWalkFlight(FlightModule module) {
        super(module);
    }

    @Subscribe
    public void onBlockShape(BlockShapeEvent event) {
        class_2338 blockPos = event.getBlockPos();
        if ((double)blockPos.method_10264() < Constants.mc.field_1724.method_23318() && event.getBlockState().method_26215()) {
            event.setVoxelShape(class_259.method_1081((double)-2.0, (double)0.0, (double)-2.0, (double)2.0, (double)1.0, (double)2.0));
        }
    }

    @Override
    public Enum<?> getEnumValue() {
        return FlightModule.Mode.AIR_WALK;
    }
}

