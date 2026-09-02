/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_761
 *  org.spongepowered.asm.mixin.Mixin
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_761;
import org.spongepowered.asm.mixin.Mixin;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_761.class})
public interface WorldRendererAccessor {
}

