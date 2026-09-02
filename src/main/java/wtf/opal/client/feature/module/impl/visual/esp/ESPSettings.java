/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual.esp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.target.TargetProperty;
import wtf.opal.client.feature.module.impl.visual.esp.ESPModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;

@Environment(value=EnvType.CLIENT)
public final class ESPSettings {
    private final TargetProperty targetProperty = new TargetProperty(true, true, true, false, false, true);
    private final BooleanProperty box = new BooleanProperty("Enabled", true);
    private final BooleanProperty boxStroke = (BooleanProperty)new BooleanProperty("Stroke", true).hideIf(() -> this.box.getValue() == false);
    private final BooleanProperty healthBar = new BooleanProperty("Enabled", true);
    private final BooleanProperty healthBarStroke = (BooleanProperty)new BooleanProperty("Stroke", true).hideIf(() -> this.healthBar.getValue() == false);
    private final BooleanProperty nameTags = new BooleanProperty("Enabled", true);
    private final MultipleBooleanProperty nameTagElements = (MultipleBooleanProperty)new MultipleBooleanProperty("Elements", new BooleanProperty("Name", true), new BooleanProperty("Health", true), new BooleanProperty("Distance", true), new BooleanProperty("Equipment", false)).hideIf(() -> this.nameTags.getValue() == false);
    private final MultipleBooleanProperty nameTagIndicators = (MultipleBooleanProperty)new MultipleBooleanProperty("Indicators", new BooleanProperty("Sneaking", true), new BooleanProperty("Strength", true), new BooleanProperty("Invisible", true), new BooleanProperty("Blocking", true)).hideIf(() -> this.nameTags.getValue() == false);
    private final BooleanProperty bloom = new BooleanProperty("Bloom", true);

    public ESPSettings(ESPModule module) {
        module.addProperties(new GroupProperty("Box", this.box, this.boxStroke), new GroupProperty("Health Bar", this.healthBar, this.healthBarStroke), new GroupProperty("Name Tags", this.nameTags, this.nameTagElements, this.nameTagIndicators), this.targetProperty.get(), this.bloom);
    }

    public TargetProperty getTargetProperty() {
        return this.targetProperty;
    }

    public boolean areNameTagsEnabled() {
        return this.nameTags.getValue();
    }

    public MultipleBooleanProperty getNameTagElements() {
        return this.nameTagElements;
    }

    public MultipleBooleanProperty getNameTagIndicators() {
        return this.nameTagIndicators;
    }

    public boolean getHealthBarStroke() {
        return this.healthBarStroke.getValue() != false && this.getHealthBar();
    }

    public boolean getHealthBar() {
        return this.healthBar.getValue();
    }

    public boolean getBoxStroke() {
        return this.boxStroke.getValue() != false && this.getBox();
    }

    public boolean getBox() {
        return this.box.getValue();
    }

    public boolean getBloom() {
        return this.bloom.getValue();
    }
}

