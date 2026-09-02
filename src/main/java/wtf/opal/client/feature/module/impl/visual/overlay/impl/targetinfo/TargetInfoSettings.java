/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.targetinfo;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.render.ScaleProperty;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;

@Environment(value=EnvType.CLIENT)
public final class TargetInfoSettings {
    private final BooleanProperty enabled = new BooleanProperty("Enabled", true);
    private final ScreenPositionProperty screenPosition = new ScreenPositionProperty("Screen Position", 0.43f, 0.65f);
    private final ScaleProperty scale = ScaleProperty.newNVGElement();

    TargetInfoSettings(OverlayModule module) {
        module.addProperties(new GroupProperty("Target information", this.enabled, this.screenPosition, this.scale.get()));
    }

    public boolean isEnabled() {
        return this.enabled.getValue();
    }

    public ScreenPositionProperty getScreenPosition() {
        return this.screenPosition;
    }

    public float getScale() {
        return this.scale.getScale();
    }
}

