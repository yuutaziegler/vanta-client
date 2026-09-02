/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_315
 *  net.minecraft.class_332
 *  net.minecraft.class_429
 *  net.minecraft.class_437
 */
package wtf.opal.client.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_315;
import net.minecraft.class_332;
import net.minecraft.class_429;
import net.minecraft.class_437;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.ImageRepository;

@Environment(value=EnvType.CLIENT)
public class TerentXOptionsScreen
extends class_429 {
    public TerentXOptionsScreen(class_437 parent, class_315 gameOptions) {
        super(parent, gameOptions);
    }

    public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
        NVGRenderer.beginFrame();
        NVGImageRenderer bgImg = ImageRepository.getImage("image/mmpng.png");
        if (bgImg != null) {
            bgImg.drawImage(0.0f, 0.0f, this.field_22789, this.field_22790);
        }
        NVGRenderer.endFrame(false);
    }
}

