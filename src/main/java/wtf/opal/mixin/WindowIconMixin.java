/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.loader.api.FabricLoader
 *  net.minecraft.class_1041
 *  org.lwjgl.BufferUtils
 *  org.lwjgl.glfw.GLFW
 *  org.lwjgl.glfw.GLFWImage
 *  org.lwjgl.glfw.GLFWImage$Buffer
 *  org.lwjgl.stb.STBImage
 *  org.lwjgl.system.MemoryStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.class_1041;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_1041.class})
public class WindowIconMixin {
    @Inject(method={"setIcon"}, at={@At(value="HEAD")}, cancellable=true)
    private void setCustomIcon(CallbackInfo ci) {
        try {
            long handle = ((class_1041)this).method_4490();
            Path iconPath = FabricLoader.getInstance().getModContainer("terentx").flatMap(c -> c.findPath("assets/terentx/logo.png")).orElse(null);
            if (iconPath == null) {
                return;
            }
            try (MemoryStack stack = MemoryStack.stackPush();){
                IntBuffer w = stack.mallocInt(1);
                IntBuffer h = stack.mallocInt(1);
                IntBuffer comp = stack.mallocInt(1);
                byte[] bytes = Files.readAllBytes(iconPath);
                ByteBuffer buf = BufferUtils.createByteBuffer((int)bytes.length);
                buf.put(bytes);
                buf.flip();
                ByteBuffer img = STBImage.stbi_load_from_memory((ByteBuffer)buf, (IntBuffer)w, (IntBuffer)h, (IntBuffer)comp, (int)4);
                if (img != null) {
                    GLFWImage.Buffer icons = GLFWImage.malloc((int)1, (MemoryStack)stack);
                    ((GLFWImage.Buffer)icons.position(0)).width(w.get(0)).height(h.get(0)).pixels(img);
                    GLFW.glfwSetWindowIcon((long)handle, (GLFWImage.Buffer)icons);
                    STBImage.stbi_image_free((ByteBuffer)img);
                    ci.cancel();
                }
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }
}

