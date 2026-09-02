/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 *  net.minecraft.class_1887
 *  net.minecraft.class_1893
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_5321
 *  net.minecraft.class_7833
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionfc
 *  org.joml.Vector3fc
 *  org.joml.Vector4d
 *  org.joml.Vector4f
 */
package wtf.opal.utility.render;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_1887;
import net.minecraft.class_1893;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_5321;
import net.minecraft.class_7833;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.joml.Vector3fc;
import org.joml.Vector4d;
import org.joml.Vector4f;
import wtf.opal.client.Constants;
import wtf.opal.mixin.GameRendererAccessor;
import wtf.opal.utility.misc.math.MathUtility;

@Environment(value=EnvType.CLIENT)
public class ESPUtility {
    public static final Map<class_5321<class_1887>, String> ENCHANTMENT_NAMES = new HashMap<class_5321<class_1887>, String>(){
        {
            this.put(class_1893.field_9111, "Pr");
            this.put(class_1893.field_9095, "Fp");
            this.put(class_1893.field_9129, "Ff");
            this.put(class_1893.field_9107, "Bp");
            this.put(class_1893.field_9096, "Pp");
            this.put(class_1893.field_9127, "Re");
            this.put(class_1893.field_9105, "Aa");
            this.put(class_1893.field_9097, "Th");
            this.put(class_1893.field_9128, "Ds");
            this.put(class_1893.field_9122, "Fw");
            this.put(class_1893.field_9113, "Bc");
            this.put(class_1893.field_23071, "Ss");
            this.put(class_1893.field_38223, "Sn");
            this.put(class_1893.field_9118, "Sh");
            this.put(class_1893.field_9123, "Sm");
            this.put(class_1893.field_9112, "BoA");
            this.put(class_1893.field_9121, "Kb");
            this.put(class_1893.field_9124, "Fa");
            this.put(class_1893.field_9110, "Lo");
            this.put(class_1893.field_9115, "Sw");
            this.put(class_1893.field_9131, "Ef");
            this.put(class_1893.field_9099, "St");
            this.put(class_1893.field_9119, "Un");
            this.put(class_1893.field_9130, "Fo");
            this.put(class_1893.field_9103, "Po");
            this.put(class_1893.field_9116, "Pu");
            this.put(class_1893.field_9126, "Fl");
            this.put(class_1893.field_9125, "In");
            this.put(class_1893.field_9114, "Lu");
            this.put(class_1893.field_9100, "Lr");
            this.put(class_1893.field_9120, "Ly");
            this.put(class_1893.field_9106, "Ip");
            this.put(class_1893.field_9104, "Ri");
            this.put(class_1893.field_9117, "Ch");
            this.put(class_1893.field_9108, "Mu");
            this.put(class_1893.field_9098, "Qc");
            this.put(class_1893.field_9132, "Pi");
            this.put(class_1893.field_9101, "Me");
            this.put(class_1893.field_9109, "Vc");
        }
    };

    private ESPUtility() {
    }

    public static Vector4d getEntityPositionsOn2D(class_1309 target, float tickDelta) {
        int[] viewport = new int[]{0, 0, Constants.mc.method_22683().method_4489(), Constants.mc.method_22683().method_4506()};
        class_4587 matrixStack = ESPUtility.createMatrixStack(tickDelta);
        Matrix4f projectionMatrix = matrixStack.method_23760().method_23761();
        class_243 position = MathUtility.interpolate(target, tickDelta);
        float width = target.method_17681() / 2.0f;
        float height = target.method_17682() + (target.method_18276() ? 0.1f : 0.2f);
        class_238 boundingBox = new class_238(position.field_1352 - (double)width, position.field_1351, position.field_1350 - (double)width, position.field_1352 + (double)width, position.field_1351 + (double)height, position.field_1350 + (double)width);
        Vector4d projection = ESPUtility.projectEntity(viewport, projectionMatrix, boundingBox);
        projection.div((double)Constants.mc.method_22683().method_4495());
        projection.z -= projection.x;
        projection.w -= projection.y;
        return projection;
    }

    public static Vector4d projectEntity(int[] viewport, Matrix4f matrix, class_238 boundingBox) {
        Vector4f windowCoords = new Vector4f();
        List<class_243> list = ESPUtility.getBoxBounds(boundingBox);
        Vector4d projected = null;
        for (class_243 pos : list) {
            matrix.project((Vector3fc)pos.method_46409(), viewport, windowCoords);
            windowCoords.y = (float)viewport[3] - windowCoords.y;
            if (windowCoords.w != 1.0f) break;
            if (projected == null) {
                projected = new Vector4d((double)windowCoords.x, (double)windowCoords.y, 0.0, 0.0);
                continue;
            }
            double windowX = windowCoords.x;
            double windowY = windowCoords.y;
            projected.x = Math.min(windowX, projected.x);
            projected.y = Math.min(windowY, projected.y);
            projected.z = Math.max(windowX, projected.z);
            projected.w = Math.max(windowY, projected.w);
        }
        return projected;
    }

    public static class_4587 createMatrixStack(float tickDelta) {
        GameRendererAccessor gameRendererAccessor = (GameRendererAccessor)Constants.mc.field_1773;
        class_4587 matrixStack = new class_4587();
        class_4184 camera = Constants.mc.field_1773.method_19418();
        float fov = gameRendererAccessor.callGetFov(camera, tickDelta, true);
        matrixStack.method_34425((Matrix4fc)Constants.mc.field_1773.method_22973(fov));
        gameRendererAccessor.callTiltViewWhenHurt(matrixStack, camera.method_55437());
        if (((Boolean)Constants.mc.field_1690.method_42448().method_41753()).booleanValue()) {
            gameRendererAccessor.callBobView(matrixStack, camera.method_55437());
        }
        matrixStack.method_22907((Quaternionfc)class_7833.field_40714.rotationDegrees(camera.method_19329()));
        matrixStack.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(camera.method_19330() + 180.0f));
        return matrixStack;
    }

    public static List<class_243> getBoxBounds(class_238 boundingBox) {
        return Arrays.asList(new class_243(boundingBox.field_1323, boundingBox.field_1322, boundingBox.field_1321), new class_243(boundingBox.field_1323, boundingBox.field_1325, boundingBox.field_1321), new class_243(boundingBox.field_1320, boundingBox.field_1322, boundingBox.field_1321), new class_243(boundingBox.field_1320, boundingBox.field_1325, boundingBox.field_1321), new class_243(boundingBox.field_1323, boundingBox.field_1322, boundingBox.field_1324), new class_243(boundingBox.field_1323, boundingBox.field_1325, boundingBox.field_1324), new class_243(boundingBox.field_1320, boundingBox.field_1322, boundingBox.field_1324), new class_243(boundingBox.field_1320, boundingBox.field_1325, boundingBox.field_1324));
    }
}

