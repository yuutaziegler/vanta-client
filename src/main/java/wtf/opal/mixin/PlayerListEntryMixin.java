/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_12079$class_12081
 *  net.minecraft.class_2960
 *  net.minecraft.class_640
 *  net.minecraft.class_8685
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_12079;
import net.minecraft.class_2960;
import net.minecraft.class_640;
import net.minecraft.class_8685;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.CapeModule;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_640.class})
public final class PlayerListEntryMixin {
    @Unique
    private static class_2960 ELYTRA_TEXTURE;
    @Final
    @Shadow
    private GameProfile field_3741;

    @Inject(method={"getSkinTextures"}, at={@At(value="TAIL")}, cancellable=true)
    private void hookSkinTextures(CallbackInfoReturnable<class_8685> cir) {
        CapeModule.CapeType capeType;
        CapeModule capeModule = OpalClient.getInstance().getModuleRepository().getModule(CapeModule.class);
        CapeModule.CapeType capeType2 = capeType = capeModule.isEnabled() ? capeModule.getType() : null;
        if (capeType == null) {
            return;
        }
        class_8685 oldTextures = (class_8685)cir.getReturnValue();
        if (oldTextures != null) {
            cir.setReturnValue((Object)new class_8685(oldTextures.comp_1626(), new class_12079.class_12081(){

                public class_2960 comp_3627() {
                    return capeType.getIdentifier();
                }

                public class_2960 comp_3626() {
                    return capeType.getIdentifier();
                }
            }, oldTextures.comp_1628(), oldTextures.comp_1629(), oldTextures.comp_1630()));
        }
    }
}

