/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.pipeline.RenderPipeline
 *  com.mojang.blaze3d.pipeline.RenderPipeline$Snippet
 *  com.mojang.blaze3d.platform.DepthTestFunction
 *  com.mojang.blaze3d.vertex.VertexFormat$class_5596
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10799
 *  net.minecraft.class_156
 *  net.minecraft.class_1921
 *  net.minecraft.class_1921$class_4688
 *  net.minecraft.class_290
 *  net.minecraft.class_2960
 *  net.minecraft.class_4668$class_4677
 */
package wtf.opal.utility.render;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.OptionalDouble;
import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10799;
import net.minecraft.class_156;
import net.minecraft.class_1921;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_4668;

@Environment(value=EnvType.CLIENT)
public class CustomRenderLayers {
    public static final class_1921 POS_COL_QUADS_NO_DEPTH_TEST = class_1921.method_24049((String)"renderer/always_depth_pos_color", (int)1024, (boolean)false, (boolean)true, (RenderPipeline)class_10799.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56860}).withLocation(class_2960.method_60655((String)"renderer", (String)"pipeline/pos_col_quads_nodepth")).withCull(true).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withDepthWrite(true).build()), (class_1921.class_4688)class_1921.class_4688.method_23598().method_23617(false));
    public static final class_1921 POS_COL_QUADS_WITH_DEPTH_TEST = class_1921.method_24049((String)"renderer/lequal_depth_pos_color", (int)1024, (boolean)false, (boolean)true, (RenderPipeline)class_10799.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56860}).withLocation(class_2960.method_60655((String)"renderer", (String)"pipeline/pos_col_quads_depth")).withCull(true).withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST).withDepthWrite(true).build()), (class_1921.class_4688)class_1921.class_4688.method_23598().method_23617(false));
    private static final RenderPipeline LINES_NODEPTH_PIPELINE = class_10799.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56859}).withLocation(class_2960.method_60655((String)"renderer", (String)"pipeline/lines_nodepth")).withDepthTestFunction(DepthTestFunction.NO_DEPTH_TEST).withDepthWrite(true).withVertexFormat(class_290.field_29337, VertexFormat.class_5596.field_27377).build());
    public static final Function<Double, class_1921> LINES_NO_DEPTH_TEST = class_156.method_34866(width -> class_1921.method_24049((String)"renderer/always_depth_lines", (int)1024, (boolean)false, (boolean)true, (RenderPipeline)LINES_NODEPTH_PIPELINE, (class_1921.class_4688)class_1921.class_4688.method_23598().method_23609(new class_4668.class_4677(width == 0.0 ? OptionalDouble.empty() : OptionalDouble.of(width))).method_23617(false)));
    private static final RenderPipeline LINES_DEPTH_PIPELINE = class_10799.method_67887((RenderPipeline)RenderPipeline.builder((RenderPipeline.Snippet[])new RenderPipeline.Snippet[]{class_10799.field_56859}).withLocation(class_2960.method_60655((String)"renderer", (String)"pipeline/lines_depth")).withVertexFormat(class_290.field_29337, VertexFormat.class_5596.field_27377).build());
    public static final Function<Double, class_1921> LINES = class_156.method_34866(width -> class_1921.method_24049((String)"renderer/lines", (int)1024, (boolean)false, (boolean)true, (RenderPipeline)LINES_DEPTH_PIPELINE, (class_1921.class_4688)class_1921.class_4688.method_23598().method_23609(new class_4668.class_4677(width == 0.0 ? OptionalDouble.empty() : OptionalDouble.of(width))).method_23617(false)));

    public static class_1921 getPositionColorQuads(boolean throughWalls) {
        if (throughWalls) {
            return POS_COL_QUADS_NO_DEPTH_TEST;
        }
        return POS_COL_QUADS_WITH_DEPTH_TEST;
    }

    public static class_1921 getLines(float width, boolean throughWalls) {
        if (throughWalls) {
            return LINES_NO_DEPTH_TEST.apply(Double.valueOf(width));
        }
        return LINES.apply(Double.valueOf(width));
    }
}

