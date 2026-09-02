/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2338
 */
package wtf.opal.duck;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;

@Environment(value=EnvType.CLIENT)
public interface ClientPlayerInteractionManagerAccess {
    public class_2338 opal$getCurrentBreakingPos();

    public float opal$currentBreakingProgress();
}

