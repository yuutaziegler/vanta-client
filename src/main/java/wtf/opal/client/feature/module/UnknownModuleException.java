/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class UnknownModuleException
extends Exception {
    private final String id;

    public UnknownModuleException(String id) {
        super(String.format("Module with the id %s could not be found.", id));
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}

