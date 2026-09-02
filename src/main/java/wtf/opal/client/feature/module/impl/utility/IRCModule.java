/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.utility;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;

@Environment(value=EnvType.CLIENT)
public final class IRCModule
extends Module {
    public IRCModule() {
        super("IRC", "Lets you chat with other Opal users.", ModuleCategory.UTILITY);
        this.setEnabled(true);
    }
}

