/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_3965
 */
package wtf.opal.event.impl.game.player.interaction.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3965;

@Environment(value=EnvType.CLIENT)
public final class BlockPlacedEvent {
    private final class_3965 blockHitResult;

    public BlockPlacedEvent(class_3965 blockHitResult) {
        this.blockHitResult = blockHitResult;
    }

    public class_3965 getBlockHitResult() {
        return this.blockHitResult;
    }
}

