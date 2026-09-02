/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.combat.velocity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.impl.combat.velocity.VelocityModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;

@Environment(value=EnvType.CLIENT)
public abstract class VelocityMode
extends ModuleMode<VelocityModule> {
    protected VelocityMode(VelocityModule module) {
        super(module);
    }

    public String getSuffix() {
        return this.getEnumValue().toString();
    }
}

