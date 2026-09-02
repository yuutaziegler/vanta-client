/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;

@Environment(value=EnvType.CLIENT)
public final class LogoSettings {
    private final ScreenPositionProperty screenPosition;
    private final BooleanProperty enabled = new BooleanProperty("Enabled", true);

    LogoSettings(OverlayModule module) {
        this.screenPosition = new ScreenPositionProperty("Screen Position", 0.05f, 0.2f);
        module.addProperties(new GroupProperty("TerentX Logo", this.enabled, this.screenPosition));
    }

    public boolean isEnabled() {
        return this.enabled.getValue();
    }

    public ScreenPositionProperty getScreenPosition() {
        return this.screenPosition;
    }
}

