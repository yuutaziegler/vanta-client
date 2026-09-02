/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 */
package wtf.opal.client.feature.module.impl.combat.criticals.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.impl.combat.criticals.CriticalsModule;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.event.impl.game.player.interaction.AttackEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.mixin.ClientPlayerEntityAccessor;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class PacketCriticals
extends ModuleMode<CriticalsModule> {
    private final BooleanProperty groundOnly = (BooleanProperty)new BooleanProperty("Ground only", this, false).hideIf(() -> ((CriticalsModule)this.module).getActiveMode() != this);

    public PacketCriticals(CriticalsModule module) {
        super(module);
    }

    @Subscribe
    public void onAttack(AttackEvent event) {
        if (event.getTarget() instanceof class_1309) {
            if (!PlayerUtility.isCriticalHitAvailable() || this.groundOnly.getValue().booleanValue() && !Constants.mc.field_1724.method_24828()) {
                return;
            }
            class_238 box = Constants.mc.field_1724.method_5829().method_989(0.0, 0.0625, 0.0);
            if (!PlayerUtility.isBoxEmpty(box)) {
                return;
            }
            class_243 pos = Constants.mc.field_1724.method_73189();
            boolean ground = Constants.mc.field_1724.method_24828();
            ClientPlayerEntityAccessor accessor = (ClientPlayerEntityAccessor)Constants.mc.field_1724;
            Constants.mc.field_1724.method_33574(pos.method_1031(0.0, 0.0625, 0.0));
            Constants.mc.field_1724.method_24830(false);
            accessor.callSendMovementPackets();
            Constants.mc.field_1724.method_33574(pos.method_1031(0.0, 0.00125, 0.0));
            Constants.mc.field_1724.method_24830(false);
            accessor.callSendMovementPackets();
            Constants.mc.field_1724.method_33574(pos);
            Constants.mc.field_1724.method_24830(ground);
        }
    }

    @Override
    public Enum<?> getEnumValue() {
        return CriticalsModule.Mode.PACKET;
    }
}

