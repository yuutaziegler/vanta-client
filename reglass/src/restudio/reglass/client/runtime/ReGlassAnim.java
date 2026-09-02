/*
 * Decompiled with CFR 0.152.
 */
package restudio.reglass.client.runtime;

import restudio.reglass.client.api.ReGlassConfig;

public final class ReGlassAnim {
    public static final ReGlassAnim INSTANCE = new ReGlassAnim();
    private boolean init;
    private float tintAlpha;
    private float smoothing;
    private float blurRadiusF;
    private float shadowExpand;
    private float shadowFactor;
    private float shadowOffsetX;
    private float shadowOffsetY;
    private float refThickness;
    private float refFactor;
    private float refDispersion;
    private float refFresnelRange;
    private float refFresnelHardness;
    private float refFresnelFactor;
    private float glareRange;
    private float glareHardness;
    private float glareConvergence;
    private float glareOppositeFactor;
    private float glareFactor;
    private float glareAngleRad;
    private float debugStep;
    private float pixelatedGridSize;
    private float hoverScalePx;
    private float focusScalePx;
    private float focusBorderWidthPx;
    private float focusBorderIntensity;
    private float focusBorderSpeed;

    private ReGlassAnim() {
    }

    public void update(ReGlassConfig cfg, double dtSeconds) {
        float tau = 0.15f;
        float a = ReGlassAnim.alpha(dtSeconds, tau);
        if (!this.init) {
            this.tintAlpha = cfg.defaultTintAlpha;
            this.smoothing = cfg.defaultSmoothing;
            this.blurRadiusF = cfg.defaultBlurRadius;
            this.shadowExpand = cfg.defaultShadowExpand;
            this.shadowFactor = cfg.defaultShadowFactor;
            this.shadowOffsetX = cfg.defaultShadowOffsetX;
            this.shadowOffsetY = cfg.defaultShadowOffsetY;
            this.refThickness = cfg.defaultRefThickness;
            this.refFactor = cfg.defaultRefFactor;
            this.refDispersion = cfg.defaultRefDispersion;
            this.refFresnelRange = cfg.defaultRefFresnelRange;
            this.refFresnelHardness = cfg.defaultRefFresnelHardness;
            this.refFresnelFactor = cfg.defaultRefFresnelFactor;
            this.glareRange = cfg.defaultGlareRange;
            this.glareHardness = cfg.defaultGlareHardness;
            this.glareConvergence = cfg.defaultGlareConvergence;
            this.glareOppositeFactor = cfg.defaultGlareOppositeFactor;
            this.glareFactor = cfg.defaultGlareFactor;
            this.glareAngleRad = cfg.defaultGlareAngleRad;
            this.debugStep = cfg.debugStep;
            this.pixelatedGridSize = cfg.pixelatedGridSize;
            this.hoverScalePx = cfg.hoverScalePx;
            this.focusScalePx = cfg.focusScalePx;
            this.focusBorderWidthPx = cfg.focusBorderWidthPx;
            this.focusBorderIntensity = cfg.focusBorderIntensity;
            this.focusBorderSpeed = cfg.focusBorderSpeed;
            this.init = true;
            return;
        }
        this.tintAlpha = ReGlassAnim.lerp(this.tintAlpha, cfg.defaultTintAlpha, a);
        this.smoothing = ReGlassAnim.lerp(this.smoothing, cfg.defaultSmoothing, a);
        this.blurRadiusF = ReGlassAnim.lerp(this.blurRadiusF, cfg.defaultBlurRadius, a);
        this.shadowExpand = ReGlassAnim.lerp(this.shadowExpand, cfg.defaultShadowExpand, a);
        this.shadowFactor = ReGlassAnim.lerp(this.shadowFactor, cfg.defaultShadowFactor, a);
        this.shadowOffsetX = ReGlassAnim.lerp(this.shadowOffsetX, cfg.defaultShadowOffsetX, a);
        this.shadowOffsetY = ReGlassAnim.lerp(this.shadowOffsetY, cfg.defaultShadowOffsetY, a);
        this.refThickness = ReGlassAnim.lerp(this.refThickness, cfg.defaultRefThickness, a);
        this.refFactor = ReGlassAnim.lerp(this.refFactor, cfg.defaultRefFactor, a);
        this.refDispersion = ReGlassAnim.lerp(this.refDispersion, cfg.defaultRefDispersion, a);
        this.refFresnelRange = ReGlassAnim.lerp(this.refFresnelRange, cfg.defaultRefFresnelRange, a);
        this.refFresnelHardness = ReGlassAnim.lerp(this.refFresnelHardness, cfg.defaultRefFresnelHardness, a);
        this.refFresnelFactor = ReGlassAnim.lerp(this.refFresnelFactor, cfg.defaultRefFresnelFactor, a);
        this.glareRange = ReGlassAnim.lerp(this.glareRange, cfg.defaultGlareRange, a);
        this.glareHardness = ReGlassAnim.lerp(this.glareHardness, cfg.defaultGlareHardness, a);
        this.glareConvergence = ReGlassAnim.lerp(this.glareConvergence, cfg.defaultGlareConvergence, a);
        this.glareOppositeFactor = ReGlassAnim.lerp(this.glareOppositeFactor, cfg.defaultGlareOppositeFactor, a);
        this.glareFactor = ReGlassAnim.lerp(this.glareFactor, cfg.defaultGlareFactor, a);
        this.glareAngleRad = ReGlassAnim.lerp(this.glareAngleRad, cfg.defaultGlareAngleRad, a);
        this.debugStep = ReGlassAnim.lerp(this.debugStep, cfg.debugStep, a);
        this.pixelatedGridSize = ReGlassAnim.lerp(this.pixelatedGridSize, cfg.pixelatedGridSize, a);
        this.hoverScalePx = ReGlassAnim.lerp(this.hoverScalePx, cfg.hoverScalePx, a);
        this.focusScalePx = ReGlassAnim.lerp(this.focusScalePx, cfg.focusScalePx, a);
        this.focusBorderWidthPx = ReGlassAnim.lerp(this.focusBorderWidthPx, cfg.focusBorderWidthPx, a);
        this.focusBorderIntensity = ReGlassAnim.lerp(this.focusBorderIntensity, cfg.focusBorderIntensity, a);
        this.focusBorderSpeed = ReGlassAnim.lerp(this.focusBorderSpeed, cfg.focusBorderSpeed, a);
    }

