/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.lwjgl.util.tinyfd.TinyFileDialogs
 */
package wtf.opal.utility.misc.system;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

@Environment(value=EnvType.CLIENT)
public final class DialogUtility {
    private DialogUtility() {
    }

    public static boolean notify(String type, String icon, String title, String message) {
        return TinyFileDialogs.tinyfd_messageBox((CharSequence)title, (CharSequence)message, (CharSequence)type, (CharSequence)icon, (boolean)true);
    }
}

