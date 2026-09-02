/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1268
 *  net.minecraft.class_1661
 *  net.minecraft.class_1747
 *  net.minecraft.class_239
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_304
 *  net.minecraft.class_310
 *  net.minecraft.class_3965
 *  net.minecraft.class_437
 *  net.minecraft.class_542
 *  net.minecraft.class_638
 *  net.minecraft.class_746
 *  net.minecraft.class_759
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.Slice
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalBooleanRef;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1661;
import net.minecraft.class_1747;
import net.minecraft.class_239;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_3965;
import net.minecraft.class_437;
import net.minecraft.class_542;
import net.minecraft.class_638;
import net.minecraft.class_746;
import net.minecraft.class_759;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseButton;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.module.impl.combat.BlockModule;
import wtf.opal.client.feature.module.impl.movement.InventoryMoveModule;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;
import wtf.opal.duck.ClientPlayerEntityAccess;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.JoinWorldEvent;
import wtf.opal.event.impl.game.PostGameTickEvent;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.ScheduledExecutablesEvent;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.impl.game.input.PostHandleInputEvent;
import wtf.opal.event.impl.game.player.interaction.AttackDelayEvent;
import wtf.opal.event.impl.game.player.interaction.ItemUseEvent;
import wtf.opal.event.impl.game.player.interaction.block.BlockPlacedEvent;
import wtf.opal.event.impl.game.server.ServerDisconnectEvent;
import wtf.opal.event.impl.render.ResolutionChangeEvent;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_310.class})
public abstract class MinecraftClientMixin {
    @Shadow
    protected int field_1771;
    @Shadow
    @Nullable
    public class_239 field_1765;
    @Shadow
    @Nullable
    public class_746 field_1724;

    @Shadow
    protected abstract boolean method_1536();

    private MinecraftClientMixin() {
    }

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void postInitialization(class_542 args, CallbackInfo ci) {
        OpalClient.getInstance().runPostInitializations();
    }

