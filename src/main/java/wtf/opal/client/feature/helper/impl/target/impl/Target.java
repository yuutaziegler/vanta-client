/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 */
package wtf.opal.client.feature.helper.impl.target.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1309;

@Environment(value=EnvType.CLIENT)
public abstract class Target<T extends class_1309> {
    protected T entity;
    private boolean invalid;

    public Target(T entity) {
        this.entity = entity;
    }

    public void tick() {
    }

    public abstract boolean isMatchingFlags(int var1);

    public T getEntity() {
        return this.entity;
    }

    public void setEntity(class_1297 entity) {
        this.entity = (class_1309)entity;
    }

    public int getEntityId() {
        return this.entity.method_5628();
    }

    public boolean isLocal() {
        return false;
    }

    public boolean isInvalid() {
        return this.invalid;
    }

    public void setInvalid() {
        this.invalid = true;
    }
}

