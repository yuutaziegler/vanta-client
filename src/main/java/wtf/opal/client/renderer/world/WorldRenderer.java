/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1921
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_4587$class_4665
 *  net.minecraft.class_4588
 *  net.minecraft.class_4597
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 */
package wtf.opal.client.renderer.world;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1921;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_4597;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import wtf.opal.client.Constants;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.Emitter;

@Environment(value=EnvType.CLIENT)
public record WorldRenderer(class_4184 camera, class_4597 vcp) {
    public WorldRenderer(class_4597 vcp) {
        this(Constants.mc.field_1773.method_19418(), vcp);
    }

    public void drawFilledQuad(class_4587 stack, class_1921 layer, class_243 position, class_241 dimensions, int color) {
        class_4588 buffer = this.vcp.method_73477(layer);
        class_4587.class_4665 transform = stack.method_23760();
        Vector3f transformedRoot = position.method_1020(this.camera.method_19326()).method_46409();
        float x = transformedRoot.x;
        float y = transformedRoot.y;
        float z = transformedRoot.z;
        int[] rgba = ColorUtility.hexToRGBA(color);
        float r = (float)rgba[0] / 255.0f;
        float g = (float)rgba[1] / 255.0f;
        float b = (float)rgba[2] / 255.0f;
        float a = (float)rgba[3] / 255.0f;
        Emitter._emit_quad__4xposition_color(transform, buffer, x + 0.0f, y + 0.0f, z + 0.0f, r, g, b, a, x + dimensions.field_1343, y + 0.0f, z + 0.0f, r, g, b, a, x + dimensions.field_1343, y + dimensions.field_1342, z + 0.0f, r, g, b, a, x + 0.0f, y + dimensions.field_1342, z + 0.0f, r, g, b, a);
    }

    public void drawFilledCube(class_4587 stack, class_1921 layer, class_243 position, class_243 dimensions, int color) {
        class_4588 buffer = this.vcp.method_73477(layer);
        class_4587.class_4665 transform = stack.method_23760();
        Vector3f transformedRoot = position.method_1020(this.camera.method_19326()).method_46409();
        float x = transformedRoot.x;
        float y = transformedRoot.y;
        float z = transformedRoot.z;
        int[] rgba = ColorUtility.hexToRGBA(color);
        float r = (float)rgba[0] / 255.0f;
        float g = (float)rgba[1] / 255.0f;
        float b = (float)rgba[2] / 255.0f;
        float a = (float)rgba[3] / 255.0f;
        Emitter._emit_cube__8xposition_color(transform, buffer, x + 0.0f, y + 0.0f, z + 0.0f, r, g, b, a, (float)((double)x + dimensions.method_10216()), y + 0.0f, z + 0.0f, r, g, b, a, (float)((double)x + dimensions.method_10216()), y + 0.0f, (float)((double)z + dimensions.method_10215()), r, g, b, a, x + 0.0f, y + 0.0f, (float)((double)z + dimensions.method_10215()), r, g, b, a, x + 0.0f, (float)((double)y + dimensions.method_10214()), z + 0.0f, r, g, b, a, (float)((double)x + dimensions.method_10216()), (float)((double)y + dimensions.method_10214()), z + 0.0f, r, g, b, a, (float)((double)x + dimensions.method_10216()), (float)((double)y + dimensions.method_10214()), (float)((double)z + dimensions.method_10215()), r, g, b, a, x + 0.0f, (float)((double)y + dimensions.method_10214()), (float)((double)z + dimensions.method_10215()), r, g, b, a);
    }

    public void drawLine(class_4587 stack, class_1921 layer, class_243 start, class_243 end, int color) {
        class_4588 buffer = this.vcp.method_73477(layer);
        class_4587.class_4665 transform = stack.method_23760();
        Vector3f tfStart = start.method_1020(this.camera.method_19326()).method_46409();
        Vector3f tfEnd = end.method_1020(this.camera.method_19326()).method_46409();
        Vector3f direction = tfEnd.sub((Vector3fc)tfStart, new Vector3f()).normalize();
        float x1 = tfStart.x;
        float y1 = tfStart.y;
        float z1 = tfStart.z;
        float x2 = tfEnd.x;
        float y2 = tfEnd.y;
        float z2 = tfEnd.z;
        int[] rgba = ColorUtility.hexToRGBA(color);
        float r = (float)rgba[0] / 255.0f;
        float g = (float)rgba[1] / 255.0f;
        float b = (float)rgba[2] / 255.0f;
        float a = (float)rgba[3] / 255.0f;
        Emitter._emit_line__2xposition_color_normal(transform, buffer, x1, y1, z1, r, g, b, a, direction.x, direction.y, direction.z, x2, y2, z2, r, g, b, a, direction.x, direction.y, direction.z);
    }
}

