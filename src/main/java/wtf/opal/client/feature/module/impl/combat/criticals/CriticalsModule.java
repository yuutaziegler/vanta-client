/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.combat.criticals;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.combat.criticals.impl.PacketCriticals;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;

@Environment(value=EnvType.CLIENT)
public final class CriticalsModule
extends Module {
    private final ModeProperty<Mode> mode = new ModeProperty<Mode>("Mode", this, Mode.PACKET);

    public CriticalsModule() {
        super("Criticals", "Forces every attack to be a critical hit.", ModuleCategory.COMBAT);
        this.addProperties(this.mode);
        this.addModuleModes(this.mode, new PacketCriticals(this));
    }

    @Override
    public String getSuffix() {
        return ((Mode)((Object)this.mode.getValue())).toString();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
        PACKET("Packet");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

