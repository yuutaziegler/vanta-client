/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11228
 *  net.minecraft.class_4184
 *  net.minecraft.class_4587
 *  net.minecraft.class_757
 *  net.minecraft.class_758
 *  net.minecraft.class_9920
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11228;
import net.minecraft.class_4184;
import net.minecraft.class_4587;
import net.minecraft.class_757;
import net.minecraft.class_758;
import net.minecraft.class_9920;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_757.class})
public interface GameRendererAccessor {
    @Accessor
    public class_9920 getPool();

    @Invoker
    public float callGetFov(class_4184 var1, float var2, boolean var3);

    @Invoker
    public void callTiltViewWhenHurt(class_4587 var1, float var2);

    @Invoker
    public void callBobView(class_4587 var1, float var2);

    @Accessor
    public class_11228 getGuiRenderer();

    @Accessor
    public class_758 getFogRenderer();
}

