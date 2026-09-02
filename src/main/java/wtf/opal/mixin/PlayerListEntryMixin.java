/*
 * Overrides the local player's skin textures so the Custom Cape / Cape modules
 * actually show up on the player model (cape + elytra + optionally skin).
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
import wtf.opal.client.feature.helper.impl.skin.SkinFetcher;
import wtf.opal.client.feature.module.impl.visual.CapeModule;
import wtf.opal.client.feature.module.impl.visual.CustomCapeModule;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_640.class})
public final class PlayerListEntryMixin {
    @Final
    @Shadow
    private GameProfile field_3741;

    @Inject(method={"getSkinTextures"}, at={@At(value="RETURN")}, cancellable=true)
    private void hookSkinTextures(CallbackInfoReturnable<class_8685> cir) {
        // Texture registrations fetched by name happen async; drain them here
        // because this method always runs on the render thread.
        SkinFetcher.drainPending();

        if (this.field_3741 == null) {
            return;
        }
        String ownName = this.field_3741.getName();
        if (ownName == null) {
            return;
        }
        // Only override textures for the local player entry.
        if (!ownName.equalsIgnoreCase(terminalName())) {
            return;
        }

        class_2960 capeOverride = null;
        class_2960 skinOverride = null;

        CustomCapeModule customCape = OpalClient.getInstance().getModuleRepository().getModule(CustomCapeModule.class);
        CapeModule capeModule = OpalClient.getInstance().getModuleRepository().getModule(CapeModule.class);

        if (customCape != null && customCape.isEnabled()) {
            capeOverride = customCape.getCapeOverride();
            skinOverride = customCape.getSkinOverride();
        }
        if (capeOverride == null && capeModule != null && capeModule.isEnabled()) {
            capeOverride = capeModule.getType().getIdentifier();
        }
        if (capeOverride == null && skinOverride == null) {
            return;
        }

        class_8685 old = (class_8685)cir.getReturnValue();
        if (old == null) {
            return;
        }
        class_12079.class_12081 body = skinOverride != null ? makeAsset(skinOverride) : old.comp_1626();
        class_12079.class_12081 cape = capeOverride != null ? makeAsset(capeOverride) : old.comp_1627();
        class_12079.class_12081 elytra = capeOverride != null ? makeAsset(capeOverride) : old.comp_1628();
        cir.setReturnValue(new class_8685(body, cape, elytra, old.comp_1629(), old.comp_1630()));
    }

    @Unique
    private String terminalName() {
        return wtf.opal.client.Constants.mc.field_1726 != null
            ? wtf.opal.client.Constants.mc.field_1726.method_1676()
            : "";
    }

    @Unique
    private static class_12079.class_12081 makeAsset(final class_2960 id) {
        return new class_12079.class_12081() {
            public class_2960 comp_3627() {
                return id;
            }

            public class_2960 comp_3626() {
                return id;
            }
        };
    }
}
