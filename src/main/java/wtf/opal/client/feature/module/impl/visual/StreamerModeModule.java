/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2561
 *  org.apache.commons.lang3.StringUtils
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;
import org.apache.commons.lang3.StringUtils;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.StringProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.event.impl.game.chat.ChatReceivedEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class StreamerModeModule
extends Module {
    private final BooleanProperty hideServerId = new BooleanProperty("Hide server ID", true);
    private final BooleanProperty hideUsername = new BooleanProperty("Hide username", true);
    private final StringProperty customUsername = (StringProperty)new StringProperty("Custom username", "You").hideIf(() -> this.hideUsername.getValue() == false);
    private final BooleanProperty hideOverlay = new BooleanProperty("Hide overlay", false);

    public StreamerModeModule() {
        super("Streamer Mode", "Features for content creators.", ModuleCategory.VISUAL);
        this.addProperties(this.hideServerId, this.hideUsername, this.customUsername, this.hideOverlay);
    }

    @Subscribe
    public void onChatReceived(ChatReceivedEvent event) {
        String message;
        if (this.hideServerId.getValue().booleanValue() && LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer && (message = event.getText().getString()).startsWith("Sending you to ")) {
            event.setCancelled();
            String serverId = message.replace("Sending you to ", "").replace("!", "");
            ChatUtility.display((class_2561)class_2561.method_43470((String)("\u00a7aSending you to \u00a7k" + serverId + "\u00a7r\u00a7a!")));
        }
    }

    public String filter(String text) {
        String customUsername;
        if (this.hideUsername.getValue().booleanValue() && !(customUsername = this.getCustomUsername()).isEmpty()) {
            text = StringUtils.replaceIgnoreCase((String)text, (String)Constants.mc.method_1548().method_1676(), (String)customUsername);
        }
        return text;
    }

    public boolean isHidingServerId() {
        return this.hideServerId.getValue();
    }

    public String getCustomUsername() {
        return ((String)this.customUsername.getValue()).trim();
    }

    public boolean isHidingOverlay() {
        return this.hideOverlay.getValue();
    }
}

