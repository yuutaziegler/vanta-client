/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.v2.WrapWithCondition
 *  com.llamalad7.mixinextras.injector.wrapoperation.Operation
 *  com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
 *  com.llamalad7.mixinextras.sugar.Local
 *  com.llamalad7.mixinextras.sugar.Share
 *  com.llamalad7.mixinextras.sugar.ref.LocalRef
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10426
 *  net.minecraft.class_10444
 *  net.minecraft.class_11659
 *  net.minecraft.class_1268
 *  net.minecraft.class_1306
 *  net.minecraft.class_1799
 *  net.minecraft.class_1819
 *  net.minecraft.class_3883
 *  net.minecraft.class_3887
 *  net.minecraft.class_4587
 *  net.minecraft.class_583
 *  net.minecraft.class_7833
 *  net.minecraft.class_989
 *  org.joml.Quaternionfc
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArgs
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.invoke.arg.Args
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10426;
import net.minecraft.class_10444;
import net.minecraft.class_11659;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1799;
import net.minecraft.class_1819;
import net.minecraft.class_3883;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_583;
import net.minecraft.class_7833;
import net.minecraft.class_989;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;
import wtf.opal.duck.BipedEntityRenderStateAccess;
import wtf.opal.utility.player.BlockUtility;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_989.class})
public abstract class HeldItemFeatureRendererMixin<S extends class_10426, M extends class_583<S>>
extends class_3887<S, M> {
    public HeldItemFeatureRendererMixin(class_3883<S, M> context) {
        super(context);
    }

    @Inject(method={"renderItem"}, at={@At(value="HEAD")})
    private void setThirdPersonStackRef(S entityState, class_10444 itemRenderState, class_1306 arm, class_4587 matrices, class_11659 orderedRenderCommandQueue, int light, CallbackInfo ci, @Share(value="stack") LocalRef<class_1799> stackRef) {
        if (BlockUtility.isThirdPersonBlockingState(entityState)) {
            stackRef.set((Object)((BipedEntityRenderStateAccess)entityState).opal$getEntity().method_61420(arm));
        }
    }

    @ModifyArgs(method={"renderItem"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V"))
    private void translateThirdPersonBlock(Args args, @Local(argsOnly=true) S entityState, @Share(value="stack") LocalRef<class_1799> stackRef) {
        if (BlockUtility.isThirdPersonBlockingState(entityState)) {
            args.setAll(new Object[]{Float.valueOf(((Float)args.get(0)).floatValue() * -1.0f), Float.valueOf(0.4375f), Float.valueOf(((Float)args.get(2)).floatValue() / 10.0f * -1.0f)});
        }
    }

    @WrapWithCondition(method={"renderItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionfc;)V")})
    private boolean removeThirdPersonMultiplyTransformations(class_4587 instance, Quaternionfc quaternion, @Local(argsOnly=true) S entityState, @Share(value="stack") LocalRef<class_1799> stackRef) {
        return !BlockUtility.isThirdPersonBlockingState(entityState);
    }

    @Inject(method={"renderItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/item/ItemRenderState;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;III)V")})
    private void applyThirdPersonBlockRotation(S entityState, class_10444 itemRenderState, class_1306 arm, class_4587 matrices, class_11659 orderedRenderCommandQueue, int light, CallbackInfo ci) {
        if (BlockUtility.isThirdPersonBlockingState(entityState)) {
            int direction = arm == class_1306.field_6183 ? 1 : -1;
            float scale = 0.625f;
            matrices.method_46416((float)direction * 0.05f, 0.0f, -0.1f);
            matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees((float)direction * -50.0f));
            matrices.method_22907((Quaternionfc)class_7833.field_40714.rotationDegrees(-10.0f));
            matrices.method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees((float)direction * -60.0f));
            matrices.method_46416((float)direction * -0.0625f, 0.1875f, 0.0f);
            matrices.method_22905(0.625f, 0.625f, 0.625f);
            matrices.method_22907((Quaternionfc)class_7833.field_40714.rotationDegrees(180.0f));
            matrices.method_22907((Quaternionfc)class_7833.field_40714.rotationDegrees(100.0f));
            matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees((float)(direction * -145)));
            matrices.method_46416(-0.011765625f, 0.0f, 0.002125f);
            matrices.method_46416(0.0f, -0.3f, 0.0f);
            matrices.method_22905(1.5f, 1.5f, 1.5f);
            matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees((float)direction * 50.0f));
            matrices.method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees((float)direction * 335.0f));
            matrices.method_46416((float)direction * -0.9375f, -0.0625f, 0.0f);
            matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees((float)direction * 180.0f));
            matrices.method_46416((float)direction * -0.5f, 0.5f, 0.03125f);
            matrices.method_22905(1.1764705f, 1.1764705f, 1.1764705f);
            matrices.method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees((float)direction * -55.0f));
            matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees((float)direction * 90.0f));
            matrices.method_46416(0.0f, -0.25f, -0.03125f);
        }
    }

    @WrapOperation(method={"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/ArmedEntityRenderState;FF)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/feature/HeldItemFeatureRenderer;renderItem(Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/util/Arm;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V")})
    private void cancelShieldRender(class_989 instance, S entityState, class_10444 itemRenderState, class_1306 arm, class_4587 matrices, class_11659 orderedRenderCommandQueue, int light, Operation<Void> original) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled() && animationsModule.isHideShield() && arm == Constants.mc.field_1724.method_6068().method_5928() && Constants.mc.field_1724.method_5998(class_1268.field_5810).method_7909() instanceof class_1819) {
            return;
        }
        original.call(new Object[]{instance, entityState, itemRenderState, arm, matrices, orderedRenderCommandQueue, light});
    }
}

