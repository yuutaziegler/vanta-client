/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click.dropdown.panel.property.impl;

import com.ibm.icu.impl.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.property.impl.number.BoundedNumberProperty;
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
public final class BoundedNumberPropertyComponent
extends PropertyPanel<BoundedNumberProperty> {
    private boolean draggingLow;
    private boolean draggingHigh;
    private Animation draggingLowAnimation;
    private Animation draggingHighAnimation;

    public BoundedNumberPropertyComponent(BoundedNumberProperty property) {
        super(property);
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        this.setHeight(26.0f);
        super.render(context, mouseX, mouseY, delta);
        BoundedNumberProperty property = (BoundedNumberProperty)this.getProperty();
        NVGTextRenderer font = FontRepository.getFont("productsans-medium");
        font.drawString(property.getName(), this.x + 5.0f, this.y + 8.5f, 7.0f, -1);
        Pair values = (Pair)property.getValue();
        Double lowValue = (Double)values.first;
        Double highValue = (Double)values.second;
        float sliderWidth = this.width - 12.0f;
        float sliderHeight = 2.5f;
        float sliderX = this.x + 6.0f;
        float sliderY = this.y + 13.0f;
        float percent = Math.min(1.0f, Math.max(0.0f, ((float)mouseX - sliderX) / sliderWidth));
        if (mouseX != -1) {
            double newValue = MathUtility.interpolate(property.getMinValue(), property.getMaxValue(), (double)percent);
            if (this.draggingLow && newValue <= highValue) {
                property.setValue((Pair<Double, Double>)Pair.of((Object)newValue, (Object)highValue));
            }
            if (this.draggingHigh && newValue >= lowValue) {
                property.setValue((Pair<Double, Double>)Pair.of((Object)lowValue, (Object)newValue));
            }
        }
        double lowPercent = (lowValue - property.getMinValue()) / (property.getMaxValue() - property.getMinValue());
        double highPercent = (highValue - property.getMinValue()) / (property.getMaxValue() - property.getMinValue());
        double lowDestination = (double)sliderWidth * lowPercent;
        if (this.draggingLowAnimation == null) {
            this.draggingLowAnimation = new Animation(Easing.LINEAR, 50L);
            this.draggingLowAnimation.setValue((float)lowDestination);
        } else {
            this.draggingLowAnimation.run((float)lowDestination);
        }
        double highDestination = (double)sliderWidth * highPercent;
        if (this.draggingHighAnimation == null) {
            this.draggingHighAnimation = new Animation(Easing.LINEAR, 50L);
            this.draggingHighAnimation.setValue((float)highDestination);
        } else {
            this.draggingHighAnimation.run((float)highDestination);
        }
        NVGRenderer.roundedRect(sliderX, sliderY, sliderWidth, 2.5f, 1.25f, -13158601);
        float lowAnim = this.draggingLowAnimation.getValue();
        float highAnim = this.draggingHighAnimation.getValue();
        if (highAnim > lowAnim) {
            int color = (Integer)ColorUtility.getClientTheme().first;
            NVGRenderer.roundedRectGradient(sliderX + lowAnim, sliderY, highAnim - lowAnim, 2.5f, 1.25f, color, ColorUtility.darker(color, 0.5f), 90.0f);
        }
        NVGRenderer.roundedRectGradient(sliderX + lowAnim - 1.0f, sliderY - 1.3f, 2.0f, 5.0f, 1.0f, -1, ColorUtility.darker(-1, 0.1f), 90.0f);
        NVGRenderer.roundedRectGradient(sliderX + highAnim - 1.0f, sliderY - 1.3f, 2.0f, 5.0f, 1.0f, -1, ColorUtility.darker(-1, 0.1f), 90.0f);
        Object lowValueString = lowValue == (double)lowValue.intValue() ? String.valueOf(lowValue.intValue()) : String.format("%.3f", lowValue).replaceAll("0+$", "").replaceAll("\\.$", "");
        Object highValueString = highValue == (double)highValue.intValue() ? String.valueOf(highValue.intValue()) : String.format("%.3f", highValue).replaceAll("0+$", "").replaceAll("\\.$", "");
        if (((BoundedNumberProperty)this.getProperty()).getSuffix() != null) {
            lowValueString = (String)lowValueString + ((BoundedNumberProperty)this.getProperty()).getSuffix();
            highValueString = (String)highValueString + ((BoundedNumberProperty)this.getProperty()).getSuffix();
        }
        font.drawString((String)lowValueString, sliderX + lowAnim - font.getStringWidth((String)lowValueString, 5.5f) / 2.0f, this.y + 22.0f, 5.5f, ColorUtility.applyOpacity(-1, 0.8f));
        font.drawString((String)highValueString, sliderX + highAnim - font.getStringWidth((String)highValueString, 5.5f) / 2.0f, this.y + 22.0f, 5.5f, ColorUtility.applyOpacity(-1, 0.8f));
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (HoverUtility.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY) && button == 0) {
            double highSliderDiff;
            float sliderWidth = this.width - 12.0f;
            float sliderX = this.x + 6.0f;
            BoundedNumberProperty property = (BoundedNumberProperty)this.getProperty();
            Pair values = (Pair)property.getValue();
            double lowPercent = ((Double)values.first - property.getMinValue()) / (property.getMaxValue() - property.getMinValue());
            double highPercent = ((Double)values.second - property.getMinValue()) / (property.getMaxValue() - property.getMinValue());
            float lowSliderX = sliderX + (float)((double)sliderWidth * lowPercent);
            float highSliderX = sliderX + (float)((double)sliderWidth * highPercent);
            double lowSliderDiff = Math.abs(mouseX - (double)lowSliderX);
            if (lowSliderDiff == (highSliderDiff = Math.abs(mouseX - (double)highSliderX))) {
                if (mouseX < (double)lowSliderX) {
                    this.draggingLow = true;
                } else {
                    this.draggingHigh = true;
                }
            } else if (lowSliderDiff < highSliderDiff) {
                this.draggingLow = true;
            } else {
                this.draggingHigh = true;
            }
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.draggingLow = false;
            this.draggingHigh = false;
        }
    }
}

