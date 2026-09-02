/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  com.llamalad7.mixinextras.injector.wrapoperation.Operation
 *  com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1299
 *  net.minecraft.class_1309
 *  net.minecraft.class_1937
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Constant
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyConstant
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1937;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.module.impl.movement.MovementFixModule;
import wtf.opal.client.feature.module.impl.movement.SprintModule;
import wtf.opal.client.feature.module.impl.movement.TargetStrafeModule;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;
import wtf.opal.duck.ClientPlayerEntityAccess;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.player.movement.JumpEvent;
import wtf.opal.event.impl.game.player.movement.JumpingCooldownEvent;
import wtf.opal.event.impl.game.player.movement.step.StepEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_1309.class})
public abstract class LivingEntityMixin
extends class_1297 {
    @Shadow
    private int field_6228;

    private LivingEntityMixin(class_1299<?> type, class_1937 world) {
        super(type, world);
    }

    @ModifyReturnValue(method={"getStepHeight"}, at={@At(value="RETURN")})
    private float hookStepHeight(float original) {
        if (this == Constants.mc.field_1724) {
            StepEvent stepEvent = new StepEvent(original);
            EventDispatcher.dispatch(stepEvent);
            return stepEvent.getStepHeight();
        }
        return original;
    }

    @WrapOperation(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/math/MathHelper;abs(F)F")})
    private float modifyBackwardsWalkingRotation(float value, Operation<Float> original) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled() && animationsModule.isOldBackwardsWalking()) {
            return 0.0f;
        }
        return ((Float)original.call(new Object[]{Float.valueOf(value)})).floatValue();
    }

    @Redirect(method={"tickMovement"}, at=@At(value="FIELD", target="Lnet/minecraft/entity/LivingEntity;jumpingCooldown:I", opcode=181, ordinal=1))
    private void modifyJumpingCooldown(class_1309 instance, int jumpingCooldown) {
        if (instance == Constants.mc.field_1724) {
            JumpingCooldownEvent event = new JumpingCooldownEvent(jumpingCooldown);
            EventDispatcher.dispatch(event);
            this.field_6228 = event.getCooldown();
        } else {
            this.field_6228 = 10;
        }
    }

    @ModifyConstant(method={"getHandSwingDuration"}, constant={@Constant(intValue=6)})
    private int modifyHandSwingDuration(int value) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        return animationsModule.isEnabled() ? (int)((float)value * animationsModule.getSwingSlowdown()) : value;
    }

    @Redirect(method={"jump"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getYaw()F"))
    private float redirectYaw(class_1309 instance) {
        if (instance instanceof class_746) {
            class_746 player = (class_746)instance;
            if (OpalClient.getInstance().getModuleRepository().getModule(MovementFixModule.class).isFixMovement()) {
                return instance.method_36454();
            }
            TargetStrafeModule targetStrafeModule = OpalClient.getInstance().getModuleRepository().getModule(TargetStrafeModule.class);
            if (targetStrafeModule.isEnabled() && targetStrafeModule.isActive()) {
                return targetStrafeModule.getYaw();
            }
            float yaw = RotationHelper.getClientHandler().getYawOr(instance.method_36454());
            if (SprintModule.isOmniSprint() && !player.field_3913.method_20622()) {
                if (player.field_3913.method_3128().field_1342 < -1.0E-5f) {
                    return yaw + 180.0f;
                }
                return player.field_3913.method_3128().field_1343 > 0.0f ? yaw - 90.0f : yaw + 90.0f;
            }
            return yaw;
        }
        return instance.method_36454();
    }

    @Redirect(method={"dropItem"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;swingHand(Lnet/minecraft/util/Hand;)V"))
    private void redirectDropSwing(class_1309 instance, class_1268 hand) {
        if (instance instanceof class_746) {
            class_746 clientPlayer = (class_746)instance;
            AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
            if (animationsModule.isEnabled() && animationsModule.isHideDropSwing()) {
                ((ClientPlayerEntityAccess)clientPlayer).opal$swingHandServerside(hand);
                return;
            }
        }
        instance.method_6104(hand);
    }

    @Inject(method={"jump"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookJumpEvent(CallbackInfo ci) {
        if (this != Constants.mc.field_1724) {
            return;
        }
        JumpEvent jumpEvent = new JumpEvent(this.method_5624());
        EventDispatcher.dispatch(jumpEvent);
        if (jumpEvent.isCancelled()) {
            ci.cancel();
        }
    }
}

