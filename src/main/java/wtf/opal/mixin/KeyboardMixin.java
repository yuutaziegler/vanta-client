/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11908
 *  net.minecraft.class_309
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_309;
import net.minecraft.class_310;
import net.minecraft.class_437;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.screen.TerentXClientMenuScreen;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.press.KeyPressEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_309.class})
public final class KeyboardMixin {
    private KeyboardMixin() {
    }

    @Inject(at={@At(value="HEAD")}, method={"onKey"}, cancellable=true)
    public void onKey(long window, int action, class_11908 input, CallbackInfo ci) {
        if (action == 1) {
            if (input.comp_4795() == -1) {
                return;
            }
            if (input.comp_4795() == 344) {
                class_310 client = class_310.method_1551();
                if (client.field_1755 == null) {
                    client.method_1507((class_437)new TerentXClientMenuScreen(null));
                    ci.cancel();
                    return;
                }
                if (client.field_1755 instanceof TerentXClientMenuScreen) {
                    client.method_1507(null);
                    ci.cancel();
                    return;
                }
            }
            EventDispatcher.dispatch(new KeyPressEvent(input.comp_4795()));
        }
    }
}

