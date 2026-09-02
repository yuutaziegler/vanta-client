/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1937
 *  net.minecraft.class_2248
 *  net.minecraft.class_2596
 *  net.minecraft.class_2663
 */
package wtf.opal.client.feature.module.impl.movement.noslow.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseButton;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder.BlockHolder;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.OutboundNetworkBlockage;
import wtf.opal.client.feature.module.impl.movement.noslow.NoSlowModule;
import wtf.opal.client.feature.module.impl.world.scaffold.LBScaffoldModule;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.feature.module.repository.ModuleRepository;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.player.movement.SlowdownEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.InventoryUtility;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class UniversalNoSlow
extends ModuleMode<NoSlowModule> {
    private final BooleanProperty slowdown = (BooleanProperty)new BooleanProperty("Slow down", false).hideIf(() -> ((NoSlowModule)this.module).getActiveMode() != this);
    private final BlockHolder oBlockHolder = new BlockHolder(OutboundNetworkBlockage.get());
    private boolean stopUse;

    public UniversalNoSlow(NoSlowModule module) {
        super(module);
        module.addProperties(this.slowdown);
    }

    @Subscribe
    public void onSlowdown(SlowdownEvent event) {
        if (!this.slowdown.getValue().booleanValue()) {
            event.setCancelled();
        }
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2663) {
            class_2663 statusS2CPacket = (class_2663)class_25962;
            if (Constants.mc.field_1724 != null && statusS2CPacket.method_11469((class_1937)Constants.mc.field_1687) == Constants.mc.field_1724) {
                this.oBlockHolder.release();
            }
        }
    }

    @Subscribe(priority=2)
    public void onPreGameTick(PreGameTickEvent event) {
        ModuleRepository moduleRepository = OpalClient.getInstance().getModuleRepository();
        boolean shouldStop = moduleRepository.getModule(LBScaffoldModule.class).isEnabled();
        if (shouldStop || Constants.mc.field_1724 == null || Constants.mc.field_1755 != null || Constants.mc.method_18506() != null) {
            this.release();
            return;
        }
        if (this.stopUse) {
            this.block();
            MouseHelper.getRightButton().setDisabled();
            this.stopUse = false;
        } else if (((NoSlowModule)this.module).getAction() == NoSlowModule.Action.BLOCKABLE || !Constants.mc.field_1724.method_6115()) {
            this.release();
        }
    }

    private void block() {
        this.oBlockHolder.block();
    }

    private void release() {
        this.oBlockHolder.release();
    }

    @Subscribe(priority=1)
    public void onMouseHandleInput(MouseHandleInputEvent event) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        MouseButton rightButton = MouseHelper.getRightButton();
        if (((NoSlowModule)this.module).getAction() == NoSlowModule.Action.BLOCKABLE) {
            if (rightButton.isPressed()) {
                if (!Constants.mc.field_1724.field_3913.field_54155.comp_3163() || !Constants.mc.field_1724.method_24828() && (Constants.mc.field_1724.method_18798().method_10214() >= 0.0 || PlayerUtility.isBoxEmpty(Constants.mc.field_1724.method_5829().method_989(0.0, Constants.mc.field_1724.method_18798().method_10214(), 0.0)))) {
                    if (!Constants.mc.field_1724.method_6115() || !this.oBlockHolder.isBlocking()) {
                        class_2248 blockOver = PlayerUtility.getBlockOver();
                        if (InventoryUtility.isBlockInteractable(blockOver) || Constants.mc.field_1761.method_2923()) {
                            return;
                        }
                        this.stopUse = true;
                        rightButton.setPressed();
                    } else if (Constants.mc.field_1724.method_6115()) {
                        rightButton.setDisabled();
                    }
                } else {
                    rightButton.setDisabled();
                }
            }
        } else {
            this.stopUse = false;
        }
    }

    @Override
    public void onDisable() {
        this.release();
        this.stopUse = false;
        super.onDisable();
    }

    @Override
    public Enum<?> getEnumValue() {
        return NoSlowModule.Mode.UNIVERSAL;
    }
}

