/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10055
 *  net.minecraft.class_1007
 *  net.minecraft.class_1268
 *  net.minecraft.class_1799
 *  net.minecraft.class_1819
 *  net.minecraft.class_5617$class_5618
 *  net.minecraft.class_583
 *  net.minecraft.class_591
 *  net.minecraft.class_742
 *  net.minecraft.class_922
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10055;
import net.minecraft.class_1007;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1819;
import net.minecraft.class_5617;
import net.minecraft.class_583;
import net.minecraft.class_591;
import net.minecraft.class_742;
import net.minecraft.class_922;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_1007.class})
public abstract class PlayerEntityRendererMixin
extends class_922<class_742, class_10055, class_591> {
    private PlayerEntityRendererMixin(class_5617.class_5618 ctx, class_591 model, float shadowRadius) {
        super(ctx, (class_583)model, shadowRadius);
    }

    @Redirect(method={"getArmPose(Lnet/minecraft/entity/PlayerLikeEntity;Lnet/minecraft/item/ItemStack;Lnet/minecraft/util/Hand;)Lnet/minecraft/client/render/entity/model/BipedEntityModel$ArmPose;"}, at=@At(value="INVOKE", target="Lnet/minecraft/item/ItemStack;isEmpty()Z"))
    private static boolean cancelShieldPose(class_1799 instance, @Local(argsOnly=true) class_1268 hand) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled() && animationsModule.isHideShield() && hand == class_1268.field_5810 && instance.method_7909() instanceof class_1819) {
            return true;
        }
        return instance.method_7960();
    }
}

