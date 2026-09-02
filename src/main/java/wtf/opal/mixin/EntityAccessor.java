/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_2940
 *  net.minecraft.class_4050
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2940;
import net.minecraft.class_4050;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_1297.class})
public interface EntityAccessor {
    @Accessor(value="POSE")
    public static class_2940<class_4050> getTrackedPose() {
        throw new AssertionError();
    }

    @Accessor(value="FLAGS")
    public static class_2940<Byte> getFlags() {
        throw new AssertionError();
    }

    @Accessor
    public void setPos(class_243 var1);

    @Invoker
    public class_238 callCalculateDefaultBoundingBox(class_243 var1);
}

