/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_765
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.FullbrightModule;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_765.class})
public final class LightmapTextureManagerMixin {
    private LightmapTextureManagerMixin() {
    }

    @Redirect(method={"update"}, at=@At(value="INVOKE", target="Ljava/lang/Double;floatValue()F", ordinal=1))
    public float redirectGammaValue(Double d) {
        float factor = OpalClient.getInstance().getModuleRepository().getModule(FullbrightModule.class).isEnabled() ? 15.0f : 1.0f;
        return d.floatValue() * factor;
    }
}

