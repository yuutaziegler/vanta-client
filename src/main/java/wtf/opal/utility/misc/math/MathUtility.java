/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_4184
 */
package wtf.opal.utility.misc.math;

import java.lang.runtime.SwitchBootstraps;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_4184;
import wtf.opal.client.Constants;

@Environment(value=EnvType.CLIENT)
public final class MathUtility {
    private MathUtility() {
    }

    public static Number roundAndClamp(Number value, Number minValue, Number maxValue, Number increment) {
        Number number = value;
        Objects.requireNonNull(number);
        Number number2 = number;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{Double.class, Float.class, Long.class}, (Object)number2, n)) {
            case 0: {
                Double casted = (Double)number2;
                casted = (double)Math.round(casted / increment.doubleValue()) * increment.doubleValue();
                casted = class_3532.method_15350((double)casted, (double)minValue.doubleValue(), (double)maxValue.doubleValue());
                return casted;
            }
            case 1: {
                Float casted = (Float)number2;
                casted = Float.valueOf((float)Math.round(casted.floatValue() / increment.floatValue()) * increment.floatValue());
                casted = Float.valueOf(class_3532.method_15363((float)casted.floatValue(), (float)minValue.floatValue(), (float)maxValue.floatValue()));
                return casted;
            }
            case 2: {
                Long casted = (Long)number2;
                casted = (long)Math.round((float)casted.longValue() / (float)increment.longValue()) * increment.longValue();
                casted = class_3532.method_53062((long)casted, (long)minValue.longValue(), (long)maxValue.longValue());
                return casted;
            }
        }
        int casted = value.intValue();
        casted = Math.round((float)casted / (float)increment.intValue()) * increment.intValue();
        casted = class_3532.method_15340((int)casted, (int)minValue.intValue(), (int)maxValue.intValue());
        return casted;
    }

    public static class_243 interpolate(class_1309 entity, float tickDelta) {
        class_4184 camera = Constants.mc.field_1773.method_19418();
        return entity.method_73189().method_1031(class_3532.method_16436((double)tickDelta, (double)entity.field_6038, (double)entity.method_23317()) - entity.method_23317(), class_3532.method_16436((double)tickDelta, (double)entity.field_5971, (double)entity.method_23318()) - entity.method_23318(), class_3532.method_16436((double)tickDelta, (double)entity.field_5989, (double)entity.method_23321()) - entity.method_23321()).method_1020(camera.method_19326());
    }

    public static double interpolate(double a, double b, double v) {
        return a + (b - a) * v;
    }

    public static float interpolate(float a, float b, float v) {
        return a + (b - a) * v;
    }

    public static long interpolate(long a, long b, long v) {
        return a + (b - a) * v;
    }

    public static int interpolate(int a, int b, int v) {
        return a + (b - a) * v;
    }
}

