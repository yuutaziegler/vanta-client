/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2596
 *  net.minecraft.class_2678
 *  net.minecraft.class_2743
 *  net.minecraft.class_6373
 */
package wtf.opal.client.feature.module.impl.combat.velocity.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2596;
import net.minecraft.class_2678;
import net.minecraft.class_2743;
import net.minecraft.class_6373;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.impl.combat.velocity.VelocityMode;
import wtf.opal.client.feature.module.impl.combat.velocity.VelocityModule;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class MushMCVelocity
extends VelocityMode {
    private boolean cancel;

    public MushMCVelocity(VelocityModule module) {
        super(module);
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2743) {
            class_2743 packet = (class_2743)class_25962;
            if (Constants.mc.field_1724 != null && packet.method_11818() == Constants.mc.field_1724.method_5628()) {
                event.setCancelled();
                this.cancel = true;
            }
        } else if (event.getPacket() instanceof class_6373) {
            if (this.cancel) {
                event.setCancelled();
                this.cancel = false;
            }
        } else if (event.getPacket() instanceof class_2678) {
            this.cancel = false;
        }
    }

    @Override
    public Enum<?> getEnumValue() {
        return VelocityModule.Mode.MUSHMC;
    }
}

