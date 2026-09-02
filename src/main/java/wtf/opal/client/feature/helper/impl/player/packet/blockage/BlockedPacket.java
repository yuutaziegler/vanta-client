/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2596
 */
package wtf.opal.client.feature.helper.impl.player.packet.blockage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;

@Environment(value=EnvType.CLIENT)
public final class BlockedPacket {
    private final class_2596<?> packet;
    private final long id;

    public BlockedPacket(class_2596<?> packet, long id) {
        this.packet = packet;
        this.id = id;
    }

    public class_2596<?> getPacket() {
        return this.packet;
    }

    public long getId() {
        return this.id;
    }
}

