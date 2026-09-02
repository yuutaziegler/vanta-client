/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10042
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_3883
 *  net.minecraft.class_5617$class_5618
 *  net.minecraft.class_583
 *  net.minecraft.class_746
 *  net.minecraft.class_897
 *  net.minecraft.class_922
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10042;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_3883;
import net.minecraft.class_5617;
import net.minecraft.class_583;
import net.minecraft.class_746;
import net.minecraft.class_897;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.module.impl.visual.NoHurtCameraModule;
import wtf.opal.client.feature.module.impl.visual.esp.ESPModule;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_922.class})
public abstract class LivingEntityRendererMixin<T extends class_1309, S extends class_10042, M extends class_583<? super S>>
extends class_897<T, S>
implements class_3883<S, M> {
    private LivingEntityRendererMixin(class_5617.class_5618 context) {
        super(context);
    }

    @Inject(method={"hasLabel(Lnet/minecraft/entity/LivingEntity;D)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookHasLabel(T livingEntity, double d, CallbackInfoReturnable<Boolean> cir) {
        ESPModule espModule;
        if (livingEntity instanceof class_1657 && (espModule = OpalClient.getInstance().getModuleRepository().getModule(ESPModule.class)).isEnabled() && espModule.getSettings().areNameTagsEnabled() && LocalDataWatch.getTargetList().hasTarget(livingEntity.method_5628())) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;F)V"}, at={@At(value="TAIL")})
    private void hookUpdateRenderStateTail(T livingEntity, S livingEntityRenderState, float f, CallbackInfo ci) {
        NoHurtCameraModule noHurtCameraModule;
        if (livingEntity instanceof class_746 && (noHurtCameraModule = OpalClient.getInstance().getModuleRepository().getModule(NoHurtCameraModule.class)).isEnabled() && noHurtCameraModule.isHideModelDamage()) {
            ((class_10042)livingEntityRenderState).field_53460 = false;
        }
    }
}

