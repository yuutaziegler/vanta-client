/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10034
 *  net.minecraft.class_1309
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10034;
import net.minecraft.class_1309;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import wtf.opal.duck.BipedEntityRenderStateAccess;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_10034.class})
public final class BipedEntityRenderStateMixin
implements BipedEntityRenderStateAccess {
    @Unique
    public class_1309 entity;

    @Override
    public class_1309 opal$getEntity() {
        return this.entity;
    }

    @Override
    public void opal$setEntity(class_1309 entity) {
        this.entity = entity;
    }
}

