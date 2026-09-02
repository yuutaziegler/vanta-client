/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.render.animation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class Animation {
    private final Easing easing;
    private long duration;
    private long millis;
    private long startTime;
    private float startValue;
    private float destinationValue;
    private float value;
    private boolean finished;

    public Animation(Easing easing, long duration) {
        this.easing = easing;
        this.startTime = System.currentTimeMillis();
        this.duration = duration;
    }

    public void run(float destinationValue) {
        this.millis = System.currentTimeMillis();
        if (this.destinationValue != destinationValue) {
            this.destinationValue = destinationValue;
            this.reset();
        } else {
            boolean bl = this.finished = this.millis - this.duration > this.startTime || this.value == destinationValue;
            if (this.finished) {
                this.value = destinationValue;
                return;
            }
        }
        float result = this.easing.getFunction().apply(Float.valueOf(this.getProgress())).floatValue();
        this.value = this.duration == 0L ? destinationValue : (this.value > destinationValue ? this.startValue - (this.startValue - destinationValue) * result : this.startValue + (destinationValue - this.startValue) * result);
        if (Float.isNaN(this.value) || !Float.isFinite(this.value)) {
            this.value = destinationValue;
        }
    }

    public float getProgress() {
        return (float)(System.currentTimeMillis() - this.startTime) / (float)this.duration;
    }

    public void reset() {
        this.startTime = System.currentTimeMillis();
        this.startValue = this.value;
        this.finished = false;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }

    public long getMillis() {
        return this.millis;
    }

    public float getValue() {
        return this.value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setStartValue(float startValue) {
        this.startValue = startValue;
        this.value = startValue;
    }

    public float getStartValue() {
        return this.startValue;
    }

    public boolean isFinished() {
        return this.finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}

