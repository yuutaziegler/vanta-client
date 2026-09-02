/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 */
package wtf.opal.duck;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;

@Environment(value=EnvType.CLIENT)
public interface BipedEntityRenderStateAccess {
    public class_1309 opal$getEntity();

    public void opal$setEntity(class_1309 var1);
}

