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
import wtf.opal.client.feature.helper.impl.player.rotation.model.EnumRotationModel;
import wtf.opal.client.feature.helper.impl.player.rotation.model.IRotationModel;

@Environment(value=EnvType.CLIENT)
public final class LinearRotationModel
implements IRotationModel {
    private final double speed;

    public LinearRotationModel(double speed) {
        this.speed = speed;
    }

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
        double maxYaw = this.speed * distributionYaw;
        double maxPitch = this.speed * distributionPitch;
        float moveYaw = (float)Math.max(Math.min((double)deltaYaw, maxYaw), -maxYaw);
        float movePitch = (float)Math.max(Math.min((double)deltaPitch, maxPitch), -maxPitch);
        return new class_241(from.field_1343 + moveYaw, from.field_1342 + movePitch);
    }

    @Override
    public EnumRotationModel getEnum() {
        return EnumRotationModel.LINEAR;
    }
}

