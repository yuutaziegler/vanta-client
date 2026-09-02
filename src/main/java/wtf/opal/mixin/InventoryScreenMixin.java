/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  net.minecraft.class_490
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
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
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.ImageRepository;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_490.class})
public final class InventoryScreenMixin {
    private InventoryScreenMixin() {
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void onRenderBackground(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        class_437 screen = (class_437)this;
        boolean frameStarted = NVGRenderer.beginFrame();
        NVGRenderer.roundedRect(0.0f, 0.0f, (float)screen.field_22789, (float)screen.field_22790, 0.0f, -1442312934);
        NVGImageRenderer logoMenu = ImageRepository.getImage("logomeniu.png");
        if (logoMenu == null) {
            logoMenu = ImageRepository.getImage("logo.png");
        }
        if (logoMenu != null) {
            float logoW = 90.0f;
            float logoH = 30.0f;
            float logoX = ((float)screen.field_22789 - logoW) / 2.0f;
            float logoY = 12.0f;
            logoMenu.drawImage(logoX, logoY, logoW, logoH);
        }
        if (frameStarted) {
            NVGRenderer.endFrame(false);
        }
    }

    @Inject(method={"drawEntity(Lnet/minecraft/client/gui/DrawContext;IIIIIFFFLnet/minecraft/entity/LivingEntity;)V"}, at={@At(value="HEAD")})
    private static void hookDrawEntityHead(class_332 context, int x1, int y1, int x2, int y2, int size, float f, float mouseX, float mouseY, class_1309 entity, CallbackInfo ci) {
        if (Constants.mc.field_1724 != null && entity == Constants.mc.field_1724) {
            RotationHelper.getClientHandler().setTicking(true);
        }
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void onRenderTopLogo(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        class_437 screen = (class_437)this;
        boolean frameStarted = NVGRenderer.beginFrame();
        NVGImageRenderer logoMenu = ImageRepository.getImage("logomeniu.png");
        if (logoMenu == null) {
            logoMenu = ImageRepository.getImage("logo.png");
        }
        if (logoMenu != null) {
            float logoW = 100.0f;
            float logoH = 34.0f;
            float logoX = ((float)screen.field_22789 - logoW) / 2.0f;
            float logoY = 10.0f;
            logoMenu.drawImage(logoX, logoY, logoW, logoH);
        }
        if (frameStarted) {
            NVGRenderer.endFrameAndReset(false);
        }
    }
}

