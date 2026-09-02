/*
 * UIEditorScreen - drag & resize HUD overlay elements.
 *
 * Complete rewrite: a liquid-glass control window (movable, scalable via
 * buttons), live element outlines with 8 resize handles, drag-to-move,
 * right-click reset, H for help, ESC to save & close.  Renders ALL overlay
 * elements (even inactive ones) so every element is editable, and polls
 * drags every frame (the old version only moved the LAST hovered element).
 */
package wtf.opal.client.screen.hud;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.VantaGlass;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.utility.misc.HoverUtility;

@Environment(value = EnvType.CLIENT)
public class UIEditorScreen extends class_437 {
    private static final int ACCENT = 0xFF4C8DFF;
    private static final int TEXT = 0xFFF4F7FF;
    private static final int TEXT_DIM = 0xFF9AA7C2;

    private static float winX = -1.0f;
    private static float winY = -1.0f;
    private static float editorScale = 1.0f;

    private final List<HandleHit> handleHits = new ArrayList<>();

    private ScreenPositionProperty draggingProperty;
    private ScreenPositionProperty resizingProperty;
    private ResizeHandle activeHandle;
    private float dragOffsetX;
    private float dragOffsetY;
    private float resizeStartX;
    private float resizeStartY;
    private float resizeStartW;
    private float resizeStartH;
    private float resizeStartRelX;
    private float resizeStartRelY;

    private boolean draggingWindow;
    private float winDragOffX;
    private float winDragOffY;

    private boolean showHelp = true;

    public UIEditorScreen() {
        super(class_2561.method_43470("Vanta HUD Editor"));
    }

    private float winW() {
        return 230.0f * editorScale;
    }

    private float winH() {
        return 74.0f * editorScale;
    }

