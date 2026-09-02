/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 *  org.lwjgl.glfw.GLFW
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.keystrokes;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import org.lwjgl.glfw.GLFW;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;

@Environment(value=EnvType.CLIENT)
public final class KeystrokesElement
implements IOverlayElement {
    private static final NVGTextRenderer BOLD_FONT = FontRepository.getFont("inter-bold");
    private final BooleanProperty enabled = new BooleanProperty("Enabled", true);
    private final BooleanProperty showMouse = new BooleanProperty("Show Mouse (LMB/RMB)", true);
    private final BooleanProperty showSpace = new BooleanProperty("Show Spacebar", true);
    private final BooleanProperty showMovementTracker = new BooleanProperty("Mouse Tracker", true);
    private final ScreenPositionProperty position = new ScreenPositionProperty("Screen Position", 0.05f, 0.4f);
    private float smoothDotX = 0.0f;
    private float smoothDotY = 0.0f;
    private float prevYaw = 0.0f;
    private float prevPitch = 0.0f;
    private final List<float[]> trail = new ArrayList<float[]>();

    public KeystrokesElement(OverlayModule module) {
        module.addProperties(new GroupProperty("Keystrokes", this.enabled, this.showMouse, this.showSpace, this.showMovementTracker, this.position));
    }

    @Override
    public ScreenPositionProperty getPositionProperty() {
        return this.position;
    }

    @Override
    public boolean isActive() {
        return this.enabled.getValue();
    }

    @Override
    public boolean isBloom() {
        return false;
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        if (Constants.mc.field_1724 == null || Constants.mc.field_1690 == null) {
            return;
        }
        float keySize = 22.0f;
        float gap = 3.0f;
        float totalW = keySize * 3.0f + gap * 2.0f;
        float totalH = keySize * 2.0f + gap;
        if (this.showMovementTracker.getValue().booleanValue()) {
            totalH += 18.0f + gap;
        }
        if (this.showMouse.getValue().booleanValue()) {
            totalH += keySize + gap;
        }
        if (this.showSpace.getValue().booleanValue()) {
            totalH += 14.0f + gap;
        }
        this.position.setWidth(totalW);
        this.position.setHeight(totalH);
        float x = this.position.getScaledX();
        float y = this.position.getScaledY();
        boolean wDown = Constants.mc.field_1690.field_1894.method_1434();
        this.drawKey(x + keySize + gap, y, keySize, keySize, "W", wDown);
        boolean aDown = Constants.mc.field_1690.field_1913.method_1434();
        boolean sDown = Constants.mc.field_1690.field_1881.method_1434();
        boolean dDown = Constants.mc.field_1690.field_1849.method_1434();
        float row2Y = y + keySize + gap;
        this.drawKey(x, row2Y, keySize, keySize, "A", aDown);
        this.drawKey(x + keySize + gap, row2Y, keySize, keySize, "S", sDown);
        this.drawKey(x + (keySize + gap) * 2.0f, row2Y, keySize, keySize, "D", dDown);
        float currY = row2Y + keySize + gap;
        if (this.showMovementTracker.getValue().booleanValue()) {
            float deltaYaw;
            float trackerH = 20.0f;
            NVGRenderer.roundedRect(x, currY, totalW, trackerH, 4.0f, -1728053248);
            float curYaw = Constants.mc.field_1724.method_36454();
            float curPitch = Constants.mc.field_1724.method_36455();
            float deltaPitch = curPitch - this.prevPitch;
            this.prevYaw = curYaw;
            this.prevPitch = curPitch;
            for (deltaYaw = curYaw - this.prevYaw; deltaYaw > 180.0f; deltaYaw -= 360.0f) {
            }
            while (deltaYaw < -180.0f) {
                deltaYaw += 360.0f;
            }
            float halfW = (totalW - 10.0f) / 2.0f;
            float halfH = (trackerH - 10.0f) / 2.0f;
            float targetX = Math.max(-halfW, Math.min(halfW, deltaYaw * 3.5f));
            float targetY = Math.max(-halfH, Math.min(halfH, deltaPitch * 3.5f));
            this.smoothDotX += (targetX - this.smoothDotX) * 0.45f;
            this.smoothDotY += (targetY - this.smoothDotY) * 0.45f;
            float centerX = x + totalW / 2.0f;
            float centerY = currY + trackerH / 2.0f;
            float dotPosX = centerX + this.smoothDotX;
            float dotPosY = centerY + this.smoothDotY;
            this.trail.add(new float[]{dotPosX, dotPosY});
            if (this.trail.size() > 16) {
                this.trail.remove(0);
            }
            NVGRenderer.roundedRect(centerX - 0.5f, centerY - 2.5f, 1.0f, 5.0f, 0.5f, 0x22FFFFFF);
            NVGRenderer.roundedRect(centerX - 2.5f, centerY - 0.5f, 5.0f, 1.0f, 0.5f, 0x22FFFFFF);
            for (int i = 0; i < this.trail.size(); ++i) {
                float[] pt = this.trail.get(i);
                float progress = (float)(i + 1) / (float)this.trail.size();
                int trAlpha = (int)(progress * 180.0f);
                float trailSize = 2.0f + progress * 2.5f;
                int trCol = trAlpha << 24 | 0x84FF;
                NVGRenderer.roundedRect(pt[0] - trailSize / 2.0f, pt[1] - trailSize / 2.0f, trailSize, trailSize, trailSize / 2.0f, trCol);
            }
            NVGRenderer.roundedRect(dotPosX - 3.0f, dotPosY - 3.0f, 6.0f, 6.0f, 3.0f, -16743169);
            NVGRenderer.roundedRect(dotPosX - 1.5f, dotPosY - 1.5f, 3.0f, 3.0f, 1.5f, -1);
            currY += trackerH + gap;
        }
        if (this.showMouse.getValue().booleanValue()) {
            boolean lmbDown = GLFW.glfwGetMouseButton((long)Constants.mc.method_22683().method_4490(), (int)0) == 1;
            boolean rmbDown = GLFW.glfwGetMouseButton((long)Constants.mc.method_22683().method_4490(), (int)1) == 1;
            float mouseW = (totalW - gap) / 2.0f;
            this.drawKey(x, currY, mouseW, keySize, "LMB", lmbDown);
            this.drawKey(x + mouseW + gap, currY, mouseW, keySize, "RMB", rmbDown);
            currY += keySize + gap;
        }
        if (this.showSpace.getValue().booleanValue()) {
            boolean spaceDown = Constants.mc.field_1690.field_1903.method_1434();
            this.drawKey(x, currY, totalW, 14.0f, "\u2014\u2014\u2014", spaceDown);
        }
    }

    private void drawKey(float x, float y, float w, float h, String text, boolean pressed) {
        int bg = pressed ? -587168513 : -2013265920;
        int textCol = pressed ? -16184558 : -1;
        NVGRenderer.roundedRect(x, y, w, h, 4.0f, bg);
        float textW = BOLD_FONT.getStringWidth(text, 7.5f);
        BOLD_FONT.drawString(text, x + (w - textW) / 2.0f, y + h / 2.0f + 3.0f, 7.5f, textCol);
    }
}

