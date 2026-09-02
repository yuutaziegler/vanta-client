/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_5223
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.ModifyVariable
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_5223;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.StreamerModeModule;
import wtf.opal.client.feature.module.repository.ModuleRepository;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_5223.class})
public final class TextVisitFactoryMixin {
    @ModifyVariable(method={"visitFormatted(Ljava/lang/String;ILnet/minecraft/text/Style;Lnet/minecraft/text/Style;Lnet/minecraft/text/CharacterVisitor;)Z"}, at=@At(value="HEAD"), argsOnly=true, ordinal=0)
    private static String modifyVisitFormatted(String text) {
        OpalClient opal = OpalClient.getInstance();
        if (opal == null) {
            return text;
        }
        ModuleRepository moduleRepository = opal.getModuleRepository();
        if (moduleRepository == null) {
            return text;
        }
        StreamerModeModule streamerModeModule = moduleRepository.getModule(StreamerModeModule.class);
        if (streamerModeModule.isEnabled()) {
            return streamerModeModule.filter(text);
        }
        return text;
    }
}

