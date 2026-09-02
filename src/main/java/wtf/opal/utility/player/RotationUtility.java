/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicate
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_238
 *  net.minecraft.class_239
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_3965
 *  net.minecraft.class_3966
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.utility.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3965;
import net.minecraft.class_3966;
import org.jetbrains.annotations.Nullable;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.utility.misc.math.RandomUtility;
import wtf.opal.utility.player.MoveUtility;
import wtf.opal.utility.player.RaycastUtility;
import wtf.opal.utility.player.RaytracedRotation;

@Environment(value=EnvType.CLIENT)
public final class RotationUtility {
    private RotationUtility() {
    }

    public static float getRotationDifference(class_241 a, class_241 b) {
        return class_3532.method_15356((float)a.field_1343, (float)b.field_1343) + Math.abs(a.field_1342 - b.field_1342);
    }

    public static double getCursorDelta(double rotationDelta, double sensitivityMultiplier) {
        return (float)(rotationDelta / sensitivityMultiplier) / 0.15f;
    }

    public static class_241 patchConstantRotation(class_241 rotation, class_241 prevRotation) {
        double sensitivity = (Double)Constants.mc.field_1690.method_42495().method_41753() * (double)0.6f + (double)0.2f;
        double multiplier = sensitivity * sensitivity * sensitivity * 8.0;
        double divisor = multiplier * (double)0.15f;
        float yawDelta = rotation.field_1343 - prevRotation.field_1343;
        float pitchDelta = rotation.field_1342 - prevRotation.field_1342;
        float yaw = prevRotation.field_1343 + (float)((double)Math.round((double)yawDelta / divisor) * divisor);
        float pitch = prevRotation.field_1342 + (float)((double)Math.round((double)pitchDelta / divisor) * divisor);
        return new class_241(yaw, pitch);
    }

    public static float getSensitivityModifiedRotation(double original) {
        double sensitivity = (Double)Constants.mc.field_1690.method_42495().method_41753() * (double)0.6f + (double)0.2f;
        double multiplier = sensitivity * sensitivity * sensitivity * 8.0;
        return (float)(RotationUtility.getCursorDelta(original, multiplier) * multiplier) * 0.15f;
    }

    public static class_241 getSentRotation(class_241 original) {
        return RotationUtility.getSensitivityModifiedRotation(RotationUtility.patchConstantRotation(original, RotationUtility.getRotation()));
    }

    public static class_241 getSensitivityModifiedRotation(class_241 original) {
        return new class_241(RotationUtility.getSensitivityModifiedRotation(original.field_1343), RotationUtility.getSensitivityModifiedRotation(original.field_1342));
    }

    public static class_241 getVanillaRotation(class_241 original) {
        class_241 sentRotation = RotationUtility.getSentRotation(original);
        float wrappedYaw = RotationUtility.getDuplicateWrapped(sentRotation.field_1343, Constants.mc.field_1724.method_36454());
        return new class_241(wrappedYaw, sentRotation.field_1342);
    }

    public static float getDuplicateWrapped(float value, float target) {
        return target + class_3532.method_15393((float)(value - target));
    }

    public static class_241 getRotation() {
        return new class_241(Constants.mc.field_1724.method_36454(), Constants.mc.field_1724.method_36455());
    }

