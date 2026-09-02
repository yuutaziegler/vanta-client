/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Builder
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Snippet
 *  com.mojang.blaze3d.platform.DepthTestFunction
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.VertexFormat$class_5596
 *  net.minecraft.class_10789
 *  net.minecraft.class_290
 *  net.minecraft.class_2960
 */
package restudio.reglass.client;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.class_10789;
import net.minecraft.class_290;
import net.minecraft.class_2960;

public final class LiquidGlassPipelines {
    private static RenderPipeline LIQUID_GLASS_GUI;

    private LiquidGlassPipelines() {
    }

    public static synchronized RenderPipeline getGuiPipeline() {
        if (LIQUID_GLASS_GUI == null) {
            RenderPipeline.Builder b = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[0]).withLocation(class_2960.method_60655((String)"reglass", (String)"pipeline/liquid_glass_gui")).withVertexShader(class_2960.method_60655((String)"reglass", (String)"core/blit_fullscreen")).withFragmentShader(class_2960.method_60655((String)"reglass", (String)"program/liquid_glass_gui")).withUniform("Projection", class_10789.field_60031).withUniform("SamplerInfo", class_10789.field_60031).withUniform("CustomUniforms", class_10789.field_60031).withUniform("WidgetInfo", class_10789.field_60031).withUniform("BgConfig", class_10789.field_60031).withSampler("Sampler0").withSampler("Sampler1").withSampler("Sampler2").withSampler("Sampler3").withSampler("Sampler4").withSampler("Sampler5").withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withDepthWrite(false).withVertexFormat(class_290.field_1592, VertexFormat.class_5596.field_27382);
            LIQUID_GLASS_GUI = b.build();
            RenderSystem.getDevice().precompilePipeline(LIQUID_GLASS_GUI, null);
        }
        return LIQUID_GLASS_GUI;
    }
}

