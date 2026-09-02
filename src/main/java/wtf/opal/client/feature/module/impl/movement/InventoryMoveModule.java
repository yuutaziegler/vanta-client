/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_408
 */
package wtf.opal.client.feature.module.impl.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_408;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.screen.click.dropdown.DropdownClickGUI;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class InventoryMoveModule
extends Module {
    public InventoryMoveModule() {
        super("Inventory Move", "Allows you to move while in inventories.", ModuleCategory.MOVEMENT);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        if (this.isBlocked()) {
            return;
        }
        PlayerUtility.updateMovementKeyStates();
    }

    public boolean isBlocked() {
        return Constants.mc.field_1755 instanceof class_408 || Constants.mc.field_1755 instanceof DropdownClickGUI;
    }
}

