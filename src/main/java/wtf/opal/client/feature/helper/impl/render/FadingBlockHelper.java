/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2338
 *  net.minecraft.class_243
 *  net.minecraft.class_4597
 *  net.minecraft.class_4597$class_4598
 *  net.minecraft.class_9799
 */
package wtf.opal.client.feature.helper.impl.render;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_4597;
import net.minecraft.class_9799;
import wtf.opal.client.feature.helper.IHelper;
import wtf.opal.client.renderer.world.WorldRenderer;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.render.RenderWorldEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.time.Stopwatch;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.CustomRenderLayers;

@Environment(value=EnvType.CLIENT)
public final class FadingBlockHelper
implements IHelper {
    private final List<FadingBlock> fadingBlocks = new ArrayList<FadingBlock>();
    private static FadingBlockHelper instance;

    private FadingBlockHelper() {
    }

    @Subscribe
    public void onRenderWorld(RenderWorldEvent event) {
        this.fadingBlocks.removeIf(FadingBlock::isRemovable);
        class_4597.class_4598 vcp = class_4597.method_22991((class_9799)new class_9799(1024));
        WorldRenderer rc = new WorldRenderer((class_4597)vcp);
        for (FadingBlock fadingBlock : this.fadingBlocks) {
            float progress = (float)fadingBlock.getRemainingLifetime() / (float)fadingBlock.lifetime;
            float fillAlpha = (float)(fadingBlock.fillColor >> 24 & 0xFF) / 255.0f;
            float outlineAlpha = (float)(fadingBlock.outlineColor >> 24 & 0xFF) / 255.0f;
            class_243 startVec = new class_243((double)fadingBlock.blockPos.method_10263(), (double)fadingBlock.blockPos.method_10264(), (double)fadingBlock.blockPos.method_10260());
            class_243 dimensions = new class_243(1.0, 1.0, 1.0);
            rc.drawFilledCube(event.matrixStack(), CustomRenderLayers.getPositionColorQuads(true), startVec, dimensions, ColorUtility.applyOpacity(fadingBlock.fillColor, fillAlpha * progress));
        }
        vcp.method_22993();
    }

    public void addFadingBlock(FadingBlock fadingBlock) {
        this.fadingBlocks.add(fadingBlock);
    }

    public static FadingBlockHelper getInstance() {
        return instance;
    }

    public static void setInstance() {
        instance = new FadingBlockHelper();
        EventDispatcher.subscribe(instance);
    }

    @Environment(value=EnvType.CLIENT)
    public static class FadingBlock {
        private final Stopwatch stopwatch = new Stopwatch();
        private final class_2338 blockPos;
        private final int outlineColor;
        private final int fillColor;
        private final long lifetime;

        public FadingBlock(class_2338 blockPos, int outlineColor, int fillColor, long lifetime) {
            this.blockPos = blockPos;
            this.outlineColor = outlineColor;
            this.fillColor = fillColor;
            this.lifetime = lifetime;
            this.stopwatch.reset();
        }

        public long getRemainingLifetime() {
            return Math.max(0L, this.lifetime - this.stopwatch.getTime());
        }

        public boolean isRemovable() {
            return this.stopwatch.hasTimeElapsed(this.lifetime);
        }
    }
}

