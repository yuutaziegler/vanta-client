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
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.StringProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.chat.ChatUtility;
import wtf.opal.utility.misc.time.Stopwatch;

@Environment(value=EnvType.CLIENT)
public final class PartySpamModule
extends Module {
    private final StringProperty username = new StringProperty("Username", "");
    private final Stopwatch stopwatch = new Stopwatch();
    private boolean state;

    public PartySpamModule() {
        super("Party Spam", "Spams specified player with party invites.", ModuleCategory.UTILITY);
        this.addProperties(this.username);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        String command;
        if (((String)this.username.getValue()).isEmpty()) {
            return;
        }
        String string = command = this.state ? "party " + (String)this.username.getValue() : "party disband";
        if (this.stopwatch.hasTimeElapsed(250L, true)) {
            ChatUtility.sendCommand(command);
            this.state = !this.state;
        }
    }
}

