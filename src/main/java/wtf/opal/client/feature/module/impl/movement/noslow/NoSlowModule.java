/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1799
 *  net.minecraft.class_3489
 */
package wtf.opal.client.feature.module.impl.movement.noslow;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1799;
import net.minecraft.class_3489;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.movement.noslow.impl.UniversalNoSlow;
import wtf.opal.client.feature.module.impl.movement.noslow.impl.VanillaNoSlow;
import wtf.opal.client.feature.module.impl.movement.noslow.impl.WatchdogNoSlow;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class NoSlowModule
extends Module {
    private final ModeProperty<Mode> mode = new ModeProperty<Mode>("Mode", this, Mode.VANILLA);
    private final BooleanProperty allowSprinting = new BooleanProperty("Allow sprinting", true);
    private Action action = Action.NONE;

    public NoSlowModule() {
        super("No Slow", "Removes vanilla slowdowns such as item usage.", ModuleCategory.MOVEMENT);
        this.addModuleModes(this.mode, new VanillaNoSlow(this), new WatchdogNoSlow(this), new UniversalNoSlow(this));
        this.addProperties(this.mode, this.allowSprinting);
    }

    @Subscribe(priority=2)
    public void onPreGameTick(PreGameTickEvent event) {
        if (Constants.mc.field_1724 == null || Constants.mc.field_1755 != null || Constants.mc.method_18506() != null) {
            this.action = Action.NONE;
            return;
        }
        SlotHelper slotHelper = SlotHelper.getInstance();
        class_1799 mainHandStack = slotHelper.getSilence() == SlotHelper.Silence.FULL ? slotHelper.getMainHandStack(Constants.mc.field_1724) : Constants.mc.field_1724.method_6047();
        switch (mainHandStack.method_7976()) {
            case field_8949: {
                this.action = Action.BLOCKABLE;
                break;
            }
            case field_8952: {
                this.action = mainHandStack.method_31573(class_3489.field_42611) ? Action.BLOCKABLE : Action.NONE;
                break;
            }
            case field_8953: {
                this.action = Action.BOW;
                break;
            }
            default: {
                this.action = Action.USEABLE;
            }
        }
    }

    @Override
    protected void onEnable() {
        super.onEnable();
    }

    @Override
    protected void onDisable() {
        super.onDisable();
    }

    @Override
    public String getSuffix() {
        return ((Mode)((Object)this.mode.getValue())).toString();
    }

    public Action getAction() {
        return this.action;
    }

    public boolean isSprintingAllowed() {
        return this.allowSprinting.getValue();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
        VANILLA("Vanilla"),
        WATCHDOG("Watchdog"),
        UNIVERSAL("Universal");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Action {
        BLOCKABLE,
        USEABLE,
        BOW,
        NONE;

    }
}

