/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.combat.killaura;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationProperty;
import wtf.opal.client.feature.helper.impl.player.rotation.model.IRotationModel;
import wtf.opal.client.feature.helper.impl.player.rotation.model.impl.InstantRotationModel;
import wtf.opal.client.feature.helper.impl.player.swing.CPSProperty;
import wtf.opal.client.feature.helper.impl.target.TargetProperty;
import wtf.opal.client.feature.module.impl.combat.killaura.KillAuraModule;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;

@Environment(value=EnvType.CLIENT)
public final class KillAuraSettings {
    private final RotationProperty rotationProperty = new RotationProperty(InstantRotationModel.INSTANCE, new Property[0]);
    private final ModeProperty<Mode> mode;
    private final TargetProperty targetProperty = new TargetProperty(true, false, false, false, false, true);
    private final CPSProperty cpsProperty;
    private final CPSProperty swingCpsProperty;
    private final NumberProperty rotationRange;
    private final NumberProperty swingRange;
    private final BooleanProperty hideFakeSwings;
    private final BooleanProperty requireAttackKey;
    private final BooleanProperty requireWeapon;
    private final BooleanProperty overrideRaycast;
    private final BooleanProperty tickLookahead;
    private final NumberProperty fov;
    private final MultipleBooleanProperty visuals;
    private final ModeProperty<RaycastMode> raycast;
    private final ModeProperty<RotationTiming> rotationTiming;
    private final BooleanProperty keepSprint;
    private final BooleanProperty aimThroughWalls;
    private final BooleanProperty ignoreOpenInventory;
    private final BooleanProperty simulateInventoryClosing;
    private final NumberProperty interactionRange;
    private final NumberProperty interactionThroughWallsRange;
    private final NumberProperty scanRangeIncrease;

    public KillAuraSettings(KillAuraModule module) {
        this.cpsProperty = new CPSProperty(module, "Attack CPS", true);
        this.swingCpsProperty = new CPSProperty(module, "Swing CPS", false).hideIf(this.cpsProperty::isModernDelay);
        this.rotationRange = new NumberProperty("Rotation range", 5.0, 3.0, 8.0, 0.1);
        this.swingRange = (NumberProperty)new NumberProperty("Swing range", 5.0, 3.0, 8.0, 0.1).hideIf(this.cpsProperty::isModernDelay);
        this.hideFakeSwings = (BooleanProperty)new BooleanProperty("Hide fake swings", true).hideIf(this.cpsProperty::isModernDelay);
        this.requireAttackKey = new BooleanProperty("Require attack key", false);
        this.requireWeapon = new BooleanProperty("Require weapon", false);
        this.overrideRaycast = new BooleanProperty("Override raycast", true);
        this.tickLookahead = (BooleanProperty)new BooleanProperty("Tick lookahead", false).hideIf(() -> !this.isOverrideRaycast());
        this.mode = new ModeProperty<Mode>("Mode", Mode.SWITCH);
        this.fov = new NumberProperty("FOV", 180.0, 1.0, 180.0, 1.0);
        this.visuals = new MultipleBooleanProperty("Visuals", new BooleanProperty("Box", false));
        this.raycast = new ModeProperty<RaycastMode>("Raycast", RaycastMode.TRACE_ALL);
        this.rotationTiming = new ModeProperty<RotationTiming>("RotationTiming", RotationTiming.NORMAL);
        this.keepSprint = new BooleanProperty("KeepSprint", true);
        this.aimThroughWalls = new BooleanProperty("AimThroughWalls", false);
        this.ignoreOpenInventory = new BooleanProperty("IgnoreOpenInventory", true);
        this.simulateInventoryClosing = new BooleanProperty("SimulateInventoryClosing", true);
        this.interactionRange = new NumberProperty("InteractionRange", 3.0, 1.0, 6.0, 0.1);
        this.interactionThroughWallsRange = new NumberProperty("ThroughWallsRange", 3.0, 0.0, 6.0, 0.1);
        this.scanRangeIncrease = new NumberProperty("ScanRangeIncrease", 2.5, 0.0, 7.0, 0.1);
        module.addProperties(this.rotationProperty.get(), new GroupProperty("Requirements", this.requireWeapon, this.requireAttackKey), this.mode, this.rotationRange, this.swingRange, this.hideFakeSwings, this.targetProperty.get(), this.fov, this.overrideRaycast, this.tickLookahead, this.visuals, new GroupProperty("LiquidBounce Settings", this.raycast, this.rotationTiming, this.keepSprint, this.aimThroughWalls, this.ignoreOpenInventory, this.simulateInventoryClosing, this.interactionRange, this.interactionThroughWallsRange, this.scanRangeIncrease));
    }

    public double getSwingRange() {
        return (Double)this.swingRange.getValue();
    }

    public boolean isHideFakeSwings() {
        return this.hideFakeSwings.getValue();
    }

    public boolean isOverrideRaycast() {
        return this.overrideRaycast.getValue();
    }

    public boolean isTickLookahead() {
        return this.tickLookahead.getValue();
    }

    public double getRotationRange() {
        return (Double)this.rotationRange.getValue();
    }

    public MultipleBooleanProperty getVisuals() {
        return this.visuals;
    }

    public TargetProperty getTargetProperty() {
        return this.targetProperty;
    }

    public CPSProperty getCpsProperty() {
        return this.cpsProperty;
    }

    public CPSProperty getSwingCpsProperty() {
        return this.swingCpsProperty;
    }

    public boolean isRequireAttackKey() {
        return this.requireAttackKey.getValue();
    }

    public boolean isRequireWeapon() {
        return this.requireWeapon.getValue();
    }

    public IRotationModel createRotationModel() {
        return this.rotationProperty.createModel();
    }

    public Mode getMode() {
        return (Mode)((Object)this.mode.getValue());
    }

    public float getFov() {
        return ((Double)this.fov.getValue()).floatValue();
    }

    public RaycastMode getRaycast() {
        return (RaycastMode)((Object)this.raycast.getValue());
    }

    public RotationTiming getRotationTiming() {
        return (RotationTiming)((Object)this.rotationTiming.getValue());
    }

    public boolean isKeepSprint() {
        return this.keepSprint.getValue();
    }

    public boolean isAimThroughWalls() {
        return this.aimThroughWalls.getValue();
    }

    public boolean isIgnoreOpenInventory() {
        return this.ignoreOpenInventory.getValue();
    }

    public boolean isSimulateInventoryClosing() {
        return this.simulateInventoryClosing.getValue();
    }

    public double getInteractionRange() {
        return (Double)this.interactionRange.getValue();
    }

    public double getInteractionThroughWallsRange() {
        return (Double)this.interactionThroughWallsRange.getValue();
    }

    public double getScanRangeIncrease() {
        return (Double)this.scanRangeIncrease.getValue();
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
}

