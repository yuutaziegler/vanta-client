/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_124
 */
package wtf.opal.client.feature.helper.impl.chat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_124;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class ChatHelper {
    private String channel = "ALL";
    private String whisperUsername;
    private static ChatHelper instance;

    private ChatHelper() {
    }

    public String getChannel() {
        return this.channel;
    }

    public String getWhisperUsername() {
        return this.whisperUsername;
    }

    public void setChannel(String channel) {
        this.channel = this.channel == channel ? "ALL" : channel;
        ChatUtility.success("You are now in the " + String.valueOf(class_124.field_1065) + this.channel + String.valueOf(class_124.field_1080) + " channel.");
    }

    public void setWhisperUsername(String whisperUsername) {
        this.whisperUsername = whisperUsername;
    }

    public static ChatHelper getInstance() {
        return instance;
    }

    public static void setInstance() {
        instance = new ChatHelper();
    }
}

