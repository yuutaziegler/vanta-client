/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11909
 *  net.minecraft.class_4069
 *  net.minecraft.class_408
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11909;
import net.minecraft.class_4069;
import net.minecraft.class_408;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.client.feature.helper.impl.render.ScreenPositionManager;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_408.class})
public abstract class ChatScreenMixin
implements class_4069 {
    @Inject(method={"mouseClicked"}, at={@At(value="HEAD")})
    private void hookMouseClicked(class_11909 click, boolean doubled, CallbackInfoReturnable<Boolean> cir) {
        ScreenPositionManager.getInstance().onMouseClick(click.comp_4798(), click.comp_4799(), click.method_74245());
    }

    public boolean method_25406(class_11909 click) {
        if (click.method_74245() == 0) {
            ScreenPositionManager.getInstance().releaseDraggedProperties();
        }
        return super.method_25406(click);
    }
}

