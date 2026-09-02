/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_1937
 *  net.minecraft.class_243
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.module.impl.movement.MovementFixModule;
import wtf.opal.client.feature.module.impl.movement.TargetStrafeModule;
import wtf.opal.client.feature.module.impl.movement.physics.PhysicsModule;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.game.player.movement.PreMoveEvent;
import wtf.opal.event.impl.game.player.movement.step.StepSuccessEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_1297.class})
public abstract class EntityMixin {
    @Shadow
    private class_1937 field_6002;

    private EntityMixin() {
    }

    @Inject(method={"setYaw"}, at={@At(value="HEAD")})
    private void setYaw(float yaw, CallbackInfo ci) {
        this.checkRotation();
    }

    @Inject(method={"setPitch"}, at={@At(value="HEAD")})
    private void setPitch(float pitch, CallbackInfo ci) {
        this.checkRotation();
    }

    @Redirect(method={"updateVelocity"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;getYaw()F"))
    private float redirectYaw(class_1297 instance) {
        TargetStrafeModule targetStrafeModule;
        boolean isPlayer;
        boolean bl = isPlayer = Constants.mc.field_1724 != null && this == Constants.mc.field_1724;
        if (isPlayer && (targetStrafeModule = OpalClient.getInstance().getModuleRepository().getModule(TargetStrafeModule.class)).isEnabled() && targetStrafeModule.isActive()) {
            return targetStrafeModule.getYaw();
        }
        if (isPlayer && !OpalClient.getInstance().getModuleRepository().getModule(MovementFixModule.class).isFixMovement()) {
            return RotationHelper.getClientHandler().getYawOr(instance.method_36454());
        }
        return instance.method_36454();
    }

    @Inject(method={"updateVelocity"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookUpdateVelocityHead(float speed, class_243 movementInput, CallbackInfo ci) {
        if (this == Constants.mc.field_1724) {
            PreMoveEvent event = new PreMoveEvent(speed, movementInput);
            EventDispatcher.dispatch(event);
            if (event.isCancelled()) {
                ci.cancel();
            }
        }
    }

    @Inject(method={"updateVelocity"}, at={@At(value="TAIL")})
    private void hookUpdateVelocityTail(float speed, class_243 movementInput, CallbackInfo ci) {
        if (this == Constants.mc.field_1724) {
            EventDispatcher.dispatch(new PostMoveEvent(speed, movementInput));
        }
    }

    @Inject(method={"adjustMovementForCollisions(Lnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;"}, at={@At(value="RETURN", ordinal=0)}, cancellable=true)
    private void hookStepHeight(class_243 movement, CallbackInfoReturnable<class_243> cir) {
        if (this == Constants.mc.field_1724) {
            StepSuccessEvent movementCollisionsEvent = new StepSuccessEvent(movement, (class_243)cir.getReturnValue());
            EventDispatcher.dispatch(movementCollisionsEvent);
            cir.setReturnValue((Object)movementCollisionsEvent.getAdjustedVec());
        }
    }

    @Redirect(method={"move"}, at=@At(value="FIELD", target="Lnet/minecraft/entity/Entity;groundCollision:Z", opcode=181))
    private void hookMoveTail(class_1297 instance, boolean value, @Local(argsOnly=true) class_243 movement) {
        instance.field_36331 = instance instanceof class_746 && OpalClient.getInstance().getModuleRepository().getModule(PhysicsModule.class).isEnabled() ? value && movement.field_1351 < -0.01 : value;
    }

    @Unique
    private void checkRotation() {
        if (Constants.mc.field_1724 != null && this == Constants.mc.field_1724 && this.field_6002.method_8608()) {
            RotationHelper.getClientHandler().onRotationSet();
        }
    }

    @ModifyExpressionValue(method={"move"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;isLogicalSideForUpdatingMovement()Z")})
    private boolean fixFallDistanceCalculation(boolean original) {
        if (this == Constants.mc.field_1724) {
            return true;
        }
        return original;
    }
}

