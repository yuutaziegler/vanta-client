/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.buffers.GpuBuffer
 *  com.mojang.blaze3d.buffers.GpuBuffer$MappedView
 *  com.mojang.blaze3d.buffers.Std140Builder
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Snippet
 *  com.mojang.blaze3d.platform.DepthTestFunction
 *  com.mojang.blaze3d.systems.CommandEncoder
 *  com.mojang.blaze3d.systems.RenderPass
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.systems.RenderSystem$class_5590
 *  com.mojang.blaze3d.textures.FilterMode
 *  com.mojang.blaze3d.textures.GpuTexture
 *  com.mojang.blaze3d.textures.GpuTextureView
 *  com.mojang.blaze3d.textures.TextureFormat
 *  com.mojang.blaze3d.vertex.VertexFormat$class_5595
 *  com.mojang.blaze3d.vertex.VertexFormat$class_5596
 *  net.minecraft.class_10789
 *  net.minecraft.class_11228
 *  net.minecraft.class_276
 *  net.minecraft.class_290
 *  net.minecraft.class_2960
 *  net.minecraft.class_310
 *  net.minecraft.class_757
 */
package restudio.reglass.client;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.systems.CommandEncoder;
import com.mojang.blaze3d.systems.RenderPass;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.textures.FilterMode;
import com.mojang.blaze3d.textures.GpuTexture;
import com.mojang.blaze3d.textures.GpuTextureView;
import com.mojang.blaze3d.textures.TextureFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalInt;
import net.minecraft.class_10789;
import net.minecraft.class_11228;
import net.minecraft.class_276;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_310;
import net.minecraft.class_757;
import restudio.reglass.client.api.ReGlassConfig;
import restudio.reglass.client.gui.QuadVertexBufferProvider;
import restudio.reglass.mixin.accessor.GameRendererAccessor;

public final class LiquidGlassPrecomputeRuntime {
    private static final LiquidGlassPrecomputeRuntime INSTANCE = new LiquidGlassPrecomputeRuntime();
    private RenderPipeline blurPipeline;
    private GpuTexture blurTempTex;
    private GpuTextureView blurTempView;
    private final HashMap<Integer, GpuTexture> blurredByRadius = new HashMap();
    private final HashMap<Integer, GpuTextureView> blurredViewByRadius = new HashMap();
    private GpuBuffer samplerInfoUbo;
    private GpuBuffer blurConfigUboX;
    private GpuBuffer blurConfigUboY;
    private static final int MAX_RADIUS = 64;
    private List<Integer> requestedRadii = new ArrayList<Integer>();
    private static final class_2960 VS_ID = class_2960.method_60655((String)"reglass", (String)"core/blit_fullscreen");
    private static final class_2960 BLUR_ID = class_2960.method_60655((String)"reglass", (String)"program/blur");

    public static LiquidGlassPrecomputeRuntime get() {
        return INSTANCE;
    }

    private LiquidGlassPrecomputeRuntime() {
    }

