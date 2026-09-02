/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.lwjgl.nanovg.NVGPaint
 *  org.lwjgl.nanovg.NanoVG
 */
package wtf.opal.client.renderer.image;

import java.io.InputStream;
import java.nio.ByteBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import wtf.opal.client.Constants;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.utility.misc.system.IOUtility;

@Environment(value=EnvType.CLIENT)
public final class NVGImageRenderer {
    private final ByteBuffer imageData;
    private final int imageHandle;

    public NVGImageRenderer(InputStream inputStream, int flags) {
        this.imageData = IOUtility.ioResourceToByteBuffer(inputStream, 524288);
        this.imageHandle = NanoVG.nvgCreateImageMem((long)Constants.VG, (int)flags, (ByteBuffer)this.imageData);
    }

    public NVGImageRenderer(InputStream inputStream) {
        this(inputStream, 0);
    }

    public void drawImage(float x, float y, float width, float height) {
        NanoVG.nvgImagePattern((long)Constants.VG, (float)x, (float)y, (float)width, (float)height, (float)0.0f, (int)this.imageHandle, (float)1.0f, (NVGPaint)NVGRenderer.NVG_PAINT);
        NanoVG.nvgBeginPath((long)Constants.VG);
        NanoVG.nvgRect((long)Constants.VG, (float)x, (float)y, (float)width, (float)height);
        NanoVG.nvgImagePattern((long)Constants.VG, (float)x, (float)y, (float)width, (float)height, (float)0.0f, (int)this.imageHandle, (float)1.0f, (NVGPaint)NVGRenderer.NVG_PAINT);
        NanoVG.nvgFillPaint((long)Constants.VG, (NVGPaint)NVGRenderer.NVG_PAINT);
        NanoVG.nvgFill((long)Constants.VG);
        NanoVG.nvgClosePath((long)Constants.VG);
    }

    public void drawImage(float x, float y, float width, float height, int colorOverlay) {
        NanoVG.nvgImagePattern((long)Constants.VG, (float)x, (float)y, (float)width, (float)height, (float)0.0f, (int)this.imageHandle, (float)1.0f, (NVGPaint)NVGRenderer.NVG_PAINT);
        NanoVG.nvgBeginPath((long)Constants.VG);
        NanoVG.nvgRect((long)Constants.VG, (float)x, (float)y, (float)width, (float)height);
        NanoVG.nvgImagePattern((long)Constants.VG, (float)x, (float)y, (float)width, (float)height, (float)0.0f, (int)this.imageHandle, (float)1.0f, (NVGPaint)NVGRenderer.NVG_PAINT);
        NVGRenderer.applyColor(colorOverlay, NVGRenderer.NVG_COLOR_1);
        NVGRenderer.NVG_PAINT.innerColor(NVGRenderer.NVG_COLOR_1);
        NanoVG.nvgFillPaint((long)Constants.VG, (NVGPaint)NVGRenderer.NVG_PAINT);
        NanoVG.nvgFill((long)Constants.VG);
        NanoVG.nvgClosePath((long)Constants.VG);
    }
}

