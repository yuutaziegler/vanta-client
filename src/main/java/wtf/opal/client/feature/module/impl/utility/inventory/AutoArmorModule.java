/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.hypixel.data.type.GameType
 *  net.minecraft.class_10192
 *  net.minecraft.class_1304
 *  net.minecraft.class_1703
 *  net.minecraft.class_1723
 *  net.minecraft.class_1735
 *  net.minecraft.class_1799
 *  net.minecraft.class_490
 *  net.minecraft.class_9334
 */
package wtf.opal.client.feature.module.impl.utility.inventory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hypixel.data.type.GameType;
import net.minecraft.class_10192;
import net.minecraft.class_1304;
import net.minecraft.class_1703;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_1799;
import net.minecraft.class_490;
import net.minecraft.class_9334;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.combat.killaura.KillAuraModule;
import wtf.opal.client.feature.module.impl.movement.InventoryMoveModule;
import wtf.opal.client.feature.module.impl.utility.inventory.manager.InventoryManagerModule;
import wtf.opal.client.feature.module.property.impl.number.BoundedNumberProperty;
import wtf.opal.client.feature.module.repository.ModuleRepository;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.InventoryUtility;

@Environment(value=EnvType.CLIENT)
public final class AutoArmorModule
extends Module {
    private final BoundedNumberProperty delay = new BoundedNumberProperty("Delay", 50.0, 100.0, 0.0, 400.0, 5.0);

    public AutoArmorModule() {
        super("Auto Armor", "Automatically equips the best armor possible.", ModuleCategory.UTILITY);
        this.addProperties(this.delay);
    }

    @Subscribe
    public void onPreGameTickEvent(PreGameTickEvent event) {
        HypixelServer.ModAPI.Location currentLocation;
        if (Constants.mc.field_1724 == null) {
            return;
        }
        ModuleRepository moduleRepository = OpalClient.getInstance().getModuleRepository();
        if (!(Constants.mc.field_1755 instanceof class_490) && !moduleRepository.getModule(InventoryMoveModule.class).isEnabled()) {
            return;
        }
        KillAuraModule killAuraModule = moduleRepository.getModule(KillAuraModule.class);
        if (killAuraModule.isEnabled() && killAuraModule.getTargeting().isTargetSelected()) {
            return;
        }
        if (LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer && (currentLocation = HypixelServer.ModAPI.get().getCurrentLocation()) != null && (currentLocation.isLobby() || currentLocation.serverType() != GameType.SKYWARS && currentLocation.serverType() != GameType.SURVIVAL_GAMES)) {
            return;
        }
        class_1703 screenHandler = Constants.mc.field_1724.field_7512;
        if (!(screenHandler instanceof class_1723)) {
            return;
        }
        class_1723 playerHandler = (class_1723)screenHandler;
        InventoryManagerModule managerModule = moduleRepository.getModule(InventoryManagerModule.class);
        List<class_1735> bestArmor = this.getBestArmor(playerHandler);
        InventoryUtility.filterSlots((class_1703)playerHandler, slot -> !slot.method_7677().method_7960() && InventoryUtility.isArmor(slot.method_7677()), true).forEach(validSlot -> {
            class_1799 itemStack = validSlot.method_7677();
            if (bestArmor.stream().noneMatch(armor -> armor.method_7677() == itemStack)) {
                if (!managerModule.canMove(this.delay.getRandomValue().longValue())) {
                    return;
                }
                InventoryUtility.drop((class_1703)playerHandler, validSlot.field_7874);
                managerModule.stopwatch.reset();
            }
        });
        bestArmor.forEach(equipmentSlotPair -> {
            List<class_1799> armorStacks = this.getArmorStacks();
            Collections.shuffle(armorStacks);
            if (armorStacks.stream().noneMatch(armor -> equipmentSlotPair.method_7677() == armor)) {
                if (!managerModule.canMove(this.delay.getRandomValue().longValue())) {
                    return;
                }
                InventoryUtility.shiftClick((class_1703)playerHandler, equipmentSlotPair.field_7874, 0);
                managerModule.stopwatch.reset();
            }
        });
    }

    private List<class_1735> getBestArmor(class_1723 screenHandler) {
        return Arrays.stream(class_1304.values()).map(slotType -> InventoryUtility.filterSlots((class_1703)screenHandler, slot -> {
            if (slot.method_7677().method_7960() || !InventoryUtility.isArmor(slot.method_7677())) {
                return false;
            }
            class_10192 equippable = (class_10192)slot.method_7677().method_57353().method_58694(class_9334.field_54196);
            return equippable != null && equippable.comp_3174() == slotType;
        }, false).stream().max(Comparator.comparing(slot -> InventoryUtility.getArmorValue(slot.method_7677()))).orElse(null)).filter(Objects::nonNull).collect(Collectors.toList());
    }

    private List<class_1799> getArmorStacks() {
        ArrayList<class_1799> armorStacks = new ArrayList<class_1799>();
        for (class_1304 slot : class_1304.values()) {
            class_1799 equippedStack = Constants.mc.field_1724.method_6118(slot);
            if (equippedStack.method_7960() || !InventoryUtility.isArmor(equippedStack)) continue;
            armorStacks.add(equippedStack);
        }
        return armorStacks;
    }
}

