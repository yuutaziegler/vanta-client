/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.movement.flight;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.movement.flight.impl.AirWalkFlight;
import wtf.opal.client.feature.module.impl.movement.flight.impl.BloxdFlight;
import wtf.opal.client.feature.module.impl.movement.flight.impl.FireballFlight;
import wtf.opal.client.feature.module.impl.movement.flight.impl.VanillaFlight;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;

@Environment(value=EnvType.CLIENT)
public final class FlightModule
extends Module {
    private final ModeProperty<Mode> mode = new ModeProperty<Mode>("Mode", this, Mode.VANILLA);

    public FlightModule() {
        super("Flight", "You grow wings in real life.", ModuleCategory.MOVEMENT);
        this.addProperties(this.mode);
        this.addModuleModes(this.mode, new VanillaFlight(this), new FireballFlight(this), new AirWalkFlight(this), new BloxdFlight(this));
    }

    @Override
    public String getSuffix() {
        return ((Mode)((Object)this.mode.getValue())).toString();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
        VANILLA("Vanilla"),
        FIREBALL("Fireball"),
        HYPIXEL("Hypixel"),
        AIR_WALK("Air Walk"),
        BLOXD("Bloxd");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

