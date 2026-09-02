/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class FullbrightModule
extends Module {
    private final ModeProperty<BrightnessMode> mode = new ModeProperty<BrightnessMode>("Mode", BrightnessMode.GAMMA);
    private double originalGamma = 1.0;

    public FullbrightModule() {
        super("Fullbright", "See in the dark", ModuleCategory.VISUAL);
        this.addProperties(this.mode);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        if (Constants.mc.field_1690 == null) {
            return;
        }
        this.originalGamma = (Double)Constants.mc.field_1690.method_42473().method_41753();
        if (this.mode.getValue() == BrightnessMode.GAMMA) {
            Constants.mc.field_1690.method_42473().method_41748((Object)100.0);
        }
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        if (Constants.mc.field_1690 == null) {
            return;
        }
        Constants.mc.field_1690.method_42473().method_41748((Object)this.originalGamma);
    }

    @Subscribe
    public void onTick(PreGameTickEvent event) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        if (this.mode.getValue() == BrightnessMode.GAMMA) {
            Constants.mc.field_1690.method_42473().method_41748((Object)100.0);
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum BrightnessMode {
        GAMMA("Gamma"),
        NIGHT_VISION("Night Vision");

        private final String name;

        private BrightnessMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

