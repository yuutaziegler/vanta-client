/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.event;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.event.registry.EventRegistry;

@Environment(value=EnvType.CLIENT)
public final class EventDispatcher {
    private static final EventRegistry eventRegistry = new EventRegistry();

    private EventDispatcher() {
    }

    public static void subscribe(Object subscriber) {
        eventRegistry.subscribe(subscriber);
    }

    public static void dispatch(Object event) {
        eventRegistry.dispatch(event);
    }
}

