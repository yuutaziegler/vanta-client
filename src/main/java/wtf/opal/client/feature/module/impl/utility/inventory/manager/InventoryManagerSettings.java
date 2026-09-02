/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.utility.inventory.manager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.impl.utility.inventory.manager.InventoryManagerModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.BoundedNumberProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;

@Environment(value=EnvType.CLIENT)
public final class InventoryManagerSettings {
    private final BoundedNumberProperty delay = new BoundedNumberProperty("Delay", 50.0, 100.0, 0.0, 400.0, 5.0);
    private final MultipleBooleanProperty slots = new MultipleBooleanProperty("Slots", new BooleanProperty("Sword", true), new BooleanProperty("Pickaxe", true), new BooleanProperty("Axe", true), new BooleanProperty("Blocks", true));
    private final NumberProperty swordSlot = (NumberProperty)new NumberProperty("Sword Slot", 1.0, 1.0, 9.0, 1.0).hideIf(() -> this.slots.getProperty("Sword").getValue() == false);
    private final NumberProperty pickaxeSlot = (NumberProperty)new NumberProperty("Pickaxe Slot", 2.0, 1.0, 9.0, 1.0).hideIf(() -> this.slots.getProperty("Pickaxe").getValue() == false);
    private final NumberProperty axeSlot = (NumberProperty)new NumberProperty("Axe Slot", 3.0, 1.0, 9.0, 1.0).hideIf(() -> this.slots.getProperty("Axe").getValue() == false);
    private final NumberProperty blockSlot = (NumberProperty)new NumberProperty("Block Slot", 4.0, 1.0, 9.0, 1.0).hideIf(() -> this.slots.getProperty("Blocks").getValue() == false);

    public InventoryManagerSettings(InventoryManagerModule module) {
        module.addProperties(this.delay, new GroupProperty("Slots", this.slots, this.swordSlot, this.pickaxeSlot, this.axeSlot, this.blockSlot));
    }

    public Double getDelay() {
        return this.delay.getRandomValue();
    }

    public MultipleBooleanProperty getSlots() {
        return this.slots;
    }

    public int getSwordSlot() {
        return ((Double)this.swordSlot.getValue()).intValue();
    }

    public int getPickaxeSlot() {
        return ((Double)this.pickaxeSlot.getValue()).intValue();
    }

    public int getAxeSlot() {
        return ((Double)this.axeSlot.getValue()).intValue();
    }

    public int getBlockSlot() {
        return ((Double)this.blockSlot.getValue()).intValue();
    }
}

