/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.player.rotation;

import java.util.Arrays;
import java.util.stream.Stream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.player.rotation.model.EnumRotationModel;
import wtf.opal.client.feature.helper.impl.player.rotation.model.IRotationModel;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;

@Environment(value=EnvType.CLIENT)
public final class RotationProperty {
    private final ModeProperty<EnumRotationModel> modelProperty;
    private final NumberProperty maxAngle;
    private final NumberProperty driftIntensity;
    private final NumberProperty jitterIntensity;
    private final GroupProperty groupProperty;

    public RotationProperty(IRotationModel defaultModel, Property<?> ... customProperties) {
        this.modelProperty = new ModeProperty<EnumRotationModel>("Model", defaultModel.getEnum());
        this.maxAngle = (NumberProperty)new NumberProperty("Max angle", 90.0, 5.0, 360.0, 5.0).hideIf(() -> this.modelProperty.getValue() == EnumRotationModel.INSTANT);
        this.driftIntensity = (NumberProperty)new NumberProperty("Drift intensity", 1.2, 0.5, 2.0, 0.1).hideIf(() -> this.modelProperty.getValue() != EnumRotationModel.ORGANIC);
        this.jitterIntensity = (NumberProperty)new NumberProperty("Jitter intensity", 0.12, 0.0, 0.3, 0.01).hideIf(() -> this.modelProperty.getValue() != EnumRotationModel.ORGANIC);
        Property[] properties = (Property[])Stream.concat(Stream.of(this.modelProperty, this.maxAngle, this.driftIntensity, this.jitterIntensity), Arrays.stream(customProperties)).toArray(Property[]::new);
        this.groupProperty = new GroupProperty("Rotation", properties);
    }

    public GroupProperty get() {
        return this.groupProperty;
    }

    public int getMaxAngle() {
        return ((Double)this.maxAngle.getValue()).intValue();
    }

    public double getDriftIntensity() {
        return (Double)this.driftIntensity.getValue();
    }

    public double getJitterIntensity() {
        return (Double)this.jitterIntensity.getValue();
    }

    public IRotationModel createModel() {
        return ((EnumRotationModel)((Object)this.modelProperty.getValue())).supply(this);
    }
}

