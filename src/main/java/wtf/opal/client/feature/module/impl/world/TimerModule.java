/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.world;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.player.timer.TimerHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class TimerModule
extends Module {
    private final NumberProperty gameSpeed = new NumberProperty("Game speed", "x", 2.0, (double)0.05f, 10.0, (double)0.05f);

    public TimerModule() {
        super("Timer", "Modifies your game speed.", ModuleCategory.WORLD);
        this.addProperties(this.gameSpeed);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        TimerHelper.getInstance().timer = ((Double)this.gameSpeed.getValue()).floatValue();
    }

    @Override
    protected void onDisable() {
        TimerHelper.getInstance().timer = 1.0f;
        super.onDisable();
    }
}

