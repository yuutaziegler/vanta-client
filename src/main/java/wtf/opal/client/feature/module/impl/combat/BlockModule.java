/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1799
 *  net.minecraft.class_1819
 *  net.minecraft.class_2248
 *  net.minecraft.class_3489
 */
package wtf.opal.client.feature.module.impl.combat;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1799;
import net.minecraft.class_1819;
import net.minecraft.class_2248;
import net.minecraft.class_3489;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.helper.impl.target.TargetList;
import wtf.opal.client.feature.helper.impl.target.TargetProperty;
import wtf.opal.client.feature.helper.impl.target.impl.TargetLivingEntity;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.world.scaffold.LBScaffoldModule;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.math.RandomUtility;
import wtf.opal.utility.player.InventoryUtility;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class BlockModule
extends Module {
    private final TargetProperty targetProperty = new TargetProperty(true, false, false, false, false, false);
    private final BooleanProperty allowSwingWhileBlocking = new BooleanProperty("Blocking", false);
    private final BooleanProperty autoBlock = new BooleanProperty("Enabled", true);
    private final BooleanProperty requireAttackKey = new BooleanProperty("Require attack key", false);
    private final NumberProperty blockRange = new NumberProperty("Block range", 3.0, 3.0, 8.0, 0.5);
    private boolean blocking;

    public BlockModule() {
        super("Block", "Allows illegitimate actions while blocking, or automatically blocks.", ModuleCategory.COMBAT);
        this.addProperties(new GroupProperty("Allow swing while...", this.allowSwingWhileBlocking), new GroupProperty("Auto block", new Property[]{this.autoBlock, this.requireAttackKey.hideIf(() -> this.autoBlock.getValue() == false), this.blockRange.hideIf(() -> this.autoBlock.getValue() == false)}), this.targetProperty.get());
    }

    @Subscribe(priority=2)
    public void onHandleInput(MouseHandleInputEvent event) {
        class_1799 mainHandStack;
        this.blocking = false;
        if (OpalClient.getInstance().getModuleRepository().getModule(LBScaffoldModule.class).isEnabled()) {
            return;
        }
        TargetList targetList = LocalDataWatch.getTargetList();
        SlotHelper slotHelper = SlotHelper.getInstance();
        class_1799 class_17992 = mainHandStack = slotHelper.getSilence() == SlotHelper.Silence.FULL ? slotHelper.getMainHandStack(Constants.mc.field_1724) : Constants.mc.field_1724.method_6047();
        if (targetList == null || !this.autoBlock.getValue().booleanValue() || !mainHandStack.method_31573(class_3489.field_42611) && !(Constants.mc.field_1724.method_6079().method_7909() instanceof class_1819)) {
            return;
        }
        if (this.requireAttackKey.getValue().booleanValue() && !Constants.mc.field_1690.field_1886.method_1434()) {
            return;
        }
        List<TargetLivingEntity> targets = targetList.collectTargets(this.targetProperty.getTargetFlags(), TargetLivingEntity.class);
        double interactionRange = (Double)this.blockRange.getValue();
        for (TargetLivingEntity target : targets) {
            Object entity;
            if (target.isLocal() || !(PlayerUtility.getDistanceToEntity(entity = target.getEntity()) <= interactionRange)) continue;
            class_2248 blockOver = PlayerUtility.getBlockOver();
            if (InventoryUtility.isBlockInteractable(blockOver)) {
                return;
            }
            MouseHelper.getRightButton().setPressed(true, RandomUtility.getRandomInt(2));
            this.blocking = true;
        }
    }

    public boolean isSwingAllowed() {
        return this.allowSwingWhileBlocking.getValue();
    }

    public boolean isBlocking() {
        return this.blocking;
    }
}

