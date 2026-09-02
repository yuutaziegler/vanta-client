/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_304
 */
package wtf.opal.client.feature.helper.impl.player.mouse;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_304;
import wtf.opal.mixin.KeyBindingAccessor;

@Environment(value=EnvType.CLIENT)
public final class MouseButton {
    private final class_304 keyBinding;
    private boolean pressed;
    private boolean disabled;
    private int holdTicks;
    private boolean showSwings = true;

    public MouseButton(class_304 keyBinding) {
        this.keyBinding = keyBinding;
    }

    public void setDisabled() {
        this.pressed = false;
        this.holdTicks = 0;
        this.disabled = true;
    }

    public void setPressed() {
        this.setPressed(true, 0);
    }

    public void setPressed(boolean pressed, int holdTicks) {
        this.pressed = pressed;
        this.holdTicks = holdTicks;
    }

    public boolean wasPressed() {
        if (this.disabled) {
            return false;
        }
        boolean pressed = false;
        if (this.pressed) {
            pressed = true;
            this.pressed = false;
        }
        return this.keyBinding.method_1436() || pressed;
    }

    public boolean isPressed() {
        if (this.disabled) {
            return false;
        }
        return this.keyBinding.method_1434() || this.pressed || this.holdTicks > 0;
    }

    public boolean isForcePressed() {
        return this.pressed;
    }

    public void tick() {
        if (this.holdTicks > 0) {
            --this.holdTicks;
        }
        if (this.keyBinding.method_1434() || this.holdTicks == 0) {
            this.showSwings = true;
        }
        this.pressed = false;
        this.disabled = false;
    }

    public int getHoldTicks() {
        return this.holdTicks;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public boolean isShowSwings() {
        return this.showSwings || ((KeyBindingAccessor)this.keyBinding).getTimesPressed() > 0;
    }

    public void setShowSwings(boolean showSwings) {
        this.showSwings = showSwings;
    }

    public class_304 getKeyBinding() {
        return this.keyBinding;
    }
}

