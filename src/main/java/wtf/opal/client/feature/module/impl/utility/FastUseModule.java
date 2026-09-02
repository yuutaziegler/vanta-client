/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.utility;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.mixin.MinecraftClientAccessor;

@Environment(value=EnvType.CLIENT)
public final class FastUseModule
extends Module {
    private final BooleanProperty fastPlaceEnabled = new BooleanProperty("Enabled", true);

    public FastUseModule() {
        super("Fast Use", "Uses things faster.", ModuleCategory.UTILITY);
        this.addProperties(new GroupProperty("Placements", this.fastPlaceEnabled));
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        if (!this.fastPlaceEnabled.getValue().booleanValue()) {
            return;
        }
        MinecraftClientAccessor minecraftClientAccessor = (MinecraftClientAccessor)Constants.mc;
        minecraftClientAccessor.setItemUseCooldown(0);
    }
}

