/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11908
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_332;

@Environment(value=EnvType.CLIENT)
public interface IOpalComponent {
    default public void init() {
    }

    default public void close() {
    }

    public void render(class_332 var1, int var2, int var3, float var4);

    public void mouseClicked(double var1, double var3, int var5);

    default public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
    }

    default public void mouseReleased(double mouseX, double mouseY, int button) {
    }

    default public void keyPressed(class_11908 keyInput) {
    }

    default public void charTyped(char chr, int modifiers) {
    }
}

