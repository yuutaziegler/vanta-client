/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2596
 */
package wtf.opal.duck;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;

@Environment(value=EnvType.CLIENT)
public interface ClientConnectionAccess {
    public void opal$channelReadSilent(class_2596<?> var1);
}

