/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_10209
 *  net.minecraft.class_1306
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1934
 *  net.minecraft.class_310
 *  net.minecraft.class_329
 *  net.minecraft.class_332
 *  net.minecraft.class_9779
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package restudio.reglass.mixin.client;

import net.minecraft.class_10209;
import net.minecraft.class_1306;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1934;
import net.minecraft.class_310;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import restudio.reglass.client.LiquidGlassUniforms;
import restudio.reglass.client.api.ReGlassApi;
import restudio.reglass.client.api.ReGlassConfig;
import restudio.reglass.client.api.WidgetStyle;

@Mixin(value={class_329.class})
public abstract class InGameHudMixin {
    @Shadow
    @Final
    private class_310 field_2035;
    @Unique
    private double reglass$slotBlobX = Double.NaN;
    @Unique
    private int reglass$lastSelected = -1;

    @Shadow
    protected abstract void method_1762(class_332 var1, int var2, int var3, class_9779 var4, class_1657 var5, class_1799 var6, int var7);

    @Inject(method={"renderHotbar"}, at={@At(value="HEAD")}, cancellable=true)
    private void reglass$onRenderHotbar(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        double deltaTicks;
        if (!ReGlassConfig.INSTANCE.features.enableRedesign || !ReGlassConfig.INSTANCE.features.hotbar) {
            return;
        }
        if (this.field_2035.field_1761.method_2920() == class_1934.field_9219) {
            return;
        }
        class_1657 player = this.method_1737();
        if (player == null) {
            return;
        }
        if (this.field_2035.field_1690.field_1842) {
            return;
        }
        ci.cancel();
        class_10209.method_64146().method_15396("reglass-hotbar");
        int hotbarWidth = 182;
        int hotbarHeight = 22;
        int x = this.field_2035.method_22683().method_4486() / 2 - hotbarWidth / 2;
        int offhandY = this.field_2035.method_22683().method_4502() - hotbarHeight;
        ReGlassApi.create(context).dimensions(x, offhandY, hotbarWidth, hotbarHeight).cornerRadius(11.0f).style(new WidgetStyle().tint(0, 0.3f)).render();
        int selectedSlot = player.method_31548().method_67532();
        int targetCircleX = x - 1 + selectedSlot * 20;
        if (Double.isNaN(this.reglass$slotBlobX)) {
            this.reglass$slotBlobX = targetCircleX;
        }
        try {
            deltaTicks = tickCounter.method_60636();
        }
        catch (Throwable t) {
            deltaTicks = 0.3333333333333333;
        }
        double deltaSeconds = deltaTicks / 20.0;
        double tau = 0.08;
        double alpha = 1.0 - Math.exp(-deltaSeconds / tau);
        if (alpha < 0.0) {
            alpha = 0.0;
        }
        if (alpha > 1.0) {
            alpha = 1.0;
        }
        this.reglass$slotBlobX += ((double)targetCircleX - this.reglass$slotBlobX) * alpha;
        int circleX = (int)Math.round(this.reglass$slotBlobX) + 1;
        WidgetStyle selectorStyle = new WidgetStyle().smoothing(-0.005f);
        ReGlassApi.create(context).dimensions(circleX, offhandY, hotbarHeight, hotbarHeight).cornerRadius(0.5f * (float)hotbarHeight).style(selectorStyle).focus(1.0f).render();
        for (int i = 0; i < 9; ++i) {
            int itemX = x + 3 + i * 20;
            int itemY = offhandY + 3;
            this.method_1762(context, itemX, itemY, tickCounter, player, player.method_31548().method_5438(i), i + 1);
        }
        class_1799 offHandStack = player.method_6079();
        if (!offHandStack.method_7960()) {
            class_1306 arm = player.method_6068().method_5928();
            int offhandX = arm == class_1306.field_6182 ? x - hotbarHeight - 4 : x + hotbarWidth + 4;
            ReGlassApi.create(context).dimensions(offhandX, offhandY, hotbarHeight, hotbarHeight).cornerRadius((float)hotbarHeight * 0.5f).style(new WidgetStyle().tint(0, 0.3f)).render();
            this.method_1762(context, offhandX + 3, offhandY + 3, tickCounter, player, offHandStack, 0);
        }
        LiquidGlassUniforms.get().tryApplyBlur(context);
        class_10209.method_64146().method_15407();
    }

    @Shadow
    private class_1657 method_1737() {
        throw new AssertionError((Object)"Mixin application failed!");
    }
}

