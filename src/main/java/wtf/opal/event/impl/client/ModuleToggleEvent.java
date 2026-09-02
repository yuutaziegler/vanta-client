/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.event.impl.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.event.EventCancellable;

@Environment(value=EnvType.CLIENT)
public final class ModuleToggleEvent
extends EventCancellable {
    private final Module module;
    private final boolean enabled;

    public ModuleToggleEvent(Module module, boolean enabled) {
        this.module = module;
        this.enabled = enabled;
    }

    public Module getModule() {
        return this.module;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}

