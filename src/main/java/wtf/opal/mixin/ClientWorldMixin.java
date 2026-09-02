/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_3414
 *  net.minecraft.class_3419
 *  net.minecraft.class_638
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3414;
import net.minecraft.class_3419;
import net.minecraft.class_638;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.world.PlaySoundEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_638.class})
public final class ClientWorldMixin {
    private ClientWorldMixin() {
    }

    @Inject(method={"playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void playSound(double x, double y, double z, class_3414 event, class_3419 category, float volume, float pitch, boolean useDistance, long seed, CallbackInfo ci) {
        PlaySoundEvent playSoundEvent = new PlaySoundEvent(event, x, y, z);
        EventDispatcher.dispatch(playSoundEvent);
        if (playSoundEvent.isCancelled()) {
            ci.cancel();
        }
    }
}

