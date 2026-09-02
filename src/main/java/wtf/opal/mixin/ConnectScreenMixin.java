/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_310
 *  net.minecraft.class_412
 *  net.minecraft.class_639
 *  net.minecraft.class_642
 *  net.minecraft.class_9112
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import net.minecraft.class_412;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_9112;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.server.ServerConnectEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_412.class})
public final class ConnectScreenMixin {
    private ConnectScreenMixin() {
    }

    @Inject(method={"connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;Lnet/minecraft/client/network/ServerInfo;Lnet/minecraft/client/network/CookieStorage;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onConnect(class_310 client, class_639 address, class_642 info, class_9112 cookieStorage, CallbackInfo ci) {
        ServerConnectEvent serverConnectEvent = new ServerConnectEvent(address);
        EventDispatcher.dispatch(serverConnectEvent);
        if (serverConnectEvent.isCancelled()) {
            ci.cancel();
        }
    }
}

