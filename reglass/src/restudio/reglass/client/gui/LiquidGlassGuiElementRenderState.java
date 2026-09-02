/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11256
 *  net.minecraft.class_2561
 *  net.minecraft.class_8030
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix3x2f
 */
package restudio.reglass.client.gui;

import java.lang.invoke.MethodHandle;
import java.lang.runtime.ObjectMethods;
import net.minecraft.class_11256;
import net.minecraft.class_2561;
import net.minecraft.class_8030;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import restudio.reglass.client.api.WidgetStyle;

public final class LiquidGlassGuiElementRenderState
extends Record
implements class_11256 {
    private final int x1;
    private final int y1;
    private final int x2;
    private final int y2;
    private final float cornerRadius;
    @Nullable
    private final class_2561 text;
    private final WidgetStyle style;
    private final Matrix3x2f pose;
    @Nullable
    private final class_8030 scissorArea;
    private final float hover;
    private final float focus;

    public LiquidGlassGuiElementRenderState(int x1, int y1, int x2, int y2, float cornerRadius, @Nullable class_2561 text, WidgetStyle style, Matrix3x2f pose, @Nullable class_8030 scissorArea, float hover, float focus) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.cornerRadius = cornerRadius;
        this.text = text;
        this.style = style;
        this.pose = pose;
        this.scissorArea = scissorArea;
        this.hover = hover;
        this.focus = focus;
    }

    public float comp_4133() {
        return 1.0f;
    }

    @Nullable
    public class_8030 comp_4128() {
        return this.scissorArea;
    }

    @Nullable
    public class_8030 comp_4274() {
        class_8030 ownBounds = new class_8030(this.x1, this.y1, this.x2 - this.x1, this.y2 - this.y1).method_71523(this.pose);
        return this.scissorArea != null ? this.scissorArea.method_49701(ownBounds) : ownBounds;
    }

    @Override
    public final String toString() {
        return ObjectMethods.bootstrap("toString", new MethodHandle[]{LiquidGlassGuiElementRenderState.class, "x1;y1;x2;y2;cornerRadius;text;style;pose;scissorArea;hover;focus", "x1", "y1", "x2", "y2", "cornerRadius", "text", "style", "pose", "scissorArea", "hover", "focus"}, this);
    }

    @Override
    public final int hashCode() {
        return (int)ObjectMethods.bootstrap("hashCode", new MethodHandle[]{LiquidGlassGuiElementRenderState.class, "x1;y1;x2;y2;cornerRadius;text;style;pose;scissorArea;hover;focus", "x1", "y1", "x2", "y2", "cornerRadius", "text", "style", "pose", "scissorArea", "hover", "focus"}, this);
    }

    @Override
    public final boolean equals(Object o) {
        return (boolean)ObjectMethods.bootstrap("equals", new MethodHandle[]{LiquidGlassGuiElementRenderState.class, "x1;y1;x2;y2;cornerRadius;text;style;pose;scissorArea;hover;focus", "x1", "y1", "x2", "y2", "cornerRadius", "text", "style", "pose", "scissorArea", "hover", "focus"}, this, o);
    }

    public int comp_4122() {
        return this.x1;
    }

    public int comp_4123() {
        return this.y1;
    }

    public int comp_4124() {
        return this.x2;
    }

    public int comp_4125() {
        return this.y2;
    }

    public float cornerRadius() {
        return this.cornerRadius;
    }

    @Nullable
    public class_2561 text() {
        return this.text;
    }

    public WidgetStyle style() {
        return this.style;
    }

    public Matrix3x2f method_72127() {
        return this.pose;
    }

    public float hover() {
        return this.hover;
    }

    public float focus() {
        return this.focus;
    }
}

