/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click.dropdown.panel.property.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.misc.math.MathUtility;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class NumberPropertyComponent
extends PropertyPanel<NumberProperty> {
    private boolean dragging;
    private Animation dragAnimation;

    public NumberPropertyComponent(NumberProperty property) {
        super(property);
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.setHeight(15.0f);
        super.render(context, mouseX, mouseY, delta);
        NumberProperty property = (NumberProperty)this.getProperty();
        NVGTextRenderer font = FontRepository.getFont("productsans-medium");
        font.drawString(property.getName(), this.x + 5.0f, this.y + 8.5f, 7.0f, -1);
        float sliderWidth = this.width - 12.0f;
        float sliderHeight = 2.5f;
        float sliderX = this.x + 6.0f;
        float sliderY = this.y + 13.0f;
        if (this.dragging && mouseX != -1) {
            float percent = Math.min(1.0f, Math.max(0.0f, ((float)mouseX - sliderX) / sliderWidth));
            property.setValue(MathUtility.interpolate(property.getMinValue(), property.getMaxValue(), (double)percent));
        }
        double widthPercent = ((Double)property.getValue() - property.getMinValue()) / (property.getMaxValue() - property.getMinValue());
        double destination = (double)sliderWidth * widthPercent;
        if (this.dragAnimation == null) {
            this.dragAnimation = new Animation(Easing.LINEAR, 50L);
            this.dragAnimation.setValue((float)destination);
        } else {
            this.dragAnimation.run((float)destination);
        }
        NVGRenderer.roundedRect(sliderX, sliderY, sliderWidth, 2.5f, 1.25f, -13158601);
        float dragAnim = this.dragAnimation.getValue();
        if (dragAnim > 1.0f) {
            int color = (Integer)ColorUtility.getClientTheme().first;
            NVGRenderer.roundedRectGradient(sliderX, sliderY, dragAnim, 2.5f, 1.25f, color, ColorUtility.darker(color, 0.5f), 90.0f);
        }
        NVGRenderer.roundedRectGradient(sliderX + dragAnim - 1.0f, sliderY - 1.3f, 2.0f, 5.0f, 1.0f, -1, ColorUtility.darker(-1, 0.1f), 90.0f);
        Number value = (Number)((NumberProperty)this.getProperty()).getValue();
        Object valueString = value.doubleValue() == (double)value.intValue() ? String.valueOf(value.intValue()) : String.format("%.3f", value.doubleValue()).replaceAll("0+$", "").replaceAll("\\.$", "");
        if (((NumberProperty)this.getProperty()).getSuffix() != null) {
            valueString = (String)valueString + ((NumberProperty)this.getProperty()).getSuffix();
        }
        font.drawString((String)valueString, sliderX + dragAnim - font.getStringWidth((String)valueString, 5.5f) / 2.0f, this.y + 22.0f, 5.5f, ColorUtility.applyOpacity(-1, 0.8f));
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (HoverUtility.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY) && button == 0) {
            this.dragging = true;
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.dragging && button == 0) {
            this.dragging = false;
        }
    }
}

