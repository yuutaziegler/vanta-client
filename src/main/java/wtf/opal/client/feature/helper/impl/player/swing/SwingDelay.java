/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.player.swing;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.IHelper;
import wtf.opal.client.feature.helper.impl.player.swing.CPSProperty;
import wtf.opal.event.EventDispatcher;
import wtf.opal.utility.misc.time.Stopwatch;

@Environment(value=EnvType.CLIENT)
public final class SwingDelay
implements IHelper {
    private final Stopwatch swingStopwatch = new Stopwatch();
    private static SwingDelay instance;

    public static void reset() {
        SwingDelay.instance.swingStopwatch.reset();
    }

    public static void setInstance() {
        instance = new SwingDelay();
        EventDispatcher.subscribe(instance);
    }

    public static boolean isSwingAvailable(CPSProperty cpsProperty, boolean reset) {
        if (cpsProperty.isModernDelay() && Constants.mc.field_1724 != null) {
            return Constants.mc.field_1724.method_7261(0.5f) >= 1.0f;
        }
        if (SwingDelay.instance.swingStopwatch.hasTimeElapsed(cpsProperty.getNextClick())) {
            if (reset) {
                cpsProperty.resetClick();
                SwingDelay.reset();
            }
            return true;
        }
        return false;
    }

    public static boolean isSwingAvailable(CPSProperty cpsProperty) {
        return SwingDelay.isSwingAvailable(cpsProperty, true);
    }
}

