/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11256
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_8030
 *  org.jetbrains.annotations.Nullable
 *  org.joml.Matrix3x2f
 *  org.joml.Matrix3x2fc
 */
package restudio.reglass.client.api;

import net.minecraft.class_11256;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_8030;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3x2f;
import org.joml.Matrix3x2fc;
import restudio.reglass.client.api.ReGlassConfig;
import restudio.reglass.client.api.WidgetStyle;
import restudio.reglass.client.gui.LiquidGlassGuiElementRenderState;
import restudio.reglass.mixin.accessor.DrawContextAccessor;

public final class ReGlassApi {
    public static WidgetStyle inactiveStyle = new WidgetStyle().tint(0, 0.3f);

    private ReGlassApi() {
    }

    public static ReGlassConfig getGlobalConfig() {
        return ReGlassConfig.INSTANCE;
    }

    public static Builder create(class_332 context) {
        return new Builder(context);
    }

    public static class Builder {
        private final class_332 context;
        private int x;
        private int y;
        private int width;
        private int height;
        private float cornerRadius = -1.0f;
        @Nullable
        private class_2561 text = null;
        private WidgetStyle style = new WidgetStyle();
        private float hoverAmount = 0.0f;
        private float focusAmount = 0.0f;

        private Builder(class_332 context) {
            this.context = context;
        }

        public Builder fromWidget(class_339 widget) {
            this.position(widget.method_46426(), widget.method_46427());
            this.size(widget.method_25368(), widget.method_25364());
            this.text(widget.method_25369());
            return this;
        }

        public Builder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        public Builder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        public Builder dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        public Builder cornerRadius(float radius) {
            this.cornerRadius = radius;
            return this;
        }

        public Builder text(class_2561 text) {
            this.text = text;
            return this;
        }

        public Builder style(WidgetStyle style) {
            this.style = style;
            return this;
        }

        public Builder hover(float amount) {
            if (Float.isNaN(amount)) {
                amount = 0.0f;
            }
            this.hoverAmount = Math.max(0.0f, Math.min(1.0f, amount));
            return this;
        }

        public Builder focus(float amount) {
            if (Float.isNaN(amount)) {
                amount = 0.0f;
            }
            this.focusAmount = Math.max(0.0f, Math.min(1.0f, amount));
            return this;
        }

        public Builder selected(float amount) {
            return this.focus(amount);
        }

        public void render() {
            float finalCornerRadius = this.cornerRadius < 0.0f ? 0.5f * (float)Math.min(this.width, this.height) : this.cornerRadius;
            Matrix3x2f pose = new Matrix3x2f((Matrix3x2fc)this.context.method_51448());
            class_8030 scissorRect = ((DrawContextAccessor)this.context).getScissorStack().method_70863();
            this.context.field_59826.method_70922((class_11256)new LiquidGlassGuiElementRenderState(this.x, this.y, this.x + this.width, this.y + this.height, finalCornerRadius, this.text, this.style, pose, scissorRect, this.hoverAmount, this.focusAmount));
        }
    }
}

