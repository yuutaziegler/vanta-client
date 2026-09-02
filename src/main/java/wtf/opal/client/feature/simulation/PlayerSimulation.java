/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1293
 *  net.minecraft.class_1297
 *  net.minecraft.class_1657
 *  net.minecraft.class_243
 *  net.minecraft.class_745
 */
package wtf.opal.client.feature.simulation;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1293;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_745;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.module.impl.movement.MovementFixModule;
import wtf.opal.mixin.LivingEntityAccessor;

@Environment(value=EnvType.CLIENT)
public final class PlayerSimulation {
    private class_745 simulatedEntity;
    private final class_1657 player;

    public PlayerSimulation(class_1657 player) {
        if (Constants.mc.field_1687 == null) {
            this.player = null;
            return;
        }
        GameProfile profile = new GameProfile(UUID.randomUUID(), "Simulated Player");
        this.simulatedEntity = new class_745(this, Constants.mc.field_1687, profile){

            public void method_5697(class_1297 entity) {
            }

            protected void method_6087(class_1297 entity) {
            }
        };
        this.player = player;
        this.cloneStates();
    }

    private void cloneStates() {
        this.simulatedEntity.field_5960 = this.player.field_5960;
        this.simulatedEntity.field_6014 = this.player.field_6014;
        this.simulatedEntity.field_6036 = this.player.field_6036;
        this.simulatedEntity.field_5969 = this.player.field_5969;
        this.simulatedEntity.field_5982 = this.player.field_5982;
        this.simulatedEntity.field_6004 = this.player.field_6004;
        this.simulatedEntity.method_33574(this.player.method_73189());
        this.simulatedEntity.method_5857(this.player.method_5829());
        this.simulatedEntity.method_18799(this.player.method_18798());
        float yaw = OpalClient.getInstance().getModuleRepository().getModule(MovementFixModule.class).isFixMovement() || this.player != Constants.mc.field_1724 ? this.player.method_36454() : RotationHelper.getClientHandler().getYawOr(this.player.method_36454());
        this.simulatedEntity.method_36456(yaw);
        this.simulatedEntity.method_36457(RotationHelper.getClientHandler().getPitchOr(this.player.method_36455()));
        this.simulatedEntity.method_5660(this.player.method_5715());
        this.simulatedEntity.method_24830(this.player.method_24828());
        this.simulatedEntity.method_5728(this.player.method_5624());
        for (class_1293 statusEffect : this.player.method_6026()) {
            this.simulatedEntity.method_6092(statusEffect);
        }
        this.simulatedEntity.method_6125(this.player.method_6029());
        this.simulatedEntity.field_6017 = this.player.field_6017;
    }

    public void simulateTicks(int tickCount) {
        LivingEntityAccessor accessor = (LivingEntityAccessor)this.simulatedEntity;
        for (int i = 0; i < tickCount; ++i) {
            accessor.callTravelMidAir(new class_243((double)this.player.field_6212, (double)this.player.field_6227, (double)this.player.field_6250));
        }
    }

    public void simulateTick() {
        this.simulateTicks(1);
    }

    public class_745 getSimulatedEntity() {
        return this.simulatedEntity;
    }
}

