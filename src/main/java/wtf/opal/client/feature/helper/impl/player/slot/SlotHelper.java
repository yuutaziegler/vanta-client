/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1661
 *  net.minecraft.class_1799
 *  net.minecraft.class_3532
 *  net.minecraft.class_746
 */
package wtf.opal.client.feature.helper.impl.player.slot;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_3532;
import net.minecraft.class_746;
import wtf.opal.client.Constants;

@Environment(value=EnvType.CLIENT)
public final class SlotHelper {
    private int currentItem;
    private int targetItem;
    private boolean active;
    private int activeTick;
    private int ticks;
    private Silence silence = Silence.DEFAULT;
    private static SlotHelper instance;

    private SlotHelper() {
    }

    public SlotHelper setTargetItem(int currentItem) {
        currentItem = class_3532.method_15340((int)currentItem, (int)0, (int)8);
        if (!this.active) {
            this.currentItem = Constants.mc.field_1724.method_31548().method_67532();
        }
        this.targetItem = currentItem;
        this.activeTick = this.ticks;
        this.active = true;
        this.sync(true, true);
        return this;
    }

    public SlotHelper silence(Silence silence) {
        this.silence = silence;
        return this;
    }

    public void setVisualSlot(int currentItem) {
        this.currentItem = currentItem;
    }

    public void stop() {
        this.activeTick = -1;
        this.sync(true, true);
    }

    public void sync(boolean reset, boolean check) {
        if (this.active && (!check || Constants.mc.method_18506() == null && Constants.mc.field_1755 == null)) {
            if (reset && this.activeTick != this.ticks) {
                Constants.mc.field_1724.method_31548().method_61496(this.currentItem);
                this.silence = Silence.DEFAULT;
                this.active = false;
            } else {
                Constants.mc.field_1724.method_31548().method_61496(this.targetItem);
            }
        }
    }

    public void tick() {
        this.sync(true, true);
        ++this.ticks;
    }

    public class_1799 getMainHandStack(class_746 player) {
        return this.active && this.silence != Silence.NONE ? (class_1799)player.method_31548().method_67533().get(this.currentItem) : player.method_6047();
    }

    public int getSelectedSlot(class_1661 inventory) {
        return this.active && this.silence == Silence.FULL ? this.currentItem : inventory.method_67532();
    }

    public boolean isActive() {
        return this.active;
    }

    public int getVisualSlot() {
        return this.currentItem;
    }

    public Silence getSilence() {
        return this.silence;
    }

    public static SlotHelper getInstance() {
        return instance;
    }

    public static void setInstance() {
        instance = new SlotHelper();
    }

    public static SlotHelper setCurrentItem(int currentItem) {
        return instance.setTargetItem(currentItem);
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Silence {
        NONE,
        DEFAULT,
        FULL;

    }
}

