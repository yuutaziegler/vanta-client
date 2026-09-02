/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.player.movement.KeepSprintEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class SprintModule
extends Module {
    private final BooleanProperty omniSprint = new BooleanProperty("Omnidirectional", false);
    private final BooleanProperty keepSprint = new BooleanProperty("Keep sprint", true);

    public SprintModule() {
        super("Sprint", "Modifies the logic behind sprinting.", ModuleCategory.MOVEMENT);
        this.addProperties(this.omniSprint, this.keepSprint);
        this.setEnabled(true);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        Constants.mc.field_1690.field_1867.method_23481(true);
    }

    @Subscribe
    public void onKeepSprint(KeepSprintEvent event) {
        if (!this.keepSprint.getValue().booleanValue()) {
            return;
        }
        event.setCancelled();
    }

    public static boolean isOmniSprint() {
        SprintModule sprintModule = OpalClient.getInstance().getModuleRepository().getModule(SprintModule.class);
        return sprintModule.isEnabled() && sprintModule.omniSprint.getValue() != false;
    }
}

