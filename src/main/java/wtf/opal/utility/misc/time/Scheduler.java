/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.misc.time;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.IHelper;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class Scheduler
implements IHelper {
    private static final Map<Runnable, AtomicInteger> TASKS = new ConcurrentHashMap<Runnable, AtomicInteger>();

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        TASKS.forEach((function, remainingTicks) -> {
            if (remainingTicks.getAndDecrement() < 1) {
                TASKS.remove(function);
                function.run();
            }
        });
    }

    public static void addTask(Runnable function, int tickDelay) {
        TASKS.put(function, new AtomicInteger(tickDelay));
    }

    static {
        EventDispatcher.subscribe(new Scheduler());
    }
}

