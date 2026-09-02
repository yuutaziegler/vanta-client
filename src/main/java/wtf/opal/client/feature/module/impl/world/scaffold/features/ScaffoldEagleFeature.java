/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_310
 *  net.minecraft.class_746
 */
package wtf.opal.client.feature.module.impl.world.scaffold.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_746;

@Environment(value=EnvType.CLIENT)
public class ScaffoldEagleFeature {
    private int placedBlocks = 0;
    private int blocksToEagleMin = 0;
    private int blocksToEagleMax = 0;
    private float edgeDistanceMin = 0.01f;
    private float edgeDistanceMax = 0.05f;
    private boolean onlyOnGround = true;
    private boolean enabled = false;

    public boolean shouldEagle(class_243 inputDirection) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null || !this.enabled) {
            return false;
        }
        if (!player.method_24828() && this.onlyOnGround) {
            return false;
        }
        boolean shouldBeActive = !player.method_31549().field_7479 && this.placedBlocks == 0;
        return shouldBeActive && this.isCloseToEdge(player, this.edgeDistanceMax);
    }

    public void onBlockPlacement() {
        if (!this.enabled) {
            return;
        }
        ++this.placedBlocks;
        if (this.placedBlocks > this.blocksToEagleMax) {
            this.placedBlocks = 0;
        }
    }

    private boolean isCloseToEdge(class_746 player, double distance) {
        class_310 mc = class_310.method_1551();
        class_238 box = player.method_5829();
        class_238 shrunkBox = box.method_1011(distance);
        return !mc.field_1687.method_18026(shrunkBox.method_989(0.0, -0.5, 0.0));
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setBlocksToEagle(int min, int max) {
        this.blocksToEagleMin = min;
        this.blocksToEagleMax = max;
    }

    public void setEdgeDistance(float min, float max) {
        this.edgeDistanceMin = min;
        this.edgeDistanceMax = max;
    }

    public void setOnlyOnGround(boolean onlyOnGround) {
        this.onlyOnGround = onlyOnGround;
    }
}

