/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2338
 */
package wtf.opal.client.feature.module.impl.world.scaffold.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;

@Environment(value=EnvType.CLIENT)
public class SupportReference {
    private final class_2338 blockPos;
    private final double offsetX;
    private final double offsetZ;

    public SupportReference(class_2338 blockPos, double offsetX, double offsetZ) {
        this.blockPos = blockPos;
        this.offsetX = offsetX;
        this.offsetZ = offsetZ;
    }

    public class_2338 getBlockPos() {
        return this.blockPos;
    }

    public double getOffsetX() {
        return this.offsetX;
    }

    public double getOffsetZ() {
        return this.offsetZ;
    }
}

