/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2535
 *  net.minecraft.class_2547
 *  net.minecraft.class_2596
 *  net.minecraft.class_634
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.client.feature.helper.impl.player.packet.blockage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2535;
import net.minecraft.class_2547;
import net.minecraft.class_2596;
import net.minecraft.class_634;
import org.jetbrains.annotations.Nullable;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.BlockedPacket;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.NetworkBlock;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.PacketTransformer;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.PacketValidator;

@Environment(value=EnvType.CLIENT)
public abstract class DirectionalNetworkBlockage<T extends class_2547> {
    private final List<NetworkBlock> blockageList = new ArrayList<NetworkBlock>();
    private final List<BlockedPacket> packetList = new ArrayList<BlockedPacket>();
    private long id;
    protected final Object lock = new Object();

    public NetworkBlock newBlockage() {
        return this.newBlockage(null, null);
    }

    public NetworkBlock newBlockage(PacketTransformer packetTransformer, PacketValidator packetValidator) {
        return this.newBlockage(packetTransformer, packetValidator, false);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public NetworkBlock newBlockage(PacketTransformer packetTransformer, PacketValidator packetValidator, boolean priority) {
        Object object = this.lock;
        synchronized (object) {
            NetworkBlock blockage = new NetworkBlock(packetTransformer, packetValidator, priority, this.getBlockageId());
            this.blockageList.add(blockage);
            return blockage;
        }
    }

    private long getBlockageId() {
        long id = this.id;
        for (NetworkBlock block : this.blockageList) {
            if (!block.isPriority() || id < block.getId()) continue;
            id = block.getId();
        }
        return id;
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void releaseBlockage(NetworkBlock networkBlock) {
        Object object = this.lock;
        synchronized (object) {
            if (this.blockageList.contains(networkBlock)) {
                this.blockageList.remove(networkBlock);
                this.sort();
                this.flush(this.blockageList.isEmpty() ? null : Long.valueOf(this.blockageList.getFirst().getId()), networkBlock.getPacketTransformer());
            }
        }
    }

    private void flush(@Nullable Long id, @Nullable PacketTransformer packetTransformer) {
        class_634 networkHandler = Constants.mc.method_1562();
        class_2535 connection = networkHandler == null ? null : networkHandler.method_48296();
        ArrayList packetsToFlush = new ArrayList();
        Iterator<BlockedPacket> iterator = this.packetList.iterator();
        while (iterator.hasNext()) {
            BlockedPacket blockedPacket = iterator.next();
            if (id != null && blockedPacket.getId() >= id) continue;
            if (connection != null) {
                class_2596<?> packet = blockedPacket.getPacket();
                if (packetTransformer != null) {
                    packet = packetTransformer.transform(packet);
                }
                if (packet != null) {
                    packetsToFlush.add(packet);
                }
            }
            iterator.remove();
        }
        for (class_2596 class_25962 : packetsToFlush) {
            this.flushPacket(connection, class_25962);
        }
    }

    protected abstract void flushPacket(class_2535 var1, class_2596<?> var2);

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isBlocked(class_2596<?> packet) {
        Object object = this.lock;
        synchronized (object) {
            if (!this.blockageList.isEmpty()) {
                this.sort();
                NetworkBlock blockage = this.blockageList.getFirst();
                PacketValidator packetValidator = blockage.getPacketValidator();
                boolean valid = false;
                if (packetValidator == null) {
                    valid = true;
                } else if (packetValidator.isValid(packet)) {
                    valid = true;
                } else {
                    for (NetworkBlock block : this.blockageList) {
                        PacketValidator blockValidator;
                        if (block.equals(blockage) || (blockValidator = block.getPacketValidator()) != null && !blockValidator.isValid(packet)) continue;
                        valid = true;
                        break;
                    }
                }
                if (valid) {
                    this.packetList.add(new BlockedPacket(packet, this.id));
                    ++this.id;
                    return true;
                }
            }
            return false;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    private void sort() {
        Object object = this.lock;
        synchronized (object) {
            this.blockageList.sort(Comparator.comparingLong(NetworkBlock::getId));
            this.packetList.sort(Comparator.comparingLong(BlockedPacket::getId));
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void reset() {
        Object object = this.lock;
        synchronized (object) {
            this.blockageList.clear();
            this.packetList.clear();
            this.id = 0L;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public boolean isAnyBlockages() {
        Object object = this.lock;
        synchronized (object) {
            return !this.blockageList.isEmpty();
        }
    }
}

