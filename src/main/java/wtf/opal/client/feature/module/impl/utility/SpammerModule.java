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
import wtf.opal.client.feature.module.property.impl.StringProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.time.Stopwatch;

@Environment(value=EnvType.CLIENT)
public final class SpammerModule
extends Module {
    private int stage;
    private final Stopwatch stopwatch = new Stopwatch();
    private final NumberProperty delay = new NumberProperty("Delay", 100.0, 0.0, 10000.0, 1.0);
    private final StringProperty message = new StringProperty("Message", "");
    private final BooleanProperty antiSpamBypass = new BooleanProperty("Anti spam bypass", true);

    public SpammerModule() {
        super("Spammer", "Spams the chat", ModuleCategory.UTILITY);
        this.addProperties(this.message, this.delay, this.antiSpamBypass);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        if (Constants.mc.method_1562() == null || ((String)this.message.getValue()).isEmpty()) {
            return;
        }
        if (this.shouldSend()) {
            Object messageToSend = (String)this.message.getValue();
            if (this.antiSpamBypass.getValue().booleanValue()) {
                messageToSend = Math.random() + " " + (String)messageToSend;
            }
            Constants.mc.method_1562().method_45729((String)messageToSend);
            ++this.stage;
        }
    }

    private boolean shouldSend() {
        return this.stopwatch.hasTimeElapsed(((Double)this.delay.getValue()).longValue(), true);
    }
}

