/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10034
 *  net.minecraft.class_10442
 *  net.minecraft.class_1309
 *  net.minecraft.class_909
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10034;
import net.minecraft.class_10442;
import net.minecraft.class_1309;
import net.minecraft.class_909;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.duck.BipedEntityRenderStateAccess;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_909.class})
public final class BipedEntityRendererMixin {
    @Inject(method={"updateBipedRenderState"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;updateRenderState(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/client/render/entity/state/ArmedEntityRenderState;Lnet/minecraft/client/item/ItemModelManager;)V", shift=At.Shift.AFTER)})
    private static void updateEntityField(class_1309 entity, class_10034 state, float tickDelta, class_10442 itemModelResolver, CallbackInfo ci) {
        ((BipedEntityRenderStateAccess)state).opal$setEntity(entity);
    }
}

