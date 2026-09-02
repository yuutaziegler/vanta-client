/*
 * Decompiled with CFR 0.152.
 */
package restudio.reglass.client.api;

import restudio.reglass.client.api.ReGlassConfig;
import restudio.reglass.client.runtime.ReGlassAnim;

public class WidgetStyle {
    private boolean hasTint;
    private int tintColor;
    private float tintAlpha;
    private boolean hasSmoothing;
    private float smoothingFactor;
    private boolean hasBlurRadius;
    private int blurRadius;
    private boolean hasShadow;
    private float shadowExpand;
    private float shadowFactor;
    private float shadowOffsetX;
    private float shadowOffsetY;
    private int shadowColor;
    private float shadowColorAlpha;
    private boolean hasRefraction;
    private float refThickness;
    private float refFactor;
    private float refDispersion;
    private float refFresnelRange;
    private float refFresnelHardness;
    private float refFresnelFactor;
    private boolean hasGlare;
    private float glareRange;
    private float glareHardness;
    private float glareConvergence;
    private float glareOppositeFactor;
    private float glareFactor;
    private float glareAngleRad;

    public static WidgetStyle create() {
        return new WidgetStyle();
    }

    public WidgetStyle tint(int color, float alpha) {
        this.hasTint = true;
        this.tintColor = color;
        this.tintAlpha = alpha;
        return this;
    }

    public WidgetStyle smoothing(float factor) {
        this.hasSmoothing = true;
        this.smoothingFactor = factor;
        return this;
    }

    public WidgetStyle blurRadius(int radius) {
        this.hasBlurRadius = true;
        this.blurRadius = Math.max(0, radius);
        return this;
    }

    public WidgetStyle shadow(float expand, float factor, float offsetX, float offsetY) {
        this.hasShadow = true;
        this.shadowExpand = expand;
        this.shadowFactor = factor;
        this.shadowOffsetX = offsetX;
        this.shadowOffsetY = offsetY;
        return this;
    }

    public WidgetStyle shadowColor(int color, float alpha) {
        this.hasShadow = true;
        this.shadowColor = color;
        this.shadowColorAlpha = alpha;
        return this;
    }

    public WidgetStyle refractionThickness(float v) {
        this.hasRefraction = true;
        this.refThickness = v;
        return this;
    }

    public WidgetStyle refractionFactor(float v) {
        this.hasRefraction = true;
        this.refFactor = v;
        return this;
    }

    public WidgetStyle refractionDispersion(float v) {
        this.hasRefraction = true;
        this.refDispersion = v;
        return this;
    }

    public WidgetStyle fresnelRange(float v) {
        this.hasRefraction = true;
        this.refFresnelRange = v;
        return this;
    }

    public WidgetStyle fresnelHardness(float v) {
        this.hasRefraction = true;
        this.refFresnelHardness = v;
        return this;
    }

    public WidgetStyle fresnelFactor(float v) {
        this.hasRefraction = true;
        this.refFresnelFactor = v;
        return this;
    }

    public WidgetStyle glareRange(float v) {
        this.hasGlare = true;
        this.glareRange = v;
        return this;
    }

    public WidgetStyle glareHardness(float v) {
        this.hasGlare = true;
        this.glareHardness = v;
        return this;
    }

    public WidgetStyle glareConvergence(float v) {
        this.hasGlare = true;
        this.glareConvergence = v;
        return this;
    }

    public WidgetStyle glareOppositeFactor(float v) {
        this.hasGlare = true;
        this.glareOppositeFactor = v;
        return this;
    }

    public WidgetStyle glareFactor(float v) {
        this.hasGlare = true;
        this.glareFactor = v;
        return this;
    }

    public WidgetStyle glareAngleRad(float v) {
        this.hasGlare = true;
        this.glareAngleRad = v;
        return this;
    }

    public int getTintColor() {
        return this.hasTint ? this.tintColor : ReGlassConfig.INSTANCE.defaultTintColor;
    }

    public float getTintAlpha() {
        return this.hasTint ? this.tintAlpha : ReGlassAnim.INSTANCE.tintAlpha();
    }

    public float getSmoothing() {
        return this.hasSmoothing ? this.smoothingFactor : ReGlassAnim.INSTANCE.smoothing();
    }

    public int getBlurRadius() {
        return this.hasBlurRadius ? this.blurRadius : ReGlassAnim.INSTANCE.blurRadiusInt();
    }

    public float getShadowExpand() {
        return this.hasShadow ? this.shadowExpand : ReGlassAnim.INSTANCE.shadowExpand();
    }

    public float getShadowFactor() {
        return this.hasShadow ? this.shadowFactor : ReGlassAnim.INSTANCE.shadowFactor();
    }

    public float getShadowOffsetX() {
        return this.hasShadow ? this.shadowOffsetX : ReGlassAnim.INSTANCE.shadowOffsetX();
    }

    public float getShadowOffsetY() {
        return this.hasShadow ? this.shadowOffsetY : ReGlassAnim.INSTANCE.shadowOffsetY();
    }

    public int getShadowColor() {
        return this.hasShadow ? this.shadowColor : ReGlassConfig.INSTANCE.defaultShadowColor;
    }

    public float getShadowColorAlpha() {
        return this.hasShadow ? this.shadowColorAlpha : ReGlassConfig.INSTANCE.defaultShadowColorAlpha;
    }

    public float getRefThickness() {
        return this.hasRefraction ? this.refThickness : ReGlassAnim.INSTANCE.refThickness();
    }

    public float getRefFactor() {
        return this.hasRefraction ? this.refFactor : ReGlassAnim.INSTANCE.refFactor();
    }

    public float getRefDispersion() {
        return this.hasRefraction ? this.refDispersion : ReGlassAnim.INSTANCE.refDispersion();
    }

    public float getRefFresnelRange() {
        return this.hasRefraction ? this.refFresnelRange : ReGlassAnim.INSTANCE.refFresnelRange();
    }

    public float getRefFresnelHardness() {
        return this.hasRefraction ? this.refFresnelHardness : ReGlassAnim.INSTANCE.refFresnelHardness();
    }

    public float getRefFresnelFactor() {
        return this.hasRefraction ? this.refFresnelFactor : ReGlassAnim.INSTANCE.refFresnelFactor();
    }

    public float getGlareRange() {
        return this.hasGlare ? this.glareRange : ReGlassAnim.INSTANCE.glareRange();
    }

    public float getGlareHardness() {
        return this.hasGlare ? this.glareHardness : ReGlassAnim.INSTANCE.glareHardness();
    }

    public float getGlareConvergence() {
        return this.hasGlare ? this.glareConvergence : ReGlassAnim.INSTANCE.glareConvergence();
    }

    public float getGlareOppositeFactor() {
        return this.hasGlare ? this.glareOppositeFactor : ReGlassAnim.INSTANCE.glareOppositeFactor();
    }

    public float getGlareFactor() {
        return this.hasGlare ? this.glareFactor : ReGlassAnim.INSTANCE.glareFactor();
    }

    public float getGlareAngleRad() {
        return this.hasGlare ? this.glareAngleRad : ReGlassAnim.INSTANCE.glareAngleRad();
    }
}

