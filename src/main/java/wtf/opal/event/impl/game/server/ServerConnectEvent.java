/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_639
 */
package wtf.opal.event.impl.game.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_639;
import wtf.opal.event.EventCancellable;

@Environment(value=EnvType.CLIENT)
public final class ServerConnectEvent
extends EventCancellable {
    private final class_639 serverAddress;

    public ServerConnectEvent(class_639 serverAddress) {
        this.serverAddress = serverAddress;
    }

    public class_639 getServerAddress() {
        return this.serverAddress;
    }
}

