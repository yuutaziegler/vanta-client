/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.event.registry;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.event.listener.ListenerMethod;
import wtf.opal.event.subscriber.IEventSubscriber;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class EventRegistry {
    private final Map<Class<?>, List<ListenerMethod>> subscriberMap = new HashMap();
    private static final MethodHandles.Lookup LOOKUP = MethodHandles.lookup();

    private void subscribe(Object instance, Class<?> clazzOwner) {
        IEventSubscriber listener = (IEventSubscriber)instance;
        for (Method method : clazzOwner.getDeclaredMethods()) {
            Subscribe subscribe = method.getDeclaredAnnotation(Subscribe.class);
            if (subscribe == null || Modifier.isStatic(method.getModifiers())) continue;
            Class<?> type = method.getParameterTypes()[0];
            MethodType methodType = MethodType.methodType(Void.TYPE, type);
            try {
                MethodHandles.Lookup privateLookup = Modifier.isPrivate(method.getModifiers()) ? MethodHandles.privateLookupIn(clazzOwner, LOOKUP) : LOOKUP;
                MethodHandle methodHandle = privateLookup.findVirtual(clazzOwner, method.getName(), methodType);
                ConstantCallSite site = new ConstantCallSite(methodHandle);
                ListenerMethod listenerMethod = new ListenerMethod(subscribe.priority(), site, listener);
                this.subscriberMap.computeIfAbsent(type, x -> new ArrayList()).add(listenerMethod);
            }
            catch (IllegalAccessException | NoSuchMethodException e) {
                throw new RuntimeException("Error subscribing event: " + method.getName(), e);
            }
        }
    }

    public void subscribe(Object subscriber) {
        this.subscribe(subscriber, subscriber.getClass());
        for (Class<?> parent = subscriber.getClass().getSuperclass(); parent != Object.class; parent = parent.getSuperclass()) {
            this.subscribe(subscriber, parent);
        }
        this.sortSubscribers();
    }

    private void sortSubscribers() {
        for (List<ListenerMethod> callsiteList : this.subscriberMap.values()) {
            callsiteList.sort(Comparator.comparingInt(ListenerMethod::getPriority));
        }
    }

    public void dispatch(Object event) {
        List<ListenerMethod> listenerMethods = this.subscriberMap.get(event.getClass());
        if (listenerMethods != null) {
            ListenerMethod listenerMethod;
            for (int i = 0; i < listenerMethods.size() && !(listenerMethod = listenerMethods.get(i)).invoke(event); ++i) {
            }
        }
    }
}

