/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11246
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package restudio.reglass.mixin.accessor;

import net.minecraft.class_11246;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_11246.class})
public interface GuiRenderStateAccessor {
    @Accessor(value="blurLayer")
    public int getBlurLayer();
}

