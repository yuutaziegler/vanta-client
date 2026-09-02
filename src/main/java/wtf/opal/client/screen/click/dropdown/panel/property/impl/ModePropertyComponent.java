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
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.render.ClientTheme;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class ModePropertyComponent
extends PropertyPanel<ModeProperty<?>> {
    private final Animation expandAnimation = new Animation(Easing.DECELERATE, 125L);
    private boolean expanded;

    public ModePropertyComponent(ModeProperty<?> property) {
        super(property);
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.expandAnimation.run(this.expanded ? 1.0f : 0.0f);
        NVGTextRenderer font = FontRepository.getFont("productsans-medium");
        NVGTextRenderer fontBold = FontRepository.getFont("productsans-bold");
        font.drawString(((ModeProperty)this.getProperty()).getName(), this.x + 5.0f, this.y + 9.5f, 7.0f, -1);
        float padding = 2.0f;
        float rectX = this.x + 3.0f;
        float rectY = this.y + 2.0f + 11.5f;
        float rectWidth = this.width - 5.0f - 2.0f;
        NVGRenderer.roundedRect(rectX, rectY, rectWidth, this.height - 2.0f - 15.0f, 4.0f, ColorUtility.applyOpacity(-16777216, 0.25f));
        fontBold.drawString(((Enum)((ModeProperty)this.getProperty()).getValue()).toString(), rectX + 4.0f, rectY + 10.0f, 7.0f, -1);
        if (((ModeProperty)this.getProperty()).isTheme()) {
            ClientTheme selectedTheme = ClientTheme.valueOf(((Enum)((ModeProperty)this.getProperty()).getValue()).name());
            Pair<Integer, Integer> colors = selectedTheme.getColors();
            float valueWidth = fontBold.getStringWidth(((Enum)((ModeProperty)this.getProperty()).getValue()).toString(), 7.0f);
            NVGRenderer.roundedRect(rectX + valueWidth + 7.0f, rectY + 4.0f, 7.0f, 7.0f, 2.0f, (Integer)colors.first);
            NVGRenderer.roundedRect(rectX + valueWidth + 16.0f, rectY + 4.0f, 7.0f, 7.0f, 2.0f, (Integer)colors.second);
        }
        String expandIcon = "\ue5cf";
        NVGTextRenderer iconFont = FontRepository.getFont("materialicons-regular");
        float iconSize = 9.0f;
        float iconWidth = iconFont.getStringWidth("\ue5cf", 9.0f);
        NVGRenderer.rotate(this.expandAnimation.getValue() * 180.0f, rectX + rectWidth - 12.0f, rectY + 2.5f, iconWidth, 9.0f, () -> iconFont.drawString("\ue5cf", 0.0f, 0.0f, 9.0f, -1, false, 18));
        NVGRenderer.scissor(rectX, rectY, rectWidth, this.height - 2.0f - 15.0f, () -> {
            int addedHeight = 0;
            if (this.expandAnimation.getValue() > 0.0f) {
                for (Enum mode : ((ModeProperty)this.getProperty()).getValues()) {
                    if (mode == null || mode == ((ModeProperty)this.getProperty()).getValue()) continue;
                    font.drawString(mode.toString(), rectX + 4.0f, rectY + 9.5f + 13.0f + (float)addedHeight, 7.0f, -1);
                    if (((ModeProperty)this.getProperty()).isTheme()) {
                        ClientTheme selectedTheme = ClientTheme.valueOf(mode.name());
                        Pair<Integer, Integer> colors = selectedTheme.getColors();
                        NVGRenderer.roundedRect(rectX + this.width - 5.0f - 2.0f - 20.5f, rectY + 3.5f + 13.0f + (float)addedHeight, 7.0f, 7.0f, 2.5f, (Integer)colors.first);
                        NVGRenderer.roundedRect(rectX + this.width - 5.0f - 2.0f - 12.0f, rectY + 3.5f + 13.0f + (float)addedHeight, 7.0f, 7.0f, 2.5f, (Integer)colors.second);
                    }
                    addedHeight += 13;
                }
            }
            this.setHeight(32.0f + (float)addedHeight * this.expandAnimation.getValue());
        });
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (HoverUtility.isHovering(this.x, this.y, this.width, 32.0f, mouseX, mouseY) && button == 1) {
            this.expanded = !this.expanded;
            return;
        }
        if (this.expanded) {
            float padding = 2.0f;
            float rectX = this.x + 3.0f;
            float rectY = this.y + 2.0f + 11.5f;
            float rectWidth = this.width - 8.0f - 2.0f - 3.0f;
            int addedHeight = 0;
            for (Enum mode : ((ModeProperty)this.getProperty()).getValues()) {
                if (mode == null || mode.ordinal() == ((Enum)((ModeProperty)this.getProperty()).getValue()).ordinal()) continue;
                if (HoverUtility.isHovering(rectX, rectY + 13.0f + (float)addedHeight, rectWidth, 13.0f, mouseX, mouseY)) {
                    ((ModeProperty)this.getProperty()).setValueOrdinal(mode.ordinal());
                    this.expanded = false;
                }
                addedHeight += 13;
            }
        }
    }
}

