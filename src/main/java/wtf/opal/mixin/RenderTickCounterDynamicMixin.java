/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_9779$class_9781
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.client.feature.helper.impl.player.timer.TimerHelper;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_9779.class_9781.class})
public final class RenderTickCounterDynamicMixin {
    @Shadow
    private float field_51958;

    private RenderTickCounterDynamicMixin() {
    }

    @Inject(at={@At(value="FIELD", target="Lnet/minecraft/client/render/RenderTickCounter$Dynamic;lastTimeMillis:J", opcode=181, ordinal=0)}, method={"beginRenderTick(J)I"})
    public void onBeginRenderTick(long timeMillis, CallbackInfoReturnable<Integer> cir) {
        float timer = TimerHelper.getInstance().timer;
        if (timer > 0.0f) {
            this.field_51958 *= timer;
        }
    }
}

