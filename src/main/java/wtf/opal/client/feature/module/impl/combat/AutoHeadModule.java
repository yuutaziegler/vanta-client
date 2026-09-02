/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_1809
 */
package wtf.opal.client.feature.module.impl.combat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_1809;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.time.Stopwatch;

@Environment(value=EnvType.CLIENT)
public final class AutoHeadModule
extends Module {
    private final NumberProperty healDelay = new NumberProperty("Heal Delay", 750.0, 0.0, 3000.0, 50.0);
    private final NumberProperty healthPercent = new NumberProperty("Health", "%", 50.0, 5.0, 95.0, 5.0);
    private final Stopwatch stopwatch = new Stopwatch();
    private boolean swapBack;

    public AutoHeadModule() {
        super("Auto Head", "Automatically eats golden heads.", ModuleCategory.COMBAT);
        this.addProperties(this.healDelay, this.healthPercent);
    }

    @Subscribe(priority=-10)
    public void onPreGameTick(PreGameTickEvent event) {
        if (this.swapBack) {
            SlotHelper slotHelper = SlotHelper.getInstance();
            slotHelper.stop();
            slotHelper.sync(true, true);
            this.swapBack = false;
        }
    }

    @Subscribe(priority=-10)
    public void onMouseHandleInput(MouseHandleInputEvent event) {
        if ((double)(Constants.mc.field_1724.method_6032() / Constants.mc.field_1724.method_6063() * 100.0f) <= (Double)this.healthPercent.getValue() && this.stopwatch.hasTimeElapsed(((Double)this.healDelay.getValue()).longValue(), false)) {
            int headSlot = this.getHeadSlot();
            if (headSlot == -1) {
                return;
            }
            if (Constants.mc.field_1724.method_6067() > 2.0f) {
                return;
            }
            SlotHelper.getInstance().setTargetItem(headSlot).silence(SlotHelper.Silence.FULL);
            MouseHelper.getRightButton().setPressed(true, 2);
            this.swapBack = true;
            this.stopwatch.reset();
        }
    }

    private int getHeadSlot() {
        for (int i = 0; i < 9; ++i) {
            class_1809 playerHeadItem;
            class_1799 itemStack = (class_1799)Constants.mc.field_1724.method_31548().method_67533().get(i);
            class_1792 class_17922 = itemStack.method_7909();
            if (!(class_17922 instanceof class_1809) || !(playerHeadItem = (class_1809)class_17922).method_63680().getString().contains("Head")) continue;
            return i;
        }
        return -1;
    }
}

