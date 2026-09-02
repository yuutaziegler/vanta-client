/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_3532
 *  net.minecraft.class_357
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package restudio.reglass.mixin.widgets;

import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_3532;
import net.minecraft.class_357;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import restudio.reglass.client.LiquidGlassUniforms;
import restudio.reglass.client.api.ReGlassApi;
import restudio.reglass.client.api.ReGlassConfig;
import restudio.reglass.client.api.WidgetStyle;
import restudio.reglass.mixin.accessor.SliderWidgetAccessor;

@Mixin(value={class_357.class})
public abstract class SliderWidgetMixin
extends class_339 {
    @Unique
    WidgetStyle knobStyle = new WidgetStyle().smoothing(-0.005f).tint(0, 0.1f);

    public SliderWidgetMixin(int x, int y, int width, int height, class_2561 message) {
        super(x, y, width, height, message);
    }

    @Inject(method={"renderWidget"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderWidget(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (!ReGlassConfig.INSTANCE.features.enableRedesign || !ReGlassConfig.INSTANCE.features.sliders) {
            return;
        }
        ci.cancel();
        int knobX = (int)((double)this.method_46426() + ((SliderWidgetAccessor)((Object)this)).getValue() * (double)(this.method_25368() - 4));
        boolean hoveredBg = this.method_25405(mouseX, mouseY);
        boolean knobHovered = mouseX >= knobX && mouseX < knobX + 4 && mouseY >= this.method_46427() && mouseY < this.method_46427() + this.method_25364();
        boolean focus = this.method_25370();
        ReGlassApi.create(context).fromWidget(this).hover(hoveredBg ? 1.0f : 0.0f).focus(focus ? 1.0f : 0.0f).render();
        ReGlassApi.create(context).size(4, this.method_25364()).position(knobX, this.method_46427()).style(this.knobStyle).hover(knobHovered ? 1.0f : 0.0f).focus(focus ? 1.0f : 0.0f).render();
        LiquidGlassUniforms.get().tryApplyBlur(context);
        class_327 textRenderer = class_310.method_1551().field_1772;
        int color = this.field_22763 ? 0xFFFFFF : 0xA0A0A0;
        int finalColor = color | class_3532.method_15386((float)(this.field_22765 * 255.0f)) << 24;
        context.method_27534(textRenderer, this.method_25369(), this.method_46426() + this.method_25368() / 2, this.method_46427() + (this.method_25364() - 8) / 2, finalColor);
    }
}

