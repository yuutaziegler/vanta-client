/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;

@Environment(value=EnvType.CLIENT)
public final class NoHurtCameraModule
extends Module {
    private final BooleanProperty hideModelDamage = new BooleanProperty("No player model hurt", false);

    public NoHurtCameraModule() {
        super("No Hurt Camera", "Disables the camera tilt when damaged.", ModuleCategory.VISUAL);
        this.addProperties(this.hideModelDamage);
    }

    public boolean isHideModelDamage() {
        return this.hideModelDamage.getValue();
    }
}

