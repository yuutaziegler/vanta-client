/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1263
 *  net.minecraft.class_1657
 *  net.minecraft.class_1707
 *  net.minecraft.class_1713
 *  net.minecraft.class_1735
 *  net.minecraft.class_1792
 *  net.minecraft.class_1802
 *  net.minecraft.class_437
 *  net.minecraft.class_476
 */
package wtf.opal.client.feature.module.impl.utility;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1263;
import net.minecraft.class_1657;
import net.minecraft.class_1707;
import net.minecraft.class_1713;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1802;
import net.minecraft.class_437;
import net.minecraft.class_476;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class AutoChestModule
extends Module {
    private static final Set<class_1792> RESOURCES = Set.of(class_1802.field_8620, class_1802.field_8695, class_1802.field_8477, class_1802.field_8687);
    private final NumberProperty ticks = new NumberProperty("Ticks", 1.0, 0.0, 10.0, 1.0);
    private final BooleanProperty autoDeposit = new BooleanProperty("Auto deposit", true);
    private ChestInteractionMode mode = ChestInteractionMode.NONE;
    private boolean hasSeenChest;
    private int tickCount;

    public AutoChestModule() {
        super("Auto Chest", "Dumps and retrieves resources in chests.", ModuleCategory.UTILITY);
        this.addProperties(this.ticks, this.autoDeposit);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        class_476 container;
        class_437 class_4372 = Constants.mc.field_1755;
        if (!(class_4372 instanceof class_476) || !(container = (class_476)class_4372).method_25440().getString().contains("Chest")) {
            this.resetState();
            return;
        }
        if (!this.hasSeenChest && this.autoDeposit.getValue().booleanValue()) {
            this.mode = ChestInteractionMode.DEPOSIT;
        }
        this.hasSeenChest = true;
        ++this.tickCount;
        if (this.tickCount - 1 < ((Double)this.ticks.getValue()).intValue()) {
            return;
        }
        this.tickCount = 0;
        if (PlayerUtility.isKeyPressed(45)) {
            this.mode = ChestInteractionMode.WITHDRAW;
        } else if (PlayerUtility.isKeyPressed(61)) {
            this.mode = ChestInteractionMode.DEPOSIT;
        }
        class_1707 screenHandler = (class_1707)container.method_17577();
        switch (this.mode.ordinal()) {
            case 0: {
                if (this.handleDeposit(screenHandler)) {
                    return;
                }
                this.mode = ChestInteractionMode.NONE;
                break;
            }
            case 1: {
                if (this.handleWithdraw(screenHandler)) {
                    return;
                }
                this.mode = ChestInteractionMode.NONE;
            }
        }
    }

    private boolean handleDeposit(class_1707 screenHandler) {
        int chestSlotCount;
        for (int i = chestSlotCount = screenHandler.method_7629().method_5439(); i < screenHandler.field_7761.size(); ++i) {
            class_1735 slot = (class_1735)screenHandler.field_7761.get(i);
            if (!RESOURCES.contains(slot.method_7677().method_7909())) continue;
            Constants.mc.field_1761.method_2906(screenHandler.field_7763, i, 0, class_1713.field_7794, (class_1657)Constants.mc.field_1724);
            return true;
        }
        return false;
    }

    private boolean handleWithdraw(class_1707 screenHandler) {
        class_1263 chestInventory = screenHandler.method_7629();
        for (int i = 0; i < chestInventory.method_5439(); ++i) {
            if (!RESOURCES.contains(chestInventory.method_5438(i).method_7909())) continue;
            Constants.mc.field_1761.method_2906(screenHandler.field_7763, i, 0, class_1713.field_7794, (class_1657)Constants.mc.field_1724);
            return true;
        }
        return false;
    }

    private void resetState() {
        this.mode = ChestInteractionMode.NONE;
        this.hasSeenChest = false;
        this.tickCount = 0;
    }

    @Environment(value=EnvType.CLIENT)
    private static enum ChestInteractionMode {
        DEPOSIT,
        WITHDRAW,
        NONE;

    }
}

