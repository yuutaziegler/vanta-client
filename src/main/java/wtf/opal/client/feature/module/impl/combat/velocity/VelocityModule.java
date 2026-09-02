/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.combat.velocity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.combat.velocity.VelocityMode;
import wtf.opal.client.feature.module.impl.combat.velocity.impl.MushMCVelocity;
import wtf.opal.client.feature.module.impl.combat.velocity.impl.NormalVelocity;
import wtf.opal.client.feature.module.impl.combat.velocity.impl.WatchdogVelocity;
import wtf.opal.client.feature.module.impl.movement.flight.FlightModule;
import wtf.opal.client.feature.module.impl.movement.longjump.LongJumpModule;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.repository.ModuleRepository;

@Environment(value=EnvType.CLIENT)
public final class VelocityModule
extends Module {
    private final ModeProperty<Mode> mode = new ModeProperty<Mode>("Mode", this, Mode.NORMAL);

    public VelocityModule() {
        super("Velocity", "Reduces or nullifies your players velocity when being hit.", ModuleCategory.COMBAT);
        this.addProperties(this.mode);
        this.addModuleModes(this.mode, new NormalVelocity(this), new WatchdogVelocity(this), new MushMCVelocity(this));
    }

    @Override
    public String getSuffix() {
        return ((VelocityMode)this.getActiveMode()).getSuffix();
    }

    public boolean isInvalid() {
        if (Constants.mc.field_1724 == null) {
            return true;
        }
        ModuleRepository moduleRepository = OpalClient.getInstance().getModuleRepository();
        if (moduleRepository.getModule(LongJumpModule.class).isEnabled() || moduleRepository.getModule(FlightModule.class).isEnabled()) {
            return true;
        }
        HypixelServer.ModAPI.Location currentLocation = HypixelServer.ModAPI.get().getCurrentLocation();
        return LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer && currentLocation != null && currentLocation.isLobby();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
        NORMAL("Normal"),
        WATCHDOG("Watchdog"),
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

