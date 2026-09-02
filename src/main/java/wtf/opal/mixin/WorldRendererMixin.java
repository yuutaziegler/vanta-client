/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  com.mojang.blaze3d.buffers.GpuBufferSlice
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11658
 *  net.minecraft.class_4184
 *  net.minecraft.class_4604
 *  net.minecraft.class_5294$class_5401
 *  net.minecraft.class_761
 *  net.minecraft.class_9909
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11658;
import net.minecraft.class_4184;
import net.minecraft.class_4604;
import net.minecraft.class_5294;
import net.minecraft.class_761;
import net.minecraft.class_9909;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.render.FrustumHelper;
import wtf.opal.client.feature.module.impl.visual.AmbienceModule;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_761.class})
public final class WorldRendererMixin {
    @Shadow
    @Final
    private class_11658 field_61737;

    @Redirect(method={"getTransparencyPostEffectProcessor"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;isFabulousGraphicsOrBetter()Z"))
    private boolean redirectFabulousGraphics() {
        return true;
    }

    @Inject(method={"renderSky"}, at={@At(value="HEAD")})
    private void redirectSkyType(class_9909 frameGraphBuilder, class_4184 camera, GpuBufferSlice fogBuffer, CallbackInfo ci) {
        AmbienceModule ambienceModule = OpalClient.getInstance().getModuleRepository().getModule(AmbienceModule.class);
        if (ambienceModule.isEnabled() && ambienceModule.isEndSky()) {
            this.field_61737.field_63087.field_63088 = class_5294.class_5401.field_25641;
        }
    }

    @Inject(method={"render"}, at={@At(value="INVOKE_ASSIGN", target="Lnet/minecraft/client/render/WorldRenderer;setupFrustum(Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/client/render/Frustum;", shift=At.Shift.AFTER)})
    private void opal$hookFrustum$render(CallbackInfo callbackInfo, @Local class_4604 frustum) {
        FrustumHelper.setFrustum(frustum);
    }
}

