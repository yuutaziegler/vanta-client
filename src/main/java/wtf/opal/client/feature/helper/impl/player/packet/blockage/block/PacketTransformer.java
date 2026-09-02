/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2596
 */
package wtf.opal.client.feature.helper.impl.player.packet.blockage.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;

@Environment(value=EnvType.CLIENT)
public interface PacketTransformer {
    public class_2596<?> transform(class_2596<?> var1);
}

