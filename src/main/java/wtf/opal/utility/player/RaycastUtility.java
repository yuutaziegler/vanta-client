/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_1675
 *  net.minecraft.class_238
 *  net.minecraft.class_239
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3966
 */
package wtf.opal.utility.player;

import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1675;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3966;
import wtf.opal.client.Constants;
import wtf.opal.utility.player.RotationUtility;

@Environment(value=EnvType.CLIENT)
public final class RaycastUtility {
    private RaycastUtility() {
    }

    public static class_239 raycastBlock(double maxDistance, float tickDelta, boolean includeFluids, float yaw, float pitch) {
        class_243 start = RaycastUtility.getCameraPosVec(tickDelta, (class_1297)Constants.mc.field_1724);
        return RaycastUtility.raycastBlock(maxDistance, includeFluids, yaw, pitch, start);
    }

    public static class_239 raycastBlock(double maxDistance, boolean includeFluids, float yaw, float pitch, class_243 start) {
        class_243 rotationVector = RotationUtility.getRotationVector(pitch, yaw);
        class_243 end = start.method_1031(rotationVector.field_1352 * maxDistance, rotationVector.field_1351 * maxDistance, rotationVector.field_1350 * maxDistance);
        return Constants.mc.field_1687.method_17742(new class_3959(start, end, class_3959.class_3960.field_17559, includeFluids ? class_3959.class_242.field_1347 : class_3959.class_242.field_1348, (class_1297)Constants.mc.field_1724));
    }

    public static class_3966 raycastEntity(double maxDistance, float tickDelta, float yaw, float pitch, Predicate<class_1297> predicate) {
        return RaycastUtility.raycastEntity(maxDistance, RaycastUtility.getCameraPosVec(tickDelta, (class_1297)Constants.mc.field_1724), yaw, pitch, predicate);
    }

    public static class_3966 raycastEntity(double maxDistance, class_243 start, float yaw, float pitch, Predicate<class_1297> predicate) {
        class_243 rotationVector = RotationUtility.getRotationVector(pitch, yaw);
        class_243 end = start.method_1031(rotationVector.field_1352 * maxDistance, rotationVector.field_1351 * maxDistance, rotationVector.field_1350 * maxDistance);
        class_238 box = Constants.mc.field_1724.method_5829().method_18804(rotationVector.method_1021(maxDistance)).method_1009(1.0, 1.0, 1.0);
        return class_1675.method_18075((class_1297)Constants.mc.field_1724, (class_243)start, (class_243)end, (class_238)box, predicate, (double)class_3532.method_33723((double)maxDistance));
    }

    public static class_243 getCameraPosVec(float tickDelta, class_1297 entity) {
        double x = class_3532.method_16436((double)tickDelta, (double)entity.field_6014, (double)entity.method_23317());
        double y = class_3532.method_16436((double)tickDelta, (double)entity.field_6036, (double)entity.method_23318()) + (double)entity.method_5751();
        double z = class_3532.method_16436((double)tickDelta, (double)entity.field_5969, (double)entity.method_23321());
        return new class_243(x, y, z);
    }
}

