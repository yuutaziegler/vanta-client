/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597$class_4598
 */
package wtf.opal.event.impl.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4587;
import net.minecraft.class_4597;

@Environment(value=EnvType.CLIENT)
public record RenderWorldEvent(class_4597.class_4598 vertexConsumers, class_4587 matrixStack, float tickDelta) {
}

