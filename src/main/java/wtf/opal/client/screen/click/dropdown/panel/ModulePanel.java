/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11908
 *  net.minecraft.class_124
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click.dropdown.panel;

import com.ibm.icu.impl.Pair;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_124;
import net.minecraft.class_332;
import wtf.opal.client.OpalClient;
import wtf.opal.client.binding.repository.BindRepository;
import wtf.opal.client.binding.type.InputType;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.client.screen.click.OpalPanelComponent;
import wtf.opal.client.screen.click.dropdown.DropdownClickGUI;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyProvider;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class ModulePanel
extends OpalPanelComponent {
    private final Module module;
    private Animation hoverAnimation;
    private Animation toggleAnimation;
    private final Animation expandAnimation = new Animation(Easing.DECELERATE, 125L);
    private boolean lastModule;
    private boolean expanded;
    private boolean selectingBind;
    private final PropertyProvider propertyProvider;
    private final BindRepository bindRepository = OpalClient.getInstance().getBindRepository();

    public ModulePanel(Module module) {
        this.module = module;
        this.propertyProvider = new PropertyProvider(module, this::isExpandedAnimation, this::isLastModule);
    }

    private boolean isExpandedAnimation() {
        return this.expanded || this.expandAnimation.getValue() > 0.0f;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.handleAnimations(mouseX, mouseY);
        int baseColor = -15592938;
        String font = this.module.isEnabled() ? "productsans-bold" : "productsans-medium";
        Pair<Integer, Integer> colors = ColorUtility.getClientTheme();
        if (this.module.isEnabled()) {
            NVGRenderer.rect(this.x + 2.0f, this.y + 2.0f, 2.0f, this.height - 4.0f, (Integer)colors.first);
        }
        NVGRenderer.scissor(this.x, this.y, this.width, this.height, () -> {
            int color = this.module.isEnabled() ? (Integer)colors.first : ColorUtility.darker(-1, 0.2f);
            FontRepository.getFont(font).drawString(this.module.getName(), this.x + 6.0f, this.y + 12.5f, 8.0f, color);
            if (this.propertyProvider.isHasProperties() && !this.selectingBind && !DropdownClickGUI.displayingBinds) {
                String expandIcon = "\ue5cf";
                NVGTextRenderer iconFont = FontRepository.getFont("materialicons-regular");
                float iconSize = 12.0f;
                float iconWidth = iconFont.getStringWidth("\ue5cf", 12.0f);
                NVGRenderer.rotate(this.expandAnimation.getValue() * 180.0f, this.x + this.width - 17.0f, this.y + 4.0f, iconWidth, 12.0f, () -> iconFont.drawString("\ue5cf", 0.0f, 0.0f, 12.0f, color, false, 18));
            }
            Optional<Pair<Integer, InputType>> bind = this.bindRepository.getBindingService().getKeyFromBindable(this.module);
            String keyString = null;
            if (this.selectingBind) {
                keyString = String.valueOf(class_124.field_1080) + "[" + String.valueOf(class_124.field_1061) + "Listening..." + String.valueOf(class_124.field_1080) + "]";
            } else if (DropdownClickGUI.displayingBinds && bind.isPresent()) {
                String key = this.bindRepository.getNameFromInteger((Integer)bind.get().first);
                keyString = String.valueOf(class_124.field_1080) + "[" + String.valueOf(class_124.field_1068) + key + String.valueOf(class_124.field_1080) + "]";
            }
            if (keyString != null) {
                FontRepository.getFont("productsans-medium").drawString(keyString, this.x + this.width - FontRepository.getFont(font).getStringWidth(keyString, 7.0f) - 5.0f, this.y + 12.0f, 7.0f, -1);
            }
            if (this.expandAnimation.isFinished() && !this.isExpanded()) {
                return;
            }
            this.propertyProvider.setX(this.x);
            this.propertyProvider.setY(this.y + 20.0f);
            this.propertyProvider.setWidth(this.width);
            this.propertyProvider.render(context, mouseX, mouseY, delta);
        });
    }

    private void handleAnimations(float mouseX, float mouseY) {
        float toggledFactor;
        float hoverFactor;
        float f = hoverFactor = HoverUtility.isHovering(this.x, this.y, this.width, this.height - (this.isExpanded() ? this.propertyProvider.getExtraHeight() : 0.0f), mouseX, mouseY) ? 0.7f : 0.0f;
        if (this.hoverAnimation == null) {
            this.hoverAnimation = new Animation(Easing.DECELERATE, 150L);
            this.hoverAnimation.setValue(hoverFactor);
        } else {
            this.hoverAnimation.run(hoverFactor);
        }
        float f2 = toggledFactor = this.module.isEnabled() ? 0.4f : 0.0f;
        if (this.toggleAnimation == null) {
            this.toggleAnimation = new Animation(Easing.DECELERATE, 150L);
            this.toggleAnimation.setValue(toggledFactor);
        } else {
            this.toggleAnimation.run(toggledFactor);
        }
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.selectingBind) {
            this.bindRepository.getBindingService().clearBindings(this.module);
            this.bindRepository.getBindingService().register(button, this.module, InputType.MOUSE);
            DropdownClickGUI.selectingBind = false;
            this.selectingBind = false;
            return;
        }
        if (HoverUtility.isHovering(this.x, this.y, this.width, this.height - (this.isExpanded() ? this.propertyProvider.getExtraHeight() : 0.0f), mouseX, mouseY)) {
            switch (button) {
                case 0: {
                    this.module.toggle();
                    break;
                }
                case 2: {
                    DropdownClickGUI.selectingBind = true;
                    this.selectingBind = true;
                    break;
                }
                case 1: {
                    if (this.module.getPropertyList().isEmpty()) break;
                    this.expanded = !this.expanded;
                    return;
                }
            }
        }
        this.propertyProvider.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(class_11908 keyInput) {
        if (this.selectingBind) {
            this.bindRepository.getBindingService().clearBindings(this.module);
            if (keyInput.comp_4795() != 256) {
                this.bindRepository.getBindingService().register(keyInput.comp_4795(), this.module, InputType.KEYBOARD);
            }
            DropdownClickGUI.selectingBind = false;
            this.selectingBind = false;
            return;
        }
        this.propertyProvider.keyPressed(keyInput);
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        this.propertyProvider.charTyped(chr, modifiers);
    }

    public float getAddedHeight() {
        return this.propertyProvider.getExtraHeight();
    }

    public Animation getExpandAnimation() {
        return this.expandAnimation;
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.propertyProvider.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.propertyProvider.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void init() {
        this.propertyProvider.init();
    }

    @Override
    public void close() {
        this.propertyProvider.close();
    }

    public void setLastModule(boolean lastModule) {
        this.lastModule = lastModule;
    }

    public boolean isLastModule() {
        return this.lastModule;
    }

    public Module getModule() {
        return this.module;
    }
}

