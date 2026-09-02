/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_3532
 *  net.minecraft.class_5611
 *  org.joml.Vector2d
 */
package wtf.opal.utility.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_3532;
import net.minecraft.class_5611;
import org.joml.Vector2d;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.timer.TimerHelper;
import wtf.opal.client.feature.module.impl.movement.TargetStrafeModule;

@Environment(value=EnvType.CLIENT)
public final class MoveUtility {
    private MoveUtility() {
    }

    public static double getBlocksPerSecond() {
        double bps = Math.hypot(Constants.mc.field_1724.method_23317() - Constants.mc.field_1724.field_6014, Constants.mc.field_1724.method_23321() - Constants.mc.field_1724.field_5969) * 20.0 * (double)TimerHelper.getInstance().timer;
        return (double)Math.round(bps * 100.0) / 100.0;
    }

    public static double[] yawPos(float yaw, double value) {
        return new double[]{(double)(-class_3532.method_15374((float)yaw)) * value, (double)class_3532.method_15362((float)yaw) * value};
    }

    public static void setSpeed(class_1297 entity, double speed, double yaw) {
        if (speed == 0.0) {
            entity.method_18800(0.0, entity.method_18798().method_10214(), 0.0);
            return;
        }
        entity.method_18800((double)(-class_3532.method_15374((float)((float)yaw))) * speed, entity.method_18798().method_10214(), (double)class_3532.method_15362((float)((float)yaw)) * speed);
    }

    public static void setSpeed(double speed) {
        double yaw = MoveUtility.getDirectionRadians(RotationHelper.getClientHandler().getYawOr(Constants.mc.field_1724.method_36454()));
        MoveUtility.setSpeed((class_1297)Constants.mc.field_1724, speed, yaw);
    }

    public static void setSpeed(double speed, double strafePercentage) {
        strafePercentage /= 100.0;
        strafePercentage = Math.min(1.0, Math.max(0.0, strafePercentage));
        double motionX = Constants.mc.field_1724.method_18798().method_10216();
        double motionZ = Constants.mc.field_1724.method_18798().method_10215();
        MoveUtility.setSpeed(speed);
        Constants.mc.field_1724.method_18800(motionX + (Constants.mc.field_1724.method_18798().method_10216() - motionX) * strafePercentage, Constants.mc.field_1724.method_18798().method_10214(), motionZ + (Constants.mc.field_1724.method_18798().method_10215() - motionZ) * strafePercentage);
    }

    public static void setSpeed(double speed, float yaw) {
        Constants.mc.field_1724.method_18800((double)(-class_3532.method_15374((float)yaw)) * speed, Constants.mc.field_1724.method_18798().method_10214(), (double)class_3532.method_15362((float)yaw) * speed);
    }

    public static double getSwiftnessSpeed(double speed, double swiftnessMultiplier) {
        if (!Constants.mc.field_1724.method_6059(class_1294.field_5904)) {
            return speed;
        }
        return speed * (1.0 + swiftnessMultiplier * (double)(Constants.mc.field_1724.method_6112(class_1294.field_5904).method_5578() + 1));
    }

    public static double getSwiftnessSpeed(double speed) {
        return MoveUtility.getSwiftnessSpeed(speed, 0.2);
    }

    public static double getSpeed() {
        return Math.hypot(Constants.mc.field_1724.method_18798().method_10216(), Constants.mc.field_1724.method_18798().method_10215());
    }

    public static float getMoveYaw(Vector2d from, Vector2d to) {
        Vector2d diff = new Vector2d(to.x - from.x, to.y - from.y);
        return (float)Math.toDegrees(Math.atan2(-diff.x, diff.y));
    }

    public static float getMoveYaw() {
        class_5611 from = new class_5611((float)Constants.mc.field_1724.field_6014, (float)Constants.mc.field_1724.field_5969);
        class_5611 to = new class_5611((float)Constants.mc.field_1724.method_23317(), (float)Constants.mc.field_1724.method_23321());
        class_5611 diff = new class_5611(to.method_32118() - from.method_32118(), to.method_32119() - from.method_32119());
        return (float)Math.toDegrees((Math.atan2(-diff.method_32118(), diff.method_32119()) + 6.2831854820251465) % 6.2831854820251465);
    }

    public static float getDirectionDegrees() {
        return MoveUtility.getDirectionDegrees(RotationHelper.getClientHandler().getYawOr(Constants.mc.field_1724.method_36454()));
    }

    public static double getDirectionRadians() {
        return MoveUtility.getDirectionRadians(RotationHelper.getClientHandler().getYawOr(Constants.mc.field_1724.method_36454()));
    }

    public static float getDirectionDegrees(float yaw) {
        TargetStrafeModule targetStrafeModule = OpalClient.getInstance().getModuleRepository().getModule(TargetStrafeModule.class);
        if (targetStrafeModule.isEnabled() && targetStrafeModule.isActive()) {
            yaw = targetStrafeModule.getYaw();
        }
        if (Constants.mc.field_1690.field_1881.method_1434()) {
            yaw += 180.0f;
        }
        float forward = 1.0f;
        if (Constants.mc.field_1690.field_1881.method_1434()) {
            forward = -0.5f;
        } else if (Constants.mc.field_1690.field_1894.method_1434()) {
            forward = 0.5f;
        }
        if (Constants.mc.field_1690.field_1913.method_1434()) {
            yaw -= 90.0f * forward;
        } else if (Constants.mc.field_1690.field_1849.method_1434()) {
            yaw += 90.0f * forward;
        }
        return yaw;
    }

    public static double getDirectionRadians(float yaw) {
        return Math.toRadians(MoveUtility.getDirectionDegrees(yaw));
    }

    public static double getDirection(float rotationYaw, double moveForward, double moveStrafing) {
        if (moveForward < 0.0) {
            rotationYaw += 180.0f;
        }
        float forward = 1.0f;
        if (moveForward < 0.0) {
            forward = -0.5f;
        } else if (moveForward > 0.0) {
            forward = 0.5f;
        }
        if (moveStrafing > 0.0) {
            rotationYaw -= 90.0f * forward;
        }
        if (moveStrafing < 0.0) {
            rotationYaw += 90.0f * forward;
        }
        return Math.toRadians(rotationYaw);
    }

    public static boolean isMoving() {
        if (Constants.mc.field_1724 == null) {
            return false;
        }
        return Constants.mc.field_1724.field_6250 != 0.0f || Constants.mc.field_1724.field_6212 != 0.0f;
    }
}

