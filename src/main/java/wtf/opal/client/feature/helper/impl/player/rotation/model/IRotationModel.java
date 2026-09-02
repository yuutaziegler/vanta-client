/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_241
 */
package wtf.opal.client.feature.helper.impl.player.rotation.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_241;
import wtf.opal.client.feature.helper.impl.player.rotation.model.EnumRotationModel;

@Environment(value=EnvType.CLIENT)
public interface IRotationModel {
    public class_241 tick(class_241 var1, class_241 var2, float var3);

    public EnumRotationModel getEnum();
}

