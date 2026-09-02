/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.target;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class TargetFlags {
    public static final int PLAYERS = 1;
    public static final int HOSTILE = 2;
    public static final int PASSIVE = 4;
    public static final int FRIENDLY = 8;
    public static final int LOCAL = 16;

    public static int get(boolean players, boolean hostile, boolean passive, boolean friendly) {
        int flags = 0;
        if (players) {
            flags |= 1;
        }
        if (hostile) {
            flags |= 2;
        }
        if (passive) {
            flags |= 4;
        }
        if (friendly) {
            flags |= 8;
        }
        return flags;
    }
}

