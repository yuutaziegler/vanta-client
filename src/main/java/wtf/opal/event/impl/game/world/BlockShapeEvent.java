/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2338
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 */
package wtf.opal.event.impl.game.world;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_265;
import net.minecraft.class_2680;

@Environment(value=EnvType.CLIENT)
public final class BlockShapeEvent {
    private final class_2338 blockPos;
    private final class_2680 blockState;
    private class_265 voxelShape;
    private final List<class_265> extraVoxelShapes;

    public BlockShapeEvent(class_2338 blockPos, class_2680 blockState, class_265 voxelShape, List<class_265> extraVoxelShapes) {
        this.blockPos = blockPos;
        this.blockState = blockState;
        this.voxelShape = voxelShape;
        this.extraVoxelShapes = extraVoxelShapes;
    }

    public class_2338 getBlockPos() {
        return this.blockPos;
    }

    public class_2680 getBlockState() {
        return this.blockState;
    }

    public class_265 getVoxelShape() {
        return this.voxelShape;
    }

    public List<class_265> getExtraVoxelShapes() {
        return this.extraVoxelShapes;
    }

    public void setVoxelShape(class_265 voxelShape) {
        this.voxelShape = voxelShape;
    }
}

