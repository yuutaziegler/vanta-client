/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_310
 *  net.minecraft.class_437
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package restudio.reglass.mixin.client;

import net.minecraft.class_310;
import net.minecraft.class_437;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import restudio.reglass.client.api.ReGlassConfig;

@Mixin(value={class_310.class})
public abstract class MinecraftClientMixin {
    @Shadow
    @Nullable
    public class_437 field_1755;

    @Shadow
    public abstract void method_1507(@Nullable class_437 var1);

    @Inject(method={"setScreen"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSetScreen(class_437 screen, CallbackInfo ci) {
        if (ReGlassConfig.INSTANCE.features.enableRedesign) {
            // empty if block
        }
    }
}

