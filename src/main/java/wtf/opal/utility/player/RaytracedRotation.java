/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_239
 *  net.minecraft.class_241
 */
package wtf.opal.utility.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_239;
import net.minecraft.class_241;

@Environment(value=EnvType.CLIENT)
public record RaytracedRotation(class_241 rotation, class_239 hitResult) {
}

