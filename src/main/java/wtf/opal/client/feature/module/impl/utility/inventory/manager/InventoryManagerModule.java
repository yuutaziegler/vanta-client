/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.hypixel.data.type.GameType
 *  net.minecraft.class_1703
 *  net.minecraft.class_1723
 *  net.minecraft.class_1735
 *  net.minecraft.class_1743
 *  net.minecraft.class_1747
 *  net.minecraft.class_1755
 *  net.minecraft.class_1792
 *  net.minecraft.class_1802
 *  net.minecraft.class_2596
 *  net.minecraft.class_2653
 *  net.minecraft.class_3489
 *  net.minecraft.class_490
 *  net.minecraft.class_9334
 */
package wtf.opal.client.feature.module.impl.utility.inventory.manager;

import java.util.Comparator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hypixel.data.type.GameType;
import net.minecraft.class_1703;
import net.minecraft.class_1723;
import net.minecraft.class_1735;
import net.minecraft.class_1743;
import net.minecraft.class_1747;
import net.minecraft.class_1755;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_2596;
import net.minecraft.class_2653;
import net.minecraft.class_3489;
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
import wtf.opal.client.feature.module.impl.utility.inventory.manager.InventoryManagerSettings;
import wtf.opal.client.feature.module.impl.world.scaffold.LBScaffoldModule;
import wtf.opal.client.feature.module.repository.ModuleRepository;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.time.Stopwatch;
import wtf.opal.utility.player.InventoryUtility;

