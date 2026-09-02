/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1819
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 *  net.minecraft.class_3966
 */
package wtf.opal.client.feature.module.impl.combat.killaura;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1819;
import net.minecraft.class_239;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_3966;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.model.impl.InstantRotationModel;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.RotationUtility;

@Environment(value=EnvType.CLIENT)
public final class LBKillAuraModule
extends Module {
    private final ModeProperty<Mode> mode = new ModeProperty<Mode>("Mode", Mode.SINGLE);
    private final ModeProperty<RaycastMode> raycastMode = new ModeProperty<RaycastMode>("Raycast", RaycastMode.TRACE_ALL);
    private final NumberProperty range = new NumberProperty("Range", 3.0, 1.0, 6.0, 0.1);
    private final NumberProperty throughWallsRange = new NumberProperty("ThroughWallsRange", 3.0, 0.0, 6.0, 0.1);
    private final NumberProperty scanRangeIncrease = new NumberProperty("ScanRangeIncrease", 2.5, 0.0, 7.0, 0.1);
    private final NumberProperty minCPS = new NumberProperty("MinCPS", 8.0, 1.0, 20.0, 0.5);
    private final NumberProperty maxCPS = new NumberProperty("MaxCPS", 12.0, 1.0, 20.0, 0.5);
    private final ModeProperty<RotationTiming> rotationTiming = new ModeProperty<RotationTiming>("RotationTiming", RotationTiming.NORMAL);
    private final BooleanProperty aimThroughWalls = new BooleanProperty("AimThroughWalls", false);
    private final NumberProperty fov = new NumberProperty("FOV", 180.0, 1.0, 180.0, 1.0);
    private final BooleanProperty keepSprint = new BooleanProperty("KeepSprint", true);
    private final BooleanProperty autoBlock = new BooleanProperty("AutoBlock", false);
    private final BooleanProperty ignoreOpenInventory = new BooleanProperty("IgnoreOpenInventory", true);
    private final BooleanProperty simulateInventoryClosing = new BooleanProperty("SimulateInventoryClosing", true);
    private final ModeProperty<SwingMode> swingMode = new ModeProperty<SwingMode>("Swing", SwingMode.DO_NOT_HIDE);
    private final BooleanProperty requireWeapon = new BooleanProperty("RequireWeapon", false);
    private class_1309 target;
    private int waitTicks = 0;
    private long lastAttackTime = 0L;

    public LBKillAuraModule() {
        super("LiquidBounceKillAura", "LiquidBounce KillAura with exact settings from LB source.", ModuleCategory.HACKED);
        this.addProperties(this.mode, this.raycastMode, this.range, this.throughWallsRange, this.scanRangeIncrease, this.minCPS, this.maxCPS, this.rotationTiming, this.aimThroughWalls, this.fov, this.keepSprint, this.autoBlock, this.ignoreOpenInventory, this.simulateInventoryClosing, this.swingMode, this.requireWeapon);
    }

    @Subscribe(priority=10)
    public void onPreGameTick(PreGameTickEvent event) {
        if (Constants.mc.field_1724 == null || Constants.mc.field_1687 == null || Constants.mc.field_1724.method_29504() || Constants.mc.field_1724.method_7325()) {
            this.target = null;
            return;
        }
        if (this.waitTicks > 0) {
            --this.waitTicks;
        }
        if (!this.ignoreOpenInventory.getValue().booleanValue() && Constants.mc.field_1755 != null) {
            return;
        }
        if (this.requireWeapon.getValue().booleanValue()) {
            boolean isWeapon;
            class_1799 mainHand = Constants.mc.field_1724.method_6047();
            boolean bl = isWeapon = mainHand.method_7909().method_7876().contains("sword") || mainHand.method_7909().method_7876().contains("axe");
            if (!isWeapon) {
                return;
            }
        }
        this.target = this.updateTarget();
        if (this.target != null) {
            double scanRange = Math.max((Double)this.range.getValue(), (Double)this.throughWallsRange.getValue()) + (Double)this.scanRangeIncrease.getValue();
            class_243 targetPos = this.target.method_5829().method_1005();
            class_241 rotations = RotationUtility.getRotationFromPosition(targetPos);
            switch (((RotationTiming)((Object)this.rotationTiming.getValue())).ordinal()) {
                case 0: {
                    RotationHelper.getHandler().rotate(rotations, InstantRotationModel.INSTANCE);
                    break;
                }
                case 1: {
                    if (!this.canAttack()) break;
                    RotationHelper.getHandler().rotate(rotations, InstantRotationModel.INSTANCE);
                    break;
                }
                case 2: {
                    Constants.mc.field_1724.method_36456(rotations.field_1343);
                    Constants.mc.field_1724.method_36457(rotations.field_1342);
                }
            }
        }
    }

    @Subscribe
    public void onHandleInput(MouseHandleInputEvent event) {
        if (this.target == null) {
            return;
        }
        if (!this.canAttack()) {
            return;
        }
        if (Constants.mc.field_1765 != null && Constants.mc.field_1765.method_17783() == class_239.class_240.field_1331) {
            class_3966 hitResult = (class_3966)Constants.mc.field_1765;
            boolean shouldAttack = false;
            switch (((RaycastMode)((Object)this.raycastMode.getValue())).ordinal()) {
                case 0: {
                    shouldAttack = (double)Constants.mc.field_1724.method_5739((class_1297)this.target) <= (Double)this.range.getValue();
                    break;
                }
                case 1: {
                    shouldAttack = hitResult.method_17782() == this.target;
                    break;
                }
                case 2: {
                    boolean bl = shouldAttack = hitResult.method_17782() == this.target || this.raycastMode.getValue() == RaycastMode.TRACE_ALL;
                }
            }
            if (shouldAttack && Constants.mc.field_1724.method_7261(0.0f) >= 1.0f) {
                if (this.simulateInventoryClosing.getValue().booleanValue() && Constants.mc.field_1755 != null) {
                    Constants.mc.field_1724.method_7346();
                }
                MouseHelper.getLeftButton().setPressed();
                if (Constants.mc.field_1761 != null) {
                    Constants.mc.field_1761.method_2918((class_1657)Constants.mc.field_1724, (class_1297)this.target);
                    if (this.swingMode.getValue() != SwingMode.HIDE) {
                        Constants.mc.field_1724.method_6104(class_1268.field_5808);
                    }
                    if (this.autoBlock.getValue().booleanValue() && Constants.mc.field_1724.method_6079().method_7909() instanceof class_1819) {
                        Constants.mc.field_1761.method_2919((class_1657)Constants.mc.field_1724, class_1268.field_5810);
                    }
                    if (!this.keepSprint.getValue().booleanValue()) {
                        Constants.mc.field_1724.method_5728(false);
                    }
                }
                this.lastAttackTime = System.currentTimeMillis();
            }
        } else if (this.raycastMode.getValue() == RaycastMode.TRACE_NONE && Constants.mc.field_1724.method_7261(0.0f) >= 1.0f && (double)Constants.mc.field_1724.method_5739((class_1297)this.target) <= (Double)this.range.getValue()) {
            Constants.mc.field_1761.method_2918((class_1657)Constants.mc.field_1724, (class_1297)this.target);
            if (this.swingMode.getValue() != SwingMode.HIDE) {
                Constants.mc.field_1724.method_6104(class_1268.field_5808);
            }
            this.lastAttackTime = System.currentTimeMillis();
        }
    }

    private boolean canAttack() {
        double cps;
        long attackDelay;
        long timeSinceLastAttack = System.currentTimeMillis() - this.lastAttackTime;
        return timeSinceLastAttack >= (attackDelay = (long)(1000.0 / (cps = (Double)this.minCPS.getValue() + Math.random() * ((Double)this.maxCPS.getValue() - (Double)this.minCPS.getValue()))));
    }

    private class_1309 updateTarget() {
        double scanRange = Math.max((Double)this.range.getValue(), (Double)this.throughWallsRange.getValue()) + (Double)this.scanRangeIncrease.getValue();
        List targets = StreamSupport.stream(Constants.mc.field_1687.method_18112().spliterator(), false).filter(e -> e instanceof class_1309 && e != Constants.mc.field_1724).map(e -> (class_1309)e).filter(e -> (double)Constants.mc.field_1724.method_5739((class_1297)e) <= scanRange).filter(e -> !e.method_29504()).filter(e -> RotationUtility.isEntityInFOV((class_1297)e, ((Double)this.fov.getValue()).floatValue())).sorted(Comparator.comparingDouble(e -> Constants.mc.field_1724.method_5739((class_1297)e))).collect(Collectors.toList());
        if (targets.isEmpty()) {
            return null;
        }
        switch (((Mode)((Object)this.mode.getValue())).ordinal()) {
            case 0: {
                return (class_1309)targets.get(0);
            }
            case 1: {
                return (class_1309)targets.get(0);
            }
        }
        return (class_1309)targets.get(0);
    }

    @Override
    protected void onDisable() {
        this.target = null;
        super.onDisable();
    }

    @Override
    public String getSuffix() {
        return ((Mode)((Object)this.mode.getValue())).toString();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum Mode {
        SINGLE("Single"),
        SWITCH("Switch");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum RaycastMode {
        TRACE_NONE("None"),
        TRACE_ONLYENEMY("Enemy"),
        TRACE_ALL("All");

        private final String name;

        private RaycastMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum RotationTiming {
        NORMAL("Normal"),
        SNAP("Snap"),
        ON_TICK("OnTick");

        private final String name;

        private RotationTiming(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum SwingMode {
        DO_NOT_HIDE("DoNotHide"),
        HIDE("Hide"),
        NONE("None");

        private final String name;

        private SwingMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

