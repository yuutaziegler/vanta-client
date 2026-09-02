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
public final class InstantRotationModel
implements IRotationModel {
    public static final InstantRotationModel INSTANCE = new InstantRotationModel();

    private InstantRotationModel() {
    }

    @Override
    public class_241 tick(class_241 from, class_241 to, float timeDelta) {
        float deltaYaw = class_3532.method_15393((float)(to.field_1343 - from.field_1343)) * timeDelta;
        float deltaPitch = (to.field_1342 - from.field_1342) * timeDelta;
        return new class_241(from.field_1343 + deltaYaw, from.field_1342 + deltaPitch);
    }

    @Override
    public EnumRotationModel getEnum() {
        return EnumRotationModel.INSTANT;
    }
}

