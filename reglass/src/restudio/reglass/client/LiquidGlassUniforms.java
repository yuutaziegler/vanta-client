/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.buffers.GpuBuffer
 *  com.mojang.blaze3d.buffers.GpuBuffer$MappedView
 *  com.mojang.blaze3d.buffers.Std140Builder
 *  com.mojang.blaze3d.buffers.Std140SizeCalculator
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.class_11246
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_8030
 *  net.minecraft.class_9848
 *  org.joml.Vector2f
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 *  org.joml.Vector4f
 *  org.joml.Vector4fc
 *  org.lwjgl.glfw.GLFW
 */
package restudio.reglass.client;

import com.mojang.blaze3d.buffers.GpuBuffer;
import com.mojang.blaze3d.buffers.Std140Builder;
import com.mojang.blaze3d.buffers.Std140SizeCalculator;
import com.mojang.blaze3d.systems.RenderSystem;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import net.minecraft.class_11246;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_8030;
import net.minecraft.class_9848;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.lwjgl.glfw.GLFW;
import restudio.reglass.client.api.ReGlassConfig;
import restudio.reglass.client.api.WidgetStyle;
import restudio.reglass.client.gui.LiquidGlassGuiElementRenderState;
import restudio.reglass.client.runtime.ReGlassAnim;
import restudio.reglass.mixin.accessor.GuiRenderStateAccessor;

public final class LiquidGlassUniforms {
    private static final LiquidGlassUniforms INSTANCE = new LiquidGlassUniforms();
    public static final int MAX_WIDGETS = 64;
    public static final int MAX_BLUR_LEVELS = 5;
    private final GpuBuffer samplerInfo;
    private final GpuBuffer customUniforms;
    private final GpuBuffer widgetInfo;
    private final GpuBuffer bgConfig;
    private final List<LiquidGlassGuiElementRenderState> widgets = new ArrayList<LiquidGlassGuiElementRenderState>();
    private boolean screenWantsBlur = false;
    private List<Integer> usedBlurRadiiOrdered = new ArrayList<Integer>();
    private final HashMap<Integer, Integer> blurRadiusToIndex = new HashMap();
    private final HashMap<Long, FadeState> fades = new HashMap();
    private double dtSeconds = 0.0;

    public static LiquidGlassUniforms get() {
        return INSTANCE;
    }

    private LiquidGlassUniforms() {
        this.samplerInfo = RenderSystem.getDevice().createBuffer(() -> "reglass SamplerInfo", 130, 16);
        Std140SizeCalculator calc = new Std140SizeCalculator();
        calc.putFloat();
        calc.align(16);
        calc.putVec4();
        calc.putFloat();
        calc.align(16);
        calc.putVec3();
        calc.align(16);
        calc.putVec4();
        calc.putFloat();
        calc.putFloat();
        calc.putFloat();
        calc.putFloat();
        calc.putFloat();
        calc.putFloat();
        calc.putFloat();
        calc.putFloat();
        calc.putFloat();
        int customUniformsSize = calc.get();
        this.customUniforms = RenderSystem.getDevice().createBuffer(() -> "reglass CustomUniforms", 130, customUniformsSize);
        int widgetInfoSize = 12304;
        this.widgetInfo = RenderSystem.getDevice().createBuffer(() -> "reglass WidgetInfo", 130, widgetInfoSize);
        Std140SizeCalculator bcalc = new Std140SizeCalculator();
        bcalc.putFloat();
        bcalc.putFloat();
        bcalc.putVec2();
        int bgConfigSize = bcalc.get();
        this.bgConfig = RenderSystem.getDevice().createBuffer(() -> "reglass BgConfig", 130, bgConfigSize);
    }

    public void beginFrame(double dtSeconds) {
        this.widgets.clear();
        this.screenWantsBlur = false;
        this.usedBlurRadiiOrdered.clear();
        this.blurRadiusToIndex.clear();
        this.dtSeconds = Math.max(0.0, dtSeconds);
    }

    public void setScreenWantsBlur(boolean wantsBlur) {
        this.screenWantsBlur = wantsBlur;
    }

