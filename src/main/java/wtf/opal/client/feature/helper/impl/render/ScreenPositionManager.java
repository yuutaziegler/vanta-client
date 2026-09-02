/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1041
 *  net.minecraft.class_408
 */
package wtf.opal.client.feature.helper.impl.render;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_408;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.IHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.render.RenderScreenEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.player.PlayerUtility;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public final class ScreenPositionManager
implements IHelper {
    private final Map<ScreenPositionProperty, Module> properties = new HashMap<ScreenPositionProperty, Module>();
    private boolean dragging;
    private static ScreenPositionManager instance;

    private ScreenPositionManager() {
    }

    public void register(Module module, ScreenPositionProperty property) {
        this.properties.put(property, module);
    }

    @Subscribe(priority=-50)
    public void onRenderScreen(RenderScreenEvent event) {
        if (!(Constants.mc.field_1755 instanceof class_408)) {
            if (this.dragging) {
                this.releaseDraggedProperties();
            }
            return;
        }
        double mouseX = event.mouseX();
        double mouseY = event.mouseY();
        this.properties.forEach((property, module) -> {
            float height;
            float width;
            float scaledY;
            float scaledX;
            if (!module.isEnabled()) {
                return;
            }
            if (property.isDragging()) {
                property.setRelativeX((float)(mouseX - (double)property.getStartX()));
                property.setRelativeY((float)(mouseY - (double)property.getStartY()));
                if (!PlayerUtility.isKeyPressed(340)) {
                    property.snapToGrid();
                }
            }
            if (HoverUtility.isHovering(scaledX = property.getScaledX(), scaledY = property.getScaledY(), width = property.getWidth(), height = property.getHeight(), mouseX, mouseY)) {
                NVGRenderer.roundedRect(scaledX - 2.0f, scaledY - 2.0f, width + 4.0f, height + 4.0f, 6.0f, ColorUtility.applyOpacity(-16777216, 0.2f));
            }
        });
        class_1041 window = Constants.mc.method_22683();
        int scaledWidth = window.method_4486();
        int scaledHeight = window.method_4502();
        if (this.dragging) {
            int gridLineColor = ColorUtility.applyOpacity(-1, 0.5f);
            NVGRenderer.rectOutline(0.0f, 0.0f, scaledWidth, scaledHeight, 1.0f, gridLineColor);
            NVGRenderer.rect(0.0f, (float)scaledHeight / 2.0f - 0.5f, (float)scaledWidth, 1.0f, gridLineColor);
            NVGRenderer.rect((float)scaledWidth / 2.0f - 1.0f, 0.0f, 1.0f, (float)scaledHeight, gridLineColor);
        }
    }

    public void onMouseClick(double mouseX, double mouseY, int button) {
        if (button != 0) {
            return;
        }
        this.properties.forEach((property, module) -> {
            float scaledY;
            if (!module.isEnabled()) {
                return;
            }
            float scaledX = property.getScaledX();
            if (HoverUtility.isHovering(scaledX, scaledY = property.getScaledY(), property.getWidth(), property.getHeight(), mouseX, mouseY)) {
                property.setStartX((float)(mouseX - (double)scaledX));
                property.setStartY((float)(mouseY - (double)scaledY));
                property.setDragging(true);
                this.dragging = true;
            }
        });
    }

    public void releaseDraggedProperties() {
        this.properties.forEach((property, _module) -> property.setDragging(false));
        this.dragging = false;
    }

    public static ScreenPositionManager getInstance() {
        return instance;
    }

    public static void setInstance() {
        instance = new ScreenPositionManager();
        EventDispatcher.subscribe(instance);
    }
}

