/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11905
 *  net.minecraft.class_11908
 *  net.minecraft.class_11909
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 */
package wtf.opal.client.screen.click.dropdown;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_437;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.visual.ClickGUIModule;
import wtf.opal.client.renderer.LiquidGlassRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.repository.ImageRepository;
import wtf.opal.client.screen.click.dropdown.panel.CategoryPanel;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.press.KeyPressEvent;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.misc.Multithreading;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class DropdownClickGUI
extends class_437 {
    private final List<CategoryPanel> categoryPanelList = new ArrayList<CategoryPanel>();
    public static boolean displayingBinds;
    public static boolean selectingBind;
    public static boolean typingString;
    public static String searchString;
    private static float panelScale;

    public DropdownClickGUI() {
        super((class_2561)class_2561.method_43473());
        int index = 0;
        for (ModuleCategory category : ModuleCategory.VALUES) {
            this.categoryPanelList.add(new CategoryPanel(category, index));
            ++index;
        }
    }

    public void method_25420(class_332 context, int mouseX, int mouseY, float deltaTicks) {
        int w = Constants.mc.method_22683().method_4486();
        int h = Constants.mc.method_22683().method_4502();
        context.method_25294(0, 0, w, h, 0x55000000); // light dim so HUD elements stay visible
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        boolean frameStarted = NVGRenderer.beginFrame();
        displayingBinds = PlayerUtility.isKeyPressed(258);
        LiquidGlassRenderer.drawGlassPanel(7.0f, 7.0f, 42.0f, 42.0f, 10.0f);
        NVGImageRenderer logo = ImageRepository.getImage("logo.png");
        if (logo != null) {
            logo.drawImage(10.0f, 10.0f, 36.0f, 36.0f);
        }
        String scaleText = String.format("Scale: %.1fx (Use T + Scroll / Wheel to resize)", Float.valueOf(panelScale));
        float textWidth = FontRepository.getFont("productsans-medium").getStringWidth(scaleText, 8.0f);
        FontRepository.getFont("productsans-medium").drawString(scaleText, ((float)Constants.mc.method_22683().method_4486() - textWidth) / 2.0f, 6.0f, 8.0f, -1426063361);
        int categoryAmount = this.categoryPanelList.size();
        for (int i = 0; i < categoryAmount; ++i) {
            CategoryPanel panel = this.categoryPanelList.get(i);
            float baseWidth = 95.0f;
            float width = 95.0f * panelScale;
            float height = 18.0f * panelScale;
            if (panel.getX() == 0.0f && panel.getY() == 0.0f) {
                float y = 24.0f;
                float spacing = 10.0f * panelScale;
                float totalWidth = (float)categoryAmount * width + (float)(categoryAmount - 1) * spacing;
                float startX = ((float)Constants.mc.method_22683().method_4486() - totalWidth) / 2.0f;
                float x = startX + (float)i * (width + spacing);
                panel.setDimensions(x, 24.0f, width, height);
            } else {
                panel.setDimensions(panel.getX(), panel.getY(), panel.getWidth() == 0.0f ? width : panel.getWidth() * panelScale, height);
            }
            panel.render(context, mouseX, mouseY, delta);
        }
        float searchWidth = 160.0f * panelScale;
        float searchHeight = 18.0f * panelScale;
        float searchX = ((float)Constants.mc.method_22683().method_4486() - searchWidth) / 2.0f;
        float searchY = 16.0f;
        LiquidGlassRenderer.drawGlassPanel(searchX, searchY, searchWidth, searchHeight, 6.0f);
        NVGRenderer.roundedRect(searchX, searchY, searchWidth, searchHeight, 6.0f, -804121314);
        String displayStr = searchString.isEmpty() && !typingString ? "Search Modules..." : searchString + (typingString && System.currentTimeMillis() % 1000L > 500L ? "_" : "");
        FontRepository.getFont("productsans-medium").drawString(displayStr, searchX + 8.0f, searchY + 12.0f, 7.5f * panelScale, searchString.isEmpty() && !typingString ? -7829368 : -1);
        if (frameStarted) {
            NVGRenderer.endFrameAndReset(true);
        }
    }

    public boolean method_25402(class_11909 click, boolean doubled) {
        float searchWidth = 160.0f * panelScale;
        float searchHeight = 18.0f * panelScale;
        float searchX = ((float)Constants.mc.method_22683().method_4486() - searchWidth) / 2.0f;
        float searchY = 16.0f;
        if (click.method_74245() == 0 && HoverUtility.isHovering(searchX, searchY, searchWidth, searchHeight, (float)click.comp_4798(), (float)click.comp_4799())) {
            typingString = true;
            return true;
        }
        typingString = false;
        this.categoryPanelList.forEach(categoryPanel -> categoryPanel.mouseClicked(click.comp_4798(), click.comp_4799(), click.method_74245()));
        return true;
    }

    public boolean method_25406(class_11909 click) {
        this.categoryPanelList.forEach(categoryPanel -> categoryPanel.mouseReleased(click.comp_4798(), click.comp_4799(), click.method_74245()));
        return true;
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (PlayerUtility.isKeyPressed(84) || PlayerUtility.isKeyPressed(341)) {
            panelScale += (float)verticalAmount * 0.05f;
            panelScale = Math.max(0.4f, Math.min(2.0f, panelScale));
            return true;
        }
        this.categoryPanelList.forEach(categoryPanel -> categoryPanel.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount));
        return true;
    }

    public boolean method_25404(class_11908 keyInput) {
        if (selectingBind) {
            EventDispatcher.dispatch(new KeyPressEvent(keyInput.comp_4795()));
            selectingBind = false;
            return true;
        }
        if (typingString) {
            if (keyInput.comp_4795() == 259) {
                if (!searchString.isEmpty()) {
                    searchString = searchString.substring(0, searchString.length() - 1);
                }
            } else if (keyInput.comp_4795() == 257 || keyInput.comp_4795() == 256) {
                typingString = false;
            }
            return true;
        }
        if (keyInput.comp_4795() == 256 || keyInput.comp_4795() == 344) {
            this.method_25419();
            return true;
        }
        this.categoryPanelList.forEach(categoryPanel -> categoryPanel.keyPressed(keyInput));
        return true;
    }

    public boolean method_25400(class_11905 charInput) {
        if (typingString) {
            if (searchString == null) {
                searchString = "";
            }
            searchString = searchString + (char)charInput.comp_4793();
            return true;
        }
        this.categoryPanelList.forEach(categoryPanel -> categoryPanel.charTyped((char)charInput.comp_4793(), charInput.comp_4794()));
        return true;
    }

    protected void method_25426() {
        searchString = "";
        typingString = false;
        this.categoryPanelList.forEach(CategoryPanel::init);
    }

    public void method_25419() {
        if (selectingBind) {
            return;
        }
        this.categoryPanelList.forEach(CategoryPanel::close);
        Multithreading.schedule(() -> OpalClient.getInstance().getModuleRepository().getModule(ClickGUIModule.class).setEnabled(false), 100L, TimeUnit.MILLISECONDS);
        if (this.field_22787 != null) {
            this.field_22787.method_1507(null);
        }
    }

    public boolean method_25421() {
        return false;
    }

    static {
        searchString = "";
        panelScale = 0.85f;
    }
}

