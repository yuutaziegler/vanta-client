/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1802
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_241
 *  net.minecraft.class_2596
 *  net.minecraft.class_2743
 *  net.minecraft.class_2885
 *  net.minecraft.class_3532
 */
package wtf.opal.client.feature.module.impl.movement.flight.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1802;
import net.minecraft.class_2350;
import net.minecraft.class_241;
import net.minecraft.class_2596;
import net.minecraft.class_2743;
import net.minecraft.class_2885;
import net.minecraft.class_3532;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.model.impl.InstantRotationModel;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.module.impl.movement.flight.FlightModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.notification.NotificationType;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.packet.SendPacketEvent;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.InventoryUtility;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class FireballFlight
extends ModuleMode<FlightModule> {
    private boolean thrown;
    private boolean damaged;
    private boolean swapBack;
    private boolean ticked;
    private int ticksSinceDamaged;
    private double yOffset;

    public FireballFlight(FlightModule module) {
        super(module);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        if (Constants.mc.field_1724 != null && !this.thrown) {
            RotationHelper.getHandler().rotate(new class_241(MoveUtility.getDirectionDegrees() + 180.0f, 45.0f), InstantRotationModel.INSTANCE);
        }
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        if (!this.damaged) {
            MoveUtility.setSpeed(0.0);
        } else if (this.thrown) {
            ++this.ticksSinceDamaged;
            if (this.ticksSinceDamaged >= 33) {
                Constants.mc.field_1724.method_18799(Constants.mc.field_1724.method_18798().method_1031(0.0, (double)0.028f, 0.0));
            } else {
                Constants.mc.field_1724.method_18799(Constants.mc.field_1724.method_18798().method_38499(class_2350.class_2351.field_11052, this.ticksSinceDamaged == 1 ? (double)0.43f : 0.0));
            }
            if (MoveUtility.isMoving() && this.ticksSinceDamaged >= 2) {
                if (this.ticksSinceDamaged == 2) {
                    MoveUtility.setSpeed(MoveUtility.getSpeed() * 2.7);
                } else {
                    MoveUtility.setSpeed(MoveUtility.getSpeed());
                }
            }
            if (this.ticksSinceDamaged > 10 && (Constants.mc.field_1724.method_24828() || Constants.mc.field_1724.method_31549().field_7478 || Constants.mc.field_1724.method_31549().field_7479)) {
                ((FlightModule)this.getModule()).toggle();
            }
        }
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        if (Math.abs(class_3532.method_15381((float)MoveUtility.getDirectionDegrees(), (float)Constants.mc.field_1724.method_36454())) > 170.0f && Constants.mc.field_1724.method_24828()) {
            this.ticked = true;
        }
        if (Constants.mc.field_1724.method_18798().method_10214() == -0.0784000015258789 && !Constants.mc.field_1724.method_24828() && this.ticksSinceDamaged > 1) {
            this.yOffset = this.ticksSinceDamaged % 3 != 0 ? (this.yOffset += 0.015625) : (this.yOffset -= 0.03125);
            event.setY(event.getY() + this.yOffset);
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
                OpalClient.getInstance().getNotificationManager().builder(NotificationType.ERROR).duration(1000).title(((FlightModule)this.module).getName()).description("No fireball in hotbar!").buildAndPublish();
                ((FlightModule)this.module).toggle();
                return;
            }
            slotHelper.setTargetItem(slot).silence(SlotHelper.Silence.DEFAULT);
            MouseHelper.getRightButton().setPressed();
        } else if (this.swapBack) {
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
        this.yOffset = 0.0;
        super.onEnable();
    }

    @Override
    public Enum<?> getEnumValue() {
        return FlightModule.Mode.FIREBALL;
    }
}

