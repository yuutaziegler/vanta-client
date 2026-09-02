/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.misc.time;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class Stopwatch {
    private long lastMs;

    public Stopwatch(long lastMs) {
        this.lastMs = lastMs;
    }

    public Stopwatch() {
        this.reset();
    }

    public void reset() {
        this.lastMs = System.currentTimeMillis();
    }

    public boolean hasTimeElapsed(long time, boolean reset) {
        if (this.getTime() > time) {
            if (reset) {
                this.reset();
            }
            return true;
        }
        return false;
    }

    public boolean hasTimeElapsed(long time) {
        return this.hasTimeElapsed(time, false);
    }

    public long getTime() {
        return System.currentTimeMillis() - this.lastMs;
    }

    public void setTime(long time) {
        this.lastMs = time;
    }

    public long remainingUntil(long time) {
        long rem = time - this.getTime();
        return Math.max(0L, rem);
    }

    public boolean isWithin(long time, long lookaheadMs) {
        long threshold;
        if (time <= 0L) {
            return false;
        }
        long now = this.getTime();
        return now >= (threshold = Math.max(0L, time - Math.max(0L, lookaheadMs)));
    }
}

