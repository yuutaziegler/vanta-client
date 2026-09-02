/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10185
 *  net.minecraft.class_304
 *  net.minecraft.class_743
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10185;
import net.minecraft.class_304;
import net.minecraft.class_743;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wtf.opal.client.Constants;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.input.MoveInputEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_743.class})
public final class KeyboardInputMixin {
    @Unique
    private MoveInputEvent moveInputEvent;

    private KeyboardInputMixin() {
    }

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;isPressed()Z", ordinal=4))
    private boolean hookMoveInputEventJump(class_304 instance) {
        return this.moveInputEvent != null && this.moveInputEvent.isJump();
    }

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/input/KeyboardInput;getMovementMultiplier(ZZ)F", ordinal=0))
    private float hookMoveInputEventForward(boolean positive, boolean negative) {
        return this.moveInputEvent == null ? 0.0f : this.moveInputEvent.getForward();
    }

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/input/KeyboardInput;getMovementMultiplier(ZZ)F", ordinal=1))
    private float hookMoveInputEventStrafe(boolean positive, boolean negative) {
        return this.moveInputEvent == null ? 0.0f : this.moveInputEvent.getSideways();
    }

    @ModifyExpressionValue(method={"tick"}, at={@At(value="NEW", target="(ZZZZZZZ)Lnet/minecraft/util/PlayerInput;")})
    private class_10185 modifyInput(class_10185 original) {
        this.moveInputEvent = new MoveInputEvent(KeyboardInputMixin.getMovementMultiplier(Constants.mc.field_1690.field_1894.method_1434(), Constants.mc.field_1690.field_1881.method_1434()), KeyboardInputMixin.getMovementMultiplier(Constants.mc.field_1690.field_1913.method_1434(), Constants.mc.field_1690.field_1849.method_1434()), Constants.mc.field_1690.field_1903.method_1434(), original.comp_3164());
        EventDispatcher.dispatch(this.moveInputEvent);
        return new class_10185(this.moveInputEvent.getForward() > 0.0f, this.moveInputEvent.getForward() < 0.0f, this.moveInputEvent.getSideways() > 0.0f, this.moveInputEvent.getSideways() < 0.0f, this.moveInputEvent.isJump(), this.moveInputEvent.isSneak(), original.comp_3165());
    }

    @Unique
    private static float getMovementMultiplier(boolean positive, boolean negative) {
        if (positive == negative) {
            return 0.0f;
        }
        return positive ? 1.0f : -1.0f;
    }
}

