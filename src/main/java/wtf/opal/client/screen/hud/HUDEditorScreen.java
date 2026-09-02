/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 */
package wtf.opal.client.screen.hud;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.utility.misc.HoverUtility;

@Environment(value=EnvType.CLIENT)
public class HUDEditorScreen
extends class_437 {
    private static final NVGTextRenderer BOLD_FONT = FontRepository.getFont("inter-bold");
    private static final NVGTextRenderer MED_FONT = FontRepository.getFont("inter-medium");
    private ScreenPositionProperty draggingProperty;
    private float dragStartX;
    private float dragStartY;

    public HUDEditorScreen() {
        super(class_2561.method_30163((String)"HUD Editor"));
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        context.method_25294(0, 0, this.field_22789, this.field_22790, 1711868690);
        boolean frameStarted = NVGRenderer.beginFrame();
        float bannerW = 340.0f;
        float bannerH = 32.0f;
        float bannerX = ((float)this.field_22789 - bannerW) / 2.0f;
        float bannerY = 16.0f;
        NVGRenderer.roundedRect(bannerX, bannerY, bannerW, bannerH, 6.0f, -586609643);
        BOLD_FONT.drawString("HUD Customizer", bannerX + 14.0f, bannerY + 14.0f, 8.5f, -16718337);
        MED_FONT.drawString("Drag elements to move \u00b7 ESC to save", bannerX + 14.0f, bannerY + 24.0f, 6.5f, -7035976);
        float rBtnW = 60.0f;
        float rBtnH = 18.0f;
        float rBtnX = bannerX + bannerW - rBtnW - 8.0f;
        float rBtnY = bannerY + 7.0f;
        boolean rHover = (float)mouseX >= rBtnX && (float)mouseX <= rBtnX + rBtnW && (float)mouseY >= rBtnY && (float)mouseY <= rBtnY + rBtnH;
        NVGRenderer.roundedRect(rBtnX, rBtnY, rBtnW, rBtnH, 4.0f, rHover ? 1140909567 : 570484223);
        float rw = MED_FONT.getStringWidth("Reset", 6.5f);
        MED_FONT.drawString("Reset", rBtnX + (rBtnW - rw) / 2.0f, rBtnY + 12.0f, 6.5f, -1);
        OverlayModule overlayModule = OpalClient.getInstance().getModuleRepository().getModule(OverlayModule.class);
        if (overlayModule != null) {
            List<IOverlayElement> elements = overlayModule.getElements();
            for (IOverlayElement element : elements) {
                boolean isHover;
                ScreenPositionProperty prop = element.getPositionProperty();
                if (prop == null) continue;
                float x = prop.getScaledX();
                float y = prop.getScaledY();
                float width = Math.max(20.0f, prop.getWidth());
                float height = Math.max(16.0f, prop.getHeight());
                if (prop.isDragging()) {
                    prop.setRelativeX((float)mouseX - prop.getStartX());
                    prop.setRelativeY((float)mouseY - prop.getStartY());
                }
                if ((isHover = HoverUtility.isHovering(x, y, width, height, mouseX, mouseY)) || prop.isDragging()) {
                    NVGRenderer.roundedRect(x - 2.0f, y - 2.0f, width + 4.0f, height + 4.0f, 4.0f, prop.isDragging() ? 1073800703 : 536929791);
                    NVGRenderer.roundedRectOutline(x - 2.0f, y - 2.0f, width + 4.0f, height + 4.0f, 4.0f, 1.0f, -16718337);
                    String name = prop.getId();
                    float nw = MED_FONT.getStringWidth(name, 6.0f) + 8.0f;
                    NVGRenderer.roundedRect(x, y - 13.0f, nw, 11.0f, 3.0f, -267842798);
                    MED_FONT.drawString(name, x + 4.0f, y - 5.0f, 6.0f, -16718337);
                    continue;
                }
                NVGRenderer.roundedRectOutline(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f, 3.0f, 1.0f, 855696895);
            }
        }
        if (frameStarted) {
            NVGRenderer.endFrameAndReset(true);
        }
    }

    public boolean method_25402(class_11909 click, boolean doubled) {
        float bannerW = 340.0f;
        float bannerH = 32.0f;
        float bannerX = ((float)this.field_22789 - bannerW) / 2.0f;
        float bannerY = 16.0f;
        float rBtnW = 60.0f;
        float rBtnH = 18.0f;
        float rBtnX = bannerX + bannerW - rBtnW - 8.0f;
        float rBtnY = bannerY + 7.0f;
        if (click.comp_4798() >= (double)rBtnX && click.comp_4798() <= (double)(rBtnX + rBtnW) && click.comp_4799() >= (double)rBtnY && click.comp_4799() <= (double)(rBtnY + rBtnH)) {
            return true;
        }
        OverlayModule overlayModule = OpalClient.getInstance().getModuleRepository().getModule(OverlayModule.class);
        if (overlayModule != null && click.method_74245() == 0) {
            for (IOverlayElement element : overlayModule.getElements()) {
                float height;
                float width;
                float y;
                float x;
                ScreenPositionProperty prop = element.getPositionProperty();
                if (prop == null || !HoverUtility.isHovering(x = prop.getScaledX(), y = prop.getScaledY(), width = Math.max(20.0f, prop.getWidth()), height = Math.max(16.0f, prop.getHeight()), (float)click.comp_4798(), (float)click.comp_4799())) continue;
                prop.setDragging(true);
                prop.setStartX((float)click.comp_4798() - x);
                prop.setStartY((float)click.comp_4799() - y);
                this.draggingProperty = prop;
                return true;
            }
        }
        return super.method_25402(click, doubled);
    }

    public boolean method_25406(class_11909 click) {
        if (click.method_74245() == 0 && this.draggingProperty != null) {
            this.draggingProperty.snapToGrid();
            this.draggingProperty.setDragging(false);
            this.draggingProperty = null;
        }
        return super.method_25406(click);
    }

    public boolean method_25421() {
        return false;
    }
}

