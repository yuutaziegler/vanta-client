/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_276
 */
package wtf.opal.utility.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_276;

@Environment(value=EnvType.CLIENT)
public final class FramebufferUtility {
    private FramebufferUtility() {
    }

    public static void blit(class_276 sourceBuffer, class_276 destinationBuffer) {
        RenderSystem.getDevice().createCommandEncoder().copyTextureToTexture(sourceBuffer.method_30277(), destinationBuffer.method_30277(), 0, 0, 0, 0, 0, destinationBuffer.field_1482, destinationBuffer.field_1481);
    }
}

