/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.injector.ModifyReturnValue
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_1299
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1661
 *  net.minecraft.class_1799
 *  net.minecraft.class_1937
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_5134
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1299;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_1937;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_5134;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.module.impl.combat.ReachModule;
import wtf.opal.client.feature.module.impl.world.FastBreakModule;
import wtf.opal.client.feature.module.impl.world.breaker.BreakerModule;
import wtf.opal.duck.PlayerEntityAccess;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.player.movement.ClipAtLedgeEvent;
import wtf.opal.event.impl.game.player.movement.KeepSprintEvent;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_1657.class})
public abstract class PlayerEntityMixin
extends class_1309
implements PlayerEntityAccess {
    @Unique
    private KeepSprintEvent keepSprintEvent;
    @Unique
    private int visualLastAttackedTicks;
    @Unique
    private class_1799 visualSelectedItem;

    protected PlayerEntityMixin(class_1299<? extends class_1309> entityType, class_1937 world) {
        super(entityType, world);
    }

    @Inject(method={"attack"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V")})
    private void hookKeepSprint(class_1297 target, CallbackInfo ci) {
        this.keepSprintEvent = new KeepSprintEvent();
        EventDispatcher.dispatch(this.keepSprintEvent);
    }

    @Redirect(method={"canHarvest"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerInventory;getSelectedStack()Lnet/minecraft/item/ItemStack;"))
    private class_1799 redirectHandStack(class_1661 instance) {
        BreakerModule breakerModule = OpalClient.getInstance().getModuleRepository().getModule(BreakerModule.class);
        if (breakerModule.isEnabled() && breakerModule.isBreaking() && breakerModule.getSlot() != -1) {
            return instance.method_5438(breakerModule.getSlot());
        }
        return instance.method_7391();
    }

    @Inject(method={"clipAtLedge"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookClipAtLedge(CallbackInfoReturnable<Boolean> ci) {
        if (this == Constants.mc.field_1724) {
            ClipAtLedgeEvent clipAtLedgeEvent = new ClipAtLedgeEvent();
            EventDispatcher.dispatch(clipAtLedgeEvent);
            if (clipAtLedgeEvent.isUpdated()) {
                ci.setReturnValue((Object)clipAtLedgeEvent.isClip());
            }
        }
    }

    @Redirect(method={"getBlockBreakingSpeed"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerInventory;getSelectedStack()Lnet/minecraft/item/ItemStack;"))
    private class_1799 redirectHandStack2(class_1661 instance) {
        BreakerModule breakerModule = OpalClient.getInstance().getModuleRepository().getModule(BreakerModule.class);
        if (breakerModule.isEnabled() && breakerModule.isBreaking() && breakerModule.getSlot() != -1) {
            return instance.method_5438(breakerModule.getSlot());
        }
        return instance.method_7391();
    }

    @ModifyArg(method={"attack"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;setSprinting(Z)V"))
    private boolean hookKeepSprintState(boolean sprinting) {
        return (this.keepSprintEvent == null || this.keepSprintEvent.isCancelled()) && Constants.mc.field_1724.method_5624();
    }

    @ModifyArg(method={"attack"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"))
    private class_243 hookKeepSprintVelocity(class_243 velocity) {
        return (this.keepSprintEvent == null || this.keepSprintEvent.isCancelled()) && Constants.mc.field_1724.method_5624() ? Constants.mc.field_1724.method_18798() : velocity;
    }

    @Inject(method={"tick"}, at={@At(value="TAIL")})
    private void incrementTicks(CallbackInfo ci) {
        if (this == Constants.mc.field_1724) {
            LocalDataWatch ldw = LocalDataWatch.get();
            if (this.method_24828()) {
                ldw.airTicks = 0;
                ++ldw.groundTicks;
            } else {
                ++ldw.airTicks;
                ldw.groundTicks = 0;
            }
        }
    }

    @Inject(method={"resetLastAttackedTicks"}, at={@At(value="TAIL")})
    private void resetVisualLastAttackedTicks(CallbackInfo ci) {
        this.visualLastAttackedTicks = 0;
    }

    @Inject(method={"tick"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;getMainHandStack()Lnet/minecraft/item/ItemStack;")})
    private void checkVisualItemStackEquality(CallbackInfo ci) {
        if (this == Constants.mc.field_1724) {
            class_1799 itemStack = SlotHelper.getInstance().getMainHandStack(Constants.mc.field_1724);
            if (this.visualSelectedItem == null) {
                this.visualSelectedItem = class_1799.field_8037;
            }
            if (!class_1799.method_7973((class_1799)this.visualSelectedItem, (class_1799)itemStack)) {
                if (!class_1799.method_7984((class_1799)this.visualSelectedItem, (class_1799)itemStack)) {
                    this.visualLastAttackedTicks = 0;
                }
                this.visualSelectedItem = itemStack.method_7972();
            }
        }
    }

    @Redirect(method={"tick"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;resetLastAttackedTicks()V"))
    private void onSwapLastAttackedTicksReset(class_1657 instance) {
        this.field_6273 = 0;
    }

    @Inject(method={"tick"}, at={@At(value="FIELD", target="Lnet/minecraft/entity/player/PlayerEntity;lastAttackedTicks:I", ordinal=0)})
    private void incrementLastAttackedTicks(CallbackInfo ci) {
        ++this.visualLastAttackedTicks;
    }

    @Unique
    private float getVisualAttackCooldownProgressPerTick() {
        SlotHelper slotHelper = SlotHelper.getInstance();
        double attackSpeed = slotHelper.isActive() && slotHelper.getSilence() != SlotHelper.Silence.NONE ? PlayerUtility.getStackAttackSpeed(slotHelper.getMainHandStack(Constants.mc.field_1724)) : this.method_45325(class_5134.field_23723);
        return (float)(1.0 / attackSpeed * 20.0);
    }

    @Override
    @Unique
    public float opal$getVisualAttackCooldownProgress(float baseTime) {
        return class_3532.method_15363((float)(((float)this.visualLastAttackedTicks + baseTime) / this.getVisualAttackCooldownProgressPerTick()), (float)0.0f, (float)1.0f);
    }

    @Inject(method={"getEntityInteractionRange"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookEntityReach(CallbackInfoReturnable<Double> cir) {
        ReachModule reachModule = OpalClient.getInstance().getModuleRepository().getModule(ReachModule.class);
        if (reachModule.isEnabled()) {
            cir.setReturnValue((Object)reachModule.getEntityInteractionRange());
        }
    }

    @Inject(method={"getBlockInteractionRange"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookBlockReach(CallbackInfoReturnable<Double> cir) {
        ReachModule reachModule = OpalClient.getInstance().getModuleRepository().getModule(ReachModule.class);
        if (reachModule.isEnabled()) {
            cir.setReturnValue((Object)reachModule.getBlockInteractionRange());
        }
    }

    @ModifyReturnValue(method={"getBlockBreakingSpeed"}, at={@At(value="RETURN")})
    private float addBreakingSpeedMultiplier(float original) {
        FastBreakModule fastBreakModule = OpalClient.getInstance().getModuleRepository().getModule(FastBreakModule.class);
        if (fastBreakModule.isEnabled() && fastBreakModule.isSpeedEnabled()) {
            float cappedSpeedIncrease = Math.min(fastBreakModule.getSpeed(), 99.0f);
            float multiplier = 1.0f / (1.0f - cappedSpeedIncrease / 100.0f);
            return original * multiplier;
        }
        return original;
    }

    @ModifyExpressionValue(method={"getBlockBreakingSpeed"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;isOnGround()Z")})
    private boolean hookAirBreakSlowdown(boolean original) {
        FastBreakModule fastBreakModule = OpalClient.getInstance().getModuleRepository().getModule(FastBreakModule.class);
        if (this == Constants.mc.field_1724 && fastBreakModule.isEnabled() && !fastBreakModule.getBreakSlowdowns().getProperty("In air").getValue().booleanValue()) {
            return true;
        }
        return original;
    }

    @ModifyExpressionValue(method={"getBlockBreakingSpeed"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;isSubmergedIn(Lnet/minecraft/registry/tag/TagKey;)Z")})
    private boolean hookWaterBreakSlowdown(boolean original) {
        FastBreakModule fastBreakModule = OpalClient.getInstance().getModuleRepository().getModule(FastBreakModule.class);
        if (this == Constants.mc.field_1724 && fastBreakModule.isEnabled() && !fastBreakModule.getBreakSlowdowns().getProperty("In water").getValue().booleanValue()) {
            return false;
        }
        return original;
    }

    @ModifyExpressionValue(method={"getBlockBreakingSpeed"}, at={@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;hasStatusEffect(Lnet/minecraft/registry/entry/RegistryEntry;)Z")})
    private boolean hookFatigueBreakSlowdown(boolean original) {
        FastBreakModule fastBreakModule = OpalClient.getInstance().getModuleRepository().getModule(FastBreakModule.class);
        if (this == Constants.mc.field_1724 && fastBreakModule.isEnabled() && !fastBreakModule.getBreakSlowdowns().getProperty("Mining fatigue").getValue().booleanValue()) {
            return false;
        }
        return original;
    }
}

