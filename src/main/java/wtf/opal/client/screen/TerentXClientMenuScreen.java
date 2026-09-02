/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11905
 *  net.minecraft.class_11908
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  org.lwjgl.glfw.GLFW
 */
package wtf.opal.client.screen;

import com.ibm.icu.impl.Pair;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import org.lwjgl.glfw.GLFW;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.binding.type.InputType;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.ColorProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.client.renderer.LiquidGlassRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.repository.ImageRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.client.screen.hud.HUDEditorScreen;

@Environment(value=EnvType.CLIENT)
public class TerentXClientMenuScreen
extends class_437 {
    private final class_437 parent;
    private Tab currentTab = Tab.MODS;
    private ModuleCategory selectedCategory = null;
    private Module settingsModule = null;
    private boolean waitingForKey = false;
    private String searchQuery = "";
    private boolean searchFocused = false;
    private int scrollOffset = 0;
    private float smoothScrollOffset = 0.0f;
    private int settingsScrollOffset = 0;
    private float smoothSettingsScrollOffset = 0.0f;
    private NumberProperty draggingNumberProperty = null;
    private ColorProperty activeColorProperty = null;
    private boolean draggingHue = false;
    private static float savedWinX = -1.0f;
    private static float savedWinY = -1.0f;
    private boolean isDraggingWindow = false;
    private float dragOffsetX = 0.0f;
    private float dragOffsetY = 0.0f;
    private static float menuScale = 0.85f;
    private float animScale = 0.85f;
    private float bgFade = 0.0f;
    private String hoveredTooltip = null;

    public TerentXClientMenuScreen(class_437 parent) {
        super((class_2561)class_2561.method_43470((String)"TerentX Client"));
        this.parent = parent;
    }

    protected void method_25426() {
        super.method_25426();
        this.animScale = 0.85f;
        this.bgFade = 0.0f;
        this.hoveredTooltip = null;
        this.scrollOffset = 0;
        this.smoothScrollOffset = 0.0f;
    }

    private void renderLiquidGlassBackground(float winX, float winY, float winW, float winH, float menuScale, int fadeAlpha, class_332 context) {
        HudSettingsModule hudSettings = OpalClient.getInstance().getModuleRepository().getModule(HudSettingsModule.class);
        if (hudSettings == null || !hudSettings.isEnabled()) {
            // Fallback to original solid background when HUD settings are disabled
            int screenW = Constants.mc.method_22683().method_4486();
            int screenH = Constants.mc.method_22683().method_4502();
            context.method_25294(0, 0, screenW, screenH, fadeAlpha << 24);
            NVGRenderer.roundedRect(winX, winY, winW, winH, 10.0f * menuScale, -267249108);
            NVGRenderer.roundedRect(winX, winY, sidebarWidth, winH, 10.0f * menuScale, -183692510);
            return;
        }

        float radius = hudSettings.getCornerRadius();
        float glassOpacity = hudSettings.getGlassOpacity();
        float frostOpacity = hudSettings.getGlassOpacity() * 0.5f;

        // Apply liquid glass effect to the main window background
        LiquidGlassRenderer.drawGlassPanel(winX, winY, winW, winH, radius);

        // Draw glass panel outline
        LiquidGlassRenderer.drawGlassPanel(winX, winY, winW, winH, radius);

        // Darken the rest of the screen - but check if Right Shift is held
        // If Right Shift is held, reduce the darkening intensity
        boolean rightShiftHeld = GLFW.glfwGetKey((long)Constants.mc.method_22683().method_4490(), 340) == 1;
        float darkeningIntensity = rightShiftHeld ? 0.3f : 1.0f; // 30% intensity when Right Shift held
        int fadeColor = (int)(120.0f * this.bgFade * darkeningIntensity);
        int screenW = Constants.mc.method_22683().method_4486();
        int screenH = Constants.mc.method_22683().method_4502();
        context.method_25294(0, 0, screenW, screenH, fadeColor << 24);

        // Draw sidebar with glass effect
        NVGRenderer.roundedRect(winX, winY, sidebarWidth, winH, 10.0f * menuScale, -183692510);
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        Tab[] tabs;
        this.bgFade += (1.0f - this.bgFade) * 0.22f;
        this.animScale += (1.0f - this.animScale) * 0.25f;
        this.smoothScrollOffset += ((float)this.scrollOffset - this.smoothScrollOffset) * 0.3f;
        this.smoothSettingsScrollOffset += ((float)this.settingsScrollOffset - this.smoothSettingsScrollOffset) * 0.3f;
        this.hoveredTooltip = null;
        int fadeAlpha = (int)(120.0f * this.bgFade);
        float screenW = Constants.mc.method_22683().method_4486();
        float screenH = Constants.mc.method_22683().method_4502();
        float baseW = 680.0f;
        float baseH = 420.0f;
        float winW = Math.min(baseW * menuScale, screenW - 20.0f);
        float winH = Math.min(baseH * menuScale, screenH - 20.0f);
        if (savedWinX < 0.0f || savedWinY < 0.0f) {
            savedWinX = (screenW - winW) / 2.0f;
            savedWinY = (screenH - winH) / 2.0f;
        }
        if (this.isDraggingWindow) {
            savedWinX = (float)mouseX - this.dragOffsetX;
            savedWinY = (float)mouseY - this.dragOffsetY;
        }
        savedWinX = Math.max(0.0f, Math.min(screenW - winW, savedWinX));
        savedWinY = Math.max(16.0f, Math.min(screenH - winH, savedWinY));
        float winX = savedWinX;
        float winY = savedWinY;
        float sidebarWidth = 145.0f * menuScale;
        float mainX = winX + sidebarWidth;
        float mainW = winW - sidebarWidth;
        boolean frameStarted = NVGRenderer.beginFrame();
        NVGTextRenderer boldFont = FontRepository.getFont("inter-bold");
        NVGTextRenderer medFont = FontRepository.getFont("inter-medium");
        NVGTextRenderer regFont = FontRepository.getFont("inter-regular");
        renderLiquidGlassBackground(winX, winY, winW, winH, menuScale, fadeAlpha, context);
        String scaleInfo = String.format("Scale: %.1fx", Float.valueOf(menuScale));
        float scaleInfoW = medFont.getStringWidth(scaleInfo, 7.5f);
        float scaleCenterX = (screenW - scaleInfoW) / 2.0f;
        float scaleY = Math.max(4.0f, winY - 14.0f);
        boolean minusHover = (float)mouseX >= scaleCenterX - 24.0f && (float)mouseX <= scaleCenterX - 8.0f && (float)mouseY >= scaleY - 2.0f && (float)mouseY <= scaleY + 12.0f;
        NVGRenderer.roundedRect(scaleCenterX - 24.0f, scaleY - 2.0f, 14.0f, 13.0f, 3.0f, minusHover ? 1711310079 : 0x33000000);
        boldFont.drawString("-", scaleCenterX - 19.0f, scaleY + 7.0f, 8.0f, -1);
        medFont.drawString(scaleInfo, scaleCenterX, scaleY + 7.0f, 7.5f, -7035976);
        boolean plusHover = (float)mouseX >= scaleCenterX + scaleInfoW + 8.0f && (float)mouseX <= scaleCenterX + scaleInfoW + 24.0f && (float)mouseY >= scaleY - 2.0f && (float)mouseY <= scaleY + 12.0f;
        NVGRenderer.roundedRect(scaleCenterX + scaleInfoW + 8.0f, scaleY - 2.0f, 14.0f, 13.0f, 3.0f, plusHover ? 1711310079 : 0x33000000);
        boldFont.drawString("+", scaleCenterX + scaleInfoW + 12.0f, scaleY + 7.0f, 8.0f, -1);
        NVGRenderer.roundedRect(winX, winY, winW, winH, 10.0f * menuScale, -267249108);
        NVGRenderer.roundedRect(winX, winY, sidebarWidth, winH, 10.0f * menuScale, -183692510);
        NVGImageRenderer logoImg = ImageRepository.getImage("logo.png");
        float logoSize = 28.0f * menuScale;
        if (logoImg != null) {
            logoImg.drawImage(winX + 12.0f * menuScale, winY + 12.0f * menuScale, logoSize, logoSize);
            boldFont.drawString("TerentX", winX + 16.0f * menuScale + logoSize, winY + 22.0f * menuScale, 10.0f * menuScale, -1);
            medFont.drawString("PvP Client", winX + 16.0f * menuScale + logoSize, winY + 34.0f * menuScale, 6.5f * menuScale, -16743169);
        } else {
            boldFont.drawString("TerentX", winX + 14.0f * menuScale, winY + 22.0f * menuScale, 11.0f * menuScale, -1);
            medFont.drawString("PvP Client", winX + 14.0f * menuScale, winY + 34.0f * menuScale, 6.5f * menuScale, -16743169);
        }
        long enabledCount = OpalClient.getInstance().getModuleRepository().getModules().stream().filter(Module::isEnabled).count();
        String statsText = "\u25cf " + enabledCount + " / " + OpalClient.getInstance().getModuleRepository().getModules().size() + " Active";
        medFont.drawString(statsText, winX + 14.0f * menuScale, winY + 46.0f * menuScale, 6.5f * menuScale, -16718218);
        NVGRenderer.roundedRect(winX + 10.0f * menuScale, winY + 54.0f * menuScale, sidebarWidth - 20.0f * menuScale, 1.0f, 0.0f, 0x22FFFFFF);
        float sideY = winY + 62.0f * menuScale;
        medFont.drawString("NAVIGATION", winX + 14.0f * menuScale, sideY, 6.0f * menuScale, -11180425);
        sideY += 10.0f * menuScale;
        for (Tab tab : tabs = Tab.values()) {
            boolean hover;
            boolean active = this.currentTab == tab && this.settingsModule == null;
            boolean bl = hover = (float)mouseX >= winX + 8.0f * menuScale && (float)mouseX <= winX + sidebarWidth - 8.0f * menuScale && (float)mouseY >= sideY && (float)mouseY <= sideY + 24.0f * menuScale;
            int tabBg = active ? 1140884735 : (hover ? 0x14FFFFFF : 0);
            NVGRenderer.roundedRect(winX + 8.0f * menuScale, sideY, sidebarWidth - 16.0f * menuScale, 24.0f * menuScale, 6.0f * menuScale, tabBg);
            if (active) {
                NVGRenderer.roundedRect(winX + 8.0f * menuScale, sideY + 4.0f * menuScale, 2.5f * menuScale, 16.0f * menuScale, 1.0f, -16743169);
            }
            int textCol = active ? -1 : (hover ? 0xDDDDDD : -7824982);
            medFont.drawString(tab.icon + "  " + tab.displayName, winX + 16.0f * menuScale, sideY + 15.0f * menuScale, 7.5f * menuScale, textCol);
            sideY += 27.0f * menuScale;
        }
        float hudBtnY = winY + winH - 34.0f * menuScale;
        boolean hudHover = (float)mouseX >= winX + 8.0f * menuScale && (float)mouseX <= winX + sidebarWidth - 8.0f * menuScale && (float)mouseY >= hudBtnY && (float)mouseY <= hudBtnY + 26.0f * menuScale;
        NVGRenderer.roundedRect(winX + 8.0f * menuScale, hudBtnY, sidebarWidth - 16.0f * menuScale, 26.0f * menuScale, 6.0f * menuScale, hudHover ? -16739862 : 570484223);
        float hudW = boldFont.getStringWidth("\u270f Edit HUD", 7.5f * menuScale);
        boldFont.drawString("\u270f Edit HUD", winX + (sidebarWidth - hudW) / 2.0f, hudBtnY + 16.0f * menuScale, 7.5f * menuScale, -1);
        if (this.settingsModule != null) {
            this.renderSettings(mouseX, mouseY, mainX, winY, mainW, winH, boldFont, medFont, regFont);
        } else {
            this.renderModules(mouseX, mouseY, mainX, winY, mainW, winH, boldFont, medFont, regFont);
        }
        if (this.hoveredTooltip != null) {
            float textW = medFont.getStringWidth(this.hoveredTooltip, 7.5f);
            float tooltipW = textW + 14.0f;
            float tooltipH = 18.0f;
            float tx = (float)mouseX + 10.0f;
            float ty = (float)mouseY - 8.0f;
            if (tx + tooltipW > screenW - 8.0f) {
                tx = (float)mouseX - tooltipW - 6.0f;
            }
            if (ty + tooltipH > screenH - 8.0f) {
                ty = screenH - tooltipH - 8.0f;
            }
            NVGRenderer.roundedRect(tx, ty, tooltipW, tooltipH, 4.0f, -100070638);
            medFont.drawString(this.hoveredTooltip, tx + 7.0f, ty + 12.0f, 7.5f, -1);
        }
        if (frameStarted) {
            NVGRenderer.endFrameAndReset(true);
        }
    }

    private void renderModules(int mouseX, int mouseY, float mainX, float winY, float mainW, float winH, NVGTextRenderer boldFont, NVGTextRenderer medFont, NVGTextRenderer regFont) {
        boolean searchHover;
        boolean allHover;
        float startX = mainX + 14.0f * menuScale;
        float startY = winY + 12.0f * menuScale;
        float catX = startX;
        boolean allActive = this.selectedCategory == null;
        boolean bl = allHover = (float)mouseX >= catX && (float)mouseX <= catX + 38.0f * menuScale && (float)mouseY >= startY && (float)mouseY <= startY + 20.0f * menuScale;
        int allBg = allActive ? 1073800703 : (allHover ? 0x20FFFFFF : 0x10FFFFFF);
        NVGRenderer.roundedRect(catX, startY, 38.0f * menuScale, 20.0f * menuScale, 5.0f * menuScale, allBg);
        float allW = medFont.getStringWidth("All", 7.5f * menuScale);
        medFont.drawString("All", catX + (38.0f * menuScale - allW) / 2.0f, startY + 13.0f * menuScale, 7.5f * menuScale, allActive ? -1 : -7824982);
        catX += 42.0f * menuScale;
        for (ModuleCategory cat : ModuleCategory.VALUES) {
            boolean hover;
            if (cat == ModuleCategory.MISC) continue;
            String txt = cat.getName();
            float w = medFont.getStringWidth(txt, 7.5f * menuScale) + 12.0f * menuScale;
            boolean active = this.selectedCategory == cat;
            boolean bl2 = hover = (float)mouseX >= catX && (float)mouseX <= catX + w && (float)mouseY >= startY && (float)mouseY <= startY + 20.0f * menuScale;
            int bg = active ? (cat == ModuleCategory.HACKED || cat == ModuleCategory.EXPLOIT ? 1157568324 : 1073800703) : (hover ? 0x20FFFFFF : 0x10FFFFFF);
            NVGRenderer.roundedRect(catX, startY, w, 20.0f * menuScale, 5.0f * menuScale, bg);
            medFont.drawString(txt, catX + 6.0f * menuScale, startY + 13.0f * menuScale, 7.5f * menuScale, active ? -1 : -7824982);
            if ((catX += w + 4.0f * menuScale) > mainX + mainW - 130.0f * menuScale) break;
        }
        float searchW = 110.0f * menuScale;
        float searchX = mainX + mainW - searchW - 14.0f * menuScale;
        float searchY = startY;
        boolean bl3 = searchHover = (float)mouseX >= searchX && (float)mouseX <= searchX + searchW && (float)mouseY >= searchY && (float)mouseY <= searchY + 20.0f * menuScale;
        int searchBg = this.searchFocused ? 1426122239 : (searchHover ? 0x25FFFFFF : 0x15FFFFFF);
        NVGRenderer.roundedRect(searchX, searchY, searchW, 20.0f * menuScale, 5.0f * menuScale, searchBg);
        String displaySearch = this.searchQuery.isEmpty() && !this.searchFocused ? "\ud83d\udd0d Search..." : this.searchQuery + (this.searchFocused && System.currentTimeMillis() / 500L % 2L == 0L ? "|" : "");
        medFont.drawString(displaySearch, searchX + 8.0f * menuScale, searchY + 13.0f * menuScale, 7.0f * menuScale, this.searchQuery.isEmpty() && !this.searchFocused ? -10061944 : -1);
        if (!this.searchQuery.isEmpty()) {
            medFont.drawString("\u2715", searchX + searchW - 12.0f * menuScale, searchY + 13.0f * menuScale, 7.0f * menuScale, -7824982);
        }
        List<Module> modules = this.getFilteredModules();
        int columns = Math.max(1, (int)((mainW - 28.0f * menuScale) / (160.0f * menuScale)));
        float spacing = 8.0f * menuScale;
        float cardW = (mainW - 28.0f * menuScale - (float)(columns - 1) * spacing) / (float)columns;
        float cardH = 56.0f * menuScale;
        float gridY = startY + 28.0f * menuScale;
        float clipY = winY + winH - 12.0f * menuScale;
        int totalRows = (int)Math.ceil((double)modules.size() / (double)columns);
        int maxScroll = (int)Math.max(0.0f, (float)totalRows * (cardH + spacing) - (winH - 55.0f * menuScale));
        NVGRenderer.scissor(mainX, gridY - 4.0f, mainW, clipY - gridY + 8.0f, () -> {
            for (int i = 0; i < modules.size(); ++i) {
                boolean isHovered;
                Module mod = (Module)modules.get(i);
                int col = i % columns;
                int row = i / columns;
                float x = startX + (float)col * (cardW + spacing);
                float y = gridY + (float)row * (cardH + spacing) - this.smoothScrollOffset;
                if (y + cardH < gridY - 20.0f || y > clipY + 20.0f) continue;
                boolean bl = isHovered = (float)mouseX >= x && (float)mouseX <= x + cardW && (float)mouseY >= y && (float)mouseY <= y + cardH;
                if (isHovered && (float)mouseY >= gridY && (float)mouseY <= clipY) {
                    this.hoveredTooltip = mod.getDescription();
                }
                int cardColor = mod.isEnabled() ? (isHovered ? -300010931 : -585619394) : (isHovered ? -300539854 : -871360730);
                NVGRenderer.roundedRect(x, y, cardW, cardH, 6.0f * menuScale, cardColor);
                if (mod.isEnabled()) {
                    NVGRenderer.roundedRect(x, y, 2.5f * menuScale, cardH, 2.0f * menuScale, -16743169);
                }
                String tag = mod.getCategory().getName().toUpperCase();
                int tagCol = mod.isEnabled() ? -16743169 : -10193781;
                int tagBg = mod.isEnabled() ? 570459391 : 288571733;
                float tagW = boldFont.getStringWidth(tag, 5.5f * menuScale) + 8.0f * menuScale;
                NVGRenderer.roundedRect(x + 7.0f * menuScale, y + 6.0f * menuScale, tagW, 11.0f * menuScale, 3.0f * menuScale, tagBg);
                boldFont.drawString(tag, x + 11.0f * menuScale, y + 14.0f * menuScale, 5.5f * menuScale, tagCol);
                boldFont.drawString(mod.getName(), x + 12.0f * menuScale + tagW + 2.0f, y + 14.0f * menuScale, 8.5f * menuScale, mod.isEnabled() ? -1 : (isHovered ? -920071 : -3418655));
                Object desc = mod.getDescription();
                if (desc == null || ((String)desc).isEmpty()) {
                    desc = "TerentX Module";
                }
                if (((String)desc).length() > 32) {
                    desc = ((String)desc).substring(0, 29) + "...";
                }
                regFont.drawString((String)desc, x + 7.0f * menuScale, y + 26.0f * menuScale, 6.5f * menuScale, -7824982);
                String keyStr = this.getModuleKeyName(mod);
                float kw = medFont.getStringWidth(keyStr, 6.0f * menuScale) + 8.0f * menuScale;
                float keyX = x + cardW - kw - 34.0f * menuScale;
                float keyY = y + 36.0f * menuScale;
                NVGRenderer.roundedRect(keyX, keyY, kw, 13.0f * menuScale, 3.0f * menuScale, 0x30000000);
                medFont.drawString(keyStr, keyX + 4.0f * menuScale, keyY + 9.0f * menuScale, 6.0f * menuScale, -7824982);
                if (!mod.getPropertyList().isEmpty()) {
                    float gearX = x + cardW - 28.0f * menuScale;
                    float gearY = y + 6.0f * menuScale;
                    boolean gearHover = (float)mouseX >= gearX && (float)mouseX <= gearX + 14.0f * menuScale && (float)mouseY >= gearY && (float)mouseY <= gearY + 14.0f * menuScale;
                    NVGRenderer.roundedRect(gearX, gearY, 14.0f * menuScale, 14.0f * menuScale, 3.0f * menuScale, gearHover ? 855672063 : 0x11FFFFFF);
                    medFont.drawString("\u2699", gearX + 2.0f * menuScale, gearY + 10.0f * menuScale, 7.0f * menuScale, gearHover ? -16743169 : -7824982);
                }
                float switchWidth = 24.0f * menuScale;
                float switchHeight = 13.0f * menuScale;
                float switchX = x + cardW - switchWidth - 6.0f * menuScale;
                float switchY = y + 36.0f * menuScale;
                int pillBg = mod.isEnabled() ? -16743169 : -14800581;
                NVGRenderer.roundedRect(switchX, switchY, switchWidth, switchHeight, 6.5f * menuScale, pillBg);
                float thumbX = mod.isEnabled() ? switchX + switchWidth - 11.0f * menuScale : switchX + 2.0f * menuScale;
                NVGRenderer.roundedRect(thumbX, switchY + 1.5f * menuScale, 9.5f * menuScale, 10.0f * menuScale, 5.0f * menuScale, -1);
            }
        });
        if (maxScroll > 0) {
            float scrollTrackX = mainX + mainW - 5.0f;
            float scrollTrackY = gridY;
            float scrollTrackH = clipY - gridY;
            float thumbH = Math.max(16.0f, scrollTrackH / (scrollTrackH + (float)maxScroll) * scrollTrackH);
            float thumbY = scrollTrackY + this.smoothScrollOffset / (float)maxScroll * (scrollTrackH - thumbH);
            NVGRenderer.roundedRect(scrollTrackX, scrollTrackY, 2.5f, scrollTrackH, 1.25f, 0x11FFFFFF);
            NVGRenderer.roundedRect(scrollTrackX, thumbY, 2.5f, thumbH, 1.25f, 1711334911);
        }
    }

    private void renderSettings(int mouseX, int mouseY, float mainX, float winY, float mainW, float winH, NVGTextRenderer boldFont, NVGTextRenderer medFont, NVGTextRenderer regFont) {
        float x = mainX + 16.0f * menuScale;
        float y = winY + 14.0f * menuScale;
        float w = mainW - 32.0f * menuScale;
        boolean backHover = (float)mouseX >= x && (float)mouseX <= x + 52.0f * menuScale && (float)mouseY >= y && (float)mouseY <= y + 20.0f * menuScale;
        NVGRenderer.roundedRect(x, y, 52.0f * menuScale, 20.0f * menuScale, 5.0f * menuScale, backHover ? 1140909567 : 570484223);
        medFont.drawString("\u2190 Back", x + 10.0f * menuScale, y + 13.0f * menuScale, 7.0f * menuScale, -1);
        boldFont.drawString(this.settingsModule.getName() + " Settings", x + 60.0f * menuScale, y + 14.0f * menuScale, 9.0f * menuScale, -1);
        float topSwitchW = 26.0f * menuScale;
        float topSwitchH = 14.0f * menuScale;
        float topSwitchX = x + w - topSwitchW - 4.0f;
        float topSwitchY = y + 3.0f;
        int pillBg = this.settingsModule.isEnabled() ? -16718218 : -14406595;
        NVGRenderer.roundedRect(topSwitchX, topSwitchY, topSwitchW, topSwitchH, 7.0f * menuScale, pillBg);
        float thumbX = this.settingsModule.isEnabled() ? topSwitchX + topSwitchW - 12.0f * menuScale : topSwitchX + 2.0f * menuScale;
        NVGRenderer.roundedRect(thumbX, topSwitchY + 2.0f * menuScale, 10.0f * menuScale, 10.0f * menuScale, 5.0f * menuScale, -1);
        float clipY = winY + winH - 12.0f * menuScale;
        NVGRenderer.scissor(mainX, winY + 38.0f * menuScale, mainW, clipY - (winY + 38.0f * menuScale), () -> {
            float setY = y + 30.0f * menuScale - this.smoothSettingsScrollOffset;
            NVGRenderer.roundedRect(x, setY, w, 28.0f * menuScale, 5.0f * menuScale, -585752526);
            boldFont.drawString("Keybind", x + 10.0f * menuScale, setY + 17.0f * menuScale, 7.5f * menuScale, -1);
            String bindText = this.waitingForKey ? "[ Press key... ]" : "[" + this.getModuleKeyName(this.settingsModule) + "]";
            float bindW = medFont.getStringWidth(bindText, 7.0f * menuScale) + 10.0f * menuScale;
            float bindBtnX = x + w - bindW - 10.0f * menuScale;
            NVGRenderer.roundedRect(bindBtnX, setY + 6.0f * menuScale, bindW, 16.0f * menuScale, 3.5f * menuScale, this.waitingForKey ? -16743169 : 1140884735);
            medFont.drawString(bindText, bindBtnX + 5.0f * menuScale, setY + 16.0f * menuScale, 7.0f * menuScale, this.waitingForKey ? -1 : -16743169);
            setY += 33.0f * menuScale;
            for (Property<?> prop : this.settingsModule.getPropertyList()) {
                float rowH = prop instanceof ColorProperty ? 52.0f * menuScale : 28.0f * menuScale;
                NVGRenderer.roundedRect(x, setY, w, rowH, 5.0f * menuScale, -870965198);
                boldFont.drawString(prop.getId(), x + 10.0f * menuScale, setY + 17.0f * menuScale, 7.5f * menuScale, -1);
                if (prop instanceof BooleanProperty) {
                    BooleanProperty bp = (BooleanProperty)prop;
                    float swW = 24.0f * menuScale;
                    float swH = 13.0f * menuScale;
                    float swX = x + w - swW - 10.0f * menuScale;
                    float swY = setY + 7.5f * menuScale;
                    int pBg = bp.getValue() != false ? -16743169 : -14800581;
                    NVGRenderer.roundedRect(swX, swY, swW, swH, 6.5f * menuScale, pBg);
                    float tX = bp.getValue() != false ? swX + swW - 11.0f * menuScale : swX + 2.0f * menuScale;
                    NVGRenderer.roundedRect(tX, swY + 1.5f * menuScale, 9.5f * menuScale, 10.0f * menuScale, 5.0f * menuScale, -1);
                } else if (prop instanceof ModeProperty) {
                    ModeProperty mp = (ModeProperty)prop;
                    String modeText = "< " + ((Enum)mp.getValue()).toString() + " >";
                    float mw = medFont.getStringWidth(modeText, 7.0f * menuScale) + 12.0f * menuScale;
                    float btnX = x + w - mw - 10.0f * menuScale;
                    NVGRenderer.roundedRect(btnX, setY + 6.0f * menuScale, mw, 16.0f * menuScale, 3.5f * menuScale, 855672063);
                    medFont.drawString(modeText, btnX + 6.0f * menuScale, setY + 16.0f * menuScale, 7.0f * menuScale, -16743169);
                } else if (prop instanceof NumberProperty) {
                    NumberProperty np = (NumberProperty)prop;
                    float sliderW = 90.0f * menuScale;
                    float sliderX = x + w - sliderW - 55.0f * menuScale;
                    float sliderY = setY + 12.0f * menuScale;
                    float sliderH = 4.5f * menuScale;
                    float min = (float)np.getMinValue();
                    float max = (float)np.getMaxValue();
                    float val = ((Double)np.getValue()).floatValue();
                    float prog = Math.max(0.0f, Math.min(1.0f, (val - min) / (max - min)));
                    NVGRenderer.roundedRect(sliderX, sliderY, sliderW, sliderH, 2.25f * menuScale, -14800581);
                    if (prog > 0.0f) {
                        NVGRenderer.roundedRect(sliderX, sliderY, sliderW * prog, sliderH, 2.25f * menuScale, -16743169);
                    }
                    NVGRenderer.roundedRect(sliderX + sliderW * prog - 2.5f, sliderY - 1.5f, 5.0f * menuScale, 7.5f * menuScale, 2.5f * menuScale, -1);
                    String valStr = String.format("%.1f", Float.valueOf(val));
                    float valW = 32.0f * menuScale;
                    float valX = x + w - valW - 10.0f * menuScale;
                    NVGRenderer.roundedRect(valX, setY + 6.0f * menuScale, valW, 16.0f * menuScale, 3.5f * menuScale, 0x15FFFFFF);
                    medFont.drawString(valStr, valX + 6.0f * menuScale, setY + 16.0f * menuScale, 7.0f * menuScale, -1);
                } else if (prop instanceof ColorProperty) {
                    ColorProperty cp = (ColorProperty)prop;
                    int colVal = (Integer)cp.getValue();
                    float swatchX = x + w - 24.0f * menuScale;
                    float swatchY = setY + 6.0f * menuScale;
                    NVGRenderer.roundedRect(swatchX, swatchY, 16.0f * menuScale, 16.0f * menuScale, 4.0f * menuScale, 0xFF000000 | colVal);
                    float hueX = x + 10.0f * menuScale;
                    float hueY = setY + 28.0f * menuScale;
                    float hueW = w - 20.0f * menuScale;
                    float hueH = 8.0f * menuScale;
                    for (int hIdx = 0; hIdx < (int)hueW; ++hIdx) {
                        float hue = (float)hIdx / hueW;
                        int c = Color.HSBtoRGB(hue, 1.0f, 1.0f);
                        NVGRenderer.rect(hueX + (float)hIdx, hueY, 1.2f, hueH, 0xFF000000 | c);
                    }
                }
                setY += rowH + 5.0f * menuScale;
            }
        });
    }

    private List<Module> getFilteredModules() {
        Collection<Module> all = OpalClient.getInstance().getModuleRepository().getModules();
        ArrayList<Module> filtered = new ArrayList<Module>();
        for (Module m : all) {
            if (m.getName().equalsIgnoreCase("Settings Menu") || m.getName().equalsIgnoreCase("ClickGUI") || this.isCheatModule(m) || m.getCategory() == ModuleCategory.HACKED || m.getCategory() == ModuleCategory.EXPLOIT) continue;
            if (!this.searchQuery.isEmpty()) {
                String q = this.searchQuery.toLowerCase();
                if (!m.getName().toLowerCase().contains(q) && !m.getDescription().toLowerCase().contains(q)) continue;
            }
            if (this.selectedCategory != null) {
                if (this.selectedCategory != m.getCategory()) continue;
                filtered.add(m);
                continue;
            }
            if (this.currentTab == Tab.MODS) {
                filtered.add(m);
                continue;
            }
            if (this.currentTab == Tab.HUD) {
                if (m.getCategory() != ModuleCategory.HUD && !m.getName().toLowerCase().contains("hud") && !m.getName().toLowerCase().contains("keystroke")) continue;
                filtered.add(m);
                continue;
            }
            if (this.currentTab == Tab.VISUAL) {
                if (m.getCategory() != ModuleCategory.VISUAL) continue;
                filtered.add(m);
                continue;
            }
            if (this.currentTab != Tab.PERFORMANCE || m.getCategory() != ModuleCategory.PERFORMANCE && m.getCategory() != ModuleCategory.WORLD) continue;
            filtered.add(m);
        }
        filtered.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return filtered;
    }

    private boolean isCheatModule(Module m) {
        String name = m.getName().toLowerCase();
        return name.contains("aura") || name.contains("velocity") || name.contains("fly") || name.contains("flight") || name.contains("scaffold") || name.contains("reach") || name.contains("clicker") || name.contains("stealer") || name.contains("esp") || name.contains("tracer") || name.contains("blink") || name.contains("crash") || name.contains("crasher") || name.contains("spam") || name.contains("disabler") || name.contains("phase") || name.contains("spider") || name.contains("criticals") || name.contains("antivoid") || name.contains("piercing");
    }

    private String getModuleKeyName(Module module) {
        Object name;
        Optional<Pair<Integer, InputType>> bind = OpalClient.getInstance().getBindRepository().getBindingService().getKeyFromBindable(module);
        if (bind.isPresent() && (name = OpalClient.getInstance().getBindRepository().getNameFromInteger((Integer)bind.get().first)) != null) {
            if (((String)name).startsWith("KEY_")) {
                name = ((String)name).substring(4);
            }
            if (((String)name).length() > 5) {
                name = ((String)name).substring(0, 4) + ".";
            }
            return name;
        }
        return "NONE";
    }

    public boolean method_25402(class_11909 click, boolean doubled) {
        double mouseX = click.comp_4798();
        double mouseY = click.comp_4799();
        int button = click.method_74245();
        float screenW = Constants.mc.method_22683().method_4486();
        float screenH = Constants.mc.method_22683().method_4502();
        float baseW = 680.0f;
        float baseH = 420.0f;
        float winW = Math.min(baseW * menuScale, screenW - 20.0f);
        float winH = Math.min(baseH * menuScale, screenH - 20.0f);
        if (savedWinX < 0.0f || savedWinY < 0.0f) {
            savedWinX = (screenW - winW) / 2.0f;
            savedWinY = (screenH - winH) / 2.0f;
        }
        float winX = savedWinX;
        float winY = savedWinY;
        float sidebarWidth = 145.0f * menuScale;
        float mainX = winX + sidebarWidth;
        float mainW = winW - sidebarWidth;
        float scaleInfoW = 45.0f;
        float scaleCenterX = (screenW - scaleInfoW) / 2.0f;
        float scaleY = Math.max(4.0f, winY - 14.0f);
        if (mouseY >= (double)(scaleY - 4.0f) && mouseY <= (double)(scaleY + 14.0f)) {
            if (mouseX >= (double)(scaleCenterX - 26.0f) && mouseX <= (double)(scaleCenterX - 6.0f)) {
                menuScale = Math.max(0.45f, menuScale - 0.05f);
                return true;
            }
            if (mouseX >= (double)(scaleCenterX + scaleInfoW + 6.0f) && mouseX <= (double)(scaleCenterX + scaleInfoW + 26.0f)) {
                menuScale = Math.min(1.45f, menuScale + 0.05f);
                return true;
            }
        }
        if (button == 0 && mouseX >= (double)winX && mouseX <= (double)(winX + sidebarWidth) && mouseY >= (double)winY && mouseY <= (double)(winY + 54.0f * menuScale)) {
            this.isDraggingWindow = true;
            this.dragOffsetX = (float)mouseX - winX;
            this.dragOffsetY = (float)mouseY - winY;
            return true;
        }
        if (this.settingsModule != null) {
            float x = mainX + 16.0f * menuScale;
            float y = winY + 14.0f * menuScale;
            float w = mainW - 32.0f * menuScale;
            if (mouseX >= (double)x && mouseX <= (double)(x + 52.0f * menuScale) && mouseY >= (double)y && mouseY <= (double)(y + 20.0f * menuScale)) {
                this.settingsModule = null;
                this.waitingForKey = false;
                this.settingsScrollOffset = 0;
                return true;
            }
            float topSwitchW = 26.0f * menuScale;
            float topSwitchX = x + w - topSwitchW - 4.0f;
            float f = y + 3.0f;
            if (mouseX >= (double)topSwitchX && mouseX <= (double)(topSwitchX + topSwitchW) && mouseY >= (double)f && mouseY <= (double)(f + 14.0f * menuScale)) {
                this.settingsModule.toggle();
                return true;
            }
            float setY = y + 30.0f * menuScale - this.smoothSettingsScrollOffset;
            if (mouseX >= (double)x && mouseX <= (double)(x + w) && mouseY >= (double)setY && mouseY <= (double)(setY + 28.0f * menuScale)) {
                if (button == 0) {
                    this.waitingForKey = !this.waitingForKey;
                } else if (button == 1) {
                    OpalClient.getInstance().getBindRepository().getBindingService().clearBindings(this.settingsModule);
                    this.waitingForKey = false;
                }
                return true;
            }
            setY += 33.0f * menuScale;
            for (Property<?> prop : this.settingsModule.getPropertyList()) {
                float rowH;
                float f2 = rowH = prop instanceof ColorProperty ? 52.0f * menuScale : 28.0f * menuScale;
                if (mouseX >= (double)x && mouseX <= (double)(x + w) && mouseY >= (double)setY && mouseY <= (double)(setY + rowH)) {
                    if (prop instanceof BooleanProperty) {
                        BooleanProperty bp;
                        bp.setValue((bp = (BooleanProperty)prop).getValue() == false);
                    } else if (prop instanceof ModeProperty) {
                        ModeProperty mp = (ModeProperty)prop;
                        mp.cycle(true);
                    } else if (prop instanceof NumberProperty) {
                        NumberProperty np = (NumberProperty)prop;
                        float sliderW = 90.0f * menuScale;
                        float sliderX = x + w - sliderW - 55.0f * menuScale;
                        if (mouseX >= (double)sliderX && mouseX <= (double)(sliderX + sliderW)) {
                            float prog = (float)((mouseX - (double)sliderX) / (double)sliderW);
                            prog = Math.max(0.0f, Math.min(1.0f, prog));
                            double val = np.getMinValue() + (double)prog * (np.getMaxValue() - np.getMinValue());
                            np.setValue(val);
                        } else if (button == 0) {
                            np.setValue((Double)np.getValue() + np.getIncrement());
                            if ((Double)np.getValue() > np.getMaxValue()) {
                                np.setValue(np.getMinValue());
                            }
                        } else if (button == 1) {
                            np.setValue((Double)np.getValue() - np.getIncrement());
                            if ((Double)np.getValue() < np.getMinValue()) {
                                np.setValue(np.getMaxValue());
                            }
                        }
                    } else if (prop instanceof ColorProperty) {
                        ColorProperty cp = (ColorProperty)prop;
                        float hueX = x + 10.0f * menuScale;
                        float hueY = setY + 28.0f * menuScale;
                        float hueW = w - 20.0f * menuScale;
                        float hueH = 8.0f * menuScale;
                        if (mouseX >= (double)hueX && mouseX <= (double)(hueX + hueW) && mouseY >= (double)hueY && mouseY <= (double)(hueY + hueH)) {
                            float newHue = (float)((mouseX - (double)hueX) / (double)hueW);
                            newHue = Math.max(0.0f, Math.min(1.0f, newHue));
                            cp.setHue(newHue);
                            cp.updateValue();
                        }
                    }
                    return true;
                }
                setY += rowH + 5.0f * menuScale;
            }
            return true;
        }
        float hudBtnY = winY + winH - 34.0f * menuScale;
        if (mouseX >= (double)(winX + 8.0f * menuScale) && mouseX <= (double)(winX + sidebarWidth - 8.0f * menuScale) && mouseY >= (double)hudBtnY && mouseY <= (double)(hudBtnY + 26.0f * menuScale)) {
            if (this.field_22787 != null) {
                this.field_22787.method_1507((class_437)new HUDEditorScreen());
            }
            return true;
        }
        float sideY = winY + 66.0f * menuScale;
        for (Tab tab : Tab.values()) {
            if (mouseX >= (double)(winX + 8.0f * menuScale) && mouseX <= (double)(winX + sidebarWidth - 8.0f * menuScale) && mouseY >= (double)sideY && mouseY <= (double)(sideY + 24.0f * menuScale)) {
                this.currentTab = tab;
                this.selectedCategory = null;
                this.scrollOffset = 0;
                return true;
            }
            sideY += 27.0f * menuScale;
        }
        float startX = mainX + 14.0f * menuScale;
        float startY = winY + 12.0f * menuScale;
        float catX = startX;
        if (mouseX >= (double)catX && mouseX <= (double)(catX + 38.0f * menuScale) && mouseY >= (double)startY && mouseY <= (double)(startY + 20.0f * menuScale)) {
            this.selectedCategory = null;
            this.scrollOffset = 0;
            return true;
        }
        catX += 42.0f * menuScale;
        for (ModuleCategory cat : ModuleCategory.VALUES) {
            if (cat == ModuleCategory.MISC) continue;
            float w = 50.0f * menuScale;
            if (mouseX >= (double)catX && mouseX <= (double)(catX + w) && mouseY >= (double)startY && mouseY <= (double)(startY + 20.0f * menuScale)) {
                this.selectedCategory = cat;
                this.scrollOffset = 0;
                return true;
            }
            if ((catX += w + 4.0f * menuScale) > mainX + mainW - 130.0f * menuScale) break;
        }
        float f = 110.0f * menuScale;
        float searchX = mainX + mainW - f - 14.0f * menuScale;
        float searchY = startY;
        if (mouseX >= (double)searchX && mouseX <= (double)(searchX + f) && mouseY >= (double)searchY && mouseY <= (double)(searchY + 20.0f * menuScale)) {
            if (!this.searchQuery.isEmpty() && mouseX >= (double)(searchX + f - 14.0f * menuScale)) {
                this.searchQuery = "";
            } else {
                this.searchFocused = true;
            }
            return true;
        }
        this.searchFocused = false;
        List<Module> modules = this.getFilteredModules();
        int columns = Math.max(1, (int)((mainW - 28.0f * menuScale) / (160.0f * menuScale)));
        float spacing = 8.0f * menuScale;
        float cardW = (mainW - 28.0f * menuScale - (float)(columns - 1) * spacing) / (float)columns;
        float cardH = 56.0f * menuScale;
        float gridY = startY + 28.0f * menuScale;
        for (int i = 0; i < modules.size(); ++i) {
            Module mod = modules.get(i);
            int col = i % columns;
            int row = i / columns;
            float cardItemX = startX + (float)col * (cardW + spacing);
            float cardItemY = gridY + (float)row * (cardH + spacing) - this.smoothScrollOffset;
            if (!(mouseX >= (double)cardItemX) || !(mouseX <= (double)(cardItemX + cardW)) || !(mouseY >= (double)cardItemY) || !(mouseY <= (double)(cardItemY + cardH))) continue;
            float gearX = cardItemX + cardW - 28.0f * menuScale;
            float gearY = cardItemY + 6.0f * menuScale;
            if (!mod.getPropertyList().isEmpty() && mouseX >= (double)gearX && mouseX <= (double)(gearX + 14.0f * menuScale) && mouseY >= (double)gearY && mouseY <= (double)(gearY + 14.0f * menuScale)) {
                this.settingsModule = mod;
                this.settingsScrollOffset = 0;
                return true;
            }
            if (button == 1) {
                this.settingsModule = mod;
                this.waitingForKey = true;
                return true;
            }
            if (button == 2) {
                if (!mod.getPropertyList().isEmpty()) {
                    this.settingsModule = mod;
                    this.settingsScrollOffset = 0;
                }
                return true;
            }
            mod.toggle();
            return true;
        }
        return super.method_25402(click, doubled);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (GLFW.glfwGetKey((long)Constants.mc.method_22683().method_4490(), (int)84) == 1 || GLFW.glfwGetKey((long)Constants.mc.method_22683().method_4490(), (int)341) == 1) {
            menuScale += (float)verticalAmount * 0.05f;
            menuScale = Math.max(0.45f, Math.min(1.5f, menuScale));
            return true;
        }
        if (this.settingsModule != null) {
            this.settingsScrollOffset -= (int)(verticalAmount * 20.0);
            this.settingsScrollOffset = Math.max(0, this.settingsScrollOffset);
        } else {
            this.scrollOffset -= (int)(verticalAmount * 20.0);
            this.scrollOffset = Math.max(0, this.scrollOffset);
        }
        return true;
    }

    public boolean method_25404(class_11908 keyInput) {
        int key = keyInput.comp_4795();
        if (this.waitingForKey && this.settingsModule != null) {
            if (key == 256) {
                OpalClient.getInstance().getBindRepository().getBindingService().clearBindings(this.settingsModule);
            } else {
                OpalClient.getInstance().getBindRepository().getBindingService().clearBindings(this.settingsModule);
                OpalClient.getInstance().getBindRepository().getBindingService().register(key, this.settingsModule, InputType.KEYBOARD);
            }
            this.waitingForKey = false;
            return true;
        }
        if (this.searchFocused) {
            if (key == 259) {
                if (!this.searchQuery.isEmpty()) {
                    this.searchQuery = this.searchQuery.substring(0, this.searchQuery.length() - 1);
                }
                return true;
            }
            if (key == 257 || key == 256) {
                this.searchFocused = false;
                return true;
            }
        }
        if (key == 344 || key == 256) {
            if (this.settingsModule != null) {
                this.settingsModule = null;
                return true;
            }
            this.method_25419();
            return true;
        }
        return super.method_25404(keyInput);
    }

    public boolean method_25406(class_11909 click) {
        this.isDraggingWindow = false;
        this.draggingNumberProperty = null;
        this.draggingHue = false;
        return super.method_25406(click);
    }

    public boolean method_25400(class_11905 charInput) {
        if (this.searchFocused) {
            this.searchQuery = this.searchQuery + (char)charInput.comp_4793();
            return true;
        }
        return super.method_25400(charInput);
    }

    public void method_25419() {
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }

    public boolean method_25421() {
        return false;
    }

    @Environment(value=EnvType.CLIENT)
    private static enum Tab {
        MODS("Mods", "\u2605"),
        HUD("HUD", "\u2694"),
        VISUAL("Render", "\ud83c\udfa8"),
        PERFORMANCE("Performance", "\u26a1");

        final String displayName;
        final String icon;

        private Tab(String displayName, String icon) {
            this.displayName = displayName;
            this.icon = icon;
        }
    }
}

