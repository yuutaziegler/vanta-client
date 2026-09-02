/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.client.LogoSettings;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.ImageRepository;

@Environment(value=EnvType.CLIENT)
public final class LogoElement
implements IOverlayElement {
    private final LogoSettings settings;
    private NVGImageRenderer logoImage;

    public LogoElement(OverlayModule module) {
        this.settings = new LogoSettings(module);
    }

    @Override
    public ScreenPositionProperty getPositionProperty() {
        return this.settings.getScreenPosition();
    }

    @Override
    public boolean isActive() {
        return this.settings.isEnabled();
    }

    @Override
    public boolean isBloom() {
        return false;
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        if (this.logoImage == null) {
            this.logoImage = ImageRepository.getImage("logo.png");
            if (this.logoImage == null) {
                return;
            }
        }
        ScreenPositionProperty pos = this.settings.getScreenPosition();
        float width = 64.0f;
        float height = 64.0f;
        pos.setWidth(width);
        pos.setHeight(height);
        float x = pos.getScaledX();
        float y = pos.getScaledY();
        this.logoImage.drawImage(x, y, width, height);
    }
}

