/*
 * Lighting Stability Mixin - Fixes lighting flickering when rotating camera
 * 
 * The issue is that during camera rotation, the lightmap updates too frequently
 * causing a flickering effect. This mixin stabilizes the lightmap updates.
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_765;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value=EnvType.CLIENT)
@Mixin(value = class_4184.class)
public abstract class LightingStabilityMixin {
    
    @Shadow
    private float field_18721;
    
    @Shadow
    private float field_18722;
    
    @Shadow
    protected abstract class_1297 method_19331();
    
    // Track smooth values for eye height
    private float targetEyeHeight = 0.0f;
    private float currentEyeHeight = 0.0f;
    private float eyeHeightLerpSpeed = 0.3f;
    
    /**
     * Smooth out eye height transitions during camera movement
     * This helps prevent the lighting "pop" that occurs when the camera
     * moves vertically during rotation
     */
    @Inject(method = "updateEyeHeight", at = @At("RETURN"))
    private void smoothEyeHeightTransition(CallbackInfo ci) {
        // If we're in a world and have a player, smoothly interpolate eye height
        if (method_19331() instanceof class_1657 player) {
            targetEyeHeight = player.method_5751();
            
            // Smoothly interpolate towards target
            if (Math.abs(currentEyeHeight - targetEyeHeight) > 0.01f) {
                currentEyeHeight += (targetEyeHeight - currentEyeHeight) * eyeHeightLerpSpeed;
                
                // Apply the smoothed value
                if (field_18721 == field_18722) {
                    field_18721 = currentEyeHeight;
                    field_18722 = currentEyeHeight;
                }
            }
        }
    }
}
