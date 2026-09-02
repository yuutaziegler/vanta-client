/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_437
 *  net.minecraft.class_442
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_437;
import net.minecraft.class_442;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.Constants;
import wtf.opal.client.screen.TerentXTitleScreen;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_442.class})
public final class TitleScreenMixin {
    private TitleScreenMixin() {
    }

    @Inject(method={"init"}, at={@At(value="HEAD")}, cancellable=true)
    private void redirectToCustomTitleScreen(CallbackInfo ci) {
        if (!(Constants.mc.field_1755 instanceof TerentXTitleScreen)) {
            Constants.mc.method_1507((class_437)new TerentXTitleScreen());
            ci.cancel();
        }
    }
}

