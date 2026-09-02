/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.client.renderer.NVGRenderer;

@Environment(value=EnvType.CLIENT)
public final class MotionBlurModule
extends Module {
    private final NumberProperty blurAmount = new NumberProperty("Blur Amount", 5.0, 1.0, 10.0, 0.5);
    private float lastYaw = 0.0f;
    private float lastPitch = 0.0f;
    private float currentBlurAlpha = 0.0f;

    public MotionBlurModule() {
        super("Motion Blur", "Adds realistic camera motion blur while looking around.", ModuleCategory.VISUAL);
        this.addProperties(this.blurAmount);
        this.setEnabled(true);
    }

    public float getBlurAmount() {
        return ((Double)this.blurAmount.getValue()).floatValue();
    }

    public void renderMotionBlur(class_332 context, float delta, int screenWidth, int screenHeight) {
        if (!this.isEnabled() || Constants.mc.field_1724 == null) {
            this.currentBlurAlpha = 0.0f;
            return;
        }
        float yaw = Constants.mc.field_1724.method_36454();
        float pitch = Constants.mc.field_1724.method_36455();
        float deltaYaw = Math.abs(yaw - this.lastYaw);
        if (deltaYaw > 180.0f) {
            deltaYaw = 360.0f - deltaYaw;
        }
        float deltaPitch = Math.abs(pitch - this.lastPitch);
        this.lastYaw = yaw;
        this.lastPitch = pitch;
        float motionIntensity = deltaYaw * 1.8f + deltaPitch * 2.2f;
        float targetAlpha = Math.min(0.24f, motionIntensity / 60.0f * (this.getBlurAmount() / 5.0f));
        this.currentBlurAlpha += (targetAlpha - this.currentBlurAlpha) * 0.35f;
        if (this.currentBlurAlpha > 0.005f) {
            int alphaInt = (int)(this.currentBlurAlpha * 255.0f);
            int blurColor = alphaInt << 24 | 0;
            NVGRenderer.rect(0.0f, 0.0f, (float)screenWidth, (float)screenHeight, blurColor);
        }
    }
}

