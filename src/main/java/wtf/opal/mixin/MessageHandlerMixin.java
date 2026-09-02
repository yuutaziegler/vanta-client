/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2556$class_7602
 *  net.minecraft.class_2561
 *  net.minecraft.class_7471
 *  net.minecraft.class_7594
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import com.mojang.authlib.GameProfile;
import java.time.Instant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2556;
import net.minecraft.class_2561;
import net.minecraft.class_7471;
import net.minecraft.class_7594;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.chat.ChatReceivedEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_7594.class})
public final class MessageHandlerMixin {
    @Inject(method={"processChatMessageInternal"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/InGameHud;getChatHud()Lnet/minecraft/client/gui/hud/ChatHud;", ordinal=0)}, cancellable=true)
    private void hookOnSignedChatMessage(class_2556.class_7602 params, class_7471 message, class_2561 decorated, GameProfile sender, boolean onlyShowSecureChat, Instant receptionTimestamp, CallbackInfoReturnable<Boolean> cir) {
        this.opal$onChatMessage(decorated, cir);
    }

    @Inject(method={"processChatMessageInternal"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/InGameHud;getChatHud()Lnet/minecraft/client/gui/hud/ChatHud;", ordinal=1)}, cancellable=true)
    private void hookOnFilteredSignedChatMessage(class_2556.class_7602 params, class_7471 message, class_2561 decorated, GameProfile sender, boolean onlyShowSecureChat, Instant receptionTimestamp, CallbackInfoReturnable<Boolean> cir) {
        class_2561 filtered = message.comp_981().method_46256(message.method_44862());
        if (filtered == null) {
            return;
        }
        this.opal$onChatMessage(params.method_44837(filtered), cir);
    }

    @Inject(method={"method_45745"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookOnProfilelessChatMessage(class_2556.class_7602 params, class_2561 content, Instant receptionTimestamp, CallbackInfoReturnable<Boolean> cir) {
        this.opal$onChatMessage(params.method_44837(content), cir);
    }

    @Unique
    private void opal$onChatMessage(class_2561 message, CallbackInfoReturnable<Boolean> cir) {
        ChatReceivedEvent event = new ChatReceivedEvent(message, false);
        EventDispatcher.dispatch(event);
        if (event.isCancelled()) {
            cir.setReturnValue((Object)false);
        }
    }

    @Inject(method={"onGameMessage"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookOnGameMessage(class_2561 message, boolean overlay, CallbackInfo ci) {
        ChatReceivedEvent event = new ChatReceivedEvent(message, overlay);
        EventDispatcher.dispatch(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}

