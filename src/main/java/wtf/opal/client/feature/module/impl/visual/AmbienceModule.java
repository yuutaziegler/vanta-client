/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2761
 */
package wtf.opal.client.feature.module.impl.visual;

import java.time.LocalTime;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2761;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.PostGameTickEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class AmbienceModule
extends Module {
    private final BooleanProperty useRealTime = new BooleanProperty("Use real time", false);
    private final NumberProperty time = (NumberProperty)new NumberProperty("Time", 1000.0, 0.0, 23450.0, 50.0).hideIf(this.useRealTime::getValue);
    private final BooleanProperty endSky = new BooleanProperty("End sky", false);

    public AmbienceModule() {
        super("Ambience", "Changes the time of day.", ModuleCategory.VISUAL);
        this.addProperties(this.useRealTime, this.time, this.endSky);
    }

    @Subscribe
    public void onPreGameTick(PostGameTickEvent event) {
        if (Constants.mc.field_1687 == null) {
            return;
        }
        long time = ((Double)this.time.getValue()).longValue();
        if (this.useRealTime.getValue().booleanValue()) {
            LocalTime localTime = LocalTime.now();
            int hour = localTime.getHour();
            int minute = localTime.getMinute();
            long totalMinutes = (long)hour * 60L + (long)minute;
            long minecraftTime = totalMinutes * 1000L / 1440L * 24L;
            time = (minecraftTime + 18000L) % 24000L;
        }
        Constants.mc.field_1687.method_28104().method_165(time);
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        if (event.getPacket() instanceof class_2761) {
            event.setCancelled();
        }
    }

    public boolean isEndSky() {
        return this.endSky.getValue();
    }
}

