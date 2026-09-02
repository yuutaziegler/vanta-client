/* 
 * UI Editor Screen - Edit UI elements 
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
import wtf.opal.client.feature.module.impl.visual.HudSettingsModule; 
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement; 
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule; 
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty; 
import wtf.opal.client.renderer.LiquidGlassRenderer; 
import wtf.opal.client.renderer.NVGRenderer; 
import wtf.opal.client.renderer.repository.FontRepository; 
import wtf.opal.client.renderer.text.NVGTextRenderer; 
import wtf.opal.utility.misc.HoverUtility; 

@Environment(value=EnvType.CLIENT) 
public class UIEditorScreen extends class_437 { 
   
    private static final NVGTextRenderer BOLD_FONT = FontRepository.getFont("inter-bold"); 
    private static final NVGTextRenderer MED_FONT = FontRepository.getFont("inter-medium"); 
   
    private ScreenPositionProperty draggingProperty; 
    private ResizeHandle activeHandle; 
    private float dragStartX; 
    private float dragStartY; 
    private float initialWidth; 
    private float initialHeight; 
   
    private boolean showHelp = true; 
   
    public UIEditorScreen() { 
        super(class_2561.method_30163("UI Editor")); 
    } 
 
    @Override 
    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) { 
        // Apply liquid glass background if HUD settings are enabled 
        HudSettingsModule hudSettings = OpalClient.getInstance().getModuleRepository().getModule(HudSettingsModule.class); 
        if (hudSettings != null && hudSettings.isEnabled()) { 
            float radius = hudSettings.getCornerRadius(); 
            LiquidGlassRenderer.drawGlassPanel(0, 0, this.field_22789, this.field_22790, radius); 
        } 
         boolean frameStarted = NVGRenderer.beginFrame(); 
        
        // Banner 
        float bannerW = 400.0f; 
        float bannerH = 50.0f; 
        float bannerX = ((float)this.field_22789 - bannerW) / 2.0f; 
        float bannerY = 16.0f; 
 
        // Apply liquid glass to banner 
        if (hudSettings != null && hudSettings.isEnabled()) { 
            LiquidGlassRenderer.drawGlassPanel(bannerX, bannerY, bannerW, bannerH, 8.0f); 
        } else { 
            NVGRenderer.roundedRect(bannerX, bannerY, bannerW, bannerH, 8.0f, -586609643); 
        } 
        BOLD_FONT.drawString("UI Editor", bannerX + 15.0f, bannerY + 12.0f, 10.0f, -16718337); 
        MED_FONT.drawString("Drag to move \\u2022 Corners to resize \\u2022 ESC to save", bannerX + 15.0f, bannerY + 30.0f, 7.0f, -7035976); 
       
        // Help button 
        float helpBtnW = 50.0f; 
        float helpBtnH = 18.0f; 
        float helpBtnX = bannerX + bannerW - helpBtnW - 10.0f; 
        float helpBtnY = bannerY + 10.0f; 
        boolean helpHover = HoverUtility.isHovering(helpBtnX, helpBtnY, helpBtnW, helpBtnH, mouseX, mouseY); 
        // Apply liquid glass to help button 
        if (hudSettings != null && hudSettings.isEnabled()) { 
            LiquidGlassRenderer.drawGlassPanel(helpBtnX, helpBtnY, helpBtnW, helpBtnH, 4.0f); 
        } else { 
            NVGRenderer.roundedRect(helpBtnX, helpBtnY, helpBtnW, helpBtnH, 4.0f, helpHover ? 1140850687 : 572665343); 
        } 
        MED_FONT.drawString("Help", helpBtnX + (helpBtnW - MED_FONT.getStringWidth("Help", 6.5f)) / 2.0f, helpBtnY + 11.0f, 6.5f, -1); 
        
        // Help overlay 
        if (this.showHelp) { 
            float helpW = 300.0f; 
            float helpH = 120.0f; 
            float helpX = bannerX + 50.0f; 
            float helpY = bannerY + bannerH + 10.0f; 
            
            // Apply liquid glass to help overlay 
            if (hudSettings != null && hudSettings.isEnabled()) { 
                LiquidGlassRenderer.drawGlassPanel(helpX, helpY, helpW, helpH, 6.0f); 
            } else { 
                NVGRenderer.roundedRect(helpX, helpY, helpW, helpH, 6.0f, -2147483647); 
            } 
            BOLD_FONT.drawString("Controls:", helpX + 10.0f, helpY + 12.0f, 8.0f, -1); 
            MED_FONT.drawString("Left Click + Drag - Move element", helpX + 10.0f, helpY + 30.0f, 7.0f, -4473925); 
            MED_FONT.drawString("Drag corners - Resize element", helpX + 10.0f, helpY + 45.0f, 7.0f, -4473925); 
            MED_FONT.drawString("Right Click - Reset position", helpX + 10.0f, helpY + 60.0f, 7.0f, -4473925); 
            MED_FONT.drawString("H - Toggle this help", helpX + 10.0f, helpY + 75.0f, 7.0f, -4473925); 
            MED_FONT.drawString("ESC - Save and exit", helpX + 10.0f, helpY + 90.0f, 7.0f, -4473925); 
        } 
        
        // Render all overlay elements 
        OverlayModule overlayModule = OpalClient.getInstance().getModuleRepository().getModule(OverlayModule.class); 
        if (overlayModule != null) { 
            for (IOverlayElement element : overlayModule.getElements()) { 
                ScreenPositionProperty prop = element.getPositionProperty(); 
                if (prop == null || !element.isActive()) continue; 
               
                float x = prop.getScaledX(); 
                float y = prop.getScaledY(); 
                float width = Math.max(20.0f, prop.getWidth()); 
                float height = Math.max(16.0f, prop.getHeight()); 
               
                // Handle dragging 
                if (prop.isDragging()) { 
                    prop.setRelativeX((float)mouseX - prop.getStartX()); 
                    prop.setRelativeY((float)mouseY - prop.getStartY()); 
                } 
                
                // Handle resizing 
                if (this.activeHandle != null) { 
                    handleResize(prop, mouseX, mouseY); 
                } 
                
                // Check if hovering 
                boolean isHover = HoverUtility.isHovering(x, y, width, height, mouseX, mouseY); 
               
                // Draw element bounds with liquid glass effect 
                if (isHover || prop.isDragging() || this.activeHandle != null) { 
                    // Apply liquid glass to highlighted element 
                    if (hudSettings != null && hudSettings.isEnabled()) { 
                        LiquidGlassRenderer.drawGlassPanel(x, y, width, height, hudSettings.getCornerRadius()); 
                    } else { 
                        NVGRenderer.roundedRect(x, y, width, height, 4.0f, -14829228); 
                    } 
                    NVGRenderer.roundedRect(x - 3.0f, y - 3.0f, width + 6.0f, height + 6.0f, 5.0f, prop.isDragging() ? 1073800703 : 536929791); 
                    NVGRenderer.roundedRectOutline(x - 3.0f, y - 3.0f, width + 6.0f, height + 6.0f, 5.0f, 2.0f, -16718337); 
                    
                    // Draw resize handles with liquid glass style 
                    drawResizeHandles(x, y, width, height, isHover); 
                    
                    // Draw element name 
                    String name = prop.getId(); 
                    float nw = MED_FONT.getStringWidth(name, 6.5f) + 10.0f; 
                    NVGRenderer.roundedRect(x + width / 2.0f - nw / 2.0f, y - 16.0f, nw, 12.0f, 3.0f, -267842798); 
                    MED_FONT.drawString(name, x + width / 2.0f - MED_FONT.getStringWidth(name, 6.5f) / 2.0f, y - 7.0f, 6.5f, -16718337); 
                } else { 
                    // Apply liquid glass outline for inactive elements 
                    if (hudSettings != null && hudSettings.isEnabled()) { 
                        LiquidGlassRenderer.drawGlassPanel(x - 1.0f, y - 1.0f, width + 2.0f, height + 2.0f, hudSettings.getCornerRadius()); 
                    } else { 
                        NVGRenderer.roundedRectOutline(x - 2.0f, y - 2.0f, width + 4.0f, height + 4.0f, 4.0f, 1.5f, 855696895); 
                    } 
                } 
            } 
        } 
        
        if (frameStarted) { 
            NVGRenderer.endFrameAndReset(true); 
        } 
    } 
 
    private void drawResizeHandles(float x, float y, float width, float height, boolean isHover) { 
        float handleSize = 8.0f; 
        
        // Corner handles with liquid glass style 
        drawHandle(x, y, handleSize, handleSize); // Top-left 
        drawHandle(x + width - handleSize, y, handleSize, handleSize); // Top-right 
        drawHandle(x, y + height - handleSize, handleSize, handleSize); // Bottom-left 
        drawHandle(x + width - handleSize, y + height - handleSize, handleSize, handleSize); // Bottom-right 
        
        // Edge handles 
        drawHandle(x + width / 2.0f - handleSize / 2.0f, y - handleSize / 2.0f, handleSize, handleSize); // Top 
        drawHandle(x + width / 2.0f - handleSize / 2.0f, y + height - handleSize / 2.0f, handleSize, handleSize); // Bottom 
        drawHandle(x - handleSize / 2.0f, y + height / 2.0f - handleSize / 2.0f, handleSize, handleSize); // Left 
        drawHandle(x + width - handleSize / 2.0f, y + height / 2.0f - handleSize / 2.0f, handleSize, handleSize); // Right 
    } 
 
    private void drawHandle(float x, float y, float width, float height) { 
        // Apply liquid glass to handle 
        if (/* hudSettings != null && hudSettings.isEnabled() */ true) { 
            // Simple glass effect for handles 
            NVGRenderer.roundedRect(x, y, width, height, 2.0f, -1); 
        } else { 
            NVGRenderer.roundedRect(x, y, width, height, 2.0f, -1); 
        } 
        NVGRenderer.roundedRectOutline(x, y, width, height, 2.0f, 1.0f, -16718337); 
    } 
 
    private ResizeHandle getHandleAt(float elementX, float elementY, float width, float height, int mouseX, int mouseY) { 
        float handleSize = 10.0f; 
        
        // Corners 
        if (HoverUtility.isHovering(elementX - handleSize / 2.0f, elementY - handleSize / 2.0f, handleSize, handleSize, mouseX, mouseY)) { 
            return ResizeHandle.TOP_LEFT; 
        } 
        if (HoverUtility.isHovering(elementX + width - handleSize / 2.0f, elementY - handleSize / 2.0f, handleSize, handleSize, mouseX, mouseY)) { 
            return ResizeHandle.TOP_RIGHT; 
        } 
        if (HoverUtility.isHovering(elementX - handleSize / 2.0f, elementY + height - handleSize / 2.0f, handleSize, handleSize, mouseX, mouseY)) { 
            return ResizeHandle.BOTTOM_LEFT; 
        } 
        if (HoverUtility.isHovering(elementX + width - handleSize / 2.0f, elementY + height - handleSize / 2.0f, handleSize, handleSize, mouseX, mouseY)) { 
            return ResizeHandle.BOTTOM_RIGHT; 
        } 
        
        // Edges 
        if (HoverUtility.isHovering(elementX + width / 2.0f - handleSize / 2.0f, elementY - handleSize / 2.0f, handleSize, handleSize, mouseX, mouseY)) { 
            return ResizeHandle.TOP; 
        } 
        if (HoverUtility.isHovering(elementX + width / 2.0f - handleSize / 2.0f, elementY + height - handleSize / 2.0f, handleSize, handleSize, mouseX, mouseY)) { 
            return ResizeHandle.BOTTOM; 
        } 
        if (HoverUtility.isHovering(elementX - handleSize / 2.0f, elementY + height / 2.0f - handleSize / 2.0f, handleSize, handleSize, mouseX, mouseY)) { 
            return ResizeHandle.LEFT; 
        } 
        if (HoverUtility.isHovering(elementX + width - handleSize / 2.0f, elementY + height / 2.0f - handleSize / 2.0f, handleSize, handleSize, mouseX, mouseY)) { 
            return ResizeHandle.RIGHT; 
        } 
        
        return null; 
    } 
 
    private void handleResize(ScreenPositionProperty prop, int mouseX, int mouseY) { 
        float dx = mouseX - this.dragStartX; 
        float dy = mouseY - this.dragStartY; 
        
        switch (this.activeHandle) { 
            case TOP_LEFT: 
                prop.setRelativeX(prop.getRelativeX() + dx / Constants.mc.method_22683().method_4486()); 
                prop.setRelativeY(prop.getRelativeY() + dy / Constants.mc.method_22683().method_4502()); 
                prop.setWidth(Math.max(20.0f, this.initialWidth - dx)); 
                prop.setHeight(Math.max(16.0f, this.initialHeight - dy)); 
                break; 
            case TOP_RIGHT: 
                prop.setRelativeY(prop.getRelativeY() + dy / Constants.mc.method_22683().method_4502()); 
                prop.setWidth(Math.max(20.0f, this.initialWidth + dx)); 
                prop.setHeight(Math.max(16.0f, this.initialHeight - dy)); 
                break; 
            case BOTTOM_LEFT: 
                prop.setRelativeX(prop.getRelativeX() + dx / Constants.mc.method_22683().method_4486()); 
                prop.setWidth(Math.max(20.0f, this.initialWidth - dx)); 
                prop.setHeight(Math.max(16.0f, this.initialHeight + dy)); 
                break; 
            case BOTTOM_RIGHT: 
                prop.setWidth(Math.max(20.0f, this.initialWidth + dx)); 
                prop.setHeight(Math.max(16.0f, this.initialHeight + dy)); 
                break; 
            case TOP: 
                prop.setRelativeY(prop.getRelativeY() + dy / Constants.mc.method_22683().method_4502()); 
                prop.setHeight(Math.max(16.0f, this.initialHeight - dy)); 
                break; 
            case BOTTOM: 
                prop.setHeight(Math.max(16.0f, this.initialHeight + dy)); 
                break; 
            case LEFT: 
                prop.setRelativeX(prop.getRelativeX() + dx / Constants.mc.method_22683().method_4486()); 
                prop.setWidth(Math.max(20.0f, this.initialWidth - dx)); 
                break; 
            case RIGHT: 
                prop.setWidth(Math.max(20.0f, this.initialWidth + dx)); 
                break; 
        } 
        
        this.dragStartX = mouseX; 
        this.dragStartY = mouseY; 
    } 
 
    @Override 
    public boolean method_25402(class_11909 click, boolean doubled) { 
        float bannerW = 400.0f; 
        float bannerH = 50.0f; 
        float bannerX = ((float)this.field_22789 - bannerW) / 2.0f; 
        float bannerY = 16.0f; 
        
        // Help button 
        float helpBtnW = 50.0f; 
        float helpBtnH = 18.0f; 
        float helpBtnX = bannerX + bannerW - helpBtnW - 10.0f; 
        float helpBtnY = bannerY + 10.0f; 
        
        if (click.comp_4798() >= (double)helpBtnX && click.comp_4798() <= (double)(helpBtnX + helpBtnW) && click.comp_4799() >= (double)helpBtnY && click.comp_4799() <= (double)(helpBtnY + helpBtnH)) { 
            this.showHelp = !this.showHelp; 
            return true; 
        } 
        
        OverlayModule overlayModule = OpalClient.getInstance().getModuleRepository().getModule(OverlayModule.class); 
        if (overlayModule != null && click.method_74245() == 0) { 
            for (IOverlayElement element : overlayModule.getElements()) { 
                ScreenPositionProperty prop = element.getPositionProperty(); 
                if (prop == null || !element.isActive()) continue; 
                
                float x = prop.getScaledX(); 
                float y = prop.getScaledY(); 
                float width = Math.max(20.0f, prop.getWidth()); 
                float height = Math.max(16.0f, prop.getHeight()); 
                
                // Check for resize handle first 
                ResizeHandle handle = getHandleAt(x, y, width, height, (int)click.comp_4798(), (int)click.comp_4799()); 
                if (handle != null) { 
                    this.activeHandle = handle; 
                    this.draggingProperty = prop; 
                    this.dragStartX = (float)click.comp_4798(); 
                    this.dragStartY = (float)click.comp_4799(); 
                    this.initialWidth = prop.getWidth(); 
                    this.initialHeight = prop.getHeight(); 
                    return true; 
                } 
                
                // Check for element drag 
                if (HoverUtility.isHovering(x, y, width, height, (int)click.comp_4798(), (int)click.comp_4799())) { 
                    prop.setDragging(true); 
                    prop.setStartX((float)click.comp_4798() - x); 
                    prop.setStartY((float)click.comp_4799() - y); 
                    this.draggingProperty = prop; 
                    return true; 
                } 
            } 
        } 
        
        return super.method_25402(click, doubled); 
    } 
 
    @Override 
    public boolean method_25406(class_11909 click) { 
        if (click.method_74245() == 0 && this.draggingProperty != null) { 
            this.draggingProperty.snapToGrid(); 
            this.draggingProperty.setDragging(false); 
            this.draggingProperty = null; 
            this.activeHandle = null; 
        } else if (click.method_74245() == 1 && this.draggingProperty != null) { 
            // Right click - reset position 
            this.draggingProperty._setRelativeX(0.5f); 
            this.draggingProperty._setRelativeY(0.5f); 
            this.draggingProperty.setDragging(false); 
            this.draggingProperty = null; 
            this.activeHandle = null; 
        } 
        return super.method_25406(click); 
    } 
 
    @Override 
    public boolean method_25404(class_11908 keyInput) { 
        if (keyInput.comp_4795() == 72) { // H key 
            this.showHelp = !this.showHelp; 
            return true; 
        } 
        return super.method_25404(keyInput); 
    } 
 
    @Override 
    public boolean method_25421() { 
        return false; 
    } 
     private enum ResizeHandle { 
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, 
        TOP, BOTTOM, LEFT, RIGHT 
    } 
} 
