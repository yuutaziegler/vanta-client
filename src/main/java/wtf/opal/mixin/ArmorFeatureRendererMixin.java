/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10034
 *  net.minecraft.class_11659
 *  net.minecraft.class_4587
 *  net.minecraft.class_970
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10034;
import net.minecraft.class_11659;
import net.minecraft.class_4587;
import net.minecraft.class_970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.utility.render.EntityRenderStateUtility;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_970.class})
public abstract class ArmorFeatureRendererMixin<S extends class_10034> {
    @Inject(method={"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V"}, at={@At(value="HEAD")})
    private void opal$setHumanRenderState(class_4587 matrixStack, class_11659 orderedRenderCommandQueue, int i, S bipedEntityRenderState, float f, float g, CallbackInfo ci) {
        EntityRenderStateUtility.setHumanRenderState(bipedEntityRenderState);
    }

    @Inject(method={"render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V"}, at={@At(value="TAIL")})
    private void opal$clearHumanRenderState(class_4587 matrixStack, class_11659 orderedRenderCommandQueue, int i, S bipedEntityRenderState, float f, float g, CallbackInfo ci) {
        EntityRenderStateUtility.clearHumanRenderState();
    }
}

