/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_6373
 */
package wtf.opal.client.feature.module.impl.utility;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_6373;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder.BlockHolder;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.InboundNetworkBlockage;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.OutboundNetworkBlockage;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.BoundedNumberProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.math.RandomUtility;
import wtf.opal.utility.misc.time.Stopwatch;

@Environment(value=EnvType.CLIENT)
public final class BlinkModule
extends Module {
    private final MultipleBooleanProperty blinkDirections = new MultipleBooleanProperty("Direction", new BooleanProperty("Inbound", true), new BooleanProperty("Outbound", true));
    private final BooleanProperty pulse = new BooleanProperty("Pulse", false);
    private final BoundedNumberProperty pulseDelay = (BoundedNumberProperty)new BoundedNumberProperty("Pulse delay", "ms", 1000.0, 2000.0, 50.0, 10000.0, 1.0).hideIf(() -> this.pulse.getValue() == false);
    private final Stopwatch oPulseTimer = new Stopwatch();
    private final Stopwatch iPulseTimer = new Stopwatch();
    private final BlockHolder iBlockHolder = new BlockHolder(InboundNetworkBlockage.get());
    private final BlockHolder oBlockHolder = new BlockHolder(OutboundNetworkBlockage.get());

    public BlinkModule() {
        super("Blink", "Blocks your network connection.", ModuleCategory.UTILITY);
        this.addProperties(this.blinkDirections, this.pulse, this.pulseDelay);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        if (this.blinkDirections.getProperty("Inbound").getValue().booleanValue()) {
            this.iBlockHolder.block(p -> p, p -> p instanceof class_6373);
            if (this.pulse.getValue().booleanValue() && this.iPulseTimer.hasTimeElapsed(RandomUtility.getRandomInt((int)this.pulseDelay.getMinValue(), (int)this.pulseDelay.getMaxValue()), true)) {
                this.iBlockHolder.release();
            }
        }
        if (this.blinkDirections.getProperty("Outbound").getValue().booleanValue()) {
            this.oBlockHolder.block();
            if (this.pulse.getValue().booleanValue() && this.oPulseTimer.hasTimeElapsed(RandomUtility.getRandomInt((int)this.pulseDelay.getMinValue(), (int)this.pulseDelay.getMaxValue()), true)) {
                this.oBlockHolder.release();
            }
        }
    }

    @Override
    protected void onDisable() {
        this.iBlockHolder.release();
        this.oBlockHolder.release();
    }
}

