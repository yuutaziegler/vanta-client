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
import wtf.opal.client.feature.helper.impl.render.ScaleProperty;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;

@Environment(value=EnvType.CLIENT)
public final class ClientElementSettings {
    private final ScaleProperty scale = ScaleProperty.newNVGElement();
    private final MultipleBooleanProperty options = new MultipleBooleanProperty("Options", new BooleanProperty("Status effects", true), new BooleanProperty("FPS", true), new BooleanProperty("BPS", true), new BooleanProperty("XYZ", false), new BooleanProperty("Ping", true), new BooleanProperty("CPS", true), new BooleanProperty("Clock", true), new BooleanProperty("Memory", false), new BooleanProperty("Combo", false), new BooleanProperty("Reach", false));
    private final BooleanProperty lowercase = new BooleanProperty("Lowercase", true);

    ClientElementSettings(OverlayModule module) {
        module.addProperties(new GroupProperty("Client elements", this.scale.get(), this.options, this.lowercase));
    }

    public float getScale() {
        return this.scale.getScale();
    }

    public MultipleBooleanProperty getOptions() {
        return this.options;
    }

    public boolean isLowercase() {
        return this.lowercase.getValue();
    }
}

