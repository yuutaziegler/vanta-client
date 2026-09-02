/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_1703
 *  net.minecraft.class_1713
 *  net.minecraft.class_1735
 *  net.minecraft.class_1747
 *  net.minecraft.class_1776
 *  net.minecraft.class_1778
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1802
 *  net.minecraft.class_1812
 *  net.minecraft.class_1819
 *  net.minecraft.class_1887
 *  net.minecraft.class_1890
 *  net.minecraft.class_1893
 *  net.minecraft.class_1922
 *  net.minecraft.class_2199
 *  net.minecraft.class_2238
 *  net.minecraft.class_2244
 *  net.minecraft.class_2248
 *  net.minecraft.class_2260
 *  net.minecraft.class_2269
 *  net.minecraft.class_2272
 *  net.minecraft.class_2281
 *  net.minecraft.class_2286
 *  net.minecraft.class_2288
 *  net.minecraft.class_2304
 *  net.minecraft.class_2309
 *  net.minecraft.class_2315
 *  net.minecraft.class_2323
 *  net.minecraft.class_2328
 *  net.minecraft.class_2331
 *  net.minecraft.class_2336
 *  net.minecraft.class_2346
 *  net.minecraft.class_2349
 *  net.minecraft.class_2354
 *  net.minecraft.class_2356
 *  net.minecraft.class_2362
 *  net.minecraft.class_2363
 *  net.minecraft.class_2377
 *  net.minecraft.class_2378
 *  net.minecraft.class_2387
 *  net.minecraft.class_2401
 *  net.minecraft.class_2406
 *  net.minecraft.class_2428
 *  net.minecraft.class_2457
 *  net.minecraft.class_2462
 *  net.minecraft.class_2478
 *  net.minecraft.class_2480
 *  net.minecraft.class_2490
 *  net.minecraft.class_2515
 *  net.minecraft.class_2530
 *  net.minecraft.class_2533
 *  net.minecraft.class_2560
 *  net.minecraft.class_259
 *  net.minecraft.class_2667
 *  net.minecraft.class_2682
 *  net.minecraft.class_3489
 *  net.minecraft.class_3708
 *  net.minecraft.class_3709
 *  net.minecraft.class_3711
 *  net.minecraft.class_3713
 *  net.minecraft.class_3715
 *  net.minecraft.class_3717
 *  net.minecraft.class_3718
 *  net.minecraft.class_3726
 *  net.minecraft.class_3748
 *  net.minecraft.class_3830
 *  net.minecraft.class_3962
 *  net.minecraft.class_4969
 *  net.minecraft.class_5321
 *  net.minecraft.class_5455
 *  net.minecraft.class_5545
 *  net.minecraft.class_5804
 *  net.minecraft.class_5805
 *  net.minecraft.class_6089
 *  net.minecraft.class_6880
 *  net.minecraft.class_7714
 *  net.minecraft.class_7923
 *  net.minecraft.class_7924
 *  net.minecraft.class_8168
 *  net.minecraft.class_9334
 *  net.minecraft.class_9424
 */
