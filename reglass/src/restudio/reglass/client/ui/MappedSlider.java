/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_357
 */
package restudio.reglass.client.ui;

import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_357;
import restudio.reglass.mixin.accessor.SliderWidgetAccessor;

public class MappedSlider
extends class_357 {
    private final double min;
    private final double max;
    private final Consumer<Double> onChange;
    private final boolean integer;
    private final class_2561 originalMessage = this.method_25369();

    public static MappedSlider floatSlider(int x, int y, int width, int height, class_2561 msg, double min, double max, double init, Consumer<Double> onChange) {
        return new MappedSlider(x, y, width, height, msg, min, max, init, onChange, false);
    }

    public static MappedSlider intSlider(int x, int y, int width, int height, class_2561 msg, int min, int max, int init, Consumer<Integer> onChange) {
        return new MappedSlider(x, y, width, height, msg, min, max, init, d -> onChange.accept(d.intValue()), true);
    }

    private MappedSlider(int x, int y, int width, int height, class_2561 message, double min, double max, double init, Consumer<Double> onChange, boolean integer) {
        super(x, y, width, height, message, 0.0);
        this.min = min;
        this.max = max;
        this.onChange = onChange;
        this.integer = integer;
        ((SliderWidgetAccessor)((Object)this)).setValuePublic(this.inverseMap(init));
        this.method_25346();
    }

    private double map(double v) {
        return this.min + v * (this.max - this.min);
    }

    private double inverseMap(double real) {
        if (this.max == this.min) {
            return 0.0;
        }
        return (real - this.min) / (this.max - this.min);
    }

    protected void method_25346() {
        double v = this.map(this.field_22753);
        if (this.integer) {
            v = Math.round(v);
        }
        this.method_25355((class_2561)class_2561.method_43470((String)(this.originalMessage.getString() + ": " + this.format(v))));
    }

    private String format(double v) {
        if (this.integer) {
            return Integer.toString((int)Math.round(v));
        }
        return String.format("%.3f", v);
    }

    protected void method_25344() {
        double v = this.map(this.field_22753);
        if (this.integer) {
            v = Math.round(v);
        }
        this.onChange.accept(v);
    }
}

