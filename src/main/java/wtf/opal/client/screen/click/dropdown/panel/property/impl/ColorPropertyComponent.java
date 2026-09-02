/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click.dropdown.panel.property.impl;

import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.property.impl.ColorProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class ColorPropertyComponent
extends PropertyPanel<ColorProperty> {
    private boolean expanded;
    private ColorDragType colorDragType;
    private final Animation expandAnimation = new Animation(Easing.DECELERATE, 125L);

    public ColorPropertyComponent(ColorProperty property) {
        super(property);
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.expandAnimation.run(this.expanded ? 1.0f : 0.0f);
        FontRepository.getFont("productsans-medium").drawString(((ColorProperty)this.getProperty()).getName(), this.x + 5.0f, this.y + 10.5f, 7.0f, -1);
        NVGRenderer.roundedRect(this.x + this.width - 22.0f, this.y + 3.5f, 18.0f, 10.0f, 3.0f, (Integer)((ColorProperty)this.getProperty()).getValue());
        float xPos = this.x + 5.0f;
        float yPos = this.y + 17.0f;
        float w = 65.0f;
        float h = 50.0f;
        NVGRenderer.scissor(this.x, this.y, this.width, this.height, () -> {
            if (this.expandAnimation.getValue() > 0.0f) {
                switch (this.colorDragType.ordinal()) {
                    case 1: {
                        ((ColorProperty)this.getProperty()).setHue(Math.min(1.0f, Math.max(0.0f, ((float)mouseY - yPos) / 50.0f)));
                        break;
                    }
                    case 0: {
                        ((ColorProperty)this.getProperty()).setSaturation(Math.min(1.0f, Math.max(0.0f, ((float)mouseX - xPos) / 65.0f)));
                        ((ColorProperty)this.getProperty()).setBrightness(Math.min(1.0f, Math.max(0.0f, 1.0f - ((float)mouseY - yPos) / 50.0f)));
                    }
                }
                float[] hsb = ((ColorProperty)this.getProperty()).getHSB();
                ((ColorProperty)this.getProperty()).updateValue();
                NVGRenderer.rect(xPos, yPos, 65.0f, 50.0f, Color.getHSBColor(hsb[0], 1.0f, 1.0f).getRGB());
                NVGRenderer.rectGradient(xPos, yPos, 65.0f, 50.0f, Color.getHSBColor(hsb[0], 0.0f, 1.0f).getRGB(), ColorUtility.applyOpacity(Color.getHSBColor(hsb[0], 0.0f, 1.0f).getRGB(), 0), 0.0f);
                NVGRenderer.rectGradient(xPos, yPos, 65.0f, 50.0f, ColorUtility.applyOpacity(Color.getHSBColor(hsb[0], 1.0f, 0.0f).getRGB(), 0), Color.getHSBColor(hsb[0], 1.0f, 0.0f).getRGB(), 90.0f);
                NVGRenderer.rainbowRect(xPos + 65.0f + 5.0f, yPos, 8.0f, 50.0f);
            }
            this.setHeight(17.0f + 50.0f * this.expandAnimation.getValue());
        });
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (HoverUtility.isHovering(this.x, this.y, this.width, 17.0f, mouseX, mouseY) && button == 1) {
            this.expanded = !this.expanded;
            return;
        }
        if (button == 0) {
            float xPos = this.x + 5.0f;
            float yPos = this.y + 17.0f;
            float w = 65.0f;
            float h = 50.0f;
            if (HoverUtility.isHovering(xPos, yPos, 65.0f, 50.0f, mouseX, mouseY)) {
                this.colorDragType = ColorDragType.PICKER;
            } else if (HoverUtility.isHovering(xPos + 65.0f + 5.0f, yPos, 8.0f, 50.0f, mouseX, mouseY)) {
                this.colorDragType = ColorDragType.HUE;
            }
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        this.colorDragType = ColorDragType.NONE;
    }

    @Environment(value=EnvType.CLIENT)
    private static enum ColorDragType {
        PICKER,
        HUE,
        OPACITY,
        NONE;

    }
}

