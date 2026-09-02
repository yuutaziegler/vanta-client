/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  net.minecraft.class_2960
 *  net.minecraft.class_332
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package restudio.reglass.mixin.widgets;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import restudio.reglass.client.api.ReGlassApi;
import restudio.reglass.client.api.ReGlassConfig;
import restudio.reglass.client.api.WidgetStyle;

@Mixin(value={class_332.class})
public abstract class DrawContextMixin {
    @Unique
    private static final class_2960 BUTTON_TEXTURE = class_2960.method_60656((String)"widget/button");
    @Unique
    private static final class_2960 BUTTON_DISABLED_TEXTURE = class_2960.method_60656((String)"widget/button_disabled");
    @Unique
    private static final class_2960 BUTTON_HIGHLIGHTED_TEXTURE = class_2960.method_60656((String)"widget/button_highlighted");

    @Inject(method={"drawGuiTexture(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/util/Identifier;IIIII)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onDrawTexture(RenderPipeline pipeline, class_2960 sprite, int x, int y, int width, int height, int color, CallbackInfo ci) {
        boolean isButtonTexture;
        boolean bl = isButtonTexture = sprite.method_12832().equals(BUTTON_TEXTURE.method_12832()) || sprite.method_12832().equals(BUTTON_DISABLED_TEXTURE.method_12832()) || sprite.method_12832().equals(BUTTON_HIGHLIGHTED_TEXTURE.method_12832());
        if (isButtonTexture && ReGlassConfig.INSTANCE.features.enableRedesign && ReGlassConfig.INSTANCE.features.buttons) {
            boolean isHighlighted = sprite.method_12832().equals(BUTTON_HIGHLIGHTED_TEXTURE.method_12832());
            boolean isDisabled = sprite.method_12832().equals(BUTTON_DISABLED_TEXTURE.method_12832());
            ReGlassApi.create((class_332)this).position(x, y).size(width, height).hover(isHighlighted ? 1.0f : 0.0f).style(WidgetStyle.create().tint(isDisabled ? -16777216 : -1, isDisabled ? 0.4f : 0.0f)).render();
            ci.cancel();
        }
    }
}

