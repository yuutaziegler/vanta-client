/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1802
 *  net.minecraft.class_241
 *  net.minecraft.class_2596
 *  net.minecraft.class_2743
 *  net.minecraft.class_2885
 */
package wtf.opal.client.feature.module.impl.movement.longjump.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1802;
import net.minecraft.class_241;
import net.minecraft.class_2596;
import net.minecraft.class_2743;
import net.minecraft.class_2885;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder.BlockHolder;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.OutboundNetworkBlockage;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.model.impl.InstantRotationModel;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.helper.impl.player.timer.TimerHelper;
import wtf.opal.client.feature.module.impl.movement.longjump.LongJumpModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.notification.NotificationType;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.impl.game.input.MoveInputEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.packet.SendPacketEvent;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.InventoryUtility;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class AntiGamingChairFireballLongJump
extends ModuleMode<LongJumpModule> {
    private boolean thrown;
    private boolean damaged;
    private boolean swapBack;
    private boolean ticked;
    private boolean blinking;
    private int ticksSinceDamaged;
    private final BlockHolder oBlockHolder = new BlockHolder(OutboundNetworkBlockage.get());

    public AntiGamingChairFireballLongJump(LongJumpModule module) {
        super(module);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        if (Constants.mc.field_1724 != null && !this.thrown) {
            RotationHelper.getHandler().rotate(new class_241(Constants.mc.field_1724.method_36454(), 90.0f), InstantRotationModel.INSTANCE);
        }
    }

    @Subscribe
    public void onMoveInput(MoveInputEvent event) {
        if (!this.damaged) {
            event.setForward(0.0f);
            event.setSideways(0.0f);
        }
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        if (this.thrown && this.damaged) {
            if (this.ticksSinceDamaged == 0 && MoveUtility.isMoving()) {
                MoveUtility.setSpeed(9.5);
                TimerHelper.getInstance().timer = 0.3f;
            }
            if (this.ticksSinceDamaged > 10 && (Constants.mc.field_1724.method_24828() || Constants.mc.field_1724.method_31549().field_7478 || Constants.mc.field_1724.method_31549().field_7479)) {
                ((LongJumpModule)this.getModule()).toggle();
            }
        }
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        if (event.getPitch() == 90.0f && Constants.mc.field_1724.method_24828()) {
            this.ticked = true;
        }
        if (this.thrown && this.damaged) {
            ++this.ticksSinceDamaged;
        }
    }

    @Subscribe
    public void onHandleInput(MouseHandleInputEvent event) {
        if (!this.ticked) {
            return;
        }
        SlotHelper slotHelper = SlotHelper.getInstance();
        if (!this.thrown) {
            int slot = InventoryUtility.findItemInHotbar(class_1802.field_8814);
            if (slot == -1) {
                OpalClient.getInstance().getNotificationManager().builder(NotificationType.ERROR).duration(1000).title(((LongJumpModule)this.module).getName()).description("No fireball in hotbar!").buildAndPublish();
                ((LongJumpModule)this.module).toggle();
                return;
            }
            slotHelper.setTargetItem(slot).silence(SlotHelper.Silence.DEFAULT);
            MouseHelper.getRightButton().setPressed();
        }
        if (this.swapBack) {
            slotHelper.stop();
            slotHelper.sync(true, true);
            this.swapBack = false;
        }
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        class_2743 velocity;
        class_2596<?> class_25962;
        if (Constants.mc.field_1724 != null && (class_25962 = event.getPacket()) instanceof class_2743 && (velocity = (class_2743)class_25962).method_11818() == Constants.mc.field_1724.method_5628() && !this.damaged) {
            this.damaged = true;
            this.ticksSinceDamaged = 0;
        }
    }

    @Subscribe
    public void onSendPacket(SendPacketEvent event) {
        if (!this.thrown && event.getPacket() instanceof class_2885) {
            this.thrown = true;
            this.swapBack = true;
        }
    }

    @Override
    public void onEnable() {
        this.ticked = false;
        this.swapBack = false;
        this.thrown = false;
        this.damaged = false;
        this.ticksSinceDamaged = 0;
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (Constants.mc.field_1724 != null) {
            MoveUtility.setSpeed(0.0);
            TimerHelper.getInstance().timer = 1.0f;
        }
        super.onDisable();
    }

    @Override
    public Enum<?> getEnumValue() {
        return LongJumpModule.Mode.ANTI_GAMING_CHAIR_FIREBALL;
    }
}

