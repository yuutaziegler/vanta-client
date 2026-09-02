/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_3532
 */
package wtf.opal.client.feature.module.impl.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3532;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.event.impl.game.input.MoveInputEvent;
import wtf.opal.event.impl.game.player.movement.JumpEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.impl.game.player.movement.SprintEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class MovementFixModule
extends Module {
    private final ModeProperty<Mode> modeProperty = new ModeProperty<Mode>("Mode", Mode.NORMAL);
    private final BooleanProperty packetOnly = (BooleanProperty)new BooleanProperty("Packet only", false).hideIf(() -> this.modeProperty.getValue() != Mode.SPRINT_ONLY);

    public MovementFixModule() {
        super("Movement Fix", "Locks your movement to your rotations.", ModuleCategory.MOVEMENT);
        this.addProperties(this.modeProperty, this.packetOnly);
    }

    @Subscribe
    public void onMoveInput(MoveInputEvent event) {
        if (this.modeProperty.getValue() != Mode.NORMAL) {
            return;
        }
        float forward = event.getForward();
        float strafe = event.getSideways();
        if (forward == 0.0f && strafe == 0.0f) {
            return;
        }
        float angle = (float)Math.toDegrees(MoveUtility.getDirection(RotationHelper.getClientHandler().getYawOr(Constants.mc.field_1724.method_36454()), forward, strafe));
        float closestForward = 0.0f;
        float closestSideways = 0.0f;
        float closestDifference = Float.MAX_VALUE;
        for (float predictedForward = -1.0f; predictedForward <= 1.0f; predictedForward += 1.0f) {
            for (float predictedStrafe = -1.0f; predictedStrafe <= 1.0f; predictedStrafe += 1.0f) {
                float predictedAngle;
                double difference;
                if (predictedStrafe == 0.0f && predictedForward == 0.0f || !((difference = (double)class_3532.method_15356((float)angle, (float)(predictedAngle = (float)Math.toDegrees(MoveUtility.getDirection(Constants.mc.field_1724.method_36454(), predictedForward, predictedStrafe))))) < (double)closestDifference)) continue;
                closestDifference = (float)difference;
                closestForward = predictedForward;
                closestSideways = predictedStrafe;
            }
        }
        event.setForward(closestForward);
        event.setSideways(closestSideways);
    }

    @Subscribe
    public void onSprint(SprintEvent event) {
        if (!this.packetOnly.getValue().booleanValue() && this.isResetSprint()) {
            Constants.mc.field_1724.method_5728(false);
            event.setCanStartSprinting(false);
        }
    }

    @Subscribe
    public void onJump(JumpEvent event) {
        if (event.isSprinting() && !this.packetOnly.getValue().booleanValue() && this.isResetSprint()) {
            event.setSprinting(false);
        }
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        if (this.packetOnly.getValue().booleanValue() && this.isResetSprint()) {
            event.setSprinting(false);
        }
    }

    private boolean isResetSprint() {
        if (this.modeProperty.getValue() != Mode.SPRINT_ONLY) {
            return false;
        }
        float rotationYaw = Constants.mc.field_1724.method_36454();
        float movementYaw = MoveUtility.getDirectionDegrees(RotationHelper.getClientHandler().getYawOr(rotationYaw));
        float diff = class_3532.method_15356((float)movementYaw, (float)rotationYaw);
        return diff > 45.005f;
    }

    public boolean isFixMovement() {
        return this.isEnabled() && this.modeProperty.getValue() == Mode.NORMAL;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
        NORMAL("Normal"),
        SPRINT_ONLY("Sprint only");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

