/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 *  net.minecraft.class_4587
 *  net.minecraft.class_4604
 *  net.minecraft.class_5481
 *  org.joml.Matrix4f
 *  org.joml.Vector3f
 *  org.joml.Vector3fc
 *  org.joml.Vector4f
 */
package wtf.opal.client.feature.module.impl.visual;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_4604;
import net.minecraft.class_5481;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector3fc;
import org.joml.Vector4f;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.render.FrustumHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.renderer.MinecraftRenderer;
import wtf.opal.duck.ClientPlayerInteractionManagerAccess;
import wtf.opal.event.impl.render.RenderScreenEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.PlayerUtility;
import wtf.opal.utility.render.ESPUtility;

@Environment(value=EnvType.CLIENT)
public final class BreakProgressModule
extends Module {
    public BreakProgressModule() {
        super("Break Progress", "Shows the progress of the block you're breaking.", ModuleCategory.VISUAL);
    }

    @Subscribe
    public void onRenderScreen(RenderScreenEvent event) {
        if (Constants.mc.field_1761 == null || !Constants.mc.field_1761.method_2923()) {
            return;
        }
        ClientPlayerInteractionManagerAccess access = (ClientPlayerInteractionManagerAccess)Constants.mc.field_1761;
        String breakProgress = (int)Math.ceil(access.opal$currentBreakingProgress() * 100.0f) + "%";
        class_2338 blockPos = access.opal$getCurrentBreakingPos();
        float tickDelta = event.tickDelta();
        class_4604 frustum = FrustumHelper.get();
        if (frustum == null) {
            return;
        }
        class_238 blockBox = PlayerUtility.getBlockBox(blockPos);
        if (!frustum.method_23093(blockBox)) {
            return;
        }
        class_243 cameraPos = Constants.mc.field_1773.method_19418().method_19326();
        float relX = (float)((double)blockPos.method_10263() - cameraPos.field_1352 + 0.5);
        float relY = (float)((double)blockPos.method_10264() - cameraPos.field_1351 + 0.5);
        float relZ = (float)((double)blockPos.method_10260() - cameraPos.field_1350 + 0.5);
        Vector3f relativePoint = new Vector3f(relX, relY, relZ);
        int[] viewport = new int[]{0, 0, Constants.mc.method_22683().method_4489(), Constants.mc.method_22683().method_4506()};
        class_4587 projectionStack = ESPUtility.createMatrixStack(tickDelta);
        Matrix4f projectionMatrix = projectionStack.method_23760().method_23761();
        Vector4f windowCoords = new Vector4f();
        projectionMatrix.project((Vector3fc)relativePoint, viewport, windowCoords);
        windowCoords.y = (float)viewport[3] - windowCoords.y;
        float scaleFactor = Constants.mc.method_22683().method_4495();
        windowCoords.x /= scaleFactor;
        windowCoords.y /= scaleFactor;
        MinecraftRenderer.addToQueue(() -> {
            class_332 class_3322 = event.drawContext();
            class_327 class_3272 = Constants.mc.field_1772;
            class_5481 class_54812 = class_2561.method_43470((String)breakProgress).method_30937();
            int n = (int)windowCoords.x - Constants.mc.field_1772.method_1727(breakProgress) / 2;
            int n2 = (int)windowCoords.y;
            Objects.requireNonNull(Constants.mc.field_1772);
            class_3322.method_51430(class_3272, class_54812, n, n2 - 9 / 2, -1, true);
        });
    }
}

