/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1657
 *  net.minecraft.class_1937
 *  net.minecraft.class_2248
 *  net.minecraft.class_2596
 *  net.minecraft.class_2663
 */
package wtf.opal.client.feature.module.impl.movement.noslow.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1657;
import net.minecraft.class_1937;
import net.minecraft.class_2248;
import net.minecraft.class_2596;
import net.minecraft.class_2663;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseButton;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.block.holder.BlockHolder;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.InboundNetworkBlockage;
import wtf.opal.client.feature.helper.impl.player.packet.blockage.impl.OutboundNetworkBlockage;
import wtf.opal.client.feature.module.impl.movement.noslow.NoSlowModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.impl.game.input.PostHandleInputEvent;
import wtf.opal.event.impl.game.input.SlotChangeEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.player.movement.SlowdownEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.InventoryUtility;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class WatchdogNoSlow
extends ModuleMode<NoSlowModule> {
    private final BlockHolder oBlockHolder = new BlockHolder(OutboundNetworkBlockage.get());
    private final BlockHolder iBlockHolder = new BlockHolder(InboundNetworkBlockage.get());
    private boolean stopUse;
    private int nextCycleTick = -1;
    private int slotChangeTick;
    private boolean runThisTick = false;

    public WatchdogNoSlow(NoSlowModule module) {
        super(module);
    }

    @Subscribe
    public void onSlowdown(SlowdownEvent event) {
        if (((NoSlowModule)this.module).getAction() != NoSlowModule.Action.BOW && (((NoSlowModule)this.module).getAction() != NoSlowModule.Action.USEABLE || this.oBlockHolder.isBlocking()) && Constants.mc.field_1724.field_6012 - this.slotChangeTick != 1) {
            event.setCancelled();
        }
    }

    @Subscribe
    public void onSlotChange(SlotChangeEvent event) {
        this.release();
        this.resetCycle();
        if (Constants.mc.field_1724 != null) {
            this.slotChangeTick = Constants.mc.field_1724.field_6012;
        }
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_2663) {
            class_2663 statusS2CPacket = (class_2663)class_25962;
            if (Constants.mc.field_1724 != null && statusS2CPacket.method_11469((class_1937)Constants.mc.field_1687) == Constants.mc.field_1724) {
                this.release();
            }
        }
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        if (Constants.mc.field_1724 == null || Constants.mc.field_1755 != null || Constants.mc.method_18506() != null) {
            this.resetCycle();
            this.release();
        }
    }

    private void block() {
        this.oBlockHolder.block();
    }

    private void release() {
        this.oBlockHolder.release();
    }

    private void resetCycle() {
        this.stopUse = false;
        this.runThisTick = false;
        this.nextCycleTick = -1;
    }

    @Subscribe
    public void onPostHandleInput(PostHandleInputEvent event) {
        if (Constants.mc.field_1724 == null || ((NoSlowModule)this.module).getAction() != NoSlowModule.Action.BLOCKABLE) {
            return;
        }
        if (this.stopUse && Constants.mc.field_1724.method_6115()) {
            this.block();
            Constants.mc.field_1761.method_2897((class_1657)Constants.mc.field_1724);
            this.stopUse = false;
        }
    }

    @Subscribe(priority=1)
    public void onMouseHandleInput(MouseHandleInputEvent event) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        MouseButton rightButton = MouseHelper.getRightButton();
        this.runThisTick = false;
        if (rightButton.isPressed() && ((NoSlowModule)this.module).getAction() == NoSlowModule.Action.BLOCKABLE) {
            int age = Constants.mc.field_1724.field_6012;
            if (this.nextCycleTick < 0) {
                this.nextCycleTick = age;
            }
            if (age >= this.nextCycleTick) {
                if (this.oBlockHolder.isBlocking()) {
                    this.release();
                }
                this.runThisTick = true;
                this.nextCycleTick = age + 2;
            } else if (!this.oBlockHolder.isBlocking()) {
                this.block();
            }
        } else {
            this.resetCycle();
            if (!Constants.mc.field_1724.method_6115()) {
                this.release();
            } else if (!this.oBlockHolder.isBlocking() && ((NoSlowModule)this.module).getAction() == NoSlowModule.Action.BLOCKABLE) {
                this.block();
            }
        }
        if (((NoSlowModule)this.module).getAction() == NoSlowModule.Action.BLOCKABLE) {
            if (rightButton.isPressed()) {
                if (this.runThisTick) {
                    if (!Constants.mc.field_1724.method_6115() || !this.oBlockHolder.isBlocking()) {
                        class_2248 blockOver = PlayerUtility.getBlockOver();
                        if (InventoryUtility.isBlockInteractable(blockOver) || Constants.mc.field_1761.method_2923()) {
                            return;
                        }
                        this.stopUse = true;
                        rightButton.setPressed();
                    } else {
                        rightButton.setDisabled();
                    }
                } else {
                    rightButton.setDisabled();
                    if (!this.oBlockHolder.isBlocking()) {
                        this.block();
                    }
                }
            } else {
                this.stopUse = false;
            }
        } else {
            this.stopUse = false;
        }
    }

    @Override
    public void onDisable() {
        this.release();
        this.resetCycle();
        super.onDisable();
    }

    @Override
    public Enum<?> getEnumValue() {
        return NoSlowModule.Mode.WATCHDOG;
    }
}

