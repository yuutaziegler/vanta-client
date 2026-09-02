/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11908
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click.dropdown.panel.property.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyProvider;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class GroupPropertyComponent
extends PropertyPanel<GroupProperty> {
    private final PropertyProvider propertyProvider;
    private final Animation expandAnimation = new Animation(Easing.DECELERATE, 125L);
    private boolean expanded;

    public GroupPropertyComponent(GroupProperty property) {
        super(property);
        this.propertyProvider = new PropertyProvider(property, this::isExpandedAnimation, null);
    }

    private boolean isExpandedAnimation() {
        return this.expanded || this.expandAnimation.getValue() > 0.0f;
    }

    @Override
    public void close() {
        this.propertyProvider.close();
    }

    @Override
    public void init() {
        this.propertyProvider.init();
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.expandAnimation.run(this.expanded ? 1.0f : 0.0f);
        float padding = 3.0f;
        float animatedPadding = 3.0f * (1.0f - this.expandAnimation.getValue());
        float rectWidth = this.width;
        float adjustedX = this.x + animatedPadding;
        float adjustedY = this.y + animatedPadding;
        float adjustedWidth = rectWidth - animatedPadding * 2.0f;
        float adjustedHeight = 22.0f - animatedPadding * 2.0f;
        float cornerRadius = 4.0f * (1.0f - this.expandAnimation.getValue());
        NVGTextRenderer font = FontRepository.getFont("productsans-bold");
        NVGRenderer.roundedRect(adjustedX, adjustedY, adjustedWidth, adjustedHeight, cornerRadius, ColorUtility.applyOpacity(-16777216, 0.2f));
        font.drawString(((GroupProperty)this.getProperty()).getName(), adjustedX + (adjustedWidth - font.getStringWidth(((GroupProperty)this.getProperty()).getName(), 7.0f)) / 2.0f, this.y + 13.5f, 7.0f, -1);
        String expandIcon = "\ue5cf";
        NVGTextRenderer iconFont = FontRepository.getFont("materialicons-regular");
        float iconSize = 12.0f;
        float iconWidth = iconFont.getStringWidth("\ue5cf", 12.0f);
        NVGRenderer.rotate(this.expandAnimation.getValue() * 180.0f, this.x + 3.0f + rectWidth - 20.0f, this.y + 5.0f, iconWidth, 12.0f, () -> iconFont.drawString("\ue5cf", 0.0f, 0.0f, 12.0f, -1, false, 18));
        NVGRenderer.scissor(this.x, this.y, this.width, this.height, () -> {
            this.propertyProvider.setX(this.x);
            this.propertyProvider.setY(this.y + 22.0f);
            this.propertyProvider.setWidth(this.width);
            this.propertyProvider.render(context, mouseX, mouseY, delta);
        });
        float height = this.propertyProvider.getExtraHeight();
        this.setHeight(height * this.expandAnimation.getValue() + 22.0f);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (HoverUtility.isHovering(this.x, this.y, this.width, 17.0f, mouseX, mouseY)) {
            this.expanded = !this.expanded;
        }
        this.propertyProvider.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public void keyPressed(class_11908 keyInput) {
        this.propertyProvider.keyPressed(keyInput);
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        this.propertyProvider.charTyped(chr, modifiers);
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        this.propertyProvider.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.propertyProvider.mouseReleased(mouseX, mouseY, button);
    }
}

