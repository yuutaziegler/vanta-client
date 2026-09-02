/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.combat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;

@Environment(value=EnvType.CLIENT)
public final class PiercingModule
extends Module {
    public PiercingModule() {
        super("Piercing", "Allows you to take players through blocks.", ModuleCategory.COMBAT);
    }
}