    private List<IOverlayElement> allElements() {
        OverlayModule overlay = OpalClient.getInstance().getModuleRepository().getModule(OverlayModule.class);
        if (overlay == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(overlay.getElements());
    }

    @Override
    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        float screenW = Constants.mc.method_22683().method_4486();
        float screenH = Constants.mc.method_22683().method_4502();
        float s = editorScale;

        if (this.draggingWindow) {
            winX = mouseX - this.winDragOffX;
            winY = mouseY - this.winDragOffY;
        }
        if (winX < 0.0f || winY < 0.0f) {
            winX = 8.0f;
            winY = 8.0f;
        }
        winX = Math.max(0.0f, Math.min(screenW - winW(), winX));
        winY = Math.max(0.0f, Math.min(screenH - winH(), winY));

        NVGTextRenderer bold = FontRepository.getFont("inter-bold");
        NVGTextRenderer med = FontRepository.getFont("inter-medium");

        boolean frameStarted = NVGRenderer.beginFrame();

        // ---- dim the world slightly so outlines are visible (light, not black)
        context.method_25294(0, 0, this.field_22789, this.field_22790, 0x66000000);

        // ---- per-frame drag polling
        for (IOverlayElement element : this.allElements()) {
            ScreenPositionProperty prop = element.getPositionProperty();
            if (prop == null) {
                continue;
            }
            if (prop.isDragging()) {
                float nx = mouseX - this.dragOffsetX;
                float ny = mouseY - this.dragOffsetY;
                nx = Math.max(0.0f, Math.min(screenW - prop.getWidth(), nx));
                ny = Math.max(0.0f, Math.min(screenH - prop.getHeight(), ny));
                prop.setRelativeX(nx);
                prop.setRelativeY(ny);
            }
            if (this.resizingProperty == prop && this.activeHandle != null) {
                this.applyResize(prop, mouseX, mouseY, screenW, screenH);
            }
        }

        // ---- render elements + outlines
        this.handleHits.clear();
        for (IOverlayElement element : this.allElements()) {
            ScreenPositionProperty prop = element.getPositionProperty();
            if (prop == null) {
                continue;
            }
            float x = prop.getScaledX();
            float y = prop.getScaledY();
            float w = Math.max(16.0f, prop.getWidth());
            float h = Math.max(12.0f, prop.getHeight());

            boolean hover = HoverUtility.isHovering(x - 4.0f, y - 4.0f, w + 8.0f, h + 8.0f, mouseX, mouseY);
            boolean active = prop.isDragging() || this.resizingProperty == prop;

            // outline
            NVGRenderer.roundedRect(x - 3.0f, y - 3.0f, w + 6.0f, h + 6.0f, 4.0f, active ? 0x554C8DFF : (hover ? 0x334C8DFF : 0x22FFFFFF));
            NVGRenderer.roundedRectOutline(x - 3.0f, y - 3.0f, w + 6.0f, h + 6.0f, 4.0f, 1.2f, active ? ACCENT : 0x88FFFFFF);

            if (hover || active) {
                this.drawHandles(x, y, w, h);
                String name = prop.getName();
                float nw = med.getStringWidth(name, 6.5f) + 10.0f;
                float labelY = Math.max(2.0f, y - 18.0f);
                NVGRenderer.roundedRect(x + w / 2.0f - nw / 2.0f, labelY, nw, 12.0f, 3.0f, 0xE616224E);
                med.drawString(name, x + w / 2.0f - med.getStringWidth(name, 6.5f) / 2.0f, labelY + 3.5f, 6.5f, TEXT);
            }
        }

        // ---- control window
        VantaGlass.panel(winX, winY, winW(), winH(), 10.0f * s, 0.16f);
        bold.drawString("HUD Editor", winX + 12.0f * s, winY + 10.0f * s, 9.5f * s, TEXT);
        med.drawString("Drag to move \u2022 corners resize \u2022 right-click reset", winX + 12.0f * s, winY + 26.0f * s, 6.5f * s, TEXT_DIM);
        med.drawString("H: help   \u2022   ESC: save & exit", winX + 12.0f * s, winY + 38.0f * s, 6.5f * s, TEXT_DIM);

        float by = winY + 52.0f * s;
        this.controlButton(winX + 12.0f * s, by, 22.0f * s, 14.0f * s, "-", mouseX, mouseY, med, s);
        this.controlButton(winX + 38.0f * s, by, 22.0f * s, 14.0f * s, "+", mouseX, mouseY, med, s);
        this.controlButton(winX + winW() - 70.0f * s, by, 58.0f * s, 14.0f * s, this.showHelp ? "Hide help" : "Show help", mouseX, mouseY, med, s);

        if (this.showHelp) {
            float hx = winX;
            float hy = winY + winH() + 6.0f;
            float hw = winW();
            float hh = 78.0f * s;
            VantaGlass.panel(hx, hy, hw, hh, 8.0f * s, 0.18f);
            med.drawString("Left-click + drag:  move element", hx + 10.0f * s, hy + 10.0f * s, 6.5f * s, TEXT);
            med.drawString("Drag white handles:  resize element", hx + 10.0f * s, hy + 24.0f * s, 6.5f * s, TEXT);
            med.drawString("Right-click element:  reset position", hx + 10.0f * s, hy + 38.0f * s, 6.5f * s, TEXT);
            med.drawString("H:  toggle this panel", hx + 10.0f * s, hy + 52.0f * s, 6.5f * s, TEXT);
            med.drawString("ESC:  save and exit", hx + 10.0f * s, hy + 66.0f * s, 6.5f * s, TEXT);
        }

        if (frameStarted) {
            NVGRenderer.endFrameAndReset(true);
        }
    }

