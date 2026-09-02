/*
 * Decompiled with CFR 0.152.
 *
 * Accessor for HandledScreen (class_465) geometry so the liquid-glass frame
 * can be drawn around the slot panel of chests/inventory/etc.
 *
 * Intermediary names (stable across versions, verified against Yarn):
 *   x               -> field_2776
 *   y               -> field_2800
 *   backgroundWidth -> field_2792
 *   backgroundHeight-> field_2779
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_465;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(value = EnvType.CLIENT)
@Mixin(value = {class_465.class})
public interface HandledScreenAccessor {
    @Accessor("field_2776")
    int opal$getX();

    @Accessor("field_2800")
    int opal$getY();

    @Accessor("field_2792")
    int opal$getBackgroundWidth();

    @Accessor("field_2779")
    int opal$getBackgroundHeight();
}
