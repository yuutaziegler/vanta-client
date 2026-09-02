/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_241
 *  net.minecraft.class_3532
 */
package wtf.opal.client.feature.helper.impl.player.rotation.model.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_241;
import net.minecraft.class_3532;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.rotation.model.EnumRotationModel;
import wtf.opal.client.feature.helper.impl.player.rotation.model.IRotationModel;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public class HypixelRotationModel
implements IRotationModel {
    @Override
    public class_241 tick(class_241 from, class_241 to, float timeDelta) {
        float deltaPitch;
        float deltaYaw = class_3532.method_15393((float)(to.field_1343 - from.field_1343)) * timeDelta;
        double distance = Math.sqrt(deltaYaw * deltaYaw + (deltaPitch = (to.field_1342 - from.field_1342) * timeDelta) * deltaPitch);
        if (distance == 0.0) {
            return new class_241(from.field_1343 + deltaYaw, from.field_1342 + deltaPitch);
        }
        double distributionYaw = Math.abs((double)deltaYaw / distance);
        double distributionPitch = Math.abs((double)deltaPitch / distance);
        double maxYaw = (double)this.getSpeed() * distributionYaw;
        double maxPitch = (double)this.getSpeed() * distributionPitch;
        float moveYaw = (float)Math.max(Math.min((double)deltaYaw, maxYaw), -maxYaw);
        float movePitch = (float)Math.max(Math.min((double)deltaPitch, maxPitch), -maxPitch);
        return new class_241(from.field_1343 + moveYaw, from.field_1342 + movePitch);
    }

    private float getSpeed() {
        return this.isYawDiagonal() ? (LocalDataWatch.get().airTicks == 1 ? 65.0f : 36.0f) : 35.0f;
    }

    private boolean isYawDiagonal() {
        float direction = Math.abs(MoveUtility.getDirectionDegrees() % 90.0f);
        int range = 30;
        return direction > 15.0f && direction < 75.0f;
    }

    @Override
    public EnumRotationModel getEnum() {
        return null;
    }
}

