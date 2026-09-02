/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  com.mojang.authlib.GameProfile
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10185
 *  net.minecraft.class_1268
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2879
 *  net.minecraft.class_299
 *  net.minecraft.class_310
 *  net.minecraft.class_3469
 *  net.minecraft.class_634
 *  net.minecraft.class_638
 *  net.minecraft.class_742
 *  net.minecraft.class_744
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Final
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

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10185;
import net.minecraft.class_1268;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2879;
import net.minecraft.class_299;
import net.minecraft.class_310;
import net.minecraft.class_3469;
import net.minecraft.class_634;
import net.minecraft.class_638;
import net.minecraft.class_742;
import net.minecraft.class_744;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.module.impl.movement.SprintModule;
import wtf.opal.client.feature.module.impl.movement.noslow.NoSlowModule;
import wtf.opal.duck.ClientPlayerEntityAccess;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.player.PlayerCreateEvent;
import wtf.opal.event.impl.game.player.PreUpdateEvent;
import wtf.opal.event.impl.game.player.interaction.SwingEvent;
import wtf.opal.event.impl.game.player.interaction.VisualSwingEvent;
import wtf.opal.event.impl.game.player.movement.PostMovementPacketEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.impl.game.player.movement.PushOutOfBlocksEvent;
import wtf.opal.event.impl.game.player.movement.SlowdownEvent;
import wtf.opal.event.impl.game.player.movement.SprintEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_746.class})
public abstract class ClientPlayerEntityMixin
extends class_742
implements ClientPlayerEntityAccess {
    @Shadow
    public class_744 field_3913;
    @Shadow
    @Final
    public class_634 field_3944;
    @Shadow
    public float field_3916;
    @Unique
    private PreMovementPacketEvent preMovementPacketEvent;
    @Unique
    private boolean prevHandSwinging;
    @Unique
    private int prevHandSwingTicks;

    public ClientPlayerEntityMixin(class_638 world, GameProfile profile) {
        super(world, profile);
    }

    @Shadow
    protected abstract boolean method_3134();

    @Shadow
    public abstract boolean method_5869();

    @Shadow
    public abstract boolean method_5715();

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void init(class_310 client, class_638 world, class_634 networkHandler, class_3469 stats, class_299 recipeBook, class_10185 lastPlayerInput, boolean lastSprinting, CallbackInfo ci) {
        EventDispatcher.dispatch(new PlayerCreateEvent());
    }

    @Inject(method={"tickMovementInput"}, at={@At(value="TAIL")})
    private void tickNewAi(CallbackInfo ci) {
        if (this.method_3134()) {
            RotationHelper.getClientHandler().tickCamera();
        }
    }

    @Inject(method={"swingHand"}, at={@At(value="HEAD")})
    private void hookSwingHandHead(class_1268 hand, CallbackInfo ci) {
        this.prevHandSwinging = this.field_6252;
        this.prevHandSwingTicks = this.field_6279;
    }

    @Inject(method={"swingHand"}, at={@At(value="TAIL")})
    private void hookSwingHandTail(class_1268 hand, CallbackInfo ci) {
        EventDispatcher.dispatch(new SwingEvent(hand));
        VisualSwingEvent visualSwingEvent = new VisualSwingEvent(hand);
        EventDispatcher.dispatch(visualSwingEvent);
        if (visualSwingEvent.isCancelled()) {
            this.field_6252 = this.prevHandSwinging;
            this.field_6279 = this.prevHandSwingTicks;
        }
    }

    @Inject(method={"pushOutOfBlocks"}, at={@At(value="HEAD")}, cancellable=true)
    private void onPushOutOfBlocks(double x, double z, CallbackInfo ci) {
        PushOutOfBlocksEvent event = new PushOutOfBlocksEvent();
        EventDispatcher.dispatch(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }

    @Redirect(method={"canStartSprinting"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z"))
    private boolean hookAllowSprint(class_746 playerEntity) {
        NoSlowModule noSlowModule = OpalClient.getInstance().getModuleRepository().getModule(NoSlowModule.class);
        if (noSlowModule.isEnabled() && noSlowModule.isSprintingAllowed()) {
            return false;
        }
        return playerEntity.method_6115();
    }

    @Redirect(method={"applyMovementSpeedFactors"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/math/Vec2f;multiply(F)Lnet/minecraft/util/math/Vec2f;", ordinal=1))
    private class_241 redirectUseSlowdown(class_241 instance, float value) {
        SlowdownEvent slowdownEvent = new SlowdownEvent(value);
        EventDispatcher.dispatch(slowdownEvent);
        return instance.method_35582(slowdownEvent.isCancelled() ? 1.0f : slowdownEvent.getSlowdown());
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookSendMovementPacketsHead(CallbackInfo callbackInfo) {
        if (this.preMovementPacketEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/util/PlayerInput;equals(Ljava/lang/Object;)Z"))
    private boolean redirectInputEquals(class_10185 instance, Object object) {
        if (this.preMovementPacketEvent.isForceInput()) {
            return false;
        }
        return instance.equals(object);
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="FIELD", target="Lnet/minecraft/client/network/ClientPlayerEntity;horizontalCollision:Z", opcode=180))
    private boolean redirectHorizontalCollision(class_746 instance) {
        return this.preMovementPacketEvent.isHorizontalCollision();
    }

    @Redirect(method={"sendSprintingPacket"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isSprinting()Z"))
    private boolean redirectPreMovementPacketSprinting(class_746 instance) {
        return this.preMovementPacketEvent != null && this.preMovementPacketEvent.isSprinting();
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/AbstractClientPlayerEntity;tick()V", shift=At.Shift.AFTER)})
    private void hookPostTick(CallbackInfo ci) {
        this.preMovementPacketEvent = new PreMovementPacketEvent(this.method_23317(), this.method_23318(), this.method_23321(), this.method_36454(), this.method_36455(), this.method_24828(), this.method_5624(), this.field_5976);
        EventDispatcher.dispatch(this.preMovementPacketEvent);
    }

    @Inject(method={"sendMovementPackets"}, at={@At(value="TAIL")})
    private void hookSendMovementPacketsTail(CallbackInfo callbackInfo) {
        if (this.preMovementPacketEvent == null) {
            return;
        }
        EventDispatcher.dispatch(new PostMovementPacketEvent(this.preMovementPacketEvent.getX(), this.preMovementPacketEvent.getY(), this.preMovementPacketEvent.getZ(), this.preMovementPacketEvent.getYaw(), this.preMovementPacketEvent.getPitch(), this.preMovementPacketEvent.isOnGround(), this.preMovementPacketEvent.isSprinting()));
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getYaw()F"))
    private float hookSendMovementPacketsYaw(class_746 instance) {
        return this.preMovementPacketEvent == null ? 0.0f : this.preMovementPacketEvent.getYaw();
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getPitch()F"))
    private float hookSendMovementPacketsPitch(class_746 instance) {
        return this.preMovementPacketEvent == null ? 0.0f : this.preMovementPacketEvent.getPitch();
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isOnGround()Z"))
    private boolean hookSendMovementPacketsGround(class_746 instance) {
        return this.preMovementPacketEvent != null && this.preMovementPacketEvent.isOnGround();
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getX()D"))
    private double hookSendMovementPacketsPosX(class_746 instance) {
        return this.preMovementPacketEvent == null ? 0.0 : this.preMovementPacketEvent.getX();
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getY()D"))
    private double hookSendMovementPacketsPosY(class_746 instance) {
        return this.preMovementPacketEvent == null ? 0.0 : this.preMovementPacketEvent.getY();
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getZ()D"))
    private double hookSendMovementPacketsPosZ(class_746 instance) {
        return this.preMovementPacketEvent == null ? 0.0 : this.preMovementPacketEvent.getZ();
    }

    @Redirect(method={"sendMovementPackets"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;getEntityPos()Lnet/minecraft/util/math/Vec3d;"))
    private class_243 hookSendMovementPacketsGetPos(class_746 instance) {
        if (this.preMovementPacketEvent == null) {
            return instance.method_73189();
        }
        return new class_243(this.preMovementPacketEvent.getX(), this.preMovementPacketEvent.getY(), this.preMovementPacketEvent.getZ());
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/Cooldown;tick()V")})
    private void hookUpdate(CallbackInfo ci) {
        EventDispatcher.dispatch(new PreUpdateEvent());
    }

    @Redirect(method={"canStartSprinting"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/input/Input;hasForwardMovement()Z"))
    private boolean redirectHasForwardMovementStartSprint(class_744 instance) {
        if (SprintModule.isOmniSprint()) {
            return this.isOmniForwardMovement();
        }
        return instance.method_20622();
    }

    @Redirect(method={"shouldStopSprinting"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/input/Input;hasForwardMovement()Z"))
    private boolean redirectHasForwardMovementStopSprint(class_744 instance) {
        if (SprintModule.isOmniSprint()) {
            return this.isOmniForwardMovement();
        }
        return instance.method_20622();
    }

    @ModifyExpressionValue(method={"tickMovement"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/input/Input;hasForwardMovement()Z")})
    private boolean redirectTickMovementHasForwardMovement(boolean original) {
        if (!original && SprintModule.isOmniSprint()) {
            return this.isOmniForwardMovement();
        }
        return original;
    }

    @Unique
    private boolean isOmniForwardMovement() {
        return Math.abs(this.field_3913.method_3128().field_1342) > 1.0E-5f || Math.abs(this.field_3913.method_3128().field_1343) > 1.0E-5f;
    }

    @ModifyReturnValue(method={"canStartSprinting"}, at={@At(value="RETURN")})
    private boolean redirectCanStartSprinting(boolean original) {
        SprintEvent sprintEvent = new SprintEvent(original);
        EventDispatcher.dispatch(sprintEvent);
        return sprintEvent.isCanStartSprinting();
    }

    @Override
    @Unique
    public void opal$swingHandClientside(class_1268 hand) {
        super.method_6104(hand);
    }

    @Override
    @Unique
    public void opal$swingHandServerside(class_1268 hand) {
        this.field_3944.method_52787((class_2596)new class_2879(hand));
        EventDispatcher.dispatch(new SwingEvent(hand));
    }
}

