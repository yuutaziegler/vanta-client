/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.world;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class FastBreakModule
extends Module {
    private final BooleanProperty speedEnabled = new BooleanProperty("Enabled", true);
    private final NumberProperty speed = new NumberProperty("Speed", "%", 20.0, 1.0, 100.0, 1.0);
    private final BooleanProperty breakCooldownEnabled = new BooleanProperty("Enabled", true);
    private final NumberProperty breakCooldown = new NumberProperty("Cooldown", 0.0, 0.0, 5.0, 1.0);
    private final MultipleBooleanProperty breakSlowdowns = new MultipleBooleanProperty("Break slowdown", new BooleanProperty("In air", true), new BooleanProperty("In water", true), new BooleanProperty("Mining fatigue", true));
    private final BooleanProperty spoofGroundState = new BooleanProperty("Spoof ground state", false);

    public FastBreakModule() {
        super("Fast Break", "Breaks blocks quicker.", ModuleCategory.WORLD);
        this.addProperties(new Property[]{new GroupProperty("Speed", this.speedEnabled, this.speed), new GroupProperty("Break cooldown", this.breakCooldownEnabled, this.breakCooldown), this.breakSlowdowns, this.spoofGroundState.hideIf(() -> this.breakSlowdowns.getProperty("In air").getValue())});
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        if (this.spoofGroundState.getValue().booleanValue() && Constants.mc.field_1761.method_2923() && !Constants.mc.field_1724.method_5799() && !this.breakSlowdowns.getProperty("In air").getValue().booleanValue()) {
            event.setOnGround(true);
        }
    }

    public boolean isSpeedEnabled() {
        return this.speedEnabled.getValue();
    }

    public float getSpeed() {
        return ((Double)this.speed.getValue()).floatValue();
    }

    public boolean isBreakCooldownEnabled() {
        return this.breakCooldownEnabled.getValue();
    }

    public int getBreakCooldown() {
        return ((Double)this.breakCooldown.getValue()).intValue();
    }

    public MultipleBooleanProperty getBreakSlowdowns() {
        return this.breakSlowdowns;
    }
}

