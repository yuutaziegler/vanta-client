/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2680
 *  net.minecraft.class_4970
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2680;
import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.player.interaction.block.BlockBreakCanHarvestEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_4970.class})
public final class AbstractBlockMixin {
    private AbstractBlockMixin() {
    }

    @ModifyExpressionValue(method={"calcBlockBreakingDelta"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;canHarvest(Lnet/minecraft/block/BlockState;)Z")})
    private boolean redirectCanHarvest(boolean original, @Local(argsOnly=true) class_2680 state) {
        BlockBreakCanHarvestEvent event = new BlockBreakCanHarvestEvent(state, original);
        EventDispatcher.dispatch(event);
        return event.isCanHarvest();
    }
}

