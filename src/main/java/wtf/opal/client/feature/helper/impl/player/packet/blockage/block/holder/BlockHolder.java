/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.DirectionalNetworkBlockage;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.NetworkBlock;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.PacketTransformer;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.PacketValidator;

@Environment(value=EnvType.CLIENT)
public final class BlockHolder {
    private final DirectionalNetworkBlockage<?> networkBlockage;
    private final boolean priority;
    private NetworkBlock networkBlock;

    public BlockHolder(DirectionalNetworkBlockage<?> networkBlockage, boolean priority) {
        this.networkBlockage = networkBlockage;
        this.priority = priority;
    }

    public BlockHolder(DirectionalNetworkBlockage<?> networkBlockage) {
        this(networkBlockage, false);
    }

    public void block(PacketTransformer packetTransformer, PacketValidator packetValidator) {
        if (this.networkBlock == null) {
            this.networkBlock = this.networkBlockage.newBlockage(packetTransformer, packetValidator, this.priority);
        }
    }

    public void block(PacketTransformer packetTransformer) {
        this.block(packetTransformer, null);
    }

    public void block() {
        this.block(null);
    }

    public void setPacketTransformer(PacketTransformer packetTransformer) {
        if (this.networkBlock != null) {
            this.networkBlock.setPacketTransformer(packetTransformer);
        }
    }

    public void release() {
        if (this.networkBlock != null) {
            this.networkBlockage.releaseBlockage(this.networkBlock);
            this.networkBlock = null;
        }
    }

    public void flush() {
        this.release();
        this.block();
    }

    public boolean isBlocking() {
        return this.networkBlock != null;
    }
}

