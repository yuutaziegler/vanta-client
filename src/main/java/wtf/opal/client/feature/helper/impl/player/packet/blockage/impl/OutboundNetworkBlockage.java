/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2535
 *  net.minecraft.class_2596
 *  net.minecraft.class_3244
 */
package wtf.opal.client.feature.helper.impl.player.packet.blockage.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2535;
import net.minecraft.class_2596;
import net.minecraft.class_3244;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.DirectionalNetworkBlockage;

@Environment(value=EnvType.CLIENT)
public final class OutboundNetworkBlockage
extends DirectionalNetworkBlockage<class_3244> {
    private static final OutboundNetworkBlockage instance = new OutboundNetworkBlockage();

    public static OutboundNetworkBlockage get() {
        return instance;
    }

    public static void sendPacketDirect(class_2596<?> packet) {
        Constants.mc.method_1562().method_48296().method_52906(packet, null, true);
    }

    @Override
    protected void flushPacket(class_2535 connection, class_2596<?> packet) {
        connection.method_10752(packet, null);
    }
}