    public void uploadSharedUniforms() {
        Std140Builder b;
        class_310 mc = class_310.method_1551();
        int outW = mc.method_1522().field_1482;
        int outH = mc.method_1522().field_1481;
        try (GpuBuffer.MappedView map = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.samplerInfo, false, true);){
            Std140Builder b2 = Std140Builder.intoBuffer((ByteBuffer)map.data());
            b2.putVec2((float)outW, (float)outH);
            b2.putVec2((float)outW, (float)outH);
        }
        double[] mx = new double[1];
        double[] my = new double[1];
        GLFW.glfwGetCursorPos((long)mc.method_22683().method_4490(), (double[])mx, (double[])my);
        float scale = mc.method_22683().method_4495();
        int fbH = mc.method_1522().field_1481;
        float time = (float)GLFW.glfwGetTime();
        ReGlassConfig config = ReGlassConfig.INSTANCE;
        try (GpuBuffer.MappedView map = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.customUniforms, false, true);){
            b = Std140Builder.intoBuffer((ByteBuffer)map.data());
            b.putFloat(time);
            b.align(16);
            float x = (float)(mx[0] * (double)scale);
            float y = (float)fbH - (float)(my[0] * (double)scale);
            b.putVec4((Vector4fc)new Vector4f(x, y, 0.0f, 0.0f));
            b.putFloat(this.screenWantsBlur ? 1.0f : 0.0f);
            b.align(16);
            Vector2f dir2 = config.rimLight.direction();
            b.putVec3((Vector3fc)new Vector3f(dir2.x, dir2.y, 0.0f));
            b.align(16);
            int rc = config.rimLight.color();
            b.putVec4((float)class_9848.method_61327((int)rc) / 255.0f, (float)class_9848.method_61329((int)rc) / 255.0f, (float)class_9848.method_61331((int)rc) / 255.0f, config.rimLight.intensity());
            b.putFloat(config.pixelEpsilon);
            b.putFloat(ReGlassAnim.INSTANCE.debugStep());
            b.putFloat(config.features.pixelatedGrid ? 1.0f : 0.0f);
            b.putFloat(ReGlassAnim.INSTANCE.pixelatedGridSize());
            b.putFloat(ReGlassAnim.INSTANCE.hoverScalePx());
            b.putFloat(ReGlassAnim.INSTANCE.focusScalePx());
            b.putFloat(ReGlassAnim.INSTANCE.focusBorderWidthPx());
            b.putFloat(ReGlassAnim.INSTANCE.focusBorderIntensity());
            b.putFloat(ReGlassAnim.INSTANCE.focusBorderSpeed());
        }
        map = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.bgConfig, false, true);
        try {
            b = Std140Builder.intoBuffer((ByteBuffer)map.data());
            b.putFloat(ReGlassAnim.INSTANCE.shadowExpand());
            b.putFloat(ReGlassAnim.INSTANCE.shadowFactor());
            float s = mc.method_22683().method_4495();
            b.putVec2(ReGlassAnim.INSTANCE.shadowOffsetX() * s, ReGlassAnim.INSTANCE.shadowOffsetY() * s);
        }
        finally {
            if (map != null) {
                map.close();
            }
        }
    }

    public void tryApplyBlur(class_332 context) {
        class_11246 state = context.field_59826;
        int blurLayer = ((GuiRenderStateAccessor)state).getBlurLayer();
        if (blurLayer == Integer.MAX_VALUE) {
            state.method_71299();
        }
    }

    public void addWidget(LiquidGlassGuiElementRenderState element) {
        if (this.widgets.size() >= 64) {
            return;
        }
        this.widgets.add(element);
    }

    private static long rectKey(int x1, int y1, int x2, int y2) {
        long a = (long)x1 & 0xFFFFFFFFL | ((long)y1 & 0xFFFFFFFFL) << 32;
        long b = (long)x2 & 0xFFFFFFFFL | ((long)y2 & 0xFFFFFFFFL) << 32;
        long h = 1469598103934665603L;
        h ^= a;
        h *= 1099511628211L;
        h ^= b;
        return h *= 1099511628211L;
    }

    private float smoothToward(float current, float target, double dt, float tau) {
        if (tau <= 1.0E-5f) {
            return target;
        }
        float a = (float)(1.0 - Math.exp(-Math.max(0.0, dt) / Math.max(1.0E-4, (double)tau)));
        float v = current + (target - current) * a;
        if (Math.abs(v - target) < 1.0E-4f) {
            return target;
        }
        return v;
    }

    public void uploadWidgetInfo() {
        int i;
        class_310 mc = class_310.method_1551();
        int fbH = mc.method_1522().field_1481;
        float scale = mc.method_22683().method_4495();
        HashSet<Integer> requested = new HashSet<Integer>();
        for (LiquidGlassGuiElementRenderState w : this.widgets) {
            WidgetStyle s = w.style();
            requested.add(Math.max(0, s.getBlurRadius()));
        }
        List sorted = requested.stream().sorted().toList();
        this.usedBlurRadiiOrdered = new ArrayList<Integer>();
        for (i = 0; i < sorted.size() && i < 5; ++i) {
            this.usedBlurRadiiOrdered.add((Integer)sorted.get(i));
        }
        if (this.usedBlurRadiiOrdered.isEmpty()) {
            this.usedBlurRadiiOrdered.add(ReGlassAnim.INSTANCE.blurRadiusInt());
        }
        this.blurRadiusToIndex.clear();
        for (i = 0; i < this.usedBlurRadiiOrdered.size(); ++i) {
            this.blurRadiusToIndex.put(this.usedBlurRadiiOrdered.get(i), i);
        }
        try (GpuBuffer.MappedView map = RenderSystem.getDevice().createCommandEncoder().mapBuffer(this.widgetInfo, false, true);){
            WidgetStyle s;
            LiquidGlassGuiElementRenderState w;
            int i2;
            Std140Builder b = Std140Builder.intoBuffer((ByteBuffer)map.data());
            b.putFloat((float)this.widgets.size());
            b.align(16);
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    w = this.widgets.get(i2);
                    float W = w.comp_4124() - w.comp_4122();
                    float H = w.comp_4125() - w.comp_4123();
                    float px = (float)w.comp_4122() * scale;
                    float pyTop = (float)w.comp_4123() * scale;
                    float pW = W * scale;
                    float pH = H * scale;
                    float cx = px + 0.5f * pW;
                    float cyTop = pyTop + 0.5f * pH;
                    float cyFB = (float)fbH - cyTop;
                    float rectX = cx - 0.5f * pW;
                    float rectY = cyFB - 0.5f * pH;
                    b.putVec4(rectX, rectY, pW, pH);
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    w = this.widgets.get(i2);
                    float rad = w.cornerRadius() * scale;
                    b.putVec4(rad, rad, rad, rad);
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    WidgetStyle style = this.widgets.get(i2).style();
                    int c = style.getTintColor();
                    b.putVec4((float)class_9848.method_61327((int)c) / 255.0f, (float)class_9848.method_61329((int)c) / 255.0f, (float)class_9848.method_61331((int)c) / 255.0f, style.getTintAlpha());
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    s = this.widgets.get(i2).style();
                    b.putVec4(s.getRefThickness(), s.getRefFactor(), s.getRefDispersion(), s.getRefFresnelRange());
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    s = this.widgets.get(i2).style();
                    b.putVec4(s.getRefFresnelHardness(), s.getRefFresnelFactor(), s.getGlareRange(), s.getGlareHardness());
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    s = this.widgets.get(i2).style();
                    b.putVec4(s.getGlareConvergence(), s.getGlareOppositeFactor(), s.getGlareFactor(), s.getGlareAngleRad());
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    s = this.widgets.get(i2).style();
                    b.putVec4(s.getSmoothing(), 0.0f, 0.0f, 0.0f);
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    w = this.widgets.get(i2);
                    class_8030 sc = w.comp_4128();
                    if (sc != null) {
                        float sL = (float)sc.method_49620() * scale;
                        float sR = (float)sc.method_49621() * scale;
                        float sT = (float)sc.method_49618() * scale;
                        float sB = (float)sc.method_49619() * scale;
                        b.putVec4(sL, (float)fbH - sB, sR, (float)fbH - sT);
                        continue;
                    }
                    b.putVec4(0.0f, 0.0f, (float)mc.method_1522().field_1482, (float)mc.method_1522().field_1481);
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    s = this.widgets.get(i2).style();
                    float sx = s.getShadowOffsetX() * scale;
                    float sy = s.getShadowOffsetY() * scale;
                    b.putVec4(s.getShadowExpand(), s.getShadowFactor(), sx, sy);
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    s = this.widgets.get(i2).style();
                    int col = s.getShadowColor();
                    b.putVec4((float)class_9848.method_61327((int)col) / 255.0f, (float)class_9848.method_61329((int)col) / 255.0f, (float)class_9848.method_61331((int)col) / 255.0f, s.getShadowColorAlpha());
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
            for (i2 = 0; i2 < 64; ++i2) {
                if (i2 < this.widgets.size()) {
                    s = this.widgets.get(i2).style();
                    int radius = Math.max(0, s.getBlurRadius());
                    Integer idx = this.blurRadiusToIndex.get(radius);
                    if (idx == null) {
                        idx = 0;
                    }
                    LiquidGlassGuiElementRenderState w2 = this.widgets.get(i2);
                    long key = LiquidGlassUniforms.rectKey(w2.comp_4122(), w2.comp_4123(), w2.comp_4124(), w2.comp_4125());
                    FadeState fs = this.fades.computeIfAbsent(key, k -> new FadeState());
                    fs.hover = this.smoothToward(fs.hover, Math.max(0.0f, Math.min(1.0f, w2.hover())), this.dtSeconds, 0.12f);
                    fs.focus = this.smoothToward(fs.focus, Math.max(0.0f, Math.min(1.0f, w2.focus())), this.dtSeconds, 0.18f);
                    double h = Math.sin((double)w2.comp_4122() * 12.9898 + (double)w2.comp_4123() * 78.233 + (double)i2 * 37.719);
                    float seed = (float)(h - Math.floor(h));
                    b.putVec4((float)idx.intValue(), fs.hover, fs.focus, seed);
                    continue;
                }
                b.putVec4(0.0f, 0.0f, 0.0f, 0.0f);
            }
        }
    }

    public int getCount() {
        return this.widgets.size();
    }

    public GpuBuffer getSamplerInfoBuffer() {
        return this.samplerInfo;
    }

    public GpuBuffer getCustomUniformsBuffer() {
        return this.customUniforms;
    }

    public GpuBuffer getWidgetInfoBuffer() {
        return this.widgetInfo;
    }

    public GpuBuffer getBgConfigBuffer() {
        return this.bgConfig;
    }

    public List<Integer> getUsedBlurRadiiOrdered() {
        return this.usedBlurRadiiOrdered;
    }

    private static final class FadeState {
        float hover;
        float focus;

        private FadeState() {
        }
    }
}

