/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_3532
 */
package wtf.opal.utility.render.animation;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3532;

@Environment(value=EnvType.CLIENT)
public enum Easing {
    LINEAR(x -> x),
    DECELERATE(x -> Float.valueOf(1.0f - (x.floatValue() - 1.0f) * (x.floatValue() - 1.0f))),
    SMOOTH_STEP(x -> Float.valueOf((float)(-2.0 * Math.pow(x.floatValue(), 3.0) + 3.0 * Math.pow(x.floatValue(), 2.0)))),
    EASE_IN_QUAD(x -> Float.valueOf(x.floatValue() * x.floatValue())),
    EASE_OUT_QUAD(x -> Float.valueOf(x.floatValue() * (2.0f - x.floatValue()))),
    EASE_IN_OUT_QUAD(x -> Float.valueOf((double)x.floatValue() < 0.5 ? 2.0f * x.floatValue() * x.floatValue() : -1.0f + (4.0f - 2.0f * x.floatValue()) * x.floatValue())),
    EASE_IN_CUBIC(x -> Float.valueOf(x.floatValue() * x.floatValue() * x.floatValue())),
    EASE_OUT_CUBIC(x -> {
        x = Float.valueOf(x.floatValue() - 1.0f);
        return Float.valueOf(x.floatValue() * x.floatValue() * x.floatValue() + 1.0f);
    }),
    EASE_IN_OUT_CUBIC(x -> Float.valueOf((double)x.floatValue() < 0.5 ? 4.0f * x.floatValue() * x.floatValue() * x.floatValue() : (x.floatValue() - 1.0f) * (2.0f * x.floatValue() - 2.0f) * (2.0f * x.floatValue() - 2.0f) + 1.0f)),
    EASE_IN_QUART(x -> Float.valueOf(x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue())),
    EASE_OUT_QUART(x -> {
        x = Float.valueOf(x.floatValue() - 1.0f);
        return Float.valueOf(1.0f - x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue());
    }),
    EASE_IN_OUT_QUART(x -> {
        float f;
        if ((double)x.floatValue() < 0.5) {
            f = 8.0f * x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue();
        } else {
            x = Float.valueOf(x.floatValue() - 1.0f);
            f = 1.0f - 8.0f * x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue();
        }
        return Float.valueOf(f);
    }),
    EASE_IN_QUINT(x -> Float.valueOf(x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue())),
    EASE_OUT_QUINT(x -> {
        x = Float.valueOf(x.floatValue() - 1.0f);
        return Float.valueOf(1.0f + x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue());
    }),
    EASE_IN_OUT_QUINT(x -> {
        float f;
        if ((double)x.floatValue() < 0.5) {
            f = 16.0f * x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue();
        } else {
            x = Float.valueOf(x.floatValue() - 1.0f);
            f = 1.0f + 16.0f * x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue() * x.floatValue();
        }
        return Float.valueOf(f);
    }),
    EASE_IN_SINE(x -> Float.valueOf(1.0f - class_3532.method_15362((float)((float)((double)x.floatValue() * Math.PI * 0.5))))),
    EASE_OUT_SINE(x -> Float.valueOf(class_3532.method_15374((float)((float)((double)x.floatValue() * Math.PI * 0.5))))),
    EASE_IN_OUT_SINE(x -> Float.valueOf(1.0f - class_3532.method_15362((float)((float)(Math.PI * (double)x.floatValue() * 0.5))))),
    EASE_IN_EXPO(x -> Float.valueOf(x.floatValue() == 0.0f ? 0.0f : (float)Math.pow(2.0, 10.0f * x.floatValue() - 10.0f))),
    EASE_OUT_EXPO(x -> Float.valueOf(x.floatValue() == 1.0f ? 1.0f : 1.0f - (float)Math.pow(2.0, -10.0f * x.floatValue()))),
    EASE_IN_OUT_EXPO(x -> Float.valueOf(x.floatValue() == 0.0f ? 0.0f : (x.floatValue() == 1.0f ? 1.0f : ((double)x.floatValue() < 0.5 ? (float)Math.pow(2.0, 20.0f * x.floatValue() - 10.0f) * 0.5f : (2.0f - (float)Math.pow(2.0, -20.0f * x.floatValue() + 10.0f)) * 0.5f)))),
    EASE_IN_CIRC(x -> Float.valueOf(1.0f - (float)Math.sqrt(1.0f - x.floatValue() * x.floatValue()))),
    EASE_OUT_CIRC(x -> {
        x = Float.valueOf(x.floatValue() - 1.0f);
        return Float.valueOf((float)Math.sqrt(1.0f - x.floatValue() * x.floatValue()));
    }),
    EASE_IN_OUT_CIRC(x -> Float.valueOf((double)x.floatValue() < 0.5 ? (1.0f - (float)Math.sqrt(1.0f - 4.0f * x.floatValue() * x.floatValue())) * 0.5f : ((float)Math.sqrt(1.0f - 4.0f * (x.floatValue() - 1.0f) * x.floatValue()) + 1.0f) * 0.5f)),
    SIGMOID(x -> Float.valueOf(1.0f / (1.0f + (float)Math.exp(-x.floatValue())))),
    EASE_OUT_ELASTIC(x -> Float.valueOf(x.floatValue() == 0.0f ? 0.0f : (x.floatValue() == 1.0f ? 1.0f : (float)(Math.pow(2.0, -10.0f * x.floatValue()) * Math.sin(((double)(x.floatValue() * 10.0f) - 0.75) * 2.0943951023931953) * 0.5 + 1.0)))),
    EASE_IN_BACK(x -> Float.valueOf(2.70158f * x.floatValue() * x.floatValue() * x.floatValue() - 1.70158f * x.floatValue() * x.floatValue())),
    DYNAMIC_ISLAND(x -> Float.valueOf((float)(1.0 - Math.cos((double)x.floatValue() * Math.PI * (0.2 + 2.5 * Math.pow(x.floatValue(), 3.0))) * Math.exp(-x.floatValue() * 5.0f))));

    private final Function<Float, Float> function;

    private Easing(Function<Float, Float> function) {
        this.function = function;
    }

    public Function<Float, Float> getFunction() {
        return this.function;
    }
}

