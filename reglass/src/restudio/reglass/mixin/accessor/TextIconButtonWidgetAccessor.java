/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_8662
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package restudio.reglass.mixin.accessor;

import net.minecraft.class_8662;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_8662.class})
public interface TextIconButtonWidgetAccessor {
    @Accessor(value="textureWidth")
    public int getTextureWidth();

    @Accessor(value="textureHeight")
    public int getTextureHeight();
}

