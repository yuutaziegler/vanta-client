/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1661
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1661;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.input.SlotChangeEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_1661.class})
public final class PlayerInventoryMixin {
    @Inject(method={"setSelectedSlot"}, at={@At(value="FIELD", target="Lnet/minecraft/entity/player/PlayerInventory;selectedSlot:I")})
    private void hookSlotChangeMethod(int slot, CallbackInfo ci) {
        EventDispatcher.dispatch(new SlotChangeEvent(slot));
    }
}

