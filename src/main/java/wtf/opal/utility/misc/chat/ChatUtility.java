/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_124
 *  net.minecraft.class_2561
 *  net.minecraft.class_2583
 *  net.minecraft.class_5250
 */
package wtf.opal.utility.misc.chat;

import java.util.EnumSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_5250;
import wtf.opal.client.Constants;
import wtf.opal.client.ReleaseInfo;

@Environment(value=EnvType.CLIENT)
public final class ChatUtility {
    private ChatUtility() {
    }

    public static void debug(Object o) {
        if (ReleaseInfo.CHANNEL != ReleaseInfo.ReleaseChannel.DEVELOPMENT) {
            return;
        }
        class_5250 text = class_2561.method_43470((String)"[").method_27692(class_124.field_1080).method_10852((class_2561)class_2561.method_43470((String)"\u27a2").method_27695(new class_124[]{class_124.field_1060, class_124.field_1067})).method_10852((class_2561)class_2561.method_43470((String)"] ").method_27692(class_124.field_1080)).method_27693(o.toString());
        ChatUtility.display((class_2561)text);
    }

    public static void print(Object o) {
        class_5250 text = class_2561.method_43470((String)"[").method_27692(class_124.field_1080).method_10852((class_2561)class_2561.method_43470((String)"\u2139").method_27692(class_124.field_1075)).method_10852((class_2561)class_2561.method_43470((String)"] ").method_27692(class_124.field_1080)).method_27693(o.toString());
        ChatUtility.display((class_2561)text);
    }

    public static void error(Object o) {
        class_5250 text = class_2561.method_43470((String)"[").method_27692(class_124.field_1080).method_10852((class_2561)class_2561.method_43470((String)"\u2716").method_27692(class_124.field_1061)).method_10852((class_2561)class_2561.method_43470((String)"] ").method_27692(class_124.field_1080)).method_27693(o.toString());
        ChatUtility.display((class_2561)text);
    }

    public static void success(Object o) {
        class_5250 text = class_2561.method_43470((String)"[").method_27692(class_124.field_1080).method_10852((class_2561)class_2561.method_43470((String)"\u2714").method_27692(class_124.field_1060)).method_10852((class_2561)class_2561.method_43470((String)"] ").method_27692(class_124.field_1080)).method_27693(o.toString());
        ChatUtility.display((class_2561)text);
    }

    public static void display(class_2561 text) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        Constants.mc.field_1705.method_1743().method_1812(text);
    }

    public static void send(String content) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        Constants.mc.field_1724.field_3944.method_45729(content);
    }

    public static void sendCommand(String command) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        Constants.mc.field_1724.field_3944.method_45730(command);
    }

    public static class_5250 translateAlternateColorCodes(String str) {
        class_5250 mutableText = class_2561.method_43473();
        char[] chars = str.toCharArray();
        EnumSet<class_124> activeFormats = EnumSet.noneOf(class_124.class);
        for (int i = 0; i < chars.length; ++i) {
            char nextChar;
            class_124 formatting;
            char c = chars[i];
            if (c == '&' && i + 1 < chars.length && (formatting = class_124.method_544((char)(nextChar = chars[i + 1]))) != null) {
                ++i;
                if (formatting == class_124.field_1070) {
                    activeFormats.clear();
                    continue;
                }
                activeFormats.add(formatting);
                continue;
            }
            class_2583 style = class_2583.field_24360;
            for (class_124 format : activeFormats) {
                style = style.method_27706(format);
            }
            mutableText.method_10852((class_2561)class_2561.method_43470((String)String.valueOf(c)).method_10862(style));
        }
        return mutableText;
    }
}

