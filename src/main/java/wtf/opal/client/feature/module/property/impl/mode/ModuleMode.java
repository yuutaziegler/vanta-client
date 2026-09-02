/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.property.impl.mode;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.subscriber.IEventSubscriber;

@Environment(value=EnvType.CLIENT)
public abstract class ModuleMode<T extends Module>
implements IEventSubscriber {
    protected T module;
    private boolean enabled;

    protected ModuleMode(T module) {
        this.module = module;
        EventDispatcher.subscribe(this);
    }

    public T getModule() {
        return this.module;
    }

    public void onEnable() {
        if (((Module)this.module).getActiveMode() == this) {
            this.enabled = true;
        }
    }

    public void onDisable() {
        if (((Module)this.module).getActiveMode() == this) {
            this.enabled = false;
        }
    }

    @Override
    public boolean isHandlingEvents() {
        return this.enabled;
    }

    public abstract Enum<?> getEnumValue();
}

