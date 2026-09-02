/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_7919
 *  net.minecraft.class_9110
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package restudio.reglass.mixin.widgets;

import net.minecraft.class_7919;
import net.minecraft.class_9110;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={class_9110.class})
public class TooltipStateMixin {
    @Shadow
    @Nullable
    private class_7919 field_48390;
}

