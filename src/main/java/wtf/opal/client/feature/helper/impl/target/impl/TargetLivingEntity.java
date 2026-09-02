/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 *  net.minecraft.class_1588
 */
package wtf.opal.client.feature.helper.impl.target.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_1588;
import wtf.opal.client.feature.helper.impl.target.impl.Target;

@Environment(value=EnvType.CLIENT)
public class TargetLivingEntity
extends Target<class_1309> {
    public TargetLivingEntity(class_1309 entity) {
        super(entity);
    }

    @Override
    public boolean isMatchingFlags(int flags) {
        if (this.entity instanceof class_1588) {
            return (flags & 2) != 0;
        }
        return (flags & 4) != 0;
    }

    public float getFullHealth() {
        return this.entity.method_6032() + this.entity.method_6067();
    }
}

