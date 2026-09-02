/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.modulelist;

import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.render.ScaleProperty;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;

@Environment(value=EnvType.CLIENT)
public final class ToggledSettings {
    private final ScaleProperty scale = ScaleProperty.newNVGElement();
    private final BooleanProperty enabled;
    private final BooleanProperty lowercase;
    private final BooleanProperty showSuffix;
    private final BooleanProperty offsetScoreboard;
    private final MultipleBooleanProperty visibleCategories;
    private final ModeProperty<BarMode> barMode = new ModeProperty<BarMode>("Bar mode", BarMode.LEFT);

    ToggledSettings(OverlayModule module) {
        this.enabled = new BooleanProperty("Enabled", false);
        this.lowercase = new BooleanProperty("Lowercase", false);
        this.showSuffix = new BooleanProperty("Show suffix", true);
        this.offsetScoreboard = new BooleanProperty("Offset scoreboard", false);
        this.visibleCategories = new MultipleBooleanProperty("Visible categories", (BooleanProperty[])Stream.of(ModuleCategory.VALUES).map(c -> new BooleanProperty(c.getName(), true)).toArray(BooleanProperty[]::new));
        module.addProperties(new GroupProperty("Toggled modules", this.scale.get(), this.barMode, this.enabled, this.lowercase, this.showSuffix, this.offsetScoreboard, this.visibleCategories));
    }

    public float getScale() {
        return this.scale.getScale();
    }

    public boolean isEnabled() {
        return this.enabled.getValue();
    }

    public boolean isLowercase() {
        return this.lowercase.getValue();
    }

    public boolean isShowSuffix() {
        return this.showSuffix.getValue();
    }

    public boolean isOffsetScoreboard() {
        return this.offsetScoreboard.getValue();
    }

    public MultipleBooleanProperty getVisibleCategories() {
        return this.visibleCategories;
    }

    public ModeProperty<BarMode> getBarMode() {
        return this.barMode;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum BarMode {
        LEFT("Left"),
        RIGHT("Right"),
        NONE("None");

        private final String name;

        private BarMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