    public static class_241 getPriorityAngle(class_241 currentRotation, float steps, boolean snap, boolean diagonal) {
        float targetYaw;
        if (snap) {
            float rounding = 45.0f / steps;
            float roundedMoveDir = (float)Math.round(MoveUtility.getDirectionDegrees() / rounding) * rounding;
            float yawRad = (float)Math.toRadians(roundedMoveDir);
            float offset = 10.0f;
            float dirX = -class_3532.method_15374((float)yawRad) * 10.0f;
            float dirZ = class_3532.method_15362((float)yawRad) * 10.0f;
            class_243 playerPos = Constants.mc.field_1724.method_73189();
            double targetBlockCenterX = Math.floor(playerPos.field_1352) + (double)dirX + 0.5;
            double targetBlockCenterZ = Math.floor(playerPos.field_1350) + (double)dirZ + 0.5;
            double deltaX = targetBlockCenterX - playerPos.field_1352;
            double deltaZ = targetBlockCenterZ - playerPos.field_1350;
            targetYaw = (float)Math.toDegrees(Math.atan2(-deltaX, deltaZ));
        } else {
            targetYaw = MoveUtility.getDirectionDegrees();
        }
        float endYaw = targetYaw + RandomUtility.getRandomFloat(-0.01f, 0.01f);
        ArrayList<Float> yaws = new ArrayList<Float>(4);
        if (!diagonal) {
            yaws.add(Float.valueOf(endYaw));
            yaws.add(Float.valueOf(endYaw + 180.0f));
        }
        for (int f = 45; f < 180; f += 90) {
            yaws.add(Float.valueOf(endYaw + (float)f));
            yaws.add(Float.valueOf(endYaw - (float)f));
        }
        yaws.sort(Comparator.comparingDouble(y -> class_3532.method_15356((float)y.floatValue(), (float)currentRotation.field_1343)));
        return new class_241(((Float)yaws.getFirst()).floatValue(), currentRotation.field_1342);
    }

    @Nullable
    public static RaytracedRotation getRotationFromRaycastedBlock(class_2338 blockPos, class_2350 side, class_241 priorityRotations, class_243 playerPos) {
        class_238 box = new class_238(blockPos);
        class_243 facedVector = box.method_1005();
        double widthX = box.method_17939();
        double height = box.method_17940();
        double widthZ = box.method_17941();
        ArrayList<RaytracedRotation> rotations = new ArrayList<RaytracedRotation>();
        float step = 12.0f;
        double vx = widthX;
        for (double x = -vx; x < vx; x += vx / 12.0) {
            double vy = height;
            for (double y = -vy; y < vy; y += vy / 12.0) {
                double vz = widthZ;
                for (double z = -vz; z < vz; z += vz / 12.0) {
                    class_243 offsetVector = new class_243(x, y, z);
                    class_243 raytraceVector = facedVector.method_1019(offsetVector);
                    class_241 raytraceRotation = RotationUtility.getVanillaRotation(RotationUtility.getRotationFromPosition(raytraceVector));
                    class_239 hitResult = RaycastUtility.raycastBlock(Constants.mc.field_1724.method_55754(), false, raytraceRotation.field_1343, raytraceRotation.field_1342, playerPos);
                    if (hitResult == null || hitResult.method_17783() != class_239.class_240.field_1332) continue;
                    class_3965 blockHitResult = (class_3965)hitResult;
                    class_2338 hitResultPos = blockHitResult.method_17777();
                    class_2350 hitResultSide = blockHitResult.method_17780();
                    if (!hitResultPos.equals((Object)blockPos) || hitResultSide != side) continue;
                    rotations.add(new RaytracedRotation(raytraceRotation, hitResult));
                }
            }
        }
        if (rotations.isEmpty()) {
            return null;
        }
        rotations.sort(Comparator.comparingDouble(r -> RotationUtility.getRotationDifference(r.rotation(), priorityRotations)));
        return (RaytracedRotation)rotations.getFirst();
    }

