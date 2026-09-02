/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.channel.Channel
 *  io.netty.channel.ChannelFutureListener
 *  io.netty.channel.ChannelHandlerContext
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2535
 *  net.minecraft.class_2547
 *  net.minecraft.class_2561
 *  net.minecraft.class_2596
 *  net.minecraft.class_2598
 *  net.minecraft.class_2987
 *  net.minecraft.class_8042
 *  org.jetbrains.annotations.Nullable
 *  org.slf4j.Logger
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import java.util.concurrent.RejectedExecutionException;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2561;
import net.minecraft.class_2596;
import net.minecraft.class_2598;
import net.minecraft.class_2987;
import net.minecraft.class_8042;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.InboundNetworkBlockage;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.OutboundNetworkBlockage;
import wtf.opal.duck.ClientConnectionAccess;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.packet.InstantaneousReceivePacketEvent;
import wtf.opal.event.impl.game.packet.InstantaneousSendPacketEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.packet.SendPacketEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_2535.class})
public abstract class ClientConnectionMixin
implements ClientConnectionAccess {
    @Shadow
    @Nullable
    private volatile class_2547 field_11652;
    @Shadow
    @Final
    private static Logger field_11642;
    @Shadow
    private int field_11658;
    @Shadow
    private Channel field_11651;

    @Shadow
    private static <T extends class_2547> void method_10759(class_2596<T> packet, class_2547 listener) {
    }

    @Shadow
    public abstract void method_10747(class_2561 var1);

    @Shadow
    protected abstract void method_10770(ChannelHandlerContext var1, class_2596<?> var2);

    @Shadow
    public abstract class_2598 method_36121();

    private ClientConnectionMixin() {
    }

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void init(class_2598 side, CallbackInfo ci) {
        InboundNetworkBlockage.get().reset();
        OutboundNetworkBlockage.get().reset();
    }

    @Inject(method={"send(Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookSendPacket(class_2596<?> packet, CallbackInfo ci) {
        InstantaneousSendPacketEvent event = new InstantaneousSendPacketEvent(packet);
        EventDispatcher.dispatch(event);
        if (event.isCancelled() || OutboundNetworkBlockage.get().isBlocked(packet)) {
            ci.cancel();
        }
    }

    @Inject(method={"send(Lnet/minecraft/network/packet/Packet;Lio/netty/channel/ChannelFutureListener;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookSendPacket(class_2596<?> packet, @Nullable ChannelFutureListener channelFutureListener, CallbackInfo ci) {
        SendPacketEvent event = new SendPacketEvent(packet);
        EventDispatcher.dispatch(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"handlePacket"}, at={@At(value="HEAD")}, cancellable=true, require=1)
    private static void hookReceivePacket(class_2596<?> packet, class_2547 listener, CallbackInfo ci) {
        if (packet instanceof class_8042) {
            class_8042 bundleS2CPacket = (class_8042)packet;
            ci.cancel();
            for (class_2596 packetInBundle : bundleS2CPacket.method_48324()) {
                try {
                    ClientConnectionMixin.method_10759(packetInBundle, listener);
                }
                catch (class_2987 class_29872) {}
            }
            return;
        }
        ReceivePacketEvent event = new ReceivePacketEvent(packet);
        EventDispatcher.dispatch(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method={"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/packet/Packet;)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookChannelRead(ChannelHandlerContext channelHandlerContext, class_2596<?> packet, CallbackInfo ci) {
        if (this.method_36121() == class_2598.field_11942) {
            if (packet instanceof class_8042) {
                class_8042 bundleS2CPacket = (class_8042)packet;
                ci.cancel();
                for (class_2596 packetInBundle : bundleS2CPacket.method_48324()) {
                    try {
                        this.method_10770(channelHandlerContext, packetInBundle);
                    }
                    catch (class_2987 class_29872) {}
                }
                return;
            }
            InstantaneousReceivePacketEvent event = new InstantaneousReceivePacketEvent(packet);
            EventDispatcher.dispatch(event);
            if (event.isCancelled() || InboundNetworkBlockage.get().isBlocked(packet)) {
                ci.cancel();
            }
        }
    }

    @Override
    @Unique
    public void opal$channelReadSilent(class_2596<?> packet) {
        if (this.field_11651.isOpen()) {
            class_2547 packetListener = this.field_11652;
            if (packetListener == null) {
                throw new IllegalStateException("Received a packet before the packet listener was initialized");
            }
            if (packetListener.method_52413(packet)) {
                try {
                    ClientConnectionMixin.method_10759(packet, packetListener);
                }
                catch (class_2987 class_29872) {
                }
                catch (RejectedExecutionException var6) {
                    this.method_10747((class_2561)class_2561.method_43471((String)"multiplayer.disconnect.server_shutdown"));
                }
                catch (ClassCastException var7) {
                    field_11642.error("Received {} that couldn't be processed", (Object)packet.getClass(), (Object)var7);
                    this.method_10747((class_2561)class_2561.method_43471((String)"multiplayer.disconnect.invalid_packet"));
                }
                ++this.field_11658;
            }
        }
    }
}

