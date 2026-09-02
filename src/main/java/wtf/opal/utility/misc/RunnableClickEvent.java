/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2558
 *  net.minecraft.class_2558$class_2559
 */
package wtf.opal.utility.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2558;

@Environment(value=EnvType.CLIENT)
public final class RunnableClickEvent
implements class_2558 {
    private final Runnable runnable;

    public RunnableClickEvent(Runnable runnable) {
        this.runnable = runnable;
    }

    public Runnable getRunnable() {
        return this.runnable;
    }

    public class_2558.class_2559 method_10845() {
        return null;
    }
}

