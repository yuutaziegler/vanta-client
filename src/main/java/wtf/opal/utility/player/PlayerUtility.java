/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1041
 *  net.minecraft.class_1292
 *  net.minecraft.class_1294
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1322
 *  net.minecraft.class_1322$class_1323
 *  net.minecraft.class_1799
 *  net.minecraft.class_1922
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_238
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_247
 *  net.minecraft.class_259
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_304
 *  net.minecraft.class_3675
 *  net.minecraft.class_3675$class_306
 *  net.minecraft.class_3726
 *  net.minecraft.class_3965
 *  net.minecraft.class_5134
 *  net.minecraft.class_5224
 *  net.minecraft.class_642
 *  net.minecraft.class_9274
 *  net.minecraft.class_9285
 *  net.minecraft.class_9285$class_9287
 *  net.minecraft.class_9334
 *  org.lwjgl.glfw.GLFW
 */
package wtf.opal.utility.player;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_1292;
import net.minecraft.class_1294;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1322;
import net.minecraft.class_1799;
import net.minecraft.class_1922;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_247;
import net.minecraft.class_259;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_304;
import net.minecraft.class_3675;
import net.minecraft.class_3726;
import net.minecraft.class_3965;
import net.minecraft.class_5134;
import net.minecraft.class_5224;
import net.minecraft.class_642;
import net.minecraft.class_9274;
import net.minecraft.class_9285;
import net.minecraft.class_9334;
import org.lwjgl.glfw.GLFW;
import wtf.opal.client.Constants;
import wtf.opal.utility.render.OrderedTextVisitor;

@Environment(value=EnvType.CLIENT)
public final class PlayerUtility {
    public static final List<class_304> MOVEMENT_KEYS = List.of(Constants.mc.field_1690.field_1894, Constants.mc.field_1690.field_1881, Constants.mc.field_1690.field_1913, Constants.mc.field_1690.field_1849, Constants.mc.field_1690.field_1903);

    private PlayerUtility() {
    }

    public static boolean isCriticalHitAvailable() {
        return !Constants.mc.field_1724.method_5799() && !Constants.mc.field_1724.method_6101() && !Constants.mc.field_1724.method_6059(class_1294.field_5919) && !Constants.mc.field_1724.method_5765();
    }

    public static boolean isNoAirBelow() {
        return PlayerUtility.isNoAirBelow(0.0, 0.0);
    }

