/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11908
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click.dropdown.panel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_332;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.screen.click.OpalPanelComponent;
import wtf.opal.client.screen.click.dropdown.DropdownClickGUI;
import wtf.opal.client.screen.click.dropdown.panel.ModulePanel;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.render.Scroller;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class CategoryPanel
extends OpalPanelComponent {
    private final Scroller scroller = new Scroller();
    private final ModuleCategory category;
    private Animation openAnimation;
    private final int panelIndex;
    private final boolean lastPanel;
    private boolean closing;
    private final List<ModulePanel> modulePanelList = new ArrayList<ModulePanel>();
    private final List<ModulePanel> visiblePanelsCache = new ArrayList<ModulePanel>();
    private String lastSearchString = null;
    private boolean dragging;
    private boolean resizing;
    private float dragX;
    private float dragY;

    public CategoryPanel(ModuleCategory category, int panelIndex) {
        this.category = category;
        this.panelIndex = panelIndex;
        this.lastPanel = panelIndex == ModuleCategory.VALUES.length - 1;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        if (this.dragging) {
            this.x = (float)mouseX - this.dragX;
            this.y = (float)mouseY - this.dragY;
        } else if (this.resizing) {
            this.width = Math.max(80.0f, (float)mouseX - this.x);
            this.height = Math.max(16.0f, (float)mouseY - this.y);
        }
        this.openAnimation.run(this.closing ? 0.0f : 1.0f);
        if (this.lastPanel && this.openAnimation.isFinished() && this.closing) {
            Constants.mc.method_1507(null);
            return;
        }
        float[] currentY = new float[]{this.y + this.height};
        float totalHeight = this.getTotalHeight();
        float openAnimationValue = this.openAnimation.getValue();
        float scissorHeight = Math.min((float)Constants.mc.method_22683().method_4502() - this.y, totalHeight * openAnimationValue);
        float scrollOffset = this.scroller.getAnimation().getValue();
        NVGRenderer.globalAlpha(openAnimationValue);
        float radius = 8.0f;
        NVGRenderer.roundedRect(this.x, this.y + scrollOffset, this.width, totalHeight, radius, -183363546);
        NVGRenderer.roundedRect(this.x, this.y + scrollOffset, this.width, this.height, radius, 1140884735);
        NVGRenderer.scissor(this.x, this.y, this.width, scissorHeight, () -> {
            FontRepository.getFont("productsans-bold").drawString(this.category.getName(), this.x + 6.0f, this.y + scrollOffset + 11.5f, 8.0f, -1);
            FontRepository.getFont("materialicons-outlined").drawString(this.category.getIcon(), this.x + this.width - 14.5f, this.y + scrollOffset + 12.5f, 8.5f, -1);
            List<ModulePanel> visiblePanels = this.getVisiblePanels();
            for (int i = 0; i < visiblePanels.size(); ++i) {
                ModulePanel panel = visiblePanels.get(i);
                float panelHeight = this.height + panel.getExpandAnimation().getValue() * panel.getAddedHeight();
                panel.setDimensions(this.x, currentY[0] + scrollOffset, this.width, panelHeight);
                panel.setLastModule(i == visiblePanels.size() - 1);
                panel.render(context, mouseX, mouseY, delta);
                currentY[0] = currentY[0] + panelHeight;
            }
        });
        NVGRenderer.globalAlpha(1.0f);
        if (openAnimationValue > 0.5f && !this.closing) {
            float handleX = this.x + this.width - 8.0f;
            float handleY = this.y + totalHeight - 6.0f;
            NVGRenderer.rect(handleX, handleY, 5.0f, 1.5f, 0x44FFFFFF);
            NVGRenderer.rect(handleX + 2.5f, handleY - 2.5f, 1.5f, 3.5f, 0x44FFFFFF);
        }
        this.scroller.onScroll(this.getMaxOffset(totalHeight));
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (HoverUtility.isHovering(this.x + this.width - 15.0f, this.y + this.getTotalHeight() - 15.0f, 15.0f, 15.0f, mouseX, mouseY)) {
                this.resizing = true;
                return;
            }
            if (HoverUtility.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY)) {
                this.dragging = true;
                this.dragX = (float)mouseX - this.x;
                this.dragY = (float)mouseY - this.y;
                return;
            }
        }
        this.getVisiblePanels().forEach(modulePanel -> modulePanel.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public void keyPressed(class_11908 keyInput) {
        this.getVisiblePanels().forEach(modulePanel -> modulePanel.keyPressed(keyInput));
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        this.getVisiblePanels().forEach(modulePanel -> modulePanel.charTyped(chr, modifiers));
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
        this.resizing = false;
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (HoverUtility.isHovering(this.x, this.y, this.width, this.getTotalHeight(), mouseX, mouseY)) {
            this.scroller.addScroll(verticalAmount, this.getMaxOffset(this.getTotalHeight()));
        }
    }

    @Override
    public void init() {
        if (this.modulePanelList.isEmpty()) {
            OpalClient.getInstance().getModuleRepository().getModulesInCategory(this.category).stream().sorted(Comparator.comparing(Module::getName)).forEach(module -> this.modulePanelList.add(new ModulePanel((Module)module)));
        }
        this.modulePanelList.forEach(ModulePanel::init);
        this.openAnimation = new Animation(Easing.DECELERATE, 200L);
        this.openAnimation.setValue(0.0f);
        this.closing = false;
    }

    @Override
    public void close() {
        this.closing = true;
        this.openAnimation.run(0.0f);
        this.modulePanelList.forEach(ModulePanel::close);
    }

    public List<ModulePanel> getVisiblePanels() {
        String currentSearch = DropdownClickGUI.searchString;
        if (currentSearch == null) {
            currentSearch = "";
        }
        if (this.lastSearchString != null && this.lastSearchString.equals(currentSearch) && !this.visiblePanelsCache.isEmpty()) {
            return this.visiblePanelsCache;
        }
        this.visiblePanelsCache.clear();
        this.lastSearchString = currentSearch;
        for (ModulePanel panel : this.modulePanelList) {
            if (!currentSearch.isEmpty() && !panel.getModule().getName().toLowerCase().contains(currentSearch.toLowerCase())) continue;
            this.visiblePanelsCache.add(panel);
        }
        return this.visiblePanelsCache;
    }

    private float getTotalHeight() {
        float total = this.height;
        List<ModulePanel> visible = this.getVisiblePanels();
        for (ModulePanel panel : visible) {
            total += this.height + panel.getExpandAnimation().getValue() * panel.getAddedHeight();
        }
        return total;
    }

    private float getMaxOffset(float totalHeight) {
        float screenH = Constants.mc.method_22683().method_4502();
        float overflow = this.y + totalHeight - screenH;
        return Math.max(0.0f, overflow + 10.0f);
    }
}

