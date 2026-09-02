/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.player.timer;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class TimerHelper {
    public float timer = 1.0f;
    private static TimerHelper instance;

    private TimerHelper() {
    }

    public static TimerHelper getInstance() {
        return instance;
    }

    public static void setInstance() {
        instance = new TimerHelper();
    }
}

