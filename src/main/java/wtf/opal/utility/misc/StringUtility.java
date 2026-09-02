/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class StringUtility {
    private StringUtility() {
    }

    public static String rotate(int shift, String str) {
        StringBuilder builder = new StringBuilder();
        shift %= 26;
        for (int i = 0; i < str.length(); ++i) {
            char c = str.charAt(i);
            if (c >= 'a' && c <= 'z') {
                c = (char)(97 + (c - 97 + shift + 26) % 26);
            } else if (c >= 'A' && c <= 'Z') {
                c = (char)(65 + (c - 65 + shift + 26) % 26);
            }
            builder.append(c);
        }
        return builder.toString();
    }
}

