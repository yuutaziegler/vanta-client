/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_2680
 *  net.minecraft.class_4970$class_4971
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_4970;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.player.interaction.block.BlockBreakHardnessEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_4970.class_4971.class})
public final class AbstractBlockStateMixin {
    @Inject(method={"getHardness"}, at={@At(value="RETURN")}, cancellable=true)
    private void hookGetHardness(class_1922 world, class_2338 pos, CallbackInfoReturnable<Float> cir) {
        class_4970.class_4971 class_49712 = (class_4970.class_4971)this;
        if (class_49712 instanceof class_2680) {
            class_2680 blockState = (class_2680)class_49712;
            BlockBreakHardnessEvent event = new BlockBreakHardnessEvent(blockState, ((Float)cir.getReturnValue()).floatValue());
            EventDispatcher.dispatch(event);
            cir.setReturnValue((Object)Float.valueOf(event.getHardness()));
        }
    }
}

