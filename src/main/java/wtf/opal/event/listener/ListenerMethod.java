/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.event.listener;

import java.lang.invoke.CallSite;
import java.lang.invoke.MethodHandle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.event.EventCancellable;
import wtf.opal.event.subscriber.IEventSubscriber;

@Environment(value=EnvType.CLIENT)
public final class ListenerMethod {
    private final int priority;
    private final CallSite callSite;
    private final IEventSubscriber subscriber;
    private final MethodHandle dynamicInvoker;

    public ListenerMethod(int priority, CallSite callSite, IEventSubscriber subscriber) {
        this.priority = -priority;
        this.callSite = callSite;
        this.subscriber = subscriber;
        this.dynamicInvoker = callSite.dynamicInvoker();
    }

    public CallSite getCallSite() {
        return this.callSite;
    }

    public int getPriority() {
        return this.priority;
    }

    public boolean invoke(Object event) {
        if (this.subscriber.isHandlingEvents()) {
            EventCancellable cancellable;
            try {
                this.dynamicInvoker.invoke(this.subscriber, event);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new RuntimeException("Error invoking event", throwable);
            }
            return event instanceof EventCancellable && (cancellable = (EventCancellable)event).isCancelled();
        }
        return false;
    }
}