    private static float alpha(double dt, float tau) {
        if (dt <= 0.0) {
            return 0.0f;
        }
        double al = 1.0 - Math.exp(-dt / Math.max(1.0E-4, (double)tau));
        return (float)al;
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    public float tintAlpha() {
        return this.tintAlpha;
    }

    public float smoothing() {
        return this.smoothing;
    }

    public int blurRadiusInt() {
        return Math.max(0, Math.round(this.blurRadiusF));
    }

    public float shadowExpand() {
        return this.shadowExpand;
    }

    public float shadowFactor() {
        return this.shadowFactor;
    }

    public float shadowOffsetX() {
        return this.shadowOffsetX;
    }

    public float shadowOffsetY() {
        return this.shadowOffsetY;
    }

    public float refThickness() {
        return this.refThickness;
    }

    public float refFactor() {
        return this.refFactor;
    }

    public float refDispersion() {
        return this.refDispersion;
    }

    public float refFresnelRange() {
        return this.refFresnelRange;
    }

    public float refFresnelHardness() {
        return this.refFresnelHardness;
    }

    public float refFresnelFactor() {
        return this.refFresnelFactor;
    }

    public float glareRange() {
        return this.glareRange;
    }

    public float glareHardness() {
        return this.glareHardness;
    }

    public float glareConvergence() {
        return this.glareConvergence;
    }

    public float glareOppositeFactor() {
        return this.glareOppositeFactor;
    }

    public float glareFactor() {
        return this.glareFactor;
    }

    public float glareAngleRad() {
        return this.glareAngleRad;
    }

    public float debugStep() {
        return this.debugStep;
    }

    public float pixelatedGridSize() {
        return this.pixelatedGridSize;
    }

    public float hoverScalePx() {
        return this.hoverScalePx;
    }

    public float focusScalePx() {
        return this.focusScalePx;
    }

    public float focusBorderWidthPx() {
        return this.focusBorderWidthPx;
    }

    public float focusBorderIntensity() {
        return this.focusBorderIntensity;
    }

    public float focusBorderSpeed() {
        return this.focusBorderSpeed;
    }
}