    private void drawHandles(float x, float y, float w, float height) {
        float hs = 7.0f;
        this.putHandle(ResizeHandle.TOP_LEFT, x - hs / 2.0f, y - hs / 2.0f, hs, hs);
        this.putHandle(ResizeHandle.TOP_RIGHT, x + w - hs / 2.0f, y - hs / 2.0f, hs, hs);
        this.putHandle(ResizeHandle.BOTTOM_LEFT, x - hs / 2.0f, y + height - hs / 2.0f, hs, hs);
        this.putHandle(ResizeHandle.BOTTOM_RIGHT, x + w - hs / 2.0f, y + height - hs / 2.0f, hs, hs);
        this.putHandle(ResizeHandle.TOP, x + w / 2.0f - hs / 2.0f, y - hs / 2.0f, hs, hs);
        this.putHandle(ResizeHandle.BOTTOM, x + w / 2.0f - hs / 2.0f, y + height - hs / 2.0f, hs, hs);
        this.putHandle(ResizeHandle.LEFT, x - hs / 2.0f, y + height / 2.0f - hs / 2.0f, hs, hs);
        this.putHandle(ResizeHandle.RIGHT, x + w - hs / 2.0f, y + height / 2.0f - hs / 2.0f, hs, hs);
    }

    private void putHandle(ResizeHandle handle, float x, float y, float w, float h) {
        this.handleHits.add(new HandleHit(handle, x, y, w, h));
        NVGRenderer.roundedRect(x, y, w, h, 2.0f, 0xF2FFFFFF);
        NVGRenderer.roundedRectOutline(x, y, w, h, 2.0f, 1.0f, ACCENT);
    }

    private ResizeHandle handleAt(double mx, double my) {
        for (int i = this.handleHits.size() - 1; i >= 0; --i) {
            HandleHit hit = this.handleHits.get(i);
            if (HoverUtility.isHovering(hit.x, hit.y, hit.w, hit.h, mx, my)) {
                return hit.handle;
            }
        }
        return null;
    }

    private void controlButton(float x, float y, float w, float h, String label, int mouseX, int mouseY, NVGTextRenderer med, float s) {
        boolean hover = HoverUtility.isHovering(x, y, w, h, mouseX, mouseY);
        NVGRenderer.roundedRect(x, y, w, h, 4.0f * s, hover ? 0x554C8DFF : 0x28FFFFFF);
        float tw = med.getStringWidth(label, 6.5f * s);
        med.drawString(label, x + (w - tw) / 2.0f, y + h / 2.0f - 4.0f * s, 6.5f * s, TEXT);
    }

    private void applyResize(ScreenPositionProperty prop, int mouseX, int mouseY, float screenW, float screenH) {
        float dx = mouseX - this.resizeStartX;
        float dy = mouseY - this.resizeStartY;
        float newW = this.resizeStartW;
        float newH = this.resizeStartH;
        float newRelX = this.resizeStartRelX;
        float newRelY = this.resizeStartRelY;
        switch (this.activeHandle) {
            case BOTTOM_RIGHT:
                newW = this.resizeStartW + dx;
                newH = this.resizeStartH + dy;
                break;
            case BOTTOM_LEFT:
                newW = this.resizeStartW - dx;
                newH = this.resizeStartH + dy;
                newRelX = this.resizeStartRelX + dx / screenW;
                break;
            case TOP_RIGHT:
                newW = this.resizeStartW + dx;
                newH = this.resizeStartH - dy;
                newRelY = this.resizeStartRelY + dy / screenH;
                break;
            case TOP_LEFT:
                newW = this.resizeStartW - dx;
                newH = this.resizeStartH - dy;
                newRelX = this.resizeStartRelX + dx / screenW;
                newRelY = this.resizeStartRelY + dy / screenH;
                break;
            case RIGHT:
                newW = this.resizeStartW + dx;
                break;
            case LEFT:
                newW = this.resizeStartW - dx;
                newRelX = this.resizeStartRelX + dx / screenW;
                break;
            case BOTTOM:
                newH = this.resizeStartH + dy;
                break;
            case TOP:
                newH = this.resizeStartH - dy;
                newRelY = this.resizeStartRelY + dy / screenH;
                break;
        }
        newW = Math.max(20.0f, newW);
        newH = Math.max(14.0f, newH);
        newRelX = Math.max(0.0f, Math.min(1.0f - newW / screenW, newRelX));
        newRelY = Math.max(0.0f, Math.min(1.0f - newH / screenH, newRelY));
        prop._setRelativeX(newRelX);
        prop._setRelativeY(newRelY);
        prop.setWidth(newW);
        prop.setHeight(newH);
    }