@Environment(value=EnvType.CLIENT)
public final class InventoryManagerModule
extends Module {
    private final InventoryManagerSettings settings = new InventoryManagerSettings(this);
    public final Stopwatch stopwatch = new Stopwatch();

    public InventoryManagerModule() {
        super("Inventory Manager", "Manages your inventory.", ModuleCategory.UTILITY);
    }

    @Subscribe
    public void onPreGameTickEvent(PreGameTickEvent event) {
        class_1703 screenHandler;
        boolean blitz;
        if (Constants.mc.field_1724 == null) {
            return;
        }
        ModuleRepository moduleRepository = OpalClient.getInstance().getModuleRepository();
        if (!(Constants.mc.field_1755 instanceof class_490) && !moduleRepository.getModule(InventoryMoveModule.class).isEnabled()) {
            return;
        }
        KillAuraModule killAuraModule = moduleRepository.getModule(KillAuraModule.class);
        LBScaffoldModule LBScaffoldModule2 = moduleRepository.getModule(LBScaffoldModule.class);
        if (killAuraModule.isEnabled() && killAuraModule.getTargeting().isTargetSelected() || LBScaffoldModule2.isEnabled()) {
            return;
        }
        if (LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer) {
            HypixelServer.ModAPI.Location currentLocation = HypixelServer.ModAPI.get().getCurrentLocation();
            if (currentLocation == null || currentLocation.isLobby()) {
                return;
            }
            if (currentLocation.serverType() == GameType.SURVIVAL_GAMES) {
                blitz = false;
            } else {
                blitz = false;
                if (currentLocation.serverType() != GameType.SKYWARS) {
                    return;
                }
            }
        } else {
            blitz = false;
        }
        if (!((screenHandler = Constants.mc.field_1724.field_7512) instanceof class_1723)) {
            return;
        }
        class_1723 playerHandler = (class_1723)screenHandler;
        class_1735 bestSword = this.getBestSword((class_1703)playerHandler);
        class_1735 preferredSwordSlot = screenHandler.method_7611(this.settings.getSwordSlot() + 35);
        class_1735 bestPickaxe = this.getBestPickaxe((class_1703)playerHandler);
        class_1735 preferredPickaxeSlot = screenHandler.method_7611(this.settings.getPickaxeSlot() + 35);
        class_1735 bestAxe = this.getBestAxe((class_1703)playerHandler);
        class_1735 preferredAxeSlot = screenHandler.method_7611(this.settings.getAxeSlot() + 35);
        class_1735 mostBlocks = this.getMostBlocks((class_1703)playerHandler);
        class_1735 preferredBlockSlot = screenHandler.method_7611(this.settings.getBlockSlot() + 35);
        InventoryUtility.filterSlots((class_1703)playerHandler, slot -> !slot.method_7677().method_7960(), true).forEach(validSlot -> {
            if (!this.canMove(this.settings.getDelay().longValue()) || InventoryUtility.isGoodItem(validSlot.method_7677())) {
                return;
            }
            if (validSlot.method_7677().method_7909().method_57347().method_58694(class_9334.field_54196) != null) {
                return;
            }
            if (this.settings.getSlots().getProperty("Sword").getValue().booleanValue()) {
                this.arrangeBestSword(screenHandler, preferredSwordSlot, bestSword);
            }
            if (this.settings.getSlots().getProperty("Pickaxe").getValue().booleanValue()) {
                this.arrangeBestPickaxe(screenHandler, preferredPickaxeSlot, bestPickaxe);
            }
            if (this.settings.getSlots().getProperty("Axe").getValue().booleanValue()) {
                this.arrangeBestAxe(screenHandler, preferredAxeSlot, bestAxe);
            }
            if (this.settings.getSlots().getProperty("Blocks").getValue().booleanValue()) {
                this.arrangeMostBlocks(screenHandler, preferredBlockSlot, mostBlocks);
            }
            if (validSlot.method_34266() == preferredSwordSlot.method_34266() && validSlot.method_7677().method_31573(class_3489.field_42611)) {
                return;
            }
            if (validSlot.method_34266() == preferredPickaxeSlot.method_34266() && validSlot.method_7677().method_31573(class_3489.field_42614)) {
                return;
            }
            if (validSlot.method_34266() == preferredAxeSlot.method_34266() && validSlot.method_7677().method_7909() instanceof class_1743) {
                return;
            }
            if (validSlot.method_7677().method_7909() instanceof class_1755) {
                return;
            }
            if (validSlot.method_7677().method_7964().method_10866().method_10967() || blitz && validSlot.method_7677().method_7909() != class_1802.field_8137) {
                InventoryUtility.drop((class_1703)playerHandler, validSlot.field_7874);
                this.stopwatch.reset();
            }
        });
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        class_2653 slotUpdate;
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2653 && (slotUpdate = (class_2653)class_25962).method_11449().method_7909() != class_1802.field_8162 && Constants.mc.field_1724 != null && slotUpdate.method_11452() == Constants.mc.field_1724.field_7498.field_7763) {
            this.stopwatch.reset();
        }
    }

    private void arrangeBestSword(class_1703 screenHandler, class_1735 preferredSwordSlot, class_1735 bestSwordSlot) {
        double preferredSwordValue;
        double bestSwordValue;
        if (bestSwordSlot != null && bestSwordSlot.method_34266() != preferredSwordSlot.method_34266() && (bestSwordValue = InventoryUtility.getSwordValue(bestSwordSlot.method_7677())) > (preferredSwordValue = InventoryUtility.getSwordValue(preferredSwordSlot.method_7677()))) {
            InventoryUtility.swap(screenHandler, bestSwordSlot.field_7874, preferredSwordSlot.field_7874 - 36);
            this.stopwatch.reset();
        }
    }

    private class_1735 getBestSword(class_1703 screenHandler) {
        return InventoryUtility.filterSlots(screenHandler, slot -> slot.method_7677().method_31573(class_3489.field_42611), false).stream().max(Comparator.comparing(swordSlot -> InventoryUtility.getSwordValue(swordSlot.method_7677()))).orElse(null);
    }

    private void arrangeBestPickaxe(class_1703 screenHandler, class_1735 preferredPickaxeSlot, class_1735 bestPickaxeSlot) {
        double preferredPickaxeValue;
        double bestPickaxeValue;
        if (bestPickaxeSlot != null && bestPickaxeSlot.method_34266() != preferredPickaxeSlot.method_34266() && (bestPickaxeValue = InventoryUtility.getToolValue(bestPickaxeSlot.method_7677())) > (preferredPickaxeValue = InventoryUtility.getToolValue(preferredPickaxeSlot.method_7677()))) {
            InventoryUtility.swap(screenHandler, bestPickaxeSlot.field_7874, preferredPickaxeSlot.field_7874 - 36);
            this.stopwatch.reset();
        }
    }

    private class_1735 getBestPickaxe(class_1703 screenHandler) {
        return InventoryUtility.filterSlots(screenHandler, slot -> slot.method_7677().method_31573(class_3489.field_42614), false).stream().max(Comparator.comparing(pickaxeSlot -> InventoryUtility.getToolValue(pickaxeSlot.method_7677()))).orElse(null);
    }

    private void arrangeBestAxe(class_1703 screenHandler, class_1735 preferredAxeSlot, class_1735 bestAxeSlot) {
        double preferredAxeValue;
        double bestAxeValue;
        if (bestAxeSlot != null && bestAxeSlot.method_34266() != preferredAxeSlot.method_34266() && (bestAxeValue = InventoryUtility.getToolValue(bestAxeSlot.method_7677())) > (preferredAxeValue = InventoryUtility.getToolValue(preferredAxeSlot.method_7677()))) {
            InventoryUtility.swap(screenHandler, bestAxeSlot.field_7874, preferredAxeSlot.field_7874 - 36);
            this.stopwatch.reset();
        }
    }

    private class_1735 getBestAxe(class_1703 screenHandler) {
        return InventoryUtility.filterSlots(screenHandler, slot -> slot.method_7677().method_7909() instanceof class_1743, false).stream().max(Comparator.comparing(axeSlot -> InventoryUtility.getToolValue(axeSlot.method_7677()))).orElse(null);
    }

    private class_1735 getMostBlocks(class_1703 screenHandler) {
        return InventoryUtility.filterSlots(screenHandler, slot -> {
            class_1792 patt0$temp = slot.method_7677().method_7909();
            if (!(patt0$temp instanceof class_1747)) return false;
            class_1747 blockItem = (class_1747)patt0$temp;
            if (slot.method_7677().method_7947() <= 0) return false;
            if (!InventoryUtility.isGoodBlock(blockItem.method_7711())) return false;
            return true;
        }, false).stream().max(Comparator.comparing(blockSlot -> blockSlot.method_7677().method_7947())).orElse(null);
    }

    private void arrangeMostBlocks(class_1703 screenHandler, class_1735 preferredBlockSlot, class_1735 mostBlockSlot) {
        double preferredBlockValue;
        double mostBlockCount;
        if (mostBlockSlot != null && mostBlockSlot.method_34266() != preferredBlockSlot.method_34266() && (mostBlockCount = (double)mostBlockSlot.method_7677().method_7947()) > (preferredBlockValue = (double)preferredBlockSlot.method_7677().method_7947())) {
            InventoryUtility.swap(screenHandler, mostBlockSlot.field_7874, preferredBlockSlot.field_7874 - 36);
            this.stopwatch.reset();
        }
    }

    public boolean canMove(long delay) {
        if (delay == 0L) {
            return true;
        }
        return this.stopwatch.hasTimeElapsed(delay);
    }
}

