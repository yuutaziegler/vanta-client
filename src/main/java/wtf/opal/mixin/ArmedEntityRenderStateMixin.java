/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10426
 *  net.minecraft.class_1306
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10426;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.utility.player.BlockUtility;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_10426.class})
public final class ArmedEntityRenderStateMixin {
    @Redirect(method={"updateRenderState"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/LivingEntity;getStackInArm(Lnet/minecraft/util/Arm;)Lnet/minecraft/item/ItemStack;"))
    private static class_1799 hookGetStackInArm(class_1309 entity, class_1306 arm) {
        if (entity == Constants.mc.field_1724 && arm == Constants.mc.field_1724.method_6068() && BlockUtility.isNoSlowBlockingState()) {
            return SlotHelper.getInstance().getMainHandStack(Constants.mc.field_1724);
        }
        return entity.method_61420(arm);
    }
}

