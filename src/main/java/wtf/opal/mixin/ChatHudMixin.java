/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2583
 *  net.minecraft.class_303$class_7590
 *  net.minecraft.class_338
 *  net.minecraft.class_5481
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2583;
import net.minecraft.class_303;
import net.minecraft.class_338;
import net.minecraft.class_5481;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_338.class})
public final class ChatHudMixin {
    @Unique
    private static class_2583 GRAY_STYLE;

    @Redirect(method={"method_71991"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;content()Lnet/minecraft/text/OrderedText;"))
    private class_5481 appendSocketUsernames(class_303.class_7590 instance) {
        return instance.comp_896();
    }
}

