/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2596
 *  net.minecraft.class_6374
 */
package wtf.opal.client.feature.helper.impl.player.hypixel;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;
import net.minecraft.class_6374;
import wtf.opal.client.feature.helper.IHelper;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.JoinWorldEvent;
import wtf.opal.event.impl.game.packet.SendPacketEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class TransactionStreamValidator
implements IHelper {
    private Integer lastTransactionId;
    private static TransactionStreamValidator instance;

    @Subscribe
    public void onSendPacket(SendPacketEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_6374) {
            class_6374 packet = (class_6374)class_25962;
            if (packet.method_36960() == 0) {
                return;
            }
            if (this.lastTransactionId != null && packet.method_36960() != this.lastTransactionId - 1) {
                System.out.println("Invalid transaction id: " + packet.method_36960() + " prev: " + this.lastTransactionId);
            }
            this.lastTransactionId = packet.method_36960();
        }
    }

    @Subscribe
    public void onJoinWorld(JoinWorldEvent event) {
        this.lastTransactionId = null;
    }

    @Override
    public boolean isHandlingEvents() {
        return LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer;
    }

    public static void setInstance() {
        instance = new TransactionStreamValidator();
        EventDispatcher.subscribe(instance);
    }
}

