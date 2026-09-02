/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicates
 *  com.llamalad7.mixinextras.injector.wrapoperation.Operation
 *  com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
 *  com.llamalad7.mixinextras.sugar.Local
 *  com.mojang.blaze3d.buffers.GpuBufferSlice
 *  com.mojang.blaze3d.opengl.GlStateManager
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_239
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_2960
 *  net.minecraft.class_3966
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_4599
 *  net.minecraft.class_751
 *  net.minecraft.class_757
 *  net.minecraft.class_761
 *  net.minecraft.class_766
 *  net.minecraft.class_9779
 *  net.minecraft.class_9922
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Vector4f
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import com.google.common.base.Predicates;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.opengl.GlStateManager;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_3966;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4599;
import net.minecraft.class_751;
import net.minecraft.class_757;
import net.minecraft.class_761;
import net.minecraft.class_766;
import net.minecraft.class_9779;
import net.minecraft.class_9922;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.combat.PiercingModule;
import wtf.opal.client.feature.module.impl.visual.NoHurtCameraModule;
import wtf.opal.client.renderer.shader.ShaderFramebuffer;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.render.RenderWorldEvent;
import wtf.opal.utility.player.RaycastUtility;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_757.class})
public abstract class GameRendererMixin {
    @Final
    @Shadow
    private class_4599 field_20948;
    @Unique
    private boolean passThroughBlocks;
    @Mutable
    @Shadow
    @Final
    protected class_751 field_60579;
    @Mutable
    @Shadow
    @Final
    protected class_766 field_60580;

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    private void replaceCubeMapRenderer(CallbackInfo ci) {
        this.field_60579 = new class_751(class_2960.method_60654((String)"terentx:panorama/panorama"));
        this.field_60580 = new class_766(this.field_60579);
    }

    @Inject(method={"onResized"}, at={@At(value="HEAD")})
    private void hookOnResized(int width, int height, CallbackInfo ci) {
        ShaderFramebuffer.onResized(width, height);
    }

    @WrapOperation(method={"renderWorld"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/WorldRenderer;render(Lnet/minecraft/client/util/ObjectAllocator;Lnet/minecraft/client/render/RenderTickCounter;ZLnet/minecraft/client/render/Camera;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lorg/joml/Matrix4f;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lorg/joml/Vector4f;Z)V")})
    private void hookRenderWorld(class_761 instance, class_9922 allocator, class_9779 tickCounter, boolean renderBlockOutline, class_4184 camera, Matrix4f positionMatrix, Matrix4f matrix4f, Matrix4f projectionMatrix, GpuBufferSlice fogBuffer, Vector4f fogColor, boolean renderSky, Operation<Void> original, @Local(ordinal=1) Matrix4f matrix4f2) {
        original.call(new Object[]{instance, allocator, tickCounter, renderBlockOutline, camera, positionMatrix, matrix4f, projectionMatrix, fogBuffer, fogColor, renderSky});
        class_4587 stack = new class_4587();
        stack.method_34425((Matrix4fc)positionMatrix);
        EventDispatcher.dispatch(new RenderWorldEvent(this.field_20948.method_23000(), stack, tickCounter.method_60637(false)));
        GlStateManager._depthMask((boolean)true);
        GlStateManager._disableBlend();
    }


    @Redirect(method={"findCrosshairTarget"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/hit/HitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"))
    private class_239.class_240 redirectBlockHitResultType(class_239 instance) {
        if (this.passThroughBlocks) {
            this.passThroughBlocks = false;
            return class_239.class_240.field_1333;
        }
        return instance.method_17783();
    }

    @Redirect(method={"findCrosshairTarget"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D", ordinal=0))
    private double redirectPassedThroughBlockDistance(class_243 instance, class_243 vec, @Local(ordinal=1, argsOnly=true) double entityInteractionRange, @Local(argsOnly=true) float tickDelta) {
        class_3966 hitResult;
        if (OpalClient.getInstance().getModuleRepository().getModule(PiercingModule.class).isEnabled() && (hitResult = RaycastUtility.raycastEntity(entityInteractionRange, tickDelta, Constants.mc.field_1724.method_36454(), Constants.mc.field_1724.method_36455(), (Predicate<class_1297>)Predicates.alwaysTrue())) != null && hitResult.method_17783() == class_239.class_240.field_1331) {
            this.passThroughBlocks = true;
            return Double.MAX_VALUE;
        }
        return instance.method_1025(vec);
    }

    @Inject(method={"tiltViewWhenHurt"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookTiltViewWhenHurt(class_4587 matrices, float tickDelta, CallbackInfo ci) {
        if (OpalClient.getInstance().getModuleRepository().getModule(NoHurtCameraModule.class).isEnabled()) {
            ci.cancel();
        }
    }
}

