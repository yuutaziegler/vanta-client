/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_124
 *  net.minecraft.class_332
 */
package wtf.opal.client.feature.module.impl.movement.clipper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_124;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.impl.movement.clipper.ClipperModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.preset.DefaultIsland;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public final class ClipperIsland
extends DefaultIsland {
    private final ClipperModule clipperModule;
    private static final NVGTextRenderer FONT = FontRepository.getFont("productsans-medium");
    private String text;
    private float width;

    public ClipperIsland(ClipperModule clipperModule) {
        this.clipperModule = clipperModule;
    }

    @Override
    public void renderIsland(class_332 context, float posX, float posY, float width, float height, float progress) {
        super.renderIsland(context, posX, posY, width, height, progress);
        FONT.drawString(this.text, posX + (width - this.width) * 0.5f, posY + 28.0f + 2.0f, 7.0f, ColorUtility.applyOpacity(-8355712, progress * 2.0f));
    }

    @Override
    public float getIslandWidth() {
        Object text = "You can press ";
        if (this.clipperModule.getUpPos() != null) {
            text = (String)text + String.valueOf(class_124.field_1068) + "UP" + String.valueOf(class_124.field_1070);
            if (this.clipperModule.getDownPos() != null) {
                text = (String)text + " or " + String.valueOf(class_124.field_1068) + "DOWN" + String.valueOf(class_124.field_1070);
            }
        } else {
            text = (String)text + String.valueOf(class_124.field_1068) + "DOWN" + String.valueOf(class_124.field_1070);
        }
        this.text = text = (String)text + " to clip";
        this.width = FONT.getStringWidth((String)text, 7.0f);
        return Math.max(super.getIslandWidth(), this.width + 13.0f + 4.0f);
    }

    @Override
    public float getIslandHeight() {
        return super.getIslandHeight() + 8.0f;
    }

    @Override
    public int getIslandPriority() {
        return -1;
    }
}

