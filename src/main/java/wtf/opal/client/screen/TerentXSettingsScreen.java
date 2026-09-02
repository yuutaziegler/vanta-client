/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 */
package wtf.opal.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.renderer.LiquidGlassRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.screen.hud.UIEditorScreen;

@Environment(value=EnvType.CLIENT)
public class TerentXSettingsScreen
extends class_437 {
    private final class_437 parent;
    private String selectedTab = "MODS";
    private int scrollOffset = 0;

    public TerentXSettingsScreen(class_437 parent) {
        super((class_2561)class_2561.method_43470((String)"TerentX Settings"));
        this.parent = parent;
    }

    protected void method_25426() {
        super.method_25426();
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"Done"), button -> this.field_22787.method_1507(this.parent)).method_46434(this.field_22789 / 2 - 100, this.field_22790 - 30, 200, 20).method_46431());
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        this.method_25420(context, mouseX, mouseY, delta);
        // Liquid glass header
        boolean frameStarted = NVGRenderer.beginFrame();
        LiquidGlassRenderer.drawGlassPanel((float)(this.field_22789 / 2 - 130), 4.0f, 260.0f, 20.0f, 7.0f);
        if (frameStarted) {
            NVGRenderer.endFrame(true);
        }
        context.method_25300(this.field_22793, "TerentX Client Settings", this.field_22789 / 2, 10, -1);
        this.renderTabs(context, mouseX, mouseY);
        if (this.selectedTab.equals("MODS")) {
            this.renderModsList(context, mouseX, mouseY);
        } else if (this.selectedTab.equals("PERFORMANCE")) {
            this.renderPerformanceSettings(context, mouseX, mouseY);
        } else if (this.selectedTab.equals("VISUAL")) {
            this.renderVisualSettings(context, mouseX, mouseY);
        } else if (this.selectedTab.equals("HUD")) {
            this.renderHudSettings(context, mouseX, mouseY);
        }
        super.method_25394(context, mouseX, mouseY, delta);
    }

    private void renderHudSettings(class_332 context, int mouseX, int mouseY) {
        int y = 60;
        context.method_25303(this.field_22793, "\u00a7lHUD / UI Editor:", 20, y, -1);
        boolean hovered = this.isHovered(mouseX, mouseY, 20, y + 15, this.field_22789 - 40, 24);
        context.method_25294(20, y + 15, this.field_22789 - 20, y + 39, hovered ? -2144128205 : -2145246686);
        context.method_25303(this.field_22793, "\u270f Open UI Editor (drag to move, corners to resize)", 30, y + 22, -1);
        context.method_51433(this.field_22793, "HUD elements are only edited from the UI Editor - not from chat.", 20, y + 48, -5592406, false);
    }

    private void renderTabs(class_332 context, int mouseX, int mouseY) {
        String[] tabs = new String[]{"MODS", "PERFORMANCE", "VISUAL", "HUD"};
        int tabWidth = 100;
        int startX = (this.field_22789 - tabs.length * tabWidth) / 2;
        int y = 30;
        for (int i = 0; i < tabs.length; ++i) {
            String tab = tabs[i];
            int x = startX + i * tabWidth;
            boolean selected = tab.equals(this.selectedTab);
            boolean hovered = this.isHovered(mouseX, mouseY, x, y, tabWidth - 5, 20);
            int color = selected ? -14575885 : (hovered ? -15108398 : -12303292);
            context.method_25294(x, y, x + tabWidth - 5, y + 20, color);
            context.method_25300(this.field_22793, tab, x + (tabWidth - 5) / 2, y + 6, -1);
        }
    }

    private void renderModsList(class_332 context, int mouseX, int mouseY) {
        int startY = 60;
        int itemHeight = 30;
        int y = startY - this.scrollOffset;
        context.method_25303(this.field_22793, "\u00a7lMods:", 20, startY, -1);
        y += 20;
        for (Module module : OpalClient.getInstance().getModuleRepository().getModules()) {
            if (y > startY && y < this.field_22790 - 50) {
                this.renderModuleItem(context, 20, y, this.field_22789 - 40, itemHeight, module, mouseX, mouseY);
            }
            y += itemHeight + 5;
        }
    }

    private void renderModuleItem(class_332 context, int x, int y, int width, int height, Module module, int mouseX, int mouseY) {
        int bgColor;
        boolean hovered = this.isHovered(mouseX, mouseY, x, y, width, height);
        int n = bgColor = module.isEnabled() ? -2147448832 : -2145246686;
        if (hovered) {
            bgColor = module.isEnabled() ? -2147440128 : -2144128205;
        }
        context.method_25294(x, y, x + width, y + height, bgColor);
        context.method_25303(this.field_22793, module.getName(), x + 10, y + 5, -1);
        Object desc = module.getDescription();
        if (((String)desc).length() > 50) {
            desc = ((String)desc).substring(0, 47) + "...";
        }
        context.method_51433(this.field_22793, (String)desc, x + 10, y + 17, -5592406, false);
        String status = module.isEnabled() ? "ON" : "OFF";
        int statusColor = module.isEnabled() ? -16711936 : -65536;
        context.method_51433(this.field_22793, status, x + width - 30, y + (height - 8) / 2, statusColor, false);
    }

    private void renderPerformanceSettings(class_332 context, int mouseX, int mouseY) {
        int y = 60;
        context.method_25303(this.field_22793, "\u00a7lPerformance Settings:", 20, y, -1);
        context.method_51433(this.field_22793, "FPS Boost: Optimizes game performance", 30, y += 20, -5592406, false);
        context.method_51433(this.field_22793, "Reduce Particles: Fewer particles for better FPS", 30, y += 15, -5592406, false);
        context.method_51433(this.field_22793, "Fast Math: Uses faster math calculations", 30, y += 15, -5592406, false);
    }

    private void renderVisualSettings(class_332 context, int mouseX, int mouseY) {
        int y = 60;
        context.method_25303(this.field_22793, "\u00a7lVisual Settings:", 20, y, -1);
        context.method_51433(this.field_22793, "Fullbright: See in the dark", 30, y += 20, -5592406, false);
        context.method_51433(this.field_22793, "Zoom: Zoom like OptiFine (Press C)", 30, y += 15, -5592406, false);
        context.method_51433(this.field_22793, "HUD: Show FPS, Coordinates, etc.", 30, y += 15, -5592406, false);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        String[] tabs = new String[]{"MODS", "PERFORMANCE", "VISUAL", "HUD"};
        int tabWidth = 100;
        int startX = (this.field_22789 - tabs.length * tabWidth) / 2;
        int y = 30;
        for (int i = 0; i < tabs.length; ++i) {
            String tab = tabs[i];
            int x = startX + i * tabWidth;
            if (!this.isHovered((int)mouseX, (int)mouseY, x, y, tabWidth - 5, 20)) continue;
            this.selectedTab = tab;
            this.scrollOffset = 0;
            return true;
        }
        if (this.selectedTab.equals("HUD")) {
            if (this.isHovered((int)mouseX, (int)mouseY, 20, 75, this.field_22789 - 40, 24)) {
                this.field_22787.method_1507((class_437)new UIEditorScreen());
                return true;
            }
            return false;
        }
        if (this.selectedTab.equals("MODS")) {
            int startY = 80;
            int itemHeight = 30;
            int itemY = startY - this.scrollOffset;
            for (Module module : OpalClient.getInstance().getModuleRepository().getModules()) {
                if (this.isHovered((int)mouseX, (int)mouseY, 20, itemY, this.field_22789 - 40, itemHeight)) {
                    module.toggle();
                    return true;
                }
                itemY += itemHeight + 5;
            }
        }
        return false;
    }

    public boolean method_25402(class_11909 click, boolean doubled) {
        if (click.method_74245() != 0) {
            return super.method_25402(click, doubled);
        }
        return this.mouseClicked(click.comp_4798(), click.comp_4799(), 0) || super.method_25402(click, doubled);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.scrollOffset -= (int)(verticalAmount * 15.0);
        this.scrollOffset = Math.max(0, this.scrollOffset);
        return true;
    }

    private boolean isHovered(int mouseX, int mouseY, int x, int y, int width, int height) {
        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;
    }

    public void method_25419() {
        this.field_22787.method_1507(this.parent);
    }
}

