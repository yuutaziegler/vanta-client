/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click.dropdown.panel.property.impl;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public final class MultipleBooleanPropertyComponent
extends PropertyPanel<MultipleBooleanProperty> {
    public MultipleBooleanPropertyComponent(MultipleBooleanProperty property) {
        super(property);
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        MultipleBooleanProperty property = (MultipleBooleanProperty)this.getProperty();
        NVGTextRenderer font = FontRepository.getFont("productsans-medium");
        font.drawString(property.getName(), this.x + 5.0f, this.y + 8.5f, 7.0f, -1);
        float addedHeight = this.processText(this.x, this.y, this.width, false);
        float boxX = this.x + 5.0f;
        float boxY = this.y + 13.0f;
        float boxWidth = this.width - 10.0f;
        float boxHeight = 10.0f + addedHeight;
        float radius = 2.5f;
        NVGRenderer.roundedRectOutline(boxX, boxY, boxWidth, boxHeight, 2.5f, 1.5f, -11513776);
        NVGRenderer.roundedRect(boxX, boxY, boxWidth, boxHeight, 2.5f, -15132391);
        this.setHeight(17.0f + boxHeight);
        this.processText(this.x, this.y, this.width, true);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        NVGTextRenderer font = FontRepository.getFont("productsans-medium");
        float addedHeight = 0.0f;
        float currentLineLength = 2.0f;
        for (BooleanProperty booleanProperty : (List)((MultipleBooleanProperty)this.getProperty()).getValue()) {
            if (booleanProperty.isHidden()) continue;
            float elementWidth = font.getStringWidth(booleanProperty.getName(), 6.0f) + 8.75f;
            if (currentLineLength + elementWidth > this.width - 10.0f) {
                addedHeight += 10.0f;
                currentLineLength = 2.0f;
            }
            if (HoverUtility.isHovering(this.x + 4.5f + currentLineLength, this.y + 13.0f + addedHeight + 7.0f - 5.5f, elementWidth - 4.0f, 8.5f, mouseX, mouseY)) {
                booleanProperty.toggle();
            }
            currentLineLength += elementWidth - 2.5f;
        }
    }

    private float processText(float x, float y, float width, boolean render) {
        NVGTextRenderer font = FontRepository.getFont("productsans-medium");
        float addedHeight = 0.0f;
        float currentLineLength = 2.0f;
        for (BooleanProperty booleanProperty : (List)((MultipleBooleanProperty)this.getProperty()).getValue()) {
            if (booleanProperty.isHidden()) continue;
            float elementWidth = font.getStringWidth(booleanProperty.getName(), 6.0f) + 8.75f;
            if (currentLineLength + elementWidth > width - 10.0f) {
                addedHeight += 10.0f;
                currentLineLength = 2.0f;
            }
            if (render) {
                NVGRenderer.roundedRect(x + 4.5f + currentLineLength, y + 13.0f + addedHeight + 7.0f - 5.5f, elementWidth - 4.0f, 8.5f, 2.5f, booleanProperty.getValue() != false ? ColorUtility.applyOpacity((int)((Integer)ColorUtility.getClientTheme().first), 0.4f) : ColorUtility.darker(-8355712, 0.6f));
                font.drawString(booleanProperty.getName(), x + 7.0f + currentLineLength, y + 13.0f + addedHeight + 8.0f, 6.0f, booleanProperty.getValue() != false ? ColorUtility.applyOpacity(-1, 0.9f) : -6250336);
            }
            currentLineLength += elementWidth - 2.5f;
        }
        return addedHeight + 1.5f;
    }
}

