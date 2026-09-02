/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.misc;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class Multithreading {
    private static final ScheduledExecutorService SCHEDULE_POOL = Executors.newScheduledThreadPool(4);
    private static final ExecutorService CACHED_POOL = Executors.newCachedThreadPool();

    private Multithreading() {
    }

    public static ScheduledFuture<?> schedule(Runnable r, long delay, TimeUnit unit) {
        return SCHEDULE_POOL.schedule(r, delay, unit);
    }

    public static ScheduledFuture<?> schedulePeriodic(Runnable r, long initialDelay, long delay, TimeUnit unit) {
        return SCHEDULE_POOL.scheduleAtFixedRate(r, initialDelay, delay, unit);
    }

    public static void runAsync(Runnable runnable) {
        CACHED_POOL.execute(runnable);
    }

    public static int getTotal() {
        return ((ThreadPoolExecutor)CACHED_POOL).getActiveCount();
    }
}

