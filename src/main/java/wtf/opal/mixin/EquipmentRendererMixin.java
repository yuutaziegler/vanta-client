/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.wrapoperation.Operation
 *  com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10034
 *  net.minecraft.class_10197
 *  net.minecraft.class_1921
 *  net.minecraft.class_2960
 *  net.minecraft.class_4608
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10034;
import net.minecraft.class_10197;
import net.minecraft.class_1921;
import net.minecraft.class_2960;
import net.minecraft.class_4608;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;
import wtf.opal.utility.render.EntityRenderStateUtility;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_10197.class})
public abstract class EquipmentRendererMixin {
    @WrapOperation(method={"render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;II)V"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/RenderLayer;getArmorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;")})
    private class_1921 opal$renderArmorTintLayer(class_2960 texture, Operation<class_1921> original) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled() && animationsModule.isOldArmorDamageTint()) {
            return class_1921.method_28116((class_2960)texture);
        }
        return (class_1921)original.call(new Object[]{texture});
    }

    @ModifyArg(method={"render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;II)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/command/RenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V", ordinal=0), index=4)
    private int opal$modifyArmorUv(int light) {
        return this.opal$packUv(light);
    }

    @ModifyArg(method={"render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;II)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/command/RenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V", ordinal=2), index=4)
    private int opal$modifyArmorTrimUv(int light) {
        return this.opal$packUv(light);
    }

    @Unique
    private int opal$packUv(int original) {
        class_10034 humanRenderState = EntityRenderStateUtility.getHumanRenderState();
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled() && animationsModule.isOldArmorDamageTint() && humanRenderState != null) {
            return class_4608.method_23625((int)class_4608.method_23210((float)0.0f), (int)class_4608.method_23212((boolean)humanRenderState.field_53460));
        }
        return original;
    }
}

