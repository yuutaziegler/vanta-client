/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1713
 *  net.minecraft.class_2596
 *  net.minecraft.class_2813
 *  net.minecraft.class_2815
 *  net.minecraft.class_2827
 *  net.minecraft.class_6374
 */
package wtf.opal.client.feature.module.impl.utility.disabler.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1713;
import net.minecraft.class_2596;
import net.minecraft.class_2813;
import net.minecraft.class_2815;
import net.minecraft.class_2827;
import net.minecraft.class_6374;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder.BlockHolder;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.OutboundNetworkBlockage;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.impl.utility.disabler.DisablerModule;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.event.impl.game.JoinWorldEvent;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.packet.InstantaneousSendPacketEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class WatchdogDisabler
extends ModuleMode<DisablerModule> {
    private final MultipleBooleanProperty options = (MultipleBooleanProperty)new MultipleBooleanProperty("Options", new BooleanProperty("Inventory Move", true)).hideIf(() -> ((DisablerModule)this.module).getActiveMode() != this);
    private boolean shouldBlink;
    private final BlockHolder blockHolder = new BlockHolder(OutboundNetworkBlockage.get());

    public WatchdogDisabler(DisablerModule module) {
        super(module);
        module.addProperties(this.options);
    }

    @Override
    public Enum<?> getEnumValue() {
        return DisablerModule.Mode.WATCHDOG;
    }

    public boolean isInventoryMoveDisabler() {
        return this.options.getProperty("Inventory Move").getValue() != false && LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer;
    }

    @Subscribe
    public void onInstantaneousSendPacketEvent(InstantaneousSendPacketEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2813) {
            HypixelServer.ModAPI.Location currentLocation;
            boolean allowedAction;
            class_2813 clickSlot = (class_2813)class_25962;
            if (!this.isInventoryMoveDisabler()) {
                return;
            }
            boolean bl = allowedAction = clickSlot.comp_3846() == class_1713.field_7794 || clickSlot.comp_3846() == class_1713.field_7791 || clickSlot.comp_3846() == class_1713.field_7795;
            if (LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer && (currentLocation = HypixelServer.ModAPI.get().getCurrentLocation()) != null && currentLocation.isLobby()) {
                this.shouldBlink = false;
                return;
            }
            if (clickSlot.comp_3842() == Constants.mc.field_1724.field_7498.field_7763 && allowedAction) {
                Constants.mc.method_1562().method_52787((class_2596)new class_2815(clickSlot.comp_3842()));
            } else {
                this.shouldBlink = true;
            }
        } else {
            class_25962 = event.getPacket();
            if (class_25962 instanceof class_2815) {
                class_2815 closeScreen = (class_2815)class_25962;
                if (Constants.mc.field_1724 != null && closeScreen.method_36168() == Constants.mc.field_1724.field_7498.field_7763) {
                    if (!this.isInventoryMoveDisabler()) {
                        return;
                    }
                    this.shouldBlink = false;
                }
            }
        }
    }

    @Override
    public void onDisable() {
        this.blockHolder.release();
        super.onDisable();
    }

    @Subscribe(priority=2)
    public void onPreGameTick(PreGameTickEvent event) {
        if (this.isInventoryMoveDisabler()) {
            if (Constants.mc.field_1755 == null) {
                this.shouldBlink = false;
            }
            if (this.shouldBlink) {
                this.blockHolder.block(p -> p, p -> !(p instanceof class_2813) && !(p instanceof class_2815) && !(p instanceof class_6374) && !(p instanceof class_2827));
            } else {
                this.blockHolder.release();
            }
        }
    }

    @Subscribe
    public void onJoinWorld(JoinWorldEvent event) {
        this.shouldBlink = false;
    }
}