    public static boolean isNoAirBelow(double offsetX, double offsetZ) {
        class_238 box = Constants.mc.field_1724.method_5829().method_989(offsetX, -(Constants.mc.field_1724.method_5829().method_17940() + 1.0), offsetZ);
        return class_2338.method_29715((class_238)box).noneMatch(pos -> {
            class_2680 blockState = Constants.mc.field_1687.method_8320(pos);
            class_265 voxelShape = blockState.method_26220((class_1922)Constants.mc.field_1687, pos).method_1096((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260());
            return (double)pos.method_10264() == Math.floor(Constants.mc.field_1724.method_23318() - 1.0) && (voxelShape.method_1110() || voxelShape.method_1105(class_2350.class_2351.field_11052) != Constants.mc.field_1724.method_23318() - Constants.mc.field_1724.method_23318() % 0.0625);
        });
    }

    public static String getFormattedEntityName(class_1309 entity) {
        if (entity.method_5476() != null) {
            OrderedTextVisitor visitor = new OrderedTextVisitor();
            entity.method_5476().method_30937().accept((class_5224)visitor);
            return visitor.getFormattedString();
        }
        return entity.method_5477().getString();
    }

    public static boolean isServerBrand(String brandString) {
        if (Constants.mc.field_1724 == null || Constants.mc.method_1542()) {
            return false;
        }
        class_642 serverInfo = Constants.mc.method_1558();
        if (serverInfo == null) {
            return false;
        }
        String brand = Constants.mc.field_1724.field_3944.method_52790();
        return brand != null && brand.toLowerCase().contains(brandString.toLowerCase());
    }

    public static int getHandSwingDuration() {
        if (class_1292.method_5576((class_1309)Constants.mc.field_1724)) {
            return 6 - (1 + class_1292.method_5575((class_1309)Constants.mc.field_1724));
        }
        if (Constants.mc.field_1724.method_6059(class_1294.field_5901)) {
            return 6 + (1 + Constants.mc.field_1724.method_6112(class_1294.field_5901).method_5578()) * 2;
        }
        return 6;
    }

    public static class_243 getClosestVectorToBox(class_243 from, class_238 box) {
        double closestX = Math.max(box.field_1323, Math.min(from.method_10216(), box.field_1320));
        double closestY = Math.max(box.field_1322, Math.min(from.method_10214(), box.field_1325));
        double closestZ = Math.max(box.field_1321, Math.min(from.method_10215(), box.field_1324));
        return new class_243(closestX, closestY, closestZ);
    }

    public static class_243 getClosestVectorToBoundingBox(class_243 from, class_1309 entity) {
        return PlayerUtility.getClosestVectorToBox(from, entity.method_5829().method_1014((double)entity.method_5871()));
    }

    public static double getDistanceToEntity(class_1309 entity) {
        class_243 eyePos = Constants.mc.field_1724.method_33571();
        return eyePos.method_1022(PlayerUtility.getClosestVectorToBox(eyePos, entity.method_5829().method_1014((double)entity.method_5871())));
    }

    public static class_238 getBlockBox(class_2338 blockPos) {
        class_265 shape = Constants.mc.field_1687.method_8320(blockPos).method_26172((class_1922)Constants.mc.field_1687, blockPos, class_3726.method_16195((class_1297)Constants.mc.field_1724));
        if (shape.method_1110()) {
            return new class_238(blockPos);
        }
        class_238 bb = shape.method_1107();
        return new class_238((double)blockPos.method_10263() + bb.field_1323, (double)blockPos.method_10264() + bb.field_1322, (double)blockPos.method_10260() + bb.field_1321, (double)blockPos.method_10263() + bb.field_1320, (double)blockPos.method_10264() + bb.field_1325, (double)blockPos.method_10260() + bb.field_1324);
    }

    public static double getDistanceToBlock(class_2338 blockPos) {
        class_243 eyePos = Constants.mc.field_1724.method_33571();
        class_238 blockBox = PlayerUtility.getBlockBox(blockPos);
        class_243 closestVector = PlayerUtility.getClosestVectorToBox(eyePos, blockBox);
        return eyePos.method_1022(closestVector);
    }

    public static boolean isAirUntil(double posY, class_238 playerBox) {
        class_238 box = new class_238(playerBox.field_1323, posY, playerBox.field_1321, playerBox.field_1320, playerBox.field_1325, playerBox.field_1324);
        return PlayerUtility.isBoxEmpty(box);
    }

    public static boolean isBoxEmpty(class_238 box) {
        return class_2338.method_29715((class_238)box).noneMatch(pos -> {
            class_2680 blockState = Constants.mc.field_1687.method_8320(pos);
            class_265 voxelShape = blockState.method_26220((class_1922)Constants.mc.field_1687, pos);
            return !voxelShape.method_1110() && class_259.method_1074((class_265)blockState.method_26220((class_1922)Constants.mc.field_1687, pos).method_1096((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260()), (class_265)class_259.method_1078((class_238)box), (class_247)class_247.field_16896);
        });
    }

    public static boolean isOverVoid(class_238 playerBox) {
        return PlayerUtility.isAirUntil(0.0, playerBox.method_1009(-0.005, 0.0, -0.005));
    }

    public static boolean isOverVoid() {
        return PlayerUtility.isOverVoid(Constants.mc.field_1724.method_5829());
    }

    public static class_2248 getBlockOver() {
        if (Constants.mc.field_1765 != null && Constants.mc.field_1765.method_17783() == class_239.class_240.field_1332) {
            class_3965 blockHitResult = (class_3965)Constants.mc.field_1765;
            class_2338 blockPos = blockHitResult.method_17777();
            return Constants.mc.field_1687.method_8320(blockPos).method_26204();
        }
        return null;
    }

    public static class_2338 getBlockPosOver() {
        if (Constants.mc.field_1765 != null && Constants.mc.field_1765.method_17783() == class_239.class_240.field_1332) {
            class_3965 blockHitResult = (class_3965)Constants.mc.field_1765;
            return blockHitResult.method_17777();
        }
        return null;
    }

    public static float getMaxFallDistance() {
        float distance = 3.0f;
        if (Constants.mc.field_1724.method_6059(class_1294.field_5913)) {
            distance += (float)(Constants.mc.field_1724.method_6112(class_1294.field_5913).method_5578() + 1);
        }
        return distance;
    }

    public static boolean isKeyPressed(int keyCode) {
        return class_3675.method_15987((class_1041)Constants.mc.method_22683(), (int)keyCode);
    }

    public static boolean isKeyPressed(class_304 keyBinding) {
        return PlayerUtility.isKeyPressed(keyBinding.method_1429().method_1444());
    }

    public static boolean isMouseButtonPressed(int button) {
        return GLFW.glfwGetMouseButton((long)Constants.mc.method_22683().method_4490(), (int)button) == 1;
    }

    public static void updateMovementKeyStates() {
        MOVEMENT_KEYS.forEach(k -> class_304.method_1416((class_3675.class_306)k.method_1429(), (boolean)PlayerUtility.isKeyPressed(k)));
    }

    public static void unpressMovementKeyStates() {
        MOVEMENT_KEYS.forEach(k -> class_304.method_1416((class_3675.class_306)k.method_1429(), (boolean)false));
    }

    public static boolean isCollisionImminent(double offsetX, double offsetY, double offsetZ) {
        return !PlayerUtility.isBoxEmpty(Constants.mc.field_1724.method_5829().method_989(offsetX, offsetY, offsetZ));
    }

    public static boolean isInsideBlock() {
        return !PlayerUtility.isBoxEmpty(Constants.mc.field_1724.method_5829());
    }

    public static boolean areOnSameTeam(class_1309 entity, class_1309 entity1) {
        int entity1Color;
        if (entity.method_5476() == null || entity1.method_5476() == null) {
            return false;
        }
        int entityColor = entity.method_22861();
        return entityColor == (entity1Color = entity1.method_22861());
    }

    public static double getStackAttackSpeed(class_1799 stack) {
        double base = 4.0;
        double attackSpeed = 4.0;
        class_9285 attributeModifiersComponent = (class_9285)stack.method_58695(class_9334.field_49636, (Object)class_9285.field_49326);
        for (class_9285.class_9287 entry : attributeModifiersComponent.comp_2393()) {
            if (entry.comp_2395() != class_5134.field_23723 || entry.comp_2397() != class_9274.field_49217) continue;
            class_1322 modifier = entry.comp_2396();
            attackSpeed += (switch (modifier.comp_2450()) {
                default -> throw new MatchException(null, null);
                case class_1322.class_1323.field_6328 -> modifier.comp_2449();
                case class_1322.class_1323.field_6330 -> modifier.comp_2449() * 4.0;
                case class_1322.class_1323.field_6331 -> modifier.comp_2449() * attackSpeed;
            });
        }
        return attackSpeed;
    }

    public static double getStackAttackDamage(class_1799 stack) {
        double attackDamage = 0.0;
        class_9285 attributeModifiersComponent = (class_9285)stack.method_58695(class_9334.field_49636, (Object)class_9285.field_49326);
        for (class_9285.class_9287 entry : attributeModifiersComponent.comp_2393()) {
            if (entry.comp_2395() != class_5134.field_23721 || entry.comp_2397() != class_9274.field_49217) continue;
            class_1322 modifier = entry.comp_2396();
            attackDamage += modifier.comp_2449();
        }
        return attackDamage;
    }

    public static double getArmorProtection(class_1799 stack) {
        double protection = 0.0;
        class_9285 attributeModifiersComponent = (class_9285)stack.method_58695(class_9334.field_49636, (Object)class_9285.field_49326);
        for (class_9285.class_9287 entry : attributeModifiersComponent.comp_2393()) {
            if (entry.comp_2395() != class_5134.field_23724) continue;
            class_1322 modifier = entry.comp_2396();
            protection += modifier.comp_2449();
        }
        return protection;
    }
}

