/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2680
 */
package wtf.opal.event.impl.game.player.interaction.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2680;

@Environment(value=EnvType.CLIENT)
public final class BlockBreakCanHarvestEvent {
    private final class_2680 blockState;
    private boolean canHarvest;

    public BlockBreakCanHarvestEvent(class_2680 blockState, boolean canHarvest) {
        this.blockState = blockState;
        this.canHarvest = canHarvest;
    }

    public class_2680 getBlockState() {
        return this.blockState;
    }

    public boolean isCanHarvest() {
        return this.canHarvest;
    }

    public void setCanHarvest(boolean canHarvest) {
        this.canHarvest = canHarvest;
    }
}

