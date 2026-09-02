/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.movement.longjump;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.movement.longjump.impl.AntiGamingChairFireballLongJump;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;

@Environment(value=EnvType.CLIENT)
public final class LongJumpModule
extends Module {
    private final ModeProperty<Mode> mode = new ModeProperty<Mode>("Mode", this, Mode.ANTI_GAMING_CHAIR_FIREBALL);

    public LongJumpModule() {
        super("Long Jump", "Allows you to jump further.", ModuleCategory.MOVEMENT);
        this.addProperties(this.mode);
        this.addModuleModes(this.mode, new AntiGamingChairFireballLongJump(this));
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Override
    public String getSuffix() {
        return ((Mode)((Object)this.mode.getValue())).toString();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
        ANTI_GAMING_CHAIR_FIREBALL("Anti Gaming Chair Fireball");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

