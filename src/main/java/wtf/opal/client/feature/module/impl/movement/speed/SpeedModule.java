/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.movement.speed;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.movement.speed.impl.MushMCSpeed;
import wtf.opal.client.feature.module.impl.movement.speed.impl.StrafeSpeed;
import wtf.opal.client.feature.module.impl.movement.speed.impl.VanillaSpeed;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;

@Environment(value=EnvType.CLIENT)
public final class SpeedModule
extends Module {
    private final ModeProperty<Mode> mode = new ModeProperty<Mode>("Mode", this, Mode.VANILLA);

    public SpeedModule() {
        super("Speed", "You become a cheetah in real life.", ModuleCategory.MOVEMENT);
        this.addProperties(this.mode);
        this.addModuleModes(this.mode, new VanillaSpeed(this), new StrafeSpeed(this), new MushMCSpeed(this));
    }

    @Override
    public String getSuffix() {
        return ((Mode)((Object)this.mode.getValue())).toString();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
        VANILLA("Vanilla"),
        WATCHDOG("Watchdog"),
        STRAFE("Strafe"),
        MUSHMC("MushMC");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

