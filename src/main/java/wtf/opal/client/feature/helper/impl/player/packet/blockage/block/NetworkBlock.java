/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.client.feature.helper.impl.player.packet.blockage.block;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.PacketTransformer;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.PacketValidator;

@Environment(value=EnvType.CLIENT)
public final class NetworkBlock {
    @Nullable
    private PacketTransformer packetTransformer;
    @Nullable
    private final PacketValidator packetValidator;
    private final boolean priority;
    private final long id;
    private final long creationTime = System.currentTimeMillis();

    public NetworkBlock(@Nullable PacketTransformer packetTransformer, @Nullable PacketValidator packetValidator, boolean priority, long id) {
        this.packetTransformer = packetTransformer;
        this.packetValidator = packetValidator;
        this.priority = priority;
        this.id = id;
    }

    @Nullable
    public PacketTransformer getPacketTransformer() {
        return this.packetTransformer;
    }

    public void setPacketTransformer(@Nullable PacketTransformer packetTransformer) {
        this.packetTransformer = packetTransformer;
    }

    @Nullable
    public PacketValidator getPacketValidator() {
        return this.packetValidator;
    }

    public long getId() {
        return this.id;
    }

    public boolean isPriority() {
        return this.priority;
    }

    public boolean equals(Object o) {
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        NetworkBlock block = (NetworkBlock)o;
        return this.id == block.id;
    }

    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    public long getCreationTime() {
        return this.creationTime;
    }
}

