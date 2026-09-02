/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_357
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 */
package restudio.reglass.mixin.accessor;

import net.minecraft.class_357;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value={class_357.class})
public interface SliderWidgetAccessor {
    @Accessor(value="value")
    public double getValue();

    @Accessor(value="value")
    public void setValuePublic(double var1);
}

