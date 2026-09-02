/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.wrapoperation.Operation
 *  com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10034
 *  net.minecraft.class_1306
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  net.minecraft.class_1819
 *  net.minecraft.class_3881
 *  net.minecraft.class_3882
 *  net.minecraft.class_572
 *  net.minecraft.class_583
 *  net.minecraft.class_630
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10034;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_1819;
import net.minecraft.class_3881;
import net.minecraft.class_3882;
import net.minecraft.class_572;
import net.minecraft.class_583;
import net.minecraft.class_630;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;
import wtf.opal.duck.BipedEntityRenderStateAccess;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_572.class})
public abstract class BipedEntityModelMixin<T extends class_10034>
extends class_583<T>
implements class_3881,
class_3882 {
    @Final
    @Shadow
    public class_630 field_3398;

    private BipedEntityModelMixin(class_630 root) {
        super(root);
    }

    @WrapOperation(method={"positionBlockingArm"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/math/MathHelper;clamp(FFF)F")})
    private float fixThirdPersonBlockRotation(float value, float min, float max, Operation<Float> original) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        return animationsModule.isEnabled() && animationsModule.isSwordBlocking() ? 0.0f : ((Float)original.call(new Object[]{Float.valueOf(value), Float.valueOf(min), Float.valueOf(max)})).floatValue();
    }

    @WrapOperation(method={"positionLeftArm", "positionRightArm"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/model/BipedEntityModel;positionBlockingArm(Lnet/minecraft/client/model/ModelPart;Z)V")})
    private void fixThirdPersonBlockPosition(class_572<?> instance, class_630 arm, boolean rightArm, Operation<Void> original, @Local(argsOnly=true) T state) {
        class_1309 entity;
        original.call(new Object[]{instance, arm, rightArm});
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled() && animationsModule.isSwordBlocking() && (entity = ((BipedEntityRenderStateAccess)state).opal$getEntity()) instanceof class_1309) {
            class_1309 livingEntity = entity;
            if (state instanceof class_10034) {
                class_1799 stack;
                class_1799 class_17992 = stack = rightArm ? livingEntity.method_61420(class_1306.field_6183) : livingEntity.method_61420(class_1306.field_6182);
                if (!(stack.method_7909() instanceof class_1819)) {
                    arm.field_3654 = arm.field_3654 * 0.5f - 0.62831855f;
                    arm.field_3675 = 0.0f;
                }
            }
        }
    }
}

