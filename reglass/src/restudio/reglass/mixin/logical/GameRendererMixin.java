/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.buffers.GpuBuffer
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.systems.RenderPass
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.systems.RenderSystem$class_5590
 *  com.mojang.blaze3d.vertex.VertexFormat$class_5596
 *  net.minecraft.class_11228
 *  net.minecraft.class_276
 *  net.minecraft.class_310
 *  net.minecraft.class_757
 *  net.minecraft.class_9779
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package restudio.reglass.mixin.logical;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.minecraft.class_11228;
import net.minecraft.class_276;
import net.minecraft.class_310;
import net.minecraft.class_757;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import restudio.reglass.client.LiquidGlassPipelines;
import restudio.reglass.client.LiquidGlassPrecomputeRuntime;
import restudio.reglass.client.LiquidGlassUniforms;
import restudio.reglass.client.api.ReGlassConfig;
import restudio.reglass.client.gui.QuadVertexBufferProvider;
import restudio.reglass.client.runtime.ReGlassAnim;
import restudio.reglass.mixin.accessor.GameRendererAccessor;

@Mixin(value={class_757.class})
public abstract class GameRendererMixin {
    @Shadow
    @Final
    private class_310 field_4015;

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void reglass$beginGuiFrame(class_9779 tickCounter, boolean tick, CallbackInfo ci) {
        double deltaTicks;
        try {
            deltaTicks = tickCounter.method_60636();
        }
        catch (Throwable t) {
            deltaTicks = 0.3333333333333333;
        }
        double dt = deltaTicks / 20.0;
        LiquidGlassUniforms.get().beginFrame(dt);
        ReGlassAnim.INSTANCE.update(ReGlassConfig.INSTANCE, dt);
    }

    @Inject(method={"renderBlur"}, at={@At(value="HEAD")}, cancellable=true)
    private void reglass$renderLiquidGlass(CallbackInfo ci) {
        LiquidGlassUniforms uniforms = LiquidGlassUniforms.get();
        if (uniforms.getCount() > 0) {
            ci.cancel();
            uniforms.uploadSharedUniforms();
            uniforms.uploadWidgetInfo();
            List<Integer> radii = uniforms.getUsedBlurRadiiOrdered();
            LiquidGlassPrecomputeRuntime.get().setRequestedRadii(radii);
            LiquidGlassPrecomputeRuntime.get().run();
            class_276 mainFb = this.field_4015.method_1522();
            try (RenderPass pass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "reglass liquid glass pass", mainFb.method_71639(), OptionalInt.empty(), mainFb.field_1478 ? mainFb.method_71640() : null, OptionalDouble.empty());){
                RenderPipeline pipeline = LiquidGlassPipelines.getGuiPipeline();
                pass.setPipeline(pipeline);
                RenderSystem.bindDefaultUniforms((RenderPass)pass);
                pass.setUniform("SamplerInfo", uniforms.getSamplerInfoBuffer());
                pass.setUniform("CustomUniforms", uniforms.getCustomUniformsBuffer());
                pass.setUniform("WidgetInfo", uniforms.getWidgetInfoBuffer());
                pass.setUniform("BgConfig", uniforms.getBgConfigBuffer());
                pass.bindSampler("Sampler0", mainFb.method_71639());
                class_11228 guiRenderer = ((GameRendererAccessor)((Object)this)).getGuiRenderer();
                GpuBuffer quadVB = ((QuadVertexBufferProvider)guiRenderer).getQuadVertexBuffer();
                RenderSystem.class_5590 quadIBInfo = RenderSystem.getSequentialBuffer((VertexFormat.class_5596)VertexFormat.class_5596.field_27382);
                GpuBuffer quadIB = quadIBInfo.method_68274(6);
                pass.setVertexBuffer(0, quadVB);
                pass.setIndexBuffer(quadIB, quadIBInfo.method_31924());
                for (int i = 0; i < 5; ++i) {
                    String samplerName;
                    switch (i) {
                        case 0: {
                            String string = "Sampler1";
                            break;
                        }
                        case 1: {
                            String string = "Sampler2";
                            break;
                        }
                        case 2: {
                            String string = "Sampler3";
                            break;
                        }
                        case 3: {
                            String string = "Sampler4";
                            break;
                        }
                        default: {
                            String string = samplerName = "Sampler5";
                        }
                    }
                    if (i < radii.size()) {
                        int r = radii.get(i);
                        if (r <= 0) {
                            pass.bindSampler(samplerName, mainFb.method_71639());
                            continue;
                        }
                        pass.bindSampler(samplerName, LiquidGlassPrecomputeRuntime.get().getBlurredViewForRadius(r));
                        continue;
                    }
                    if (!radii.isEmpty()) {
                        int r0 = radii.getFirst();
                        if (r0 <= 0) {
                            pass.bindSampler(samplerName, mainFb.method_71639());
                            continue;
                        }
                        pass.bindSampler(samplerName, LiquidGlassPrecomputeRuntime.get().getBlurredViewForRadius(r0));
                        continue;
                    }
                    pass.bindSampler(samplerName, mainFb.method_71639());
                }
                pass.drawIndexed(0, 0, 6, 1);
            }
        }
    }
}

