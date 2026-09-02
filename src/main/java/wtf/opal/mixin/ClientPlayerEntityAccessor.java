/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_746
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Mutable
 *  org.spongepowered.asm.mixin.gen.Accessor
 *  org.spongepowered.asm.mixin.gen.Invoker
 */
package wtf.opal.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_746;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_746.class})
public interface ClientPlayerEntityAccessor {
    @Accessor
    public void setInSneakingPose(boolean var1);

    @Accessor
    public double getLastXClient();

    @Accessor
    public double getLastYClient();

    @Accessor
    public double getLastZClient();

    @Mutable
    @Accessor
    public void setLastXClient(double var1);

    @Mutable
    @Accessor
    public void setLastYClient(double var1);

    @Mutable
    @Accessor
    public void setLastZClient(double var1);

    @Mutable
    @Accessor
    public void setLastYawClient(float var1);

    @Mutable
    @Accessor
    public void setLastPitchClient(float var1);

    @Mutable
    @Accessor
    public void setLastOnGround(boolean var1);

    @Accessor
    public float getLastYawClient();

    @Accessor
    public float getLastPitchClient();

    @Accessor
    public boolean isLastOnGround();

    @Accessor
    public boolean getLastSprinting();

    @Accessor
    public int getTicksSinceLastPositionPacketSent();

    @Mutable
    @Accessor(value="lastSprinting")
    public void setLastSprinting(boolean var1);

    @Mutable
    @Accessor
    public void setTicksSinceLastPositionPacketSent(int var1);

    @Invoker
    public void callSendMovementPackets();

    @Invoker
    public boolean callCanStartSprinting();
}

