/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11659
 *  net.minecraft.class_1268
 *  net.minecraft.class_1306
 *  net.minecraft.class_1657
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1819
 *  net.minecraft.class_3489
 *  net.minecraft.class_4587
 *  net.minecraft.class_742
 *  net.minecraft.class_746
 *  net.minecraft.class_759
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11659;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1657;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1819;
import net.minecraft.class_3489;
import net.minecraft.class_4587;
import net.minecraft.class_742;
import net.minecraft.class_746;
import net.minecraft.class_759;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;
import wtf.opal.duck.PlayerEntityAccess;
import wtf.opal.utility.player.BlockUtility;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_759.class})
public abstract class HeldItemRendererMixin {
    @Shadow
    private class_1799 field_4047;
    @Shadow
    private float field_4043;

    @Shadow
    protected abstract void method_3217(class_4587 var1, class_1306 var2, float var3);

    private HeldItemRendererMixin() {
    }

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/util/math/MatrixStack;push()V", shift=At.Shift.AFTER)})
    private void hookRenderFirstPersonItem(class_742 player, float tickProgress, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_11659 orderedRenderCommandQueue, int light, CallbackInfo ci) {
        AnimationsModule animationModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationModule.isEnabled() && class_1268.field_5808 == hand) {
            matrices.method_46416(animationModule.getMainHandX(), animationModule.getMainHandY(), animationModule.getMainHandScale());
        }
    }

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="HEAD")}, cancellable=true)
    private void hideShield(class_742 player, float tickProgress, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_11659 orderedRenderCommandQueue, int light, CallbackInfo ci) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (hand == class_1268.field_5810 && item.method_7909() instanceof class_1819 && animationsModule.isEnabled() && animationsModule.isHideShield()) {
            ci.cancel();
        }
    }

    @ModifyArg(method={"updateHeldItems"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/MathHelper;clamp(FFF)F", ordinal=2), index=0)
    private float modifyMainHandEquipProgress(float value, @Local(ordinal=0) class_1799 itemStack) {
        boolean oldCooldownAnimation;
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        boolean bl = oldCooldownAnimation = animationsModule.isEnabled() && animationsModule.isOldCooldownAnimation();
        if (oldCooldownAnimation && this.field_4047 == itemStack) {
            return 1.0f - this.field_4043;
        }
        return value;
    }

    @Redirect(method={"updateHeldItems"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getAttackCooldownProgress(F)F"))
    private float redirectGetAttackCooldown(class_746 instance, float v) {
        return ((PlayerEntityAccess)instance).opal$getVisualAttackCooldownProgress(v);
    }

    @ModifyArg(method={"renderFirstPersonItem"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal=3), index=2)
    private float applyEquipOffset(float equipProgress) {
        AnimationsModule animationModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationModule.isEnabled() && !animationModule.isEquipOffset()) {
            return 0.0f;
        }
        return equipProgress;
    }

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/item/HeldItemRenderer;renderItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;I)V", ordinal=1)})
    private void applySwordBlockingTransformation(class_742 player, float tickProgress, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_11659 orderedRenderCommandQueue, int light, CallbackInfo ci) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled() && animationsModule.isSwordBlocking() && (BlockUtility.isForceBlockUseState((class_1657)player) || BlockUtility.isBlockUseState((class_1657)player) || BlockUtility.isNoSlowBlockingState())) {
            animationsModule.applyTransformations(matrices, swingProgress);
        }
    }

    @Inject(method={"swingArm"}, at={@At(value="HEAD")}, cancellable=true)
    private void cancelSwingArm(float swingProgress, float equipProgress, class_4587 matrices, int armX, class_1306 arm, CallbackInfo ci) {
        if (BlockUtility.isForceBlockUseState((class_1657)Constants.mc.field_1724) || BlockUtility.isNoSlowBlockingState()) {
            matrices.method_46416(0.56f, -0.52f, -0.72f);
            ci.cancel();
        }
    }

    @Redirect(method={"renderFirstPersonItem"}, at=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"))
    private class_1792 cancelBlockTransformation(class_1799 instance, @Local(argsOnly=true) class_4587 matrices) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled() && animationsModule.isSwordBlocking() && instance.method_31573(class_3489.field_42611)) {
            return class_1802.field_8255;
        }
        return instance.method_7909();
    }

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal=2, shift=At.Shift.AFTER)})
    private void applyEatingAndDrinkingOffset(class_742 player, float tickProgress, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_11659 orderedRenderCommandQueue, int light, CallbackInfo ci) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled() && player.field_6252) {
            this.method_3217(matrices, player.method_6058() == class_1268.field_5808 ? player.method_6068() : player.method_6068().method_5928(), swingProgress);
        }
    }

    @Inject(method={"renderFirstPersonItem"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/item/HeldItemRenderer;applyEquipOffset(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/util/Arm;F)V", ordinal=4, shift=At.Shift.AFTER)})
    private void applyBowOffset(class_742 player, float tickProgress, float pitch, class_1268 hand, float swingProgress, class_1799 item, float equipProgress, class_4587 matrices, class_11659 orderedRenderCommandQueue, int light, CallbackInfo ci) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled()) {
            this.method_3217(matrices, player.method_6058() == class_1268.field_5808 ? player.method_6068() : player.method_6068().method_5928(), swingProgress);
        }
    }

    @Redirect(method={"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getYaw(F)F"))
    private float redirectItemYaw(class_746 instance, float tickDelta) {
        return RotationHelper.getClientHandler().getYawOr(instance.method_5705(tickDelta));
    }

    @Redirect(method={"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getPitch(F)F"))
    private float redirectItemPitch(class_746 instance, float tickDelta) {
        return RotationHelper.getClientHandler().getPitchOr(instance.method_5695(tickDelta));
    }

    @Redirect(method={"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"}, at=@At(value="FIELD", target="Lnet/minecraft/client/network/ClientPlayerEntity;lastRenderYaw:F", opcode=180))
    private float redirectItemLastRenderYaw(class_746 instance) {
        return RotationHelper.getClientHandler().getLastRenderYawOr(instance.field_3931);
    }

    @Redirect(method={"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"}, at=@At(value="FIELD", target="Lnet/minecraft/client/network/ClientPlayerEntity;lastRenderPitch:F", opcode=180))
    private float redirectItemLastRenderPitch(class_746 instance) {
        return RotationHelper.getClientHandler().getLastRenderPitchOr(instance.field_3914);
    }

    @Redirect(method={"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"}, at=@At(value="FIELD", target="Lnet/minecraft/client/network/ClientPlayerEntity;renderYaw:F", opcode=180))
    private float redirectItemRenderYaw(class_746 instance) {
        return RotationHelper.getClientHandler().getRenderYawOr(instance.field_3932);
    }

    @Redirect(method={"renderItem(FLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;Lnet/minecraft/client/network/ClientPlayerEntity;I)V"}, at=@At(value="FIELD", target="Lnet/minecraft/client/network/ClientPlayerEntity;renderPitch:F", opcode=180))
    private float redirectItemRenderPitch(class_746 instance) {
        return RotationHelper.getClientHandler().getRenderPitchOr(instance.field_3916);
    }

    @Redirect(method={"updateHeldItems"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getMainHandStack()Lnet/minecraft/item/ItemStack;"))
    private class_1799 getMainHandStack(class_746 instance) {
        return SlotHelper.getInstance().getMainHandStack(instance);
    }
}

