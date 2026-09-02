/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class HoverUtility {
    private HoverUtility() {
    }

    public static boolean isHovering(float x, float y, float width, float height, double mouseX, double mouseY) {
        return mouseX >= (double)x && mouseY >= (double)y && mouseX < (double)(x + width) && mouseY < (double)(y + height);
    }

    public static boolean isHovering(float x, float y, float width, float height, double mouseX, double mouseY, float scaleFactor) {
        float scaledWidth = width * scaleFactor;
        float scaledHeight = height * scaleFactor;
        float offsetX = x + (width - scaledWidth) / 2.0f;
        float offsetY = y + (height - scaledHeight) / 2.0f;
        return mouseX >= (double)offsetX && mouseY >= (double)offsetY && mouseX < (double)(offsetX + scaledWidth) && mouseY < (double)(offsetY + scaledHeight);
    }
}

