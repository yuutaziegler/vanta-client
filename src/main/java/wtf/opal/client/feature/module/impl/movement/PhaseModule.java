/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2596
 *  net.minecraft.class_2708
 *  net.minecraft.class_3532
 */
package wtf.opal.client.feature.module.impl.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_3532;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.MoveUtility;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class PhaseModule
extends Module {
    private final BooleanProperty autoDisable = new BooleanProperty("Auto disable", true);
    private boolean collision;
    private boolean phased;
    private boolean shouldForward;

    public PhaseModule() {
        super("Phase", "Allows you to walk through walls.", ModuleCategory.MOVEMENT);
        this.addProperties(this.autoDisable);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        this.collision = Constants.mc.field_1724 != null && Constants.mc.field_1724.field_5976;
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        double amount;
        if (Constants.mc.field_1724 == null) {
            return;
        }
        double yaw = MoveUtility.getDirectionRadians(RotationHelper.getClientHandler().getYawOr(Constants.mc.field_1724.method_36454()));
        if (!this.phased) {
            if (this.collision) {
                amount = 0.005;
                Constants.mc.field_1724.method_5814(Constants.mc.field_1724.method_23317() - (double)class_3532.method_15374((float)((float)yaw)) * 0.005, Constants.mc.field_1724.method_23318(), Constants.mc.field_1724.method_23321() + (double)class_3532.method_15362((float)((float)yaw)) * 0.005);
                this.phased = true;
            }
        } else if (!PlayerUtility.isInsideBlock() && this.shouldForward) {
            if (this.autoDisable.getValue().booleanValue()) {
                this.setEnabled(false);
            } else {
                this.phased = false;
            }
        }
        if (this.phased && LocalDataWatch.get().ticksSinceTeleport == 3) {
            amount = 0.8;
            Constants.mc.field_1724.method_5814(Constants.mc.field_1724.method_23317() - (double)class_3532.method_15374((float)((float)yaw)) * 0.8, Constants.mc.field_1724.method_23318(), Constants.mc.field_1724.method_23321() + (double)class_3532.method_15362((float)((float)yaw)) * 0.8);
        }
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        if (this.shouldForward) {
            MoveUtility.setSpeed(0.0);
        }
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2708) {
            class_2708 posLook = (class_2708)class_25962;
            this.shouldForward = true;
        }
    }

    @Override
    protected void onEnable() {
        this.phased = false;
        this.shouldForward = false;
        super.onEnable();
    }
}

