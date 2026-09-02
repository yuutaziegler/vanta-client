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

    /*
     * Lighting flicker fix:
     * The lightmap update interpolates between the previous and the target gamma.
     * Redirecting only one of the gamma reads (the old "ordinal=1" behaviour) made
     * the interpolation oscillate between the raw and the boosted value, which the
     * player saw as light "rubbing"/flickering every time the lightmap refreshed
     * (most noticeable when turning the camera left/right).
     * Redirecting every gamma read with the same factor keeps the curve consistent
     * and completely removes the flicker.
     */
    @Redirect(method={"update"}, at=@At(value="INVOKE", target="Ljava/lang/Double;floatValue()F"))
    public float redirectGammaValue(Double d) {
        float factor = OpalClient.getInstance().getModuleRepository().getModule(FullbrightModule.class).isEnabled() ? 15.0f : 1.0f;
        return d.floatValue() * factor;
    }
}

