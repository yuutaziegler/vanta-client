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
public final class BlockBreakHardnessEvent {
    private final class_2680 blockState;
    private float hardness;

    public BlockBreakHardnessEvent(class_2680 blockState, float hardness) {
        this.blockState = blockState;
        this.hardness = hardness;
    }

    public class_2680 getBlockState() {
        return this.blockState;
    }

    public float getHardness() {
        return this.hardness;
    }

    public void setHardness(float hardness) {
        this.hardness = hardness;
    }
}

