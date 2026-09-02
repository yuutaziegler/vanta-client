/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11246
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package restudio.reglass.mixin.client;

import net.minecraft.class_11246;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import restudio.reglass.client.LiquidGlassUniforms;
import restudio.reglass.client.api.ReGlassConfig;
import restudio.reglass.mixin.accessor.GuiRenderStateAccessor;

@Mixin(value={class_437.class})
public class ScreenMixin {
    @Inject(method={"renderBackground(Lnet/minecraft/client/gui/DrawContext;IIF)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/screen/Screen;applyBlur(Lnet/minecraft/client/gui/DrawContext;)V")})
    private void reglass$onScreenBlur(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        LiquidGlassUniforms.get().setScreenWantsBlur(true);
    }

    @Inject(method={"applyBlur"}, at={@At(value="HEAD")}, cancellable=true)
    private void reglass$checkBeforeBlur(class_332 context, CallbackInfo ci) {
        class_11246 state = context.field_59826;
        int blurLayer = ((GuiRenderStateAccessor)state).getBlurLayer();
        if (blurLayer != Integer.MAX_VALUE) {
            ci.cancel();
        }
    }

    @Inject(method={"renderDarkening(Lnet/minecraft/client/gui/DrawContext;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void reglass$onRenderDarkening(class_332 context, CallbackInfo ci) {
        if (ReGlassConfig.INSTANCE.features.enableRedesign && ReGlassConfig.INSTANCE.features.cancelScreenDarkening) {
            ci.cancel();
        }
    }
}

