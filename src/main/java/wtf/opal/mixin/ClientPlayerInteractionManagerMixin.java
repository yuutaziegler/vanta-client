/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1268
 *  net.minecraft.class_1269
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_2338
 *  net.minecraft.class_3965
 *  net.minecraft.class_636
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.At$Shift
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1269;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_2338;
import net.minecraft.class_3965;
import net.minecraft.class_636;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.module.impl.world.FastBreakModule;
import wtf.opal.client.feature.module.impl.world.breaker.BreakerModule;
import wtf.opal.duck.ClientPlayerInteractionManagerAccess;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.player.interaction.AttackEvent;
import wtf.opal.event.impl.game.player.interaction.CancelBlockBreakingEvent;
import wtf.opal.event.impl.game.player.interaction.block.PostBlockInteractEvent;
import wtf.opal.event.impl.game.player.interaction.block.PreBlockInteractEvent;
import wtf.opal.mixin.ClientPlayerInteractionManagerAccessor;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_636.class})
public class ClientPlayerInteractionManagerMixin
implements ClientPlayerInteractionManagerAccess {
    @Shadow
    private class_2338 field_3714;
    @Shadow
    private float field_3715;

    private ClientPlayerInteractionManagerMixin() {
    }

    @Inject(method={"attackEntity"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerInteractionManager;syncSelectedSlot()V", shift=At.Shift.AFTER)})
    private void hookAttackEvent(class_1657 player, class_1297 target, CallbackInfo callbackInfo) {
        EventDispatcher.dispatch(new AttackEvent(target));
    }

    @Inject(method={"interactBlock"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerInteractionManager;sendSequencedPacket(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/network/SequencedPacketCreator;)V")})
    private void handleBlockPlacementHead(class_746 player, class_1268 hand, class_3965 hitResult, CallbackInfoReturnable<class_1269> cir) {
        EventDispatcher.dispatch(new PreBlockInteractEvent());
    }

    @Inject(method={"interactBlock"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerInteractionManager;sendSequencedPacket(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/network/SequencedPacketCreator;)V", shift=At.Shift.AFTER)})
    private void handleBlockPlacementTail(class_746 player, class_1268 hand, class_3965 hitResult, CallbackInfoReturnable<class_1269> cir) {
        EventDispatcher.dispatch(new PostBlockInteractEvent());
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    private void tick(CallbackInfo ci) {
        SlotHelper.getInstance().tick();
    }

    @Override
    public class_2338 opal$getCurrentBreakingPos() {
        return this.field_3714;
    }

    @Override
    public float opal$currentBreakingProgress() {
        return this.field_3715;
    }

    @Redirect(method={"updateBlockBreakingProgress"}, at=@At(value="FIELD", target="Lnet/minecraft/client/network/ClientPlayerInteractionManager;blockBreakingCooldown:I", opcode=181, ordinal=2))
    private void redirectBreakingCooldown(class_636 instance, int value) {
        FastBreakModule fastBreakModule = OpalClient.getInstance().getModuleRepository().getModule(FastBreakModule.class);
        if (fastBreakModule.isEnabled() && fastBreakModule.isBreakCooldownEnabled()) {
            value = fastBreakModule.getBreakCooldown();
        }
        ((ClientPlayerInteractionManagerAccessor)instance).setBlockBreakingCooldown(value);
    }

    @Inject(method={"isCurrentlyBreaking"}, at={@At(value="HEAD")}, cancellable=true)
    private void redirectCurrentlyBreaking(class_2338 pos, CallbackInfoReturnable<Boolean> cir) {
        BreakerModule breakerModule = OpalClient.getInstance().getModuleRepository().getModule(BreakerModule.class);
        if (breakerModule.isEnabled() && breakerModule.isBreaking() && breakerModule.getSlot() != -1) {
            cir.setReturnValue((Object)pos.equals((Object)this.field_3714));
        }
    }

    @Inject(method={"cancelBlockBreaking"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookCancelBlockBreaking(CallbackInfo ci) {
        CancelBlockBreakingEvent event = new CancelBlockBreakingEvent();
        EventDispatcher.dispatch(event);
        if (event.isCancelled()) {
            ci.cancel();
        }
    }
}

