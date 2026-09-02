/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1268
 */
package wtf.opal.duck;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;

@Environment(value=EnvType.CLIENT)
public interface ClientPlayerEntityAccess {
    public void opal$swingHandClientside(class_1268 var1);

    public void opal$swingHandServerside(class_1268 var1);
}