    @Nullable
    public static RaytracedRotation getRotationFromRaycastedEntity(class_1309 entity, class_243 closestVector, double entityInteractionRange) {
        com.google.common.base.Predicate targetPredicate = e -> e == entity;
        class_238 box = entity.method_5829().method_1014((double)entity.method_5871());
        class_243 facedVector = box.method_1005();
        double widthX = box.method_17939();
        double height = box.method_17940();
        double widthZ = box.method_17941();
        ArrayList<RaytracedRotation> rotations = new ArrayList<RaytracedRotation>();
        class_241 rotationFromPosition = RotationUtility.getRotationFromPosition(closestVector);
        float range = (float)RandomUtility.getJoinRandomDouble(0.01, 0.05);
        class_241 randomAddition = new class_241(RandomUtility.getRandomFloat(-range, range), RandomUtility.getRandomFloat(-range, range));
        class_241 randomClosestRotation = rotationFromPosition.method_35586(randomAddition);
        class_241 closestVectorRotation = RotationUtility.getVanillaRotation(randomClosestRotation);
        class_3966 closestHitResult = RaycastUtility.raycastEntity(entityInteractionRange, 1.0f, closestVectorRotation.field_1343, closestVectorRotation.field_1342, (Predicate<class_1297>)targetPredicate);
        if (closestHitResult != null) {
            return new RaytracedRotation(closestVectorRotation, (class_239)closestHitResult);
        }
        float step = 8.0f - RandomUtility.RANDOM.nextFloat() * 0.25f;
        double vx = widthX;
        for (double x = -vx; x < vx; x += vx / (double)step) {
            double vy = height;
            for (double y = -vy; y < vy; y += vy / (double)step) {
                double vz = widthZ;
                for (double z = -vz; z < vz; z += vz / (double)step) {
                    class_243 offsetVector = new class_243(x, y, z);
                    class_243 raytraceVector = facedVector.method_1019(offsetVector);
                    class_241 raytraceRotation = RotationUtility.getVanillaRotation(RotationUtility.getRotationFromPosition(raytraceVector));
                    class_3966 hitResult = RaycastUtility.raycastEntity(entityInteractionRange, 1.0f, raytraceRotation.field_1343, raytraceRotation.field_1342, (Predicate<class_1297>)targetPredicate);
                    if (hitResult == null) continue;
                    rotations.add(new RaytracedRotation(raytraceRotation, (class_239)hitResult));
                }
            }
        }
        if (rotations.isEmpty()) {
            return null;
        }
        rotations.sort(Comparator.comparingDouble(r -> RotationUtility.getRotationDifference(r.rotation(), closestVectorRotation)));
        return (RaytracedRotation)rotations.getFirst();
    }

    public static class_241 getRotationFromBlock(class_2338 blockPos, class_2350 direction) {
        float xDiff = (float)((double)blockPos.method_10263() + 0.5 - Constants.mc.field_1724.method_23317() + (double)direction.method_10148() * 0.5);
        float yDiff = (float)(Constants.mc.field_1724.method_23318() + (double)Constants.mc.field_1724.method_18381(Constants.mc.field_1724.method_18376()) - (double)blockPos.method_10264() - (double)direction.method_10164() * 0.5);
        float zDiff = (float)((double)blockPos.method_10260() + 0.5 - Constants.mc.field_1724.method_23321() + (double)direction.method_10165() * 0.5);
        double distance = class_3532.method_15355((float)(xDiff * xDiff + zDiff * zDiff));
        float yaw = (float)Math.toDegrees(-Math.atan2(xDiff, zDiff));
        float pitch = (float)Math.toDegrees(Math.atan((double)yDiff / distance));
        return new class_241(yaw, pitch);
    }

    public static class_241 getRotationFromPosition(class_243 pos) {
        return RotationUtility.getRotationFromPosition(Constants.mc.field_1724.method_33571(), pos);
    }

    public static class_241 getRotationFromPosition(class_243 from, class_243 to) {
        double xDiff = to.method_10216() - from.method_10216();
        double yDiff = to.method_10214() - from.method_10214();
        double zDiff = to.method_10215() - from.method_10215();
        double distance = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)Math.toDegrees(-Math.atan2(xDiff, zDiff));
        float pitch = (float)(-Math.toDegrees(Math.atan2(yDiff, distance)));
        return new class_241(yaw, pitch);
    }

    public static class_243 getRotationVector(float pitch, float yaw) {
        float f = pitch * ((float)Math.PI / 180);
        float g = -yaw * ((float)Math.PI / 180);
        float h = class_3532.method_15362((float)g);
        float i = class_3532.method_15374((float)g);
        float j = class_3532.method_15362((float)f);
        float k = class_3532.method_15374((float)f);
        return new class_243((double)(i * j), (double)(-k), (double)(h * j));
    }

    public static double getEntityFOV(class_1297 entity) {
        double yawDiff = (double)(RotationHelper.getClientHandler().getYawOr(Constants.mc.field_1724.method_36454()) - RotationUtility.getRotationFromPosition((class_243)entity.method_73189()).field_1343) % 360.0 + 540.0;
        return yawDiff % 360.0 - 180.0;
    }

    public static boolean isEntityInFOV(class_1297 entity, float fov) {
        if (fov >= 180.0f) {
            return true;
        }
        double angle = RotationUtility.getEntityFOV(entity);
        return Math.abs(angle) < (double)fov;
    }
}

