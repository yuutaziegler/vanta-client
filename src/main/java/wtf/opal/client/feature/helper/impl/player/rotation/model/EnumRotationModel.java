/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.player.rotation.model;

import java.util.function.Function;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationProperty;
import wtf.opal.client.feature.helper.impl.player.rotation.model.IRotationModel;
import wtf.opal.client.feature.helper.impl.player.rotation.model.impl.InstantRotationModel;
import wtf.opal.client.feature.helper.impl.player.rotation.model.impl.LinearRotationModel;
import wtf.opal.client.feature.helper.impl.player.rotation.model.impl.OrganicRotationModel;

@Environment(value=EnvType.CLIENT)
public enum EnumRotationModel {
    INSTANT("Instant", r -> InstantRotationModel.INSTANCE),
    LINEAR("Linear", r -> new LinearRotationModel(r.getMaxAngle())),
    ORGANIC("Organic", r -> new OrganicRotationModel(r.getMaxAngle(), r.getDriftIntensity(), r.getJitterIntensity()));

    private final String name;
    private final Function<RotationProperty, IRotationModel> modelSupplier;

    private EnumRotationModel(String name, Function<RotationProperty, IRotationModel> modelSupplier) {
        this.name = name;
        this.modelSupplier = modelSupplier;
    }

    public String toString() {
        return this.name;
    }

    public IRotationModel supply(RotationProperty property) {
        return this.modelSupplier.apply(property);
    }
}

