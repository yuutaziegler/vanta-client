/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2338
 *  net.minecraft.class_310
 *  net.minecraft.class_746
 */
package wtf.opal.client.feature.module.impl.world.scaffold.tower;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_310;
import net.minecraft.class_746;

@Environment(value=EnvType.CLIENT)
public class ScaffoldTowerMotion {
    private float motion = 0.42f;
    private float triggerHeight = 0.78f;
    private float slow = 1.0f;
    private double jumpOffPosition = Double.NaN;

    public void onPlayerJump() {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player != null) {
            this.jumpOffPosition = player.method_23318();
        }
    }

    public void onTick(boolean jumpKeyDown, int blockCount, boolean isBlockBelow) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            return;
        }
        if (!jumpKeyDown || blockCount <= 0 || !isBlockBelow) {
            this.jumpOffPosition = Double.NaN;
            return;
        }
        if (Double.isNaN(this.jumpOffPosition)) {
            return;
        }
        if (player.method_23318() > this.jumpOffPosition + (double)this.triggerHeight) {
            player.method_5814(player.method_23317(), Math.floor(player.method_23318()), player.method_23321());
            player.method_18800(player.method_18798().field_1352 * (double)this.slow, (double)this.motion, player.method_18798().field_1350 * (double)this.slow);
            this.jumpOffPosition = player.method_23318();
        }
    }

    public class_2338 getTargetedPosition(class_2338 blockPos) {
        return blockPos.method_10074();
    }

    public void setMotion(float motion) {
        this.motion = motion;
    }

    public void setTriggerHeight(float triggerHeight) {
        this.triggerHeight = triggerHeight;
    }

    public void setSlow(float slow) {
        this.slow = slow;
    }
}

