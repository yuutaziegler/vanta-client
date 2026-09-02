/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.v2.WrapWithCondition
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_243
 *  net.minecraft.class_2535
 *  net.minecraft.class_2596
 *  net.minecraft.class_2664
 *  net.minecraft.class_2708
 *  net.minecraft.class_2739
 *  net.minecraft.class_2743
 *  net.minecraft.class_2797
 *  net.minecraft.class_310
 *  net.minecraft.class_3515$class_7426
 *  net.minecraft.class_408
 *  net.minecraft.class_634
 *  net.minecraft.class_746
 *  net.minecraft.class_7469
 *  net.minecraft.class_7608
 *  net.minecraft.class_7610$class_7612
 *  net.minecraft.class_7637
 *  net.minecraft.class_7637$class_7816
 *  net.minecraft.class_8673
 *  net.minecraft.class_8675
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import java.time.Instant;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_243;
import net.minecraft.class_2535;
import net.minecraft.class_2596;
import net.minecraft.class_2664;
import net.minecraft.class_2708;
import net.minecraft.class_2739;
import net.minecraft.class_2743;
import net.minecraft.class_2797;
import net.minecraft.class_310;
import net.minecraft.class_3515;
import net.minecraft.class_408;
import net.minecraft.class_634;
import net.minecraft.class_746;
import net.minecraft.class_7469;
import net.minecraft.class_7608;
import net.minecraft.class_7610;
import net.minecraft.class_7637;
import net.minecraft.class_8673;
import net.minecraft.class_8675;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;
import wtf.opal.client.screen.click.dropdown.DropdownClickGUI;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.player.movement.knockback.VelocityUpdateEvent;
import wtf.opal.event.impl.game.player.teleport.PostTeleportEvent;
import wtf.opal.event.impl.game.player.teleport.PreTeleportEvent;
import wtf.opal.mixin.EntityAccessor;
import wtf.opal.mixin.LivingEntityAccessor;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_634.class})
public abstract class ClientPlayNetworkHandlerMixin
extends class_8673 {
    @Shadow
    private class_7637 field_39858;
    @Shadow
    private class_7610.class_7612 field_39808;
    @Unique
    private PreTeleportEvent preTeleportEvent;

    protected ClientPlayNetworkHandlerMixin(class_310 client, class_2535 connection, class_8675 connectionState) {
        super(client, connection, connectionState);
    }

    @WrapWithCondition(method={"onCloseScreen"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;closeScreen()V")})
    private boolean preventScreenClose(class_746 instance) {
        return !(Constants.mc.field_1755 instanceof class_408) && !(Constants.mc.field_1755 instanceof DropdownClickGUI);
    }

    @Inject(method={"onPlayerPositionLook"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/NetworkThreadUtils;forceMainThread(Lnet/minecraft/network/packet/Packet;Lnet/minecraft/network/listener/PacketListener;Lnet/minecraft/network/PacketApplyBatcher;)V", shift=At.Shift.AFTER)}, cancellable=true)
    private void hookOnPlayerPositionLook(class_2708 packet, CallbackInfo ci) {
        this.preTeleportEvent = new PreTeleportEvent(packet.comp_3133(), packet.comp_3228(), packet.comp_3229());
        EventDispatcher.dispatch(this.preTeleportEvent);
        if (this.preTeleportEvent.isCancelled()) {
            ci.cancel();
            this.preTeleportEvent = null;
        }
    }

    @Inject(method={"onPlayerPositionLook"}, at={@At(value="TAIL")})
    private void hookOnPlayerPositionLookTail(class_2708 packet, CallbackInfo ci) {
        if (this.preTeleportEvent == null) {
            return;
        }
        EventDispatcher.dispatch(new PostTeleportEvent(this.preTeleportEvent.getTeleportId(), this.preTeleportEvent.getChange(), this.preTeleportEvent.getRelatives()));
        this.preTeleportEvent = null;
    }

    @Redirect(method={"onPlayerPositionLook"}, at=@At(value="INVOKE", target="Lnet/minecraft/network/packet/s2c/play/PlayerPositionLookS2CPacket;teleportId()I"))
    private int redirectTeleportId(class_2708 instance) {
        return this.preTeleportEvent == null ? 0 : this.preTeleportEvent.getTeleportId();
    }

    @Inject(method={"sendChatMessage"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSendChatMessage(String message, CallbackInfo ci) {
        Instant instant = Instant.now();
        long l = class_3515.class_7426.method_43531();
        class_7637.class_7816 lastSeenMessages = this.field_39858.method_46266();
        class_7469 messageSignatureData = this.field_39808.pack(new class_7608(message, instant, l, lastSeenMessages.comp_1073()));
        this.method_52787((class_2596)new class_2797(message, instant, l, messageSignatureData, lastSeenMessages.comp_1074()));
        ci.cancel();
    }

    @Inject(method={"onEntityTrackerUpdate"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/data/DataTracker;writeUpdatedEntries(Ljava/util/List;)V", shift=At.Shift.BEFORE)})
    private void onEntityTrackerUpdate(class_2739 packet, CallbackInfo ci) {
        AnimationsModule animationsModule;
        if (Constants.mc.field_1724 != null && packet.comp_1127() == Constants.mc.field_1724.method_5628() && (animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class)).isEnabled() && animationsModule.isFixPoseRepeat()) {
            packet.comp_1128().removeIf(entry -> ClientPlayNetworkHandlerMixin.isBadId(entry.comp_1115()));
        }
    }

    @Unique
    private static boolean isBadId(int id) {
        return id == EntityAccessor.getTrackedPose().comp_2327() || id == LivingEntityAccessor.getTrackedLivingFlags().comp_2327();
    }

    @Inject(method={"onEntityVelocityUpdate"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;setVelocityClient(Lnet/minecraft/util/math/Vec3d;)V")}, cancellable=true)
    private void hookVelocityUpdate(class_2743 packet, CallbackInfo ci) {
        if (packet.method_11818() != Constants.mc.field_1724.method_5628()) {
            return;
        }
        VelocityUpdateEvent velocityUpdateEvent = new VelocityUpdateEvent(packet.method_73085().field_1352, packet.method_73085().field_1351, packet.method_73085().field_1350, false);
        EventDispatcher.dispatch(velocityUpdateEvent);
        if (!velocityUpdateEvent.isCancelled()) {
            Constants.mc.field_1724.method_5750(new class_243(velocityUpdateEvent.getVelocityX(), velocityUpdateEvent.getVelocityY(), velocityUpdateEvent.getVelocityZ()));
        }
        ci.cancel();
    }

    @Inject(method={"onExplosion"}, at={@At(value="INVOKE", target="Lnet/minecraft/network/packet/s2c/play/ExplosionS2CPacket;playerKnockback()Ljava/util/Optional;")}, cancellable=true)
    private void hookVelocityUpdateExplosion(class_2664 packet, CallbackInfo ci) {
        packet.comp_2884().ifPresent(knockback -> {
            class_243 velocityAdded = Constants.mc.field_1724.method_18798().method_1019(knockback);
            VelocityUpdateEvent velocityUpdateEvent = new VelocityUpdateEvent(velocityAdded.method_10216(), velocityAdded.method_10214(), velocityAdded.method_10215(), true);
            EventDispatcher.dispatch(velocityUpdateEvent);
            if (!velocityUpdateEvent.isCancelled()) {
                Constants.mc.field_1724.method_45319(new class_243(velocityUpdateEvent.getVelocityX(), velocityUpdateEvent.getVelocityY(), velocityUpdateEvent.getVelocityZ()));
            }
            ci.cancel();
        });
    }
}

