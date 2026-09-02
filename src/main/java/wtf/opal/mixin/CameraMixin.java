/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_4050
 *  net.minecraft.class_4184
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_4050;
import net.minecraft.class_4184;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_4184.class})
public abstract class CameraMixin {
    @Shadow
    private float field_18721;
    @Shadow
    private float field_18722;
    @Unique
    private class_4050 prevPose;

    private CameraMixin() {
    }

    @Redirect(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;getYaw(F)F"))
    private float redirectYaw(class_1297 instance, float tickDelta) {
        return RotationHelper.getClientHandler().getYawOr(instance.method_5705(tickDelta));
    }

    @Redirect(method={"update"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/Entity;getPitch(F)F"))
    private float redirectPitch(class_1297 instance, float tickDelta) {
        return RotationHelper.getClientHandler().getPitchOr(instance.method_5695(tickDelta));
    }

    @Shadow
    public abstract class_1297 method_19331();

    @Inject(method={"updateEyeHeight"}, at={@At(value="FIELD", target="Lnet/minecraft/client/render/Camera;lastCameraY:F", opcode=181)}, cancellable=true)
    private void modifyCameraY(CallbackInfo ci) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        class_1297 focusedEntity = this.method_19331();
        class_4050 pose = focusedEntity.method_18376();
        if (animationsModule.isOldSneaking() && this.prevPose != null && (pose == class_4050.field_18081 || pose == class_4050.field_18076 && this.prevPose == class_4050.field_18081)) {
            this.field_18721 = focusedEntity.method_5751();
            if (pose == class_4050.field_18081) {
                this.field_18721 += 0.27f;
            }
            this.field_18722 = this.field_18721;
            ci.cancel();
        }
        this.prevPose = pose;
    }
}

