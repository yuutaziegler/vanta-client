/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11910
 *  net.minecraft.class_1661
 *  net.minecraft.class_312
 *  net.minecraft.class_315
 *  net.minecraft.class_746
 *  net.minecraft.class_9928
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11910;
import net.minecraft.class_1661;
import net.minecraft.class_312;
import net.minecraft.class_315;
import net.minecraft.class_746;
import net.minecraft.class_9928;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.input.MouseUpdateEvent;
import wtf.opal.event.impl.press.MousePressEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_312.class})
public final class MouseMixin {
    @Shadow
    private double field_1789;
    @Shadow
    private double field_1787;
    @Unique
    private MouseUpdateEvent event;
    @Unique
    private boolean unlockCursorRun;

    private MouseMixin() {
    }

    @Inject(method={"onMouseButton"}, at={@At(value="HEAD")})
    private void onMouseButton(long window, class_11910 input, int action, CallbackInfo ci) {
        if (action == 1) {
            if (input.comp_4801() == -1) {
                return;
            }
            EventDispatcher.dispatch(new MousePressEvent(input.comp_4801()));
        }
    }

    @Redirect(method={"onMouseScroll"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerInventory;setSelectedSlot(I)V"))
    private void setSelectedSlot(class_1661 instance, int slot, @Local int i) {
        SlotHelper slotHelper = SlotHelper.getInstance();
        if (slotHelper.isActive()) {
            if (slotHelper.getSilence() != SlotHelper.Silence.NONE) {
                slotHelper.setVisualSlot(class_9928.method_61972((double)i, (int)slotHelper.getVisualSlot(), (int)class_1661.method_7368()));
            }
        } else {
            instance.method_61496(slot);
        }
    }

    @Redirect(method={"updateMouse"}, at=@At(value="FIELD", target="Lnet/minecraft/client/option/GameOptions;smoothCameraEnabled:Z", opcode=180))
    private boolean redirectCheck(class_315 instance, @Local(ordinal=3) double multiplier) {
        this.event = new MouseUpdateEvent(this.field_1789, this.field_1787, multiplier, this.unlockCursorRun);
        EventDispatcher.dispatch(this.event);
        return instance.field_1914 && !this.event.isHandled();
    }

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/Mouse;isCursorLocked()Z"))
    private boolean redirectTickCursorLock(class_312 instance) {
        if (instance.method_1613()) {
            return true;
        }
        if (RotationHelper.getHandler().isUnlockCursor()) {
            this.unlockCursorRun = true;
            return true;
        }
        return false;
    }

    @Inject(method={"updateMouse"}, at={@At(value="TAIL")})
    private void updateMouseTail(double timeDelta, CallbackInfo ci) {
        RotationHelper.getClientHandler().onPostMouseUpdate();
        this.unlockCursorRun = false;
        this.event = null;
    }

    @Redirect(method={"updateMouse"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingSpyglass()Z"))
    private boolean redirectCheck(class_746 instance) {
        return instance.method_31550() && (this.event == null || !this.event.isHandled());
    }

    @Redirect(method={"updateMouse"}, at=@At(value="FIELD", target="Lnet/minecraft/client/Mouse;cursorDeltaX:D", opcode=180))
    private double redirectCursorX(class_312 instance) {
        return this.event == null ? 0.0 : this.event.getDeltaX();
    }

    @Redirect(method={"updateMouse"}, at=@At(value="FIELD", target="Lnet/minecraft/client/Mouse;cursorDeltaY:D", opcode=180))
    private double redirectCursorY(class_312 instance) {
        return this.event == null ? 0.0 : this.event.getDeltaY();
    }
}

