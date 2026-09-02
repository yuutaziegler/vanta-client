/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.utility.nofall;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.utility.nofall.impl.SpoofNoFall;
import wtf.opal.client.feature.module.impl.utility.nofall.impl.WatchdogNoFall;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class NoFallModule
extends Module {
    public final ModeProperty<Mode> mode = new ModeProperty<Mode>("Mode", this, Mode.SPOOF);
    private double fallDistance;

    public NoFallModule() {
        super("No Fall", "Removes your players fall damage.", ModuleCategory.UTILITY);
        this.addProperties(this.mode);
        this.addModuleModes(this.mode, new WatchdogNoFall(this), new SpoofNoFall(this));
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        if (Constants.mc.field_1724.field_6017 == 0.0) {
            this.syncFallDifference();
        }
    }

    public void syncFallDifference() {
        this.fallDistance = Constants.mc.field_1724.field_6017;
    }

    public double getFallDifference() {
        if (Constants.mc.field_1724.method_31549().field_7478) {
            return 0.0;
        }
        return Constants.mc.field_1724.field_6017 - this.fallDistance;
    }

    @Override
    protected void onEnable() {
        this.fallDistance = 0.0;
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
        SPOOF("Spoof"),
        WATCHDOG("Watchdog");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

