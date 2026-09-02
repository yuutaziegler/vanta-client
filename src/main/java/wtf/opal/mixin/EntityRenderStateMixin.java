/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10017
 *  net.minecraft.class_1297
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10017;
import net.minecraft.class_1297;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import wtf.opal.duck.EntityRenderStateAccess;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_10017.class})
public final class EntityRenderStateMixin
implements EntityRenderStateAccess {
    @Unique
    private class_1297 entity;

    private EntityRenderStateMixin() {
    }

    @Override
    public class_1297 opal$getEntity() {
        return this.entity;
    }

    @Override
    public void opal$setEntity(class_1297 entity) {
        this.entity = entity;
    }
}

