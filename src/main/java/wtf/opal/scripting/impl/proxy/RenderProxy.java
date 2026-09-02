/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.scripting.impl.proxy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.renderer.NVGRenderer;

@Environment(value=EnvType.CLIENT)
public class RenderProxy {
    public void rect(float x, float y, float width, float height, int color) {
        NVGRenderer.rect(x, y, width, height, color);
    }

    public void roundedRect(float x, float y, float width, float height, float radius, int color) {
        NVGRenderer.roundedRect(x, y, width, height, radius, color);
    }
}