    private void ensurePipelines() {
        if (this.blurPipeline == null) {
            this.blurPipeline = RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[0]).withLocation(class_2960.method_60655((String)"reglass", (String)"pipeline/blur")).withVertexShader(VS_ID).withFragmentShader(BLUR_ID).withUniform("Projection", class_10789.field_60031).withUniform("SamplerInfo", class_10789.field_60031).withUniform("Config", class_10789.field_60031).withSampler("DiffuseSampler").withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withDepthWrite(false).withVertexFormat(class_290.field_1592, VertexFormat.class_5596.field_27382).build();
            RenderSystem.getDevice().precompilePipeline(this.blurPipeline, null);
        }
        if (this.samplerInfoUbo == null) {
            this.samplerInfoUbo = RenderSystem.getDevice().createBuffer(() -> "reglass SamplerInfo (pre)", 130, 16);
        }
        int blurConfigSize = 1056;
        if (this.blurConfigUboX == null) {
            this.blurConfigUboX = RenderSystem.getDevice().createBuffer(() -> "reglass BlurConfig X", 130, blurConfigSize);
        }
        if (this.blurConfigUboY == null) {
            this.blurConfigUboY = RenderSystem.getDevice().createBuffer(() -> "reglass BlurConfig Y", 130, blurConfigSize);
        }
    }

    private void ensureTempTarget(int w, int h) {
        if (this.blurTempTex == null || this.blurTempTex.getWidth(0) != w || this.blurTempTex.getHeight(0) != h) {
            if (this.blurTempTex != null) {
                if (this.blurTempView != null) {
                    this.blurTempView.close();
                }
                this.blurTempTex.close();
            }
            this.blurTempTex = RenderSystem.getDevice().createTexture("reglass blurTemp", 12, TextureFormat.RGBA8, w, h, 1, 1);
            this.blurTempTex.setTextureFilter(FilterMode.LINEAR, false);
            this.blurTempView = RenderSystem.getDevice().createTextureView(this.blurTempTex);
        }
    }

    private void ensureOutputForRadius(int w, int h, int radius) {
        GpuTexture tex = this.blurredByRadius.get(radius);
        if (tex == null || tex.getWidth(0) != w || tex.getHeight(0) != h) {
            if (tex != null) {
                GpuTextureView old = this.blurredViewByRadius.get(radius);
                if (old != null) {
                    old.close();
                }
                tex.close();
            }
            GpuTexture newTex = RenderSystem.getDevice().createTexture("reglass blurred r=" + radius, 12, TextureFormat.RGBA8, w, h, 1, 1);
            newTex.setTextureFilter(FilterMode.LINEAR, false);
            GpuTextureView newView = RenderSystem.getDevice().createTextureView(newTex);
            this.blurredByRadius.put(radius, newTex);
            this.blurredViewByRadius.put(radius, newView);
        }
    }

    private static float[] gaussian(int radius) {
        int i;
        radius = Math.max(0, Math.min(radius, 64));
        float sigma = (float)radius / 3.0f;
        if (radius == 0) {
            return new float[]{1.0f};
        }
        float[] kernel = new float[radius + 1];
        float sum = 0.0f;
        for (i = 0; i <= radius; ++i) {
            float w;
            kernel[i] = w = (float)Math.exp(-0.5 * (double)((float)i * (float)i) / (double)(sigma * sigma));
            sum += i == 0 ? w : 2.0f * w;
        }
        i = 0;
        while (i <= radius) {
            int n = i++;
            kernel[n] = kernel[n] / sum;
        }
        return kernel;
    }

    private void uploadBlur(GpuBuffer ubo, float dx, float dy, int radius) {
        radius = Math.max(0, Math.min(radius, 64));
        float[] weights = LiquidGlassPrecomputeRuntime.gaussian(radius);
        try (GpuBuffer.MappedView map = RenderSystem.getDevice().createCommandEncoder().mapBuffer(ubo, false, true);){
            Std140Builder b = Std140Builder.intoBuffer((ByteBuffer)map.data());
            b.putVec4(dx, dy, (float)radius, 0.0f);
            for (int i = 0; i <= 64; ++i) {
                float w = i <= radius ? weights[i] : 0.0f;
                b.putFloat(w);
                b.align(16);
            }
        }
    }

    public void setRequestedRadii(List<Integer> ordered) {
        this.requestedRadii = new ArrayList<Integer>(ordered);
    }

    public void run() {
        this.ensurePipelines();
        class_310 mc = class_310.method_1551();
        class_276 main = mc.method_1522();
        int w = main.field_1482;
        int h = main.field_1481;
        this.ensureTempTarget(w, h);
        try (GpuBuffer.MappedView map = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.samplerInfoUbo, false, true);){
            Std140Builder.intoBuffer((ByteBuffer)map.data()).putVec2((float)w, (float)h).putVec2((float)w, (float)h);
        }
        CommandEncoder ce = RenderSystem.getDevice().createCommandEncoder();
        class_757 gameRenderer = mc.field_1773;
        class_11228 guiRenderer = ((GameRendererAccessor)gameRenderer).getGuiRenderer();
        GpuBuffer quadVB = ((QuadVertexBufferProvider)guiRenderer).getQuadVertexBuffer();
        RenderSystem.class_5590 idxInfo = RenderSystem.getSequentialBuffer((VertexFormat.class_5596)VertexFormat.class_5596.field_27382);
        GpuBuffer ib = idxInfo.method_68274(6);
        VertexFormat.class_5595 it = idxInfo.method_31924();
        int max = Math.min(5, this.requestedRadii == null ? 0 : this.requestedRadii.size());
        if (max == 0) {
            int r = ReGlassConfig.INSTANCE.defaultBlurRadius;
            this.requestedRadii = List.of(Integer.valueOf(r));
            max = 1;
        }
        for (int k = 0; k < max; ++k) {
            int radius = this.requestedRadii.get(k);
            if (radius <= 0) continue;
            this.ensureOutputForRadius(w, h, radius);
            this.uploadBlur(this.blurConfigUboX, 1.0f, 0.0f, radius);
            this.uploadBlur(this.blurConfigUboY, 0.0f, 1.0f, radius);
            try (RenderPass pass = ce.createRenderPass(() -> "reglass blur X r=" + radius, this.blurTempView, OptionalInt.empty());){
                pass.setPipeline(this.blurPipeline);
                RenderSystem.bindDefaultUniforms((RenderPass)pass);
                pass.setUniform("SamplerInfo", this.samplerInfoUbo);
                pass.setUniform("Config", this.blurConfigUboX);
                pass.bindSampler("DiffuseSampler", main.method_71639());
                pass.setVertexBuffer(0, quadVB);
                pass.setIndexBuffer(ib, it);
                pass.drawIndexed(0, 0, 6, 1);
            }
            pass = ce.createRenderPass(() -> "reglass blur Y r=" + radius, this.blurredViewByRadius.get(radius), OptionalInt.empty());
            try {
                pass.setPipeline(this.blurPipeline);
                RenderSystem.bindDefaultUniforms((RenderPass)pass);
                pass.setUniform("SamplerInfo", this.samplerInfoUbo);
                pass.setUniform("Config", this.blurConfigUboY);
                pass.bindSampler("DiffuseSampler", this.blurTempView);
                pass.setVertexBuffer(0, quadVB);
                pass.setIndexBuffer(ib, it);
                pass.drawIndexed(0, 0, 6, 1);
                continue;
            }
            finally {
                if (pass != null) {
                    pass.close();
                }
            }
        }
    }

    public GpuTextureView getBlurredViewForRadius(int radius) {
        return this.blurredViewByRadius.get(radius);
    }
}

