/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 */
package wtf.opal.scripting.impl.proxy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_241;
import net.minecraft.class_243;
import wtf.opal.utility.player.RaytracedRotation;
import wtf.opal.utility.player.RotationUtility;

@Environment(value=EnvType.CLIENT)
public class RotationProxy {
    public float getRotationDifference(class_241 a, class_241 b) {
        return RotationUtility.getRotationDifference(a, b);
    }

    public double getCursorDelta(double rotationDelta, double sensitivityMultiplier) {
        return RotationUtility.getCursorDelta(rotationDelta, sensitivityMultiplier);
    }

    public class_241 patchConstantRotation(class_241 rotation, class_241 prevRotation) {
        return RotationUtility.patchConstantRotation(rotation, prevRotation);
    }

    public float getSensitivityModifiedRotation(double original) {
        return RotationUtility.getSensitivityModifiedRotation(original);
    }

    public class_241 getSentRotation(class_241 original) {
        return RotationUtility.getSentRotation(original);
    }

    public class_241 getSensitivityModifiedRotation(class_241 original) {
        return RotationUtility.getSensitivityModifiedRotation(original);
    }

    public class_241 getVanillaRotation(class_241 original) {
        return RotationUtility.getVanillaRotation(original);
    }

    public float getDuplicateWrapped(float value, float target) {
        return RotationUtility.getDuplicateWrapped(value, target);
    }

    public class_241 getRotation() {
        return RotationUtility.getRotation();
    }

    public RaytracedRotation getRotationFromRaycastedBlock(class_2338 blockPos, class_2350 side, class_241 priorityRotations, class_243 playerPos) {
        return RotationUtility.getRotationFromRaycastedBlock(blockPos, side, priorityRotations, playerPos);
    }

    public RaytracedRotation getRotationFromRaycastedEntity(class_1309 entity, class_243 closestVector, double entityInteractionRange) {
        return RotationUtility.getRotationFromRaycastedEntity(entity, closestVector, entityInteractionRange);
    }

    public class_241 getRotationFromBlock(class_2338 blockPos, class_2350 direction) {
        return RotationUtility.getRotationFromBlock(blockPos, direction);
    }

    public class_241 getRotationFromPosition(class_243 pos) {
        return RotationUtility.getRotationFromPosition(pos);
    }

    public class_243 getRotationVector(float pitch, float yaw) {
        return RotationUtility.getRotationVector(pitch, yaw);
    }

    public double getEntityFOV(class_1297 entity) {
        return RotationUtility.getEntityFOV(entity);
    }

    public boolean isEntityInFOV(class_1297 entity, float fov) {
        return RotationUtility.isEntityInFOV(entity, fov);
    }
}

