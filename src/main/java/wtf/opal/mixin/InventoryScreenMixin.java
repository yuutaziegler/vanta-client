/*
 * Decompiled with CFR 0.152.
 *
 * Rewrite notes:
 *  - The old onRenderBackground drew an OPAQUE dark rounded rectangle over
 *    the whole screen (NVGRenderer.roundedRect(..., -1442312934) == 0xAA000000
 *    was stacked over a full-screen fill), which made the inventory look
 *    completely black/dark.  That overlay is removed.
 *  - A small, non-intrusive liquid-glass logo plaque is drawn instead, so
 *    the branding stays visible WITHOUT covering any slots.
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_490;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.VantaGlass;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.ImageRepository;

@Environment(value = EnvType.CLIENT)
@Mixin(value = {class_490.class})
public final class InventoryScreenMixin {
    private InventoryScreenMixin() {
    }

    @Inject(method = {"render"}, at = {@At(value = "TAIL")})
    private void onRenderBranding(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        class_437 screen = (class_437) this;
        boolean frameStarted = NVGRenderer.beginFrame();
        NVGImageRenderer logoMenu = ImageRepository.getImage("logomeniu.png");
        if (logoMenu == null) {
            logoMenu = ImageRepository.getImage("logo.png");
        }
        if (logoMenu != null) {
            float logoW = 84.0f;
            float logoH = 28.0f;
            float logoX = ((float) screen.field_22789 - logoW) / 2.0f;
            float logoY = 6.0f;
            // glass plaque BEHIND the logo only (does not cover slots)
            VantaGlass.frame(logoX - 8.0f, logoY - 4.0f, logoW + 16.0f, logoH + 8.0f, 6.0f);
            logoMenu.drawImage(logoX, logoY, logoW, logoH);
        }
        if (frameStarted) {
            NVGRenderer.endFrameAndReset(false);
        }
    }

    @Inject(method = {"drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V"}, at = {@At(value = "HEAD")})
    private static void hookDrawEntityHead(class_332 context, int x1, int y1, int x2, int y2, int size, float f, float mouseX, float mouseY, class_1309 entity, CallbackInfo ci) {
        if (Constants.mc.field_1724 != null && entity == Constants.mc.field_1724) {
            RotationHelper.getClientHandler().setTicking(true);
        }
    }
}
