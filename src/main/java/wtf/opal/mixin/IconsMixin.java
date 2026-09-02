/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.loader.impl.launch.knot.Knot
 *  net.minecraft.class_3262
 *  net.minecraft.class_7367
 *  net.minecraft.class_8518
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import java.io.InputStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.impl.launch.knot.Knot;
import net.minecraft.class_3262;
import net.minecraft.class_7367;
import net.minecraft.class_8518;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_8518.class})
public final class IconsMixin {
    private IconsMixin() {
    }

    @Inject(at={@At(value="HEAD")}, method={"getIcon"}, cancellable=true)
    private void getIcon(class_3262 resourcePack, String fileName, CallbackInfoReturnable<class_7367<InputStream>> info) {
        info.setReturnValue(() -> Knot.getLauncher().getTargetClassLoader().getResourceAsStream("assets/terentx/window-icons/" + fileName));
    }
}