package wtf.opal.utility.player;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1747;
import net.minecraft.class_1776;
import net.minecraft.class_1778;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1802;
import net.minecraft.class_1812;
import net.minecraft.class_1819;
import net.minecraft.class_1887;
import net.minecraft.class_1890;
import net.minecraft.class_1893;
import net.minecraft.class_1922;
import net.minecraft.class_2199;
import net.minecraft.class_2238;
import net.minecraft.class_2244;
import net.minecraft.class_2248;
import net.minecraft.class_2260;
import net.minecraft.class_2269;
import net.minecraft.class_2272;
import net.minecraft.class_2281;
import net.minecraft.class_2286;
import net.minecraft.class_2288;
import net.minecraft.class_2304;
import net.minecraft.class_2309;
import net.minecraft.class_2315;
import net.minecraft.class_2323;
import net.minecraft.class_2328;
import net.minecraft.class_2331;
import net.minecraft.class_2336;
import net.minecraft.class_2346;
import net.minecraft.class_2349;
import net.minecraft.class_2354;
import net.minecraft.class_2356;
import net.minecraft.class_2362;
import net.minecraft.class_2363;
import net.minecraft.class_2377;
import net.minecraft.class_2378;
import net.minecraft.class_2387;
import net.minecraft.class_2401;
import net.minecraft.class_2406;
import net.minecraft.class_2428;
import net.minecraft.class_2457;
import net.minecraft.class_2462;
import net.minecraft.class_2478;
import net.minecraft.class_2480;
import net.minecraft.class_2490;
import net.minecraft.class_2515;
import net.minecraft.class_2530;
import net.minecraft.class_2533;
import net.minecraft.class_2560;
import net.minecraft.class_259;
import net.minecraft.class_2667;
import net.minecraft.class_2682;
import net.minecraft.class_3489;
import net.minecraft.class_3708;
import net.minecraft.class_3709;
import net.minecraft.class_3711;
import net.minecraft.class_3713;
import net.minecraft.class_3715;
import net.minecraft.class_3717;
import net.minecraft.class_3718;
import net.minecraft.class_3726;
import net.minecraft.class_3748;
import net.minecraft.class_3830;
import net.minecraft.class_3962;
import net.minecraft.class_4969;
import net.minecraft.class_5321;
import net.minecraft.class_5455;
import net.minecraft.class_5545;
import net.minecraft.class_5804;
import net.minecraft.class_5805;
import net.minecraft.class_6089;
import net.minecraft.class_6880;
import net.minecraft.class_7714;
import net.minecraft.class_7923;
import net.minecraft.class_7924;
import net.minecraft.class_8168;
import net.minecraft.class_9334;
import net.minecraft.class_9424;
import wtf.opal.client.Constants;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class InventoryUtility {
    private static final List<class_2248> interactableBlocks = class_7923.field_41175.method_10220().filter(block -> block instanceof class_2533 || block instanceof class_3830 || block instanceof class_2363 || block instanceof class_2478 || block instanceof class_2199 || block instanceof class_3708 || block instanceof class_2238 || block instanceof class_2244 || block instanceof class_3709 || block instanceof class_2260 || block instanceof class_2269 || block instanceof class_2272 || block instanceof class_5545 || block instanceof class_3711 || block instanceof class_5804 || block instanceof class_5805 || block instanceof class_2281 || block instanceof class_7714 || block instanceof class_2288 || block instanceof class_2286 || block instanceof class_3962 || block instanceof class_2304 || block instanceof class_2309 || block instanceof class_8168 || block instanceof class_2315 || block instanceof class_2323 || block instanceof class_2328 || block instanceof class_2331 || block instanceof class_2336 || block instanceof class_2354 || block instanceof class_2349 || block instanceof class_2362 || block instanceof class_3713 || block instanceof class_2377 || block instanceof class_3748 || block instanceof class_2387 || block instanceof class_3715 || block instanceof class_2401 || block instanceof class_6089 || block instanceof class_2406 || block instanceof class_2428 || block instanceof class_2667 || block instanceof class_2457 || block instanceof class_2462 || block instanceof class_4969 || block instanceof class_2480 || block instanceof class_3717 || block instanceof class_3718 || block instanceof class_2356 || block instanceof class_2515 || block instanceof class_2490 || block instanceof class_2560).toList();

    private InventoryUtility() {
    }

    public static int findItemInHotbar(class_1792 item) {
        return IntStream.range(0, 9).filter(i -> {
            class_1799 itemStack = (class_1799)Constants.mc.field_1724.method_31548().method_67533().get(i);
            return itemStack.method_7909() == item && itemStack.method_7947() > 0;
        }).findFirst().orElse(-1);
    }

    public static boolean isInventoryFull() {
        return Constants.mc.field_1724.method_31548().method_67533().stream().noneMatch(class_1799::method_7960);
    }

    public static boolean isArmor(class_1799 itemStack) {
        if (itemStack.method_7909() == class_1802.field_8575 || itemStack.method_7909() == class_1802.field_17518) {
            return false;
        }
        return itemStack.method_57353().method_58694(class_9334.field_54196) != null;
    }

    public static double getSwordValue(class_1799 itemStack) {
        if (!itemStack.method_31573(class_3489.field_42611)) {
            return 0.0;
        }
        double score = PlayerUtility.getStackAttackDamage(itemStack);
        int sharpnessLevel = InventoryUtility.calculateEnchantmentLevel(itemStack, (class_5321<class_1887>)class_1893.field_9118) + 1;
        score *= (double)sharpnessLevel;
        score += (double)InventoryUtility.calculateEnchantmentLevel(itemStack, (class_5321<class_1887>)class_1893.field_9124);
        float durabilityRatio = (float)itemStack.method_7919() / (float)itemStack.method_7936();
        return score -= (double)durabilityRatio * 0.1;
    }

    public static double getArmorValue(class_1799 itemStack) {
        if (!InventoryUtility.isArmor(itemStack)) {
            return 0.0;
        }
        double score = PlayerUtility.getArmorProtection(itemStack);
        int protectionLevel = InventoryUtility.calculateEnchantmentLevel(itemStack, (class_5321<class_1887>)class_1893.field_9111) + 1;
        score *= (double)protectionLevel;
        score += (double)InventoryUtility.calculateEnchantmentLevel(itemStack, (class_5321<class_1887>)class_1893.field_9097);
        score += (double)InventoryUtility.calculateEnchantmentLevel(itemStack, (class_5321<class_1887>)class_1893.field_9119) * 0.5;
        score += (double)InventoryUtility.calculateEnchantmentLevel(itemStack, (class_5321<class_1887>)class_1893.field_9096) * 0.25;
        float durabilityRatio = (float)itemStack.method_7919() / (float)itemStack.method_7936();
        return score -= (double)durabilityRatio * 0.1;
    }

    public static double getToolValue(class_1799 itemStack) {
        class_9424 toolComponent = (class_9424)itemStack.method_58694(class_9334.field_50077);
        if (toolComponent == null) {
            return 0.0;
        }
        double score = toolComponent.comp_2500();
        int efficiencyLevel = InventoryUtility.calculateEnchantmentLevel(itemStack, (class_5321<class_1887>)class_1893.field_9131) + 1;
        score *= (double)efficiencyLevel;
        score += (double)InventoryUtility.calculateEnchantmentLevel(itemStack, (class_5321<class_1887>)class_1893.field_9119);
        float durabilityRatio = (float)itemStack.method_7919() / (float)itemStack.method_7936();
        return score -= (double)durabilityRatio * 0.1;
    }

    public static boolean isGoodItem(class_1799 itemStack) {
        class_1792 item = itemStack.method_7909();
        if (item instanceof class_1747) {
            class_1747 blockItem = (class_1747)item;
            return InventoryUtility.isGoodBlock(blockItem.method_7711());
        }
        if (item == class_1802.field_8575 || item == class_1802.field_17518 || item == class_1802.field_17519) {
            return false;
        }
        return item instanceof class_1776 || item instanceof class_1812 || item instanceof class_1819 || item instanceof class_1778 || item.method_57347().method_57832(class_9334.field_50075);
    }

    public static List<class_1735> filterSlots(class_1703 screenHandler, Predicate<class_1735> filterCondition, boolean shuffle) {
        List<class_1735> filteredSlots = screenHandler.field_7761.stream().filter(filterCondition).collect(Collectors.toList());
        if (shuffle) {
            Collections.shuffle(filteredSlots);
        }
        return filteredSlots;
    }

    public static void drop(class_1703 screenHandler, int slot) {
        Constants.mc.field_1761.method_2906(screenHandler.field_7763, slot, 1, class_1713.field_7795, (class_1657)Constants.mc.field_1724);
    }

    public static void shiftClick(class_1703 screenHandler, int slot, int mouseButton) {
        Constants.mc.field_1761.method_2906(screenHandler.field_7763, slot, mouseButton, class_1713.field_7794, (class_1657)Constants.mc.field_1724);
    }

    public static void swap(class_1703 screenHandler, int originalSlot, int newSlot) {
        Constants.mc.field_1761.method_2906(screenHandler.field_7763, originalSlot, newSlot, class_1713.field_7791, (class_1657)Constants.mc.field_1724);
    }

    public static int calculateEnchantmentLevel(class_1799 itemStack, class_5321<class_1887> enchantment) {
        class_5455 drm = Constants.mc.field_1687.method_30349();
        class_2378 registryWrapper = drm.method_30530(class_7924.field_41265);
        return class_1890.method_8225((class_6880)registryWrapper.method_46747(enchantment), (class_1799)itemStack);
    }

    public static boolean isGoodBlock(class_2248 block) {
        return !InventoryUtility.isBlockInteractable(block) && block.method_9564().method_26172((class_1922)class_2682.field_12294, Constants.mc.field_1724.method_24515(), class_3726.method_16195((class_1297)Constants.mc.field_1724)) == class_259.method_1077() && !(block instanceof class_2530) && !(block instanceof class_2346);
    }

    public static boolean isBlockInteractable(class_2248 block) {
        return interactableBlocks.contains(block);
    }
}

