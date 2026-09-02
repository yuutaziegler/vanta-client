/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_6382
 */
package restudio.reglass.client;

import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_6382;
import restudio.reglass.client.LiquidGlassUniforms;
import restudio.reglass.client.api.ReGlassApi;
import restudio.reglass.client.api.WidgetStyle;

public class LiquidGlassWidget
extends class_339 {
    private float cornerRadiusPx;
    private boolean moveable;
    private boolean dragging;
    private int dragOffsetX;
    private int dragOffsetY;
    public WidgetStyle style = new WidgetStyle();

    public LiquidGlassWidget(int x, int y, int width, int height, WidgetStyle style) {
        super(x, y, width, height, (class_2561)class_2561.method_43473());
        this.cornerRadiusPx = 0.5f * (float)Math.min(width, height);
        if (style != null) {
            this.style = style;
        }
    }

    public LiquidGlassWidget setCornerRadiusPx(float radiusPx) {
        this.cornerRadiusPx = Math.max(0.0f, radiusPx);
        return this;
    }

    public LiquidGlassWidget setMoveable(boolean moveable) {
        this.moveable = moveable;
        return this;
    }

    public void method_48579(class_332 context, int mouseX, int mouseY, float delta) {
        ReGlassApi.create(context).fromWidget(this).cornerRadius(this.cornerRadiusPx).style(this.style).render();
        LiquidGlassUniforms.get().tryApplyBlur(context);
    }

    public boolean method_25402(class_11909 click, boolean isDouble) {
        if (!this.moveable) {
            return super.method_25402(click, isDouble);
        }
        if (click.method_74245() == 0 && click.comp_4798() >= (double)this.method_46426() && click.comp_4798() < (double)(this.method_46426() + this.method_25368()) && click.comp_4799() >= (double)this.method_46427() && click.comp_4799() < (double)(this.method_46427() + this.method_25364())) {
            this.dragging = true;
            this.dragOffsetX = (int)(click.comp_4798() - (double)this.method_46426());
            this.dragOffsetY = (int)(click.comp_4799() - (double)this.method_46427());
            return true;
        }
        return super.method_25402(click, isDouble);
    }

    public boolean method_25403(class_11909 click, double offsetX, double offsetY) {
        if (this.dragging && click.method_74245() == 0) {
            int newX = (int)(click.comp_4798() - (double)this.dragOffsetX);
            int newY = (int)(click.comp_4799() - (double)this.dragOffsetY);
            this.method_46421(newX);
            this.method_46419(newY);
            return true;
        }
        return super.method_25403(click, offsetX, offsetY);
    }

    public boolean method_25406(class_11909 click) {
        if (this.dragging && click.method_74245() == 0) {
            this.dragging = false;
        }
        return super.method_25406(click);
    }

    protected void method_47399(class_6382 builder) {
    }
}

