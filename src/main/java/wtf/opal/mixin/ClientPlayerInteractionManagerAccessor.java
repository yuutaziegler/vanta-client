/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_636
 *  net.minecraft.class_638
 *  net.minecraft.class_7204
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_636;
import net.minecraft.class_638;
import net.minecraft.class_7204;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_636.class})
public interface ClientPlayerInteractionManagerAccessor {
    @Invoker
    public void callSendSequencedPacket(class_638 var1, class_7204 var2);

    @Invoker
    public void callSyncSelectedSlot();

    @Accessor
    public void setBlockBreakingCooldown(int var1);
}

