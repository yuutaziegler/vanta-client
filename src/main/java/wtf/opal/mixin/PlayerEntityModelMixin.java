/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10055
 *  net.minecraft.class_10426
 *  net.minecraft.class_1268
 *  net.minecraft.class_1306
 *  net.minecraft.class_1309
 *  net.minecraft.class_572$class_573
 *  net.minecraft.class_591
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10055;
import net.minecraft.class_10426;
import net.minecraft.class_1268;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_572;
import net.minecraft.class_591;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import wtf.opal.client.Constants;
import wtf.opal.duck.BipedEntityRenderStateAccess;
import wtf.opal.utility.player.BlockUtility;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_591.class})
public final class PlayerEntityModelMixin {
    @ModifyVariable(method={"setAngles(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)V"}, at=@At(value="HEAD"), argsOnly=true)
    private class_10055 modifyThirdPersonRenderState(class_10055 state) {
        class_1309 livingEntity = ((BipedEntityRenderStateAccess)state).opal$getEntity();
        if (livingEntity == Constants.mc.field_1724 && BlockUtility.isThirdPersonBlockingState((class_10426)state)) {
            state.field_53414 = true;
            state.field_53409 = class_1268.field_5808;
            if (state.field_55303 == class_1306.field_6183) {
                state.field_55306 = class_572.class_573.field_3409;
                state.field_55304 = class_572.class_573.field_3406;
            } else {
                state.field_55306 = class_572.class_573.field_3406;
                state.field_55304 = class_572.class_573.field_3409;
            }
        }
        return state;
    }
}