    @Inject(method={"handleInputEvents"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;isUsingItem()Z", ordinal=0)})
    private void handleInputEventsMouse(CallbackInfo info) {
        EventDispatcher.dispatch(new MouseHandleInputEvent());
    }

    @Inject(method={"handleInputEvents"}, at={@At(value="TAIL")})
    private void handleInputEventsTail(CallbackInfo ci) {
        MouseHelper.getInstance().tick();
        EventDispatcher.dispatch(new PostHandleInputEvent());
    }

    @Redirect(method={"doAttack"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"))
    private void redirectAttackSwings(class_746 instance, class_1268 hand) {
        MouseButton leftButton = MouseHelper.getLeftButton();
        if (leftButton.isShowSwings()) {
            instance.method_6104(hand);
        } else {
            ((ClientPlayerEntityAccess)instance).opal$swingHandServerside(hand);
        }
    }

    @Redirect(method={"doItemUse"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerEntity;swingHand(Lnet/minecraft/util/Hand;)V"))
    private void redirectUseSwings(class_746 instance, class_1268 hand) {
        MouseButton rightButton = MouseHelper.getRightButton();
        if (rightButton.isShowSwings()) {
            instance.method_6104(hand);
        } else {
            ((ClientPlayerEntityAccess)instance).opal$swingHandServerside(hand);
        }
    }

    @Inject(method={"setScreen"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;unpressAll()V", shift=At.Shift.AFTER)})
    private void hookSetScreen(class_437 screen, CallbackInfo ci) {
        InventoryMoveModule inventoryMove;
        if (OpalClient.getInstance().isPostInitialization() && (inventoryMove = OpalClient.getInstance().getModuleRepository().getModule(InventoryMoveModule.class)).isEnabled() && !inventoryMove.isBlocked()) {
            PlayerUtility.updateMovementKeyStates();
        }
    }

    @Redirect(method={"handleInputEvents"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerInventory;setSelectedSlot(I)V"))
    private void redirectSelectedSlot(class_1661 instance, int value) {
        SlotHelper slotHelper = SlotHelper.getInstance();
        if (slotHelper.isActive()) {
            if (slotHelper.getSilence() != SlotHelper.Silence.NONE) {
                slotHelper.setVisualSlot(value);
            }
        } else {
            instance.method_61496(value);
        }
    }

    @Inject(method={"handleInputEvents"}, at={@At(value="FIELD", target="Lnet/minecraft/client/option/GameOptions;socialInteractionsKey:Lnet/minecraft/client/option/KeyBinding;", shift=At.Shift.BEFORE)})
    private void postSlotHandleInput(CallbackInfo ci) {
        SlotHelper.getInstance().sync(false, false);
    }

    @Redirect(method={"handleInputEvents"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;isPressed()Z"))
    private boolean redirectIsPressed(class_304 instance) {
        MouseButton mouseButton = MouseHelper.getButtonFromBinding(instance);
        if (mouseButton != null) {
            return mouseButton.isPressed();
        }
        return instance.method_1434();
    }

    @Redirect(method={"handleInputEvents"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;wasPressed()Z"), slice=@Slice(from=@At(value="FIELD", target="Lnet/minecraft/client/option/GameOptions;useKey:Lnet/minecraft/client/option/KeyBinding;", ordinal=1), to=@At(value="TAIL")))
    private boolean redirectWasPressed(class_304 instance) {
        MouseButton mouseButton = MouseHelper.getButtonFromBinding(instance);
        if (mouseButton != null) {
            return mouseButton.wasPressed();
        }
        return instance.method_1436();
    }

    @Redirect(method={"handleInputEvents"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/option/KeyBinding;wasPressed()Z", ordinal=11))
    private boolean redirectUsingAttack(class_304 instance, @Local LocalBooleanRef bl3) {
        if (this.isSwingWhileUsing() && MouseHelper.getLeftButton().wasPressed()) {
            boolean currentValue = bl3.get();
            boolean newValue = currentValue | this.method_1536();
            bl3.set(newValue);
            return true;
        }
        return false;
    }

    @Inject(method={"doItemUse"}, at={@At(value="HEAD")})
    private void hookItemUse(CallbackInfo ci) {
        EventDispatcher.dispatch(new ItemUseEvent());
    }

    @Inject(method={"handleInputEvents"}, at={@At(value="FIELD", target="Lnet/minecraft/client/option/GameOptions;useKey:Lnet/minecraft/client/option/KeyBinding;", ordinal=1)})
    private void onItemUseMouseHandle(CallbackInfo ci) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        MouseButton leftButton = MouseHelper.getLeftButton();
        if (animationsModule.isEnabled() && animationsModule.isSwingWhileUsing() && leftButton.isPressed() && leftButton.isShowSwings() && (this.field_1765 != null && this.field_1765.method_17783() == class_239.class_240.field_1332 || leftButton.wasPressed())) {
            ((ClientPlayerEntityAccess)this.field_1724).opal$swingHandClientside(class_1268.field_5808);
        }
        while (leftButton.wasPressed()) {
        }
    }

    @Unique
    private boolean isSwingWhileUsing() {
        BlockModule blockModule = OpalClient.getInstance().getModuleRepository().getModule(BlockModule.class);
        return blockModule.isEnabled() && blockModule.isSwingAllowed();
    }

    @Redirect(method={"doAttack"}, at=@At(value="FIELD", target="Lnet/minecraft/client/MinecraftClient;attackCooldown:I", opcode=181))
    private void onAttackCooldown(class_310 instance, int value) {
        AttackDelayEvent event = new AttackDelayEvent(value);
        EventDispatcher.dispatch(event);
        this.field_1771 = event.getDelay();
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;runTasks()V", shift=At.Shift.BEFORE)})
    private void onGameLoop(boolean tick, CallbackInfo ci, @Local(ordinal=0) int ticks) {
        EventDispatcher.dispatch(new ScheduledExecutablesEvent(ticks > 0));
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void tickHead(CallbackInfo info) {
        EventDispatcher.dispatch(new PreGameTickEvent());
    }

    @Inject(method={"joinWorld"}, at={@At(value="HEAD")})
    private void hookJoinWorld(class_638 world, CallbackInfo ci) {
        EventDispatcher.dispatch(new JoinWorldEvent());
    }

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void tickTail(CallbackInfo info) {
        EventDispatcher.dispatch(new PostGameTickEvent());
    }

    @Inject(method={"onDisconnected"}, at={@At(value="HEAD")})
    private void disconnected(CallbackInfo ci) {
        EventDispatcher.dispatch(new ServerDisconnectEvent());
    }

    @Inject(method={"isTelemetryEnabledByApi"}, at={@At(value="HEAD")}, cancellable=true)
    private void disableTelemetry(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue((Object)false);
    }

    @Inject(method={"onResolutionChanged"}, at={@At(value="HEAD")})
    private void resolutionChange(CallbackInfo ci) {
        EventDispatcher.dispatch(new ResolutionChangeEvent());
    }

    @Inject(method={"doItemUse"}, at={@At(value="INVOKE", target="Lnet/minecraft/util/ActionResult$Success;swingSource()Lnet/minecraft/util/ActionResult$SwingSource;", ordinal=1)})
    private void hookBlockPlaceEvent(CallbackInfo ci, @Local class_3965 blockHitResult) {
        EventDispatcher.dispatch(new BlockPlacedEvent(blockHitResult));
    }

    @Redirect(method={"doItemUse"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/item/HeldItemRenderer;resetEquipProgress(Lnet/minecraft/util/Hand;)V", ordinal=0))
    private void redirectResetEquipProgress(class_759 instance, class_1268 hand) {
        SlotHelper slotHelper = SlotHelper.getInstance();
        if (hand == class_1268.field_5808 && slotHelper.isActive() && !(slotHelper.getMainHandStack(this.field_1724).method_7909() instanceof class_1747)) {
            return;
        }
        instance.method_3215(hand);
    }
}

