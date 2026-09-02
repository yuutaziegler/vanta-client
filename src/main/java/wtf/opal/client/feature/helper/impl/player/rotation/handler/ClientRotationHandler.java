/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_241
 */
package wtf.opal.client.feature.helper.impl.player.rotation.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_241;
import wtf.opal.client.Constants;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.input.MouseUpdateEvent;
import wtf.opal.event.subscriber.IEventSubscriber;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.RotationUtility;

@Environment(value=EnvType.CLIENT)
public final class ClientRotationHandler
implements IEventSubscriber {
    private class_241 rotation;
    private boolean ticking;
    private float lastRenderYaw;
    private float renderYaw;
    private float lastRenderPitch;
    private float renderPitch;

    public ClientRotationHandler() {
        EventDispatcher.subscribe(this);
    }

    @Subscribe(priority=1)
    public void onMouseUpdate(MouseUpdateEvent event) {
        if (Constants.mc.field_1724 != null && !event.isUnlockCursorRun()) {
            if (this.rotation == null) {
                this.initializeRotation();
            }
            double multiplier = event.getSensitivityMultiplier();
            double cursorX = event.getDeltaX() * multiplier;
            double cursorY = event.getDeltaY() * multiplier;
            int yMultiplier = 1;
            if (((Boolean)Constants.mc.field_1690.method_42438().method_41753()).booleanValue()) {
                yMultiplier = -1;
            }
            float deltaYaw = (float)cursorX * 0.15f;
            float deltaPitch = (float)(cursorY * (double)yMultiplier) * 0.15f;
            float yaw = this.rotation.field_1343 + deltaYaw;
            float pitch = this.rotation.field_1342 + deltaPitch;
            this.rotation = new class_241(yaw, Math.clamp(pitch % 360.0f, -90.0f, 90.0f));
        }
        this.ticking = true;
    }

    private void initializeRotation() {
        this.rotation = RotationUtility.getRotation();
        this.lastRenderYaw = Constants.mc.field_1724.field_3931;
        this.renderYaw = Constants.mc.field_1724.field_3932;
        this.lastRenderPitch = Constants.mc.field_1724.field_3914;
        this.renderPitch = Constants.mc.field_1724.field_3916;
    }

    public void tickCamera() {
        if (this.rotation != null) {
            this.lastRenderYaw = this.renderYaw;
            this.lastRenderPitch = this.renderPitch;
            this.renderPitch += (this.rotation.field_1342 - this.renderPitch) * 0.5f;
            this.renderYaw += (this.rotation.field_1343 - this.renderYaw) * 0.5f;
        }
    }

    public void onPostMouseUpdate() {
        this.ticking = false;
    }

    public void onRotationSet() {
        if (!this.ticking) {
            this.rotation = null;
        }
    }

    public float getYawOr(float fallback) {
        return this.rotation == null ? fallback : this.rotation.field_1343;
    }

    public float getPitchOr(float fallback) {
        return this.rotation == null ? fallback : this.rotation.field_1342;
    }

    public float getLastRenderYawOr(float fallback) {
        return this.rotation == null ? fallback : this.lastRenderYaw;
    }

    public float getLastRenderPitchOr(float fallback) {
        return this.rotation == null ? fallback : this.lastRenderPitch;
    }

    public float getRenderYawOr(float fallback) {
        return this.rotation == null ? fallback : this.renderYaw;
    }

    public float getRenderPitchOr(float fallback) {
        return this.rotation == null ? fallback : this.renderPitch;
    }

    public class_241 getRotation() {
        return this.rotation;
    }

    public void setRotation(class_241 rotation) {
        this.rotation = rotation;
    }

    public void setTicking(boolean ticking) {
        this.ticking = ticking;
    }
}

