/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.DirectionalNetworkBlockage;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.NetworkBlock;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.PacketValidator;

@Environment(value=EnvType.CLIENT)
public final class TickingBlockHolder {
    private final DirectionalNetworkBlockage<?> networkBlockage;
    @Nullable
    private final PacketValidator packetValidator;
    private final List<NetworkBlock> networkBlockList = new ArrayList<NetworkBlock>();

    public TickingBlockHolder(DirectionalNetworkBlockage<?> networkBlockage, @Nullable PacketValidator packetValidator) {
        this.networkBlockage = networkBlockage;
        this.packetValidator = packetValidator;
    }

    public TickingBlockHolder(DirectionalNetworkBlockage<?> networkBlockage) {
        this(networkBlockage, null);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void tick() {
        List<NetworkBlock> list = this.networkBlockList;
        synchronized (list) {
            this.networkBlockList.add(this.networkBlockage.newBlockage(null, this.packetValidator));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void release(int count) {
        List<NetworkBlock> list = this.networkBlockList;
        synchronized (list) {
            while (!this.networkBlockList.isEmpty() && count > 0) {
                NetworkBlock block = this.networkBlockList.removeFirst();
                this.networkBlockage.releaseBlockage(block);
                --count;
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void release() {
        List<NetworkBlock> list = this.networkBlockList;
        synchronized (list) {
            while (!this.networkBlockList.isEmpty()) {
                NetworkBlock block = this.networkBlockList.removeFirst();
                this.networkBlockage.releaseBlockage(block);
            }
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isBlocking() {
        List<NetworkBlock> list = this.networkBlockList;
        synchronized (list) {
            return !this.networkBlockList.isEmpty();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public int getTickCount() {
        List<NetworkBlock> list = this.networkBlockList;
        synchronized (list) {
            return this.networkBlockList.size();
        }
    }

    public List<NetworkBlock> getNetworkBlockList() {
        return this.networkBlockList;
    }
}