    @Override
    public boolean method_25402(class_11909 click, boolean doubled) {
        double mx = click.comp_4798();
        double my = click.comp_4799();
        int button = click.method_74245();
        float s = editorScale;

        // control window buttons
        float by = winY + 52.0f * s;
        if (button == 0 && HoverUtility.isHovering(winX + 12.0f * s, by, 22.0f * s, 14.0f * s, mx, my)) {
            editorScale = Math.max(0.7f, editorScale - 0.1f);
            return true;
        }
        if (button == 0 && HoverUtility.isHovering(winX + 38.0f * s, by, 22.0f * s, 14.0f * s, mx, my)) {
            editorScale = Math.min(1.6f, editorScale + 0.1f);
            return true;
        }
        if (button == 0 && HoverUtility.isHovering(winX + winW() - 70.0f * s, by, 58.0f * s, 14.0f * s, mx, my)) {
            this.showHelp = !this.showHelp;
            return true;
        }

        // drag window by its title area
        if (button == 0 && HoverUtility.isHovering(winX, winY, winW(), 24.0f * s, mx, my)) {
            this.draggingWindow = true;
            this.winDragOffX = (float) mx - winX;
            this.winDragOffY = (float) my - winY;
            return true;
        }

        // elements: check topmost (last drawn hovered) first
        List<IOverlayElement> elements = this.allElements();
        for (int i = elements.size() - 1; i >= 0; --i) {
            ScreenPositionProperty prop = elements.get(i).getPositionProperty();
            if (prop == null) {
                continue;
            }
            float x = prop.getScaledX();
            float y = prop.getScaledY();
            float w = Math.max(16.0f, prop.getWidth());
            float h = Math.max(12.0f, prop.getHeight());

            if (button == 0) {
                ResizeHandle handle = this.handleAt(mx, my);
                if (handle != null && HoverUtility.isHovering(x - 6.0f, y - 6.0f, w + 12.0f, h + 12.0f, mx, my)) {
                    this.resizingProperty = prop;
                    this.activeHandle = handle;
                    this.resizeStartX = (float) mx;
                    this.resizeStartY = (float) my;
                    this.resizeStartW = w;
                    this.resizeStartH = h;
                    this.resizeStartRelX = prop.getRelativeX();
                    this.resizeStartRelY = prop.getRelativeY();
                    return true;
                }
                if (HoverUtility.isHovering(x, y, w, h, mx, my)) {
                    prop.setDragging(true);
                    this.draggingProperty = prop;
                    this.dragOffsetX = (float) mx - x;
                    this.dragOffsetY = (float) my - y;
                    return true;
                }
            } else if (button == 1 && HoverUtility.isHovering(x, y, w, h, mx, my)) {
                prop._setRelativeX(0.5f);
                prop._setRelativeY(0.5f);
                return true;
            }
        }
        return true;
    }

    @Override
    public boolean method_25406(class_11909 click) {
        if (this.draggingProperty != null) {
            this.draggingProperty.snapToGrid();
            this.draggingProperty.setDragging(false);
            this.draggingProperty = null;
        }
        this.resizingProperty = null;
        this.activeHandle = null;
        this.draggingWindow = false;
        return true;
    }

    @Override
    public boolean method_25404(class_11908 keyInput) {
        if (keyInput.comp_4795() == 72) { // H
            this.showHelp = !this.showHelp;
            return true;
        }
        if (keyInput.comp_4795() == 256 || keyInput.comp_4795() == 344) { // ESC / RShift
            Constants.mc.method_1507(null);
            return true;
        }
        return true;
    }

    @Override
    public boolean method_25421() {
        return false;
    }

    private enum ResizeHandle {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT,
        TOP, BOTTOM, LEFT, RIGHT
    }

    private static final class HandleHit {
        final ResizeHandle handle;
        final float x;
        final float y;
        final float w;
        final float h;

        HandleHit(ResizeHandle handle, float x, float y, float w, float h) {
            this.handle = handle;
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
        }
    }
}
