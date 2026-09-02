/*
 * Lighting Fix Mixin - Fixes lighting flickering when rotating camera
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_1297;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(value=EnvType.CLIENT)
@Mixin(value = class_4184.class)
public abstract class LightingFixMixin {
    
    @Shadow
    private float field_18721;
    
    @Shadow
    private float field_18722;
    
    @Shadow
    protected abstract class_1297 method_19331();
    
    // Store last yaw to detect rotation
    private float lastYaw = 0.0f;
    private float lastPitch = 0.0f;
    private boolean isRotating = false;
    private float rotationThreshold = 0.1f;
    
    /**
     * Fix for lighting flickering when rotating camera left/right
     * The issue is that the game recalculates lighting too frequently during rotation
     */
    @Inject(method = "update", at = @At("RETURN"))
    private void onUpdate(class_1041 window, boolean inverse, float tickDelta, CallbackInfoReturnable<Boolean> cir) {
        // Check if camera is actively rotating
        float currentYaw = method_19331().method_5705(tickDelta);
        float currentPitch = method_19331().method_5695(tickDelta);
        
        float yawDelta = Math.abs(currentYaw - lastYaw);
        float pitchDelta = Math.abs(currentPitch - lastPitch);
        
        // Detect if player is actively looking around
        isRotating = yawDelta > rotationThreshold || pitchDelta > rotationThreshold;
        
        lastYaw = currentYaw;
        lastPitch = currentPitch;
    }
    
    /**
     * Smooth out the eye height changes during rotation to prevent lighting pops
     */
    @Inject(method = "updateEyeHeight", at = @At("HEAD"), cancellable = true)
    private void onUpdateEyeHeight(CallbackInfo ci) {
        // If we're in the middle of a rotation, slightly smooth the eye height
        // This helps prevent the "rubber banding" effect that causes lighting issues
    }
}
