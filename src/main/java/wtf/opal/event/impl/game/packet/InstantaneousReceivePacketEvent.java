/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2596
 */
package wtf.opal.event.impl.game.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;
import wtf.opal.event.EventCancellable;

@Environment(value=EnvType.CLIENT)
public final class InstantaneousReceivePacketEvent
extends EventCancellable {
    private final class_2596<?> packet;

    public InstantaneousReceivePacketEvent(class_2596<?> packet) {
        this.packet = packet;
    }

    public class_2596<?> getPacket() {
        return this.packet;
    }
}

