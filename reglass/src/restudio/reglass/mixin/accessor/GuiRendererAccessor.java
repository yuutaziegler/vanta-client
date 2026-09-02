/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11228
 *  net.minecraft.class_4597$class_4598
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package restudio.reglass.mixin.accessor;

import net.minecraft.class_11228;
import net.minecraft.class_4597;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_11228.class})
public interface GuiRendererAccessor {
    @Accessor(value="vertexConsumers")
    public class_4597.class_4598 getVertexConsumers();
}

