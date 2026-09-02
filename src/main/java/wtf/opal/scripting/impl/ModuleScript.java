/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.graalvm.polyglot.Value
 */
package wtf.opal.scripting.impl;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.graalvm.polyglot.Value;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.packet.InstantaneousReceivePacketEvent;
import wtf.opal.event.impl.game.packet.InstantaneousSendPacketEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.packet.SendPacketEvent;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.game.player.movement.PostMovementPacketEvent;
import wtf.opal.event.impl.game.player.movement.PreMoveEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.impl.render.RenderScreenEvent;
import wtf.opal.event.subscriber.IEventSubscriber;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public class ModuleScript
implements IEventSubscriber {
    private String name;
    private String description;
    private boolean enabled;
    private final Map<String, Value> handlerMap = new HashMap<String, Value>();

    public ModuleScript(String name, String description) {
        this.name = name;
        this.description = description;
        EventDispatcher.subscribe(this);
    }

    public final void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) {
            this.onEnable();
        } else {
            this.onDisable();
        }
    }

    public final void on(String event, Value handler) {
        this.handlerMap.put(event, handler);
    }

    private void executeCallback(String event, Object ... args) {
        Value handler = this.handlerMap.get(event);
        if (handler != null && handler.canExecute()) {
            handler.execute(args);
        }
    }

    @Override
    public final boolean isHandlingEvents() {
        return this.enabled;
    }

    private void onEnable() {
        this.executeCallback("enable", new Object[0]);
    }

    private void onDisable() {
        this.executeCallback("disable", new Object[0]);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        this.executeCallback("preGameTick", event);
    }

    @Subscribe
    public void onRenderScreen(RenderScreenEvent event) {
        this.executeCallback("renderScreen", event);
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        this.executeCallback("preMovementPacket", event);
    }

    @Subscribe
    public void onPostMovementPacket(PostMovementPacketEvent event) {
        this.executeCallback("postMovementPacket", event);
    }

    @Subscribe
    public void onPreMove(PreMoveEvent event) {
        this.executeCallback("preMove", event);
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        this.executeCallback("postMove", event);
    }

    @Subscribe
    public void onSendPacket(SendPacketEvent event) {
        this.executeCallback("sendPacket", event);
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        this.executeCallback("receivePacket", event);
    }

    @Subscribe
    public void onInstantaneousSendPacket(InstantaneousSendPacketEvent event) {
        this.executeCallback("instantaneousSendPacket", event);
    }

    @Subscribe
    public void onInstantaneousReceivePacket(InstantaneousReceivePacketEvent event) {
        this.executeCallback("instantaneousReceivePacket", event);
    }
}

