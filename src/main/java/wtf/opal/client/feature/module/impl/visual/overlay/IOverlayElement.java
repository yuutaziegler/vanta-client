/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.feature.module.impl.visual.overlay;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;

@Environment(value=EnvType.CLIENT)
public interface IOverlayElement {
    public void render(class_332 var1, float var2, boolean var3);

    default public void renderBlur(class_332 context, float delta) {
    }

    default public void onResize() {
    }

    default public void tick() {
    }

    default public void onDisable() {
    }

    default public boolean isActive() {
        return true;
    }

    default public ScreenPositionProperty getPositionProperty() {
        return null;
    }

    public boolean isBloom();
}

