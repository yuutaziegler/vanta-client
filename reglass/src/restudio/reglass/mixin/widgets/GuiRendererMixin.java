/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.buffers.GpuBuffer
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_11228
 *  net.minecraft.class_11246
 *  net.minecraft.class_11659
 *  net.minecraft.class_11684
 *  net.minecraft.class_4597$class_4598
 *  org.lwjgl.system.MemoryStack
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package restudio.reglass.mixin.widgets;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import net.minecraft.class_11228;
import net.minecraft.class_11246;
import net.minecraft.class_11659;
import net.minecraft.class_11684;
import net.minecraft.class_4597;
import org.lwjgl.system.MemoryStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import restudio.reglass.client.gui.QuadVertexBufferProvider;

@Mixin(value={class_11228.class})
public class GuiRendererMixin
implements QuadVertexBufferProvider {
    @Unique
    private GpuBuffer reglass$quadVertexBuffer;

    @Inject(method={"<init>"}, at={@At(value="TAIL")})
    private void reglass$onInit(class_11246 state, class_4597.class_4598 vertexConsumers, class_11659 queue, class_11684 dispatcher, List specialElementRenderers, CallbackInfo ci) {
        try (MemoryStack stack = MemoryStack.stackPush();){
            ByteBuffer byteBuffer = stack.malloc(48);
            FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
            floatBuffer.put(new float[]{-1.0f, -1.0f, 0.0f, 1.0f, -1.0f, 0.0f, 1.0f, 1.0f, 0.0f, -1.0f, 1.0f, 0.0f});
            byteBuffer.rewind();
            this.reglass$quadVertexBuffer = RenderSystem.getDevice().createBuffer(() -> "reglass_quad_vbo", 32, byteBuffer);
        }
    }

    @Inject(method={"close"}, at={@At(value="HEAD")})
    private void reglass$onClose(CallbackInfo ci) {
        if (this.reglass$quadVertexBuffer != null) {
            this.reglass$quadVertexBuffer.close();
        }
    }

    @Override
    @Unique
    public GpuBuffer getQuadVertexBuffer() {
        return this.reglass$quadVertexBuffer;
    }
}

