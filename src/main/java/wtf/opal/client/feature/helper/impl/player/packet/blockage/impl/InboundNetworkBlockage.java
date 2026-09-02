/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2535
 *  net.minecraft.class_2596
 *  net.minecraft.class_2616
 *  net.minecraft.class_2663
 *  net.minecraft.class_2739
 *  net.minecraft.class_2744
 *  net.minecraft.class_2767
 *  net.minecraft.class_2770
 *  net.minecraft.class_310
 *  net.minecraft.class_5888
 *  net.minecraft.class_5903
 *  net.minecraft.class_5904
 *  net.minecraft.class_5905
 *  net.minecraft.class_634
 *  net.minecraft.class_7438
 *  net.minecraft.class_746
 *  net.minecraft.class_7597
 */
package wtf.opal.client.feature.helper.impl.player.packet.blockage.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2535;
import net.minecraft.class_2596;
import net.minecraft.class_2616;
import net.minecraft.class_2663;
import net.minecraft.class_2739;
import net.minecraft.class_2744;
import net.minecraft.class_2767;
import net.minecraft.class_2770;
import net.minecraft.class_310;
import net.minecraft.class_5888;
import net.minecraft.class_5903;
import net.minecraft.class_5904;
import net.minecraft.class_5905;
import net.minecraft.class_634;
import net.minecraft.class_7438;
import net.minecraft.class_746;
import net.minecraft.class_7597;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.DirectionalNetworkBlockage;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.PacketValidator;
import wtf.opal.duck.ClientConnectionAccess;

@Environment(value=EnvType.CLIENT)
public final class InboundNetworkBlockage
extends DirectionalNetworkBlockage<class_634> {
    private static final InboundNetworkBlockage instance = new InboundNetworkBlockage();
    public static final PacketValidator VISUAL_VALIDATOR = p -> {
        if (p instanceof class_2663) {
            class_2663 status = (class_2663)p;
            return status.method_11470() != 2 && status.method_11470() != 3;
        }
        if (p instanceof class_2739) {
            class_2739 tracker = (class_2739)p;
            class_746 clientPlayer = class_310.method_1551().field_1724;
            return clientPlayer == null || tracker.comp_1127() == clientPlayer.method_5628();
        }
        return !(p instanceof class_2616) && !(p instanceof class_5904) && !(p instanceof class_5905) && !(p instanceof class_5888) && !(p instanceof class_2767) && !(p instanceof class_2770) && !(p instanceof class_7438) && !(p instanceof class_7597) && !(p instanceof class_2744) && !(p instanceof class_5903);
    };

    public static InboundNetworkBlockage get() {
        return instance;
    }

    @Override
    protected void flushPacket(class_2535 connection, class_2596<?> packet) {
        ClientConnectionAccess access = (ClientConnectionAccess)connection;
        class_310.method_1551().method_63588(() -> access.opal$channelReadSilent(packet));
    }
}

