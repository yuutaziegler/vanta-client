/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.utility.disabler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.utility.disabler.impl.CubecraftDisabler;
import wtf.opal.client.feature.module.impl.utility.disabler.impl.WatchdogDisabler;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;

@Environment(value=EnvType.CLIENT)
public final class DisablerModule
extends Module {
    private final ModeProperty<Mode> mode = new ModeProperty<Mode>("Mode", this, Mode.WATCHDOG);

    public DisablerModule() {
        super("Disabler", "Lessens anti-cheat strength.", ModuleCategory.UTILITY);
        this.addProperties(this.mode);
        this.addModuleModes(this.mode, new WatchdogDisabler(this), new CubecraftDisabler(this));
    }

    @Override
    public String getSuffix() {
        return ((Mode)((Object)this.mode.getValue())).toString();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
        WATCHDOG("Watchdog"),
        CUBECRAFT("Cubecraft");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

