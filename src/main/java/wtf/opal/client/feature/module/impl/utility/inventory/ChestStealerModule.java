/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10192
 *  net.minecraft.class_1263
 *  net.minecraft.class_1304
 *  net.minecraft.class_1703
 *  net.minecraft.class_1707
 *  net.minecraft.class_1743
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_3489
 *  net.minecraft.class_437
 *  net.minecraft.class_476
 *  net.minecraft.class_6862
 *  net.minecraft.class_9334
 */
package wtf.opal.client.feature.module.impl.utility.inventory;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10192;
import net.minecraft.class_1263;
import net.minecraft.class_1304;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1743;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_3489;
import net.minecraft.class_437;
import net.minecraft.class_476;
import net.minecraft.class_6862;
import net.minecraft.class_9334;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.BoundedNumberProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.time.Stopwatch;
import wtf.opal.utility.player.InventoryUtility;

@Environment(value=EnvType.CLIENT)
public final class ChestStealerModule
extends Module {
    private final Stopwatch stopwatch = new Stopwatch();
    private final BooleanProperty smart = new BooleanProperty("Smart", true);
    private final BooleanProperty highlight = (BooleanProperty)new BooleanProperty("Highlight items", true).hideIf(() -> this.smart.getValue() == false);
    private final BoundedNumberProperty delay = new BoundedNumberProperty("Delay", 50.0, 100.0, 0.0, 400.0, 5.0);

    public ChestStealerModule() {
        super("Chest Stealer", "Steals only useful or upgraded items from chests.", ModuleCategory.UTILITY);
        this.addProperties(this.smart, this.highlight, this.delay);
    }

    @Subscribe
    public void onPreGameTickEvent(PreGameTickEvent event) {
        class_437 class_4372 = Constants.mc.field_1755;
        if (!(class_4372 instanceof class_476)) {
            return;
        }
        class_476 container = (class_476)class_4372;
        class_1707 screenHandler = (class_1707)container.method_17577();
        class_1263 chestInventory = screenHandler.method_7629();
        if (!container.method_25440().getString().toLowerCase().contains("chest")) {
            return;
        }
        if (chestInventory.method_5442() || InventoryUtility.isInventoryFull()) {
            container.method_25419();
            return;
        }
        Map<class_1304, class_1799> bestChestArmor = this.getBestChestArmor(chestInventory);
        class_1799 bestChestSword = this.getBestChestSword(chestInventory);
        class_1799 bestChestPickaxe = this.getBestChestTool(chestInventory, (class_6862<class_1792>)class_3489.field_42614);
        class_1799 bestChestAxe = this.getBestChestTool(chestInventory, (class_6862<class_1792>)class_3489.field_42612);
        boolean tookItem = false;
        for (int i = 0; i < chestInventory.method_5439(); ++i) {
            class_1799 stack = chestInventory.method_5438(i);
            if (stack.method_7960() || !this.canMove() || !this.shouldTake(stack, bestChestArmor, bestChestSword, bestChestPickaxe, bestChestAxe) && this.smart.getValue().booleanValue()) continue;
            InventoryUtility.shiftClick((class_1703)screenHandler, i, 0);
            this.stopwatch.reset();
            tookItem = true;
            break;
        }
        if (this.smart.getValue().booleanValue() && !tookItem) {
            boolean hasValuableLeft = false;
            for (int i = 0; i < chestInventory.method_5439(); ++i) {
                class_1799 stack = chestInventory.method_5438(i);
                if (stack.method_7960() || !this.shouldTake(stack, bestChestArmor, bestChestSword, bestChestPickaxe, bestChestAxe)) continue;
                hasValuableLeft = true;
                break;
            }
            if (!hasValuableLeft) {
                container.method_25419();
            }
        }
    }

    public BooleanProperty getHighlight() {
        return this.highlight;
    }

    public BooleanProperty getSmart() {
        return this.smart;
    }

    public boolean shouldTake(class_1799 stack, Map<class_1304, class_1799> bestChestArmor, class_1799 bestChestSword, class_1799 bestChestPickaxe, class_1799 bestChestAxe) {
        double equippedValue;
        if (InventoryUtility.isGoodItem(stack)) {
            return true;
        }
        if (stack.method_31573(class_3489.field_42611)) {
            double value = InventoryUtility.getSwordValue(stack);
            double current = InventoryUtility.getSwordValue(this.getBestHotbarSword());
            return stack == bestChestSword && value > current;
        }
        if (stack.method_31573(class_3489.field_42614)) {
            double value = InventoryUtility.getToolValue(stack);
            double current = InventoryUtility.getToolValue(this.getBestHotbarTool((class_6862<class_1792>)class_3489.field_42614));
            return stack == bestChestPickaxe && value > current;
        }
        if (stack.method_31573(class_3489.field_42612)) {
            double value = InventoryUtility.getToolValue(stack);
            double current = InventoryUtility.getToolValue(this.getBestHotbarAxe());
            return stack == bestChestAxe && value > current;
        }
        if (!InventoryUtility.isArmor(stack)) {
            return false;
        }
        class_10192 equip = (class_10192)stack.method_57353().method_58694(class_9334.field_54196);
        if (equip == null) {
            return false;
        }
        class_1304 slot = equip.comp_3174();
        class_1799 currentEquipped = Constants.mc.field_1724.method_6118(slot);
        class_1799 bestInChest = bestChestArmor.getOrDefault(slot, class_1799.field_8037);
        if (stack != bestInChest) {
            return false;
        }
        double stackValue = InventoryUtility.getArmorValue(stack);
        return stackValue > (equippedValue = InventoryUtility.getArmorValue(currentEquipped));
    }

    public Map<class_1304, class_1799> getBestChestArmor(class_1263 chest) {
        return IntStream.range(0, chest.method_5439()).mapToObj(arg_0 -> ((class_1263)chest).method_5438(arg_0)).filter(InventoryUtility::isArmor).map(stack -> {
            class_10192 equip = (class_10192)stack.method_57353().method_58694(class_9334.field_54196);
            return equip != null ? Map.entry(equip.comp_3174(), stack) : null;
        }).filter(Objects::nonNull).collect(HashMap::new, (map, entry) -> map.merge((class_1304)entry.getKey(), (class_1799)entry.getValue(), (existing, replacement) -> InventoryUtility.getArmorValue(replacement) > InventoryUtility.getArmorValue(existing) ? replacement : existing), HashMap::putAll);
    }

    public class_1799 getBestChestSword(class_1263 chest) {
        return IntStream.range(0, chest.method_5439()).mapToObj(arg_0 -> ((class_1263)chest).method_5438(arg_0)).filter(stack -> stack.method_31573(class_3489.field_42611)).max(Comparator.comparingDouble(InventoryUtility::getSwordValue)).orElse(class_1799.field_8037);
    }

    public class_1799 getBestChestTool(class_1263 chest, class_6862<class_1792> tag) {
        return IntStream.range(0, chest.method_5439()).mapToObj(arg_0 -> ((class_1263)chest).method_5438(arg_0)).filter(stack -> stack.method_31573(tag)).max(Comparator.comparingDouble(InventoryUtility::getToolValue)).orElse(class_1799.field_8037);
    }

    private class_1799 getBestHotbarSword() {
        return IntStream.range(0, 9).mapToObj(i -> Constants.mc.field_1724.method_31548().method_5438(i)).filter(stack -> stack.method_31573(class_3489.field_42611)).max(Comparator.comparingDouble(InventoryUtility::getSwordValue)).orElse(class_1799.field_8037);
    }

    private class_1799 getBestHotbarTool(class_6862<class_1792> tag) {
        return IntStream.range(0, 9).mapToObj(i -> Constants.mc.field_1724.method_31548().method_5438(i)).filter(stack -> stack.method_31573(tag)).max(Comparator.comparingDouble(InventoryUtility::getToolValue)).orElse(class_1799.field_8037);
    }

    private class_1799 getBestHotbarAxe() {
        return IntStream.range(0, 9).mapToObj(i -> Constants.mc.field_1724.method_31548().method_5438(i)).filter(stack -> stack.method_7909() instanceof class_1743).max(Comparator.comparingDouble(InventoryUtility::getToolValue)).orElse(class_1799.field_8037);
    }

    public boolean canMove() {
        long delayMs = this.delay.getRandomValue().longValue();
        return delayMs == 0L || this.stopwatch.hasTimeElapsed(delayMs);
    }
}

