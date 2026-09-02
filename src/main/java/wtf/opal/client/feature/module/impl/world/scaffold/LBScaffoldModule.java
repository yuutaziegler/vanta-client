/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1268
 *  net.minecraft.class_1297
 *  net.minecraft.class_1747
 *  net.minecraft.class_1799
 *  net.minecraft.class_1922
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2346
 *  net.minecraft.class_2350
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2382
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_3532
 *  net.minecraft.class_3959
 *  net.minecraft.class_3959$class_242
 *  net.minecraft.class_3959$class_3960
 *  net.minecraft.class_3965
 */
package wtf.opal.client.feature.module.impl.world.scaffold;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;
import net.minecraft.class_1297;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1922;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2346;
import net.minecraft.class_2350;
import net.minecraft.class_2382;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3965;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.model.impl.InstantRotationModel;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.world.scaffold.ScaffoldMovementPlanner;
import wtf.opal.client.feature.module.impl.world.scaffold.features.ScaffoldAutoBlockFeature;
import wtf.opal.client.feature.module.impl.world.scaffold.features.ScaffoldEagleFeature;
import wtf.opal.client.feature.module.impl.world.scaffold.features.ScaffoldMovementPrediction;
import wtf.opal.client.feature.module.impl.world.scaffold.tower.ScaffoldTowerMotion;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MoveInputEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.RotationUtility;

@Environment(value=EnvType.CLIENT)
public final class LBScaffoldModule
extends Module {
    private final NumberProperty delayMin = new NumberProperty("Delay Min", 0.0, 0.0, 40.0, 1.0);
    private final NumberProperty delayMax = new NumberProperty("Delay Max", 0.0, 0.0, 40.0, 1.0);
    private final NumberProperty minDist = new NumberProperty("MinDist", 0.0, 0.0, 0.25, 0.01);
    private final NumberProperty timer = new NumberProperty("Timer", 1.0, 0.01, 10.0, 0.01);
    private final ModeProperty<TechniqueMode> technique = new ModeProperty<TechniqueMode>("Technique", TechniqueMode.NORMAL);
    private final ModeProperty<SameYMode> sameYMode = new ModeProperty<SameYMode>("SameY", SameYMode.OFF);
    private final ModeProperty<TowerMode> towerMode = new ModeProperty<TowerMode>("Tower", TowerMode.NONE);
    private final ModeProperty<RotationTiming> rotationTiming = new ModeProperty<RotationTiming>("RotationTiming", RotationTiming.NORMAL);
    private final BooleanProperty considerInventory = new BooleanProperty("ConsiderInventory", false);
    private final BooleanProperty requiresSight = new BooleanProperty("RequiresSight", false);
    private final BooleanProperty safeWalk = new BooleanProperty("SafeWalk", true);
    private final BooleanProperty sprintControl = new BooleanProperty("SprintControl", false);
    private final NumberProperty sprintSpeed = new NumberProperty("SprintSpeed", 0.2, 0.0, 1.0, 0.01);
    private final BooleanProperty speedLimiter = new BooleanProperty("SpeedLimiter", false);
    private final NumberProperty speedLimit = new NumberProperty("SpeedLimit", 0.12, 0.0, 1.0, 0.01);
    private final ModeProperty<EagleMode> eagleMode = new ModeProperty<EagleMode>("Eagle", EagleMode.NORMAL);
    private final BooleanProperty simulatePlacementAttempts = new BooleanProperty("SimulatePlacementAttempts", false);
    private final BooleanProperty autoBlock = new BooleanProperty("AutoBlock", true);
    private final NumberProperty doNotUseBelowCount = new NumberProperty("DoNotUseBelowCount", 0.0, 0.0, 64.0, 1.0);
    private final ModeProperty<SwingMode> swingMode = new ModeProperty<SwingMode>("Swing", SwingMode.CLIENT);
    private int placementY;
    private int startY;
    private int jumps;
    private int delayTicks;
    private class_2338 lastPlaced;
    private Set<class_2248> disallowedBlocks;
    private Set<class_2248> unfavorableBlocks;
    private class_241 currentRotation;
    private final ScaffoldMovementPlanner movementPlanner = new ScaffoldMovementPlanner();
    private final ScaffoldMovementPrediction movementPrediction = new ScaffoldMovementPrediction();
    private final ScaffoldEagleFeature eagleFeature = new ScaffoldEagleFeature();
    private final ScaffoldAutoBlockFeature autoBlockFeature = new ScaffoldAutoBlockFeature();
    private final ScaffoldTowerMotion towerMotion = new ScaffoldTowerMotion();
    private class_243 lastFallOffPosition = null;

    public LBScaffoldModule() {
        super("LBScaffold", "Complete LiquidBounce Scaffold (Core Logic)", ModuleCategory.HACKED);
        this.disallowedBlocks = new HashSet<class_2248>(Arrays.asList(class_2246.field_10375, class_2246.field_10343, class_2246.field_10316));
        this.unfavorableBlocks = new HashSet<class_2248>(Arrays.asList(class_2246.field_9980, class_2246.field_16540, class_2246.field_16329, class_2246.field_16331, class_2246.field_10485, class_2246.field_10593, class_2246.field_10092, class_2246.field_10114, class_2246.field_10030, class_2246.field_21211));
        this.addProperties(new GroupProperty("Core Settings", this.delayMin, this.delayMax, this.minDist, this.timer), new GroupProperty("Mode Selection", this.technique, this.sameYMode, this.towerMode), new GroupProperty("Rotation", this.rotationTiming, this.considerInventory, this.requiresSight), new GroupProperty("Movement", this.safeWalk, this.sprintControl, this.sprintSpeed, this.speedLimiter, this.speedLimit), new GroupProperty("Eagle & Features", this.eagleMode, this.simulatePlacementAttempts), new GroupProperty("Block Selection", this.autoBlock, this.doNotUseBelowCount), this.swingMode);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        if (Constants.mc.field_1724 == null) {
            return;
        }
        this.placementY = Constants.mc.field_1724.method_24515().method_10264() - 1;
        this.startY = Constants.mc.field_1724.method_24515().method_10264();
        this.jumps = 2;
        this.delayTicks = 0;
        this.lastPlaced = null;
        this.currentRotation = null;
        this.lastFallOffPosition = null;
        this.movementPlanner.reset();
        this.movementPrediction.reset();
        this.eagleFeature.setEnabled(this.eagleMode.getValue() != EagleMode.SILENT);
        this.autoBlockFeature.setEnabled(this.autoBlock.getValue());
        this.towerMotion.setMotion(0.42f);
        this.towerMotion.setTriggerHeight(0.78f);
        this.towerMotion.setSlow(1.0f);
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        this.currentRotation = null;
        this.movementPlanner.reset();
        this.movementPrediction.reset();
    }

    @Subscribe
    public void onMoveInput(MoveInputEvent event) {
        if (Constants.mc.field_1724 == null || !this.safeWalk.getValue().booleanValue()) {
            return;
        }
        if (Constants.mc.field_1724.method_24828()) {
            double x = Constants.mc.field_1724.method_23317();
            double y = Constants.mc.field_1724.method_23318() - 1.0;
            double z = Constants.mc.field_1724.method_23321();
            class_2338 below = new class_2338((int)Math.floor(x), (int)Math.floor(y), (int)Math.floor(z));
            if (Constants.mc.field_1687.method_8320(below).method_26215()) {
                event.setSneak(true);
            }
        }
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        class_3965 raycast;
        class_2338 targetPos;
        if (Constants.mc.field_1724 == null || Constants.mc.field_1687 == null) {
            return;
        }
        if (Constants.mc.field_1724.method_24828()) {
            this.placementY = Constants.mc.field_1724.method_24515().method_10264() - 1;
            ++this.jumps;
        }
        if (Constants.mc.field_1690.field_1903.method_1434()) {
            this.startY = Constants.mc.field_1724.method_24515().method_10264();
            this.jumps = 2;
        }
        if (this.delayTicks > 0) {
            --this.delayTicks;
            return;
        }
        int bestSlot = this.findBestBlockSlot();
        if (bestSlot == -1) {
            return;
        }
        if (this.autoBlock.getValue().booleanValue()) {
            SlotHelper.setCurrentItem(bestSlot).silence(SlotHelper.Silence.DEFAULT);
        }
        if ((targetPos = this.getTargetPosition()) == null) {
            return;
        }
        PlacementTarget target = this.findPlacementTarget(targetPos);
        if (target == null) {
            return;
        }
        class_241 rotation = this.calculateRotation(target);
        if (rotation == null) {
            return;
        }
        if (this.requiresSight.getValue().booleanValue() && ((raycast = this.raycastFromPlayer(rotation)) == null || raycast.method_17777() != target.neighbor || raycast.method_17780() != target.direction)) {
            return;
        }
        if (this.rotationTiming.getValue() == RotationTiming.NORMAL) {
            this.applyRotation(rotation);
            this.currentRotation = rotation;
        } else if (this.rotationTiming.getValue() == RotationTiming.ON_TICK || this.rotationTiming.getValue() == RotationTiming.ON_TICK_SNAP) {
            this.applyRotation(rotation);
            this.currentRotation = rotation;
        }
        if (this.placeBlock(target, rotation)) {
            this.lastPlaced = target.placedBlock;
            int min = ((Double)this.delayMin.getValue()).intValue();
            int max = ((Double)this.delayMax.getValue()).intValue();
            this.delayTicks = min + (max > min ? new Random().nextInt(max - min + 1) : 0);
            if (this.rotationTiming.getValue() == RotationTiming.ON_TICK_SNAP) {
                this.applyRotation(rotation);
            }
        }
    }

    private int findBestBlockSlot() {
        int bestSlot = -1;
        int bestCount = 0;
        class_2248 bestBlock = null;
        for (int i = 0; i < 9; ++i) {
            boolean bestIsUnfavorable;
            class_1799 stack = Constants.mc.field_1724.method_31548().method_5438(i);
            if (!this.isValidBlock(stack)) continue;
            class_2248 block = ((class_1747)stack.method_7909()).method_7711();
            int count = stack.method_7947();
            if ((double)count <= (Double)this.doNotUseBelowCount.getValue()) continue;
            boolean isUnfavorable = this.isBlockUnfavorable(block);
            boolean bl = bestIsUnfavorable = bestBlock != null && this.isBlockUnfavorable(bestBlock);
            if (bestSlot != -1 && (isUnfavorable || !bestIsUnfavorable) && (isUnfavorable != bestIsUnfavorable || count <= bestCount)) continue;
            bestSlot = i;
            bestCount = count;
            bestBlock = block;
        }
        return bestSlot;
    }

    private boolean isValidBlock(class_1799 stack) {
        if (stack == null || stack.method_7960()) {
            return false;
        }
        if (!(stack.method_7909() instanceof class_1747)) {
            return false;
        }
        class_2248 block = ((class_1747)stack.method_7909()).method_7711();
        class_2680 state = block.method_9564();
        if (!class_2248.method_9501((class_265)state.method_26220((class_1922)Constants.mc.field_1687, class_2338.field_10980), (class_2350)class_2350.field_11036)) {
            return false;
        }
        if (block instanceof class_2346) {
            return false;
        }
        return !this.disallowedBlocks.contains(block);
    }

    private boolean isBlockUnfavorable(class_2248 block) {
        if (block == null) {
            return true;
        }
        class_2680 state = block.method_9564();
        if (block.method_9499() > 0.6f) {
            return true;
        }
        return this.unfavorableBlocks.contains(block);
    }

    private class_2338 getTargetPosition() {
        class_2338 playerPos = Constants.mc.field_1724.method_24515();
        if (this.towerMode.getValue() != TowerMode.NONE && Constants.mc.field_1690.field_1903.method_1434()) {
            return playerPos.method_10074();
        }
        SameYMode sameY = (SameYMode)((Object)this.sameYMode.getValue());
        switch (sameY.ordinal()) {
            case 1: {
                return playerPos.method_33096(this.placementY);
            }
            case 2: {
                if (Constants.mc.field_1690.field_1903.method_1434()) break;
                return playerPos.method_33096(this.placementY);
            }
            case 3: {
                if (!(Constants.mc.field_1724.method_18798().field_1351 < 0.2)) break;
                return playerPos.method_33096(this.placementY);
            }
            case 4: {
                if (Constants.mc.field_1724.method_18798().field_1351 == -0.15233518685055708 && this.jumps >= 2) {
                    this.jumps = 0;
                    return playerPos.method_33096(this.startY);
                }
                return playerPos.method_33096(this.startY - 1);
            }
        }
        return playerPos.method_10074();
    }

    private PlacementTarget findPlacementTarget(class_2338 targetPos) {
        class_2350[] priorityOrder;
        if (!Constants.mc.field_1687.method_8320(targetPos).method_45474()) {
            return null;
        }
        for (class_2350 dir : priorityOrder = new class_2350[]{class_2350.field_11033, class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034, class_2350.field_11036}) {
            class_2338 neighbor = targetPos.method_10093(dir);
            class_2680 neighborState = Constants.mc.field_1687.method_8320(neighbor);
            if (neighborState.method_45474() || neighborState.method_26215()) continue;
            return new PlacementTarget(targetPos, neighbor, dir.method_10153());
        }
        return null;
    }

    private class_241 calculateRotation(PlacementTarget target) {
        class_243 eyePos = Constants.mc.field_1724.method_33571();
        class_243 targetVec = class_243.method_24953((class_2382)target.neighbor).method_1031((double)target.direction.method_10148() * 0.5, (double)target.direction.method_10164() * 0.5, (double)target.direction.method_10165() * 0.5);
        return RotationUtility.getRotationFromPosition(eyePos, targetVec);
    }

    private class_3965 raycastFromPlayer(class_241 rotation) {
        class_243 start = Constants.mc.field_1724.method_33571();
        class_243 direction = this.getVectorForRotation(rotation);
        class_243 end = start.method_1019(direction.method_1021(4.5));
        return Constants.mc.field_1687.method_17742(new class_3959(start, end, class_3959.class_3960.field_17559, class_3959.class_242.field_1348, (class_1297)Constants.mc.field_1724));
    }

    private class_243 getVectorForRotation(class_241 rotation) {
        float pitch = rotation.field_1342 * ((float)Math.PI / 180);
        float yaw = rotation.field_1343 * ((float)Math.PI / 180);
        float f = class_3532.method_15362((float)(-yaw - (float)Math.PI));
        float g = class_3532.method_15374((float)(-yaw - (float)Math.PI));
        float h = -class_3532.method_15362((float)(-pitch));
        float i = class_3532.method_15374((float)(-pitch));
        return new class_243((double)(g * h), (double)i, (double)(f * h));
    }

    private void applyRotation(class_241 rotation) {
        RotationHelper.getHandler().rotate(rotation, InstantRotationModel.INSTANCE);
    }

    private boolean placeBlock(PlacementTarget target, class_241 rotation) {
        class_3965 hitResult = new class_3965(class_243.method_24953((class_2382)target.neighbor).method_1031((double)target.direction.method_10148() * 0.5, (double)target.direction.method_10164() * 0.5, (double)target.direction.method_10165() * 0.5), target.direction, target.neighbor, false);
        double minD = (Double)this.minDist.getValue();
        if (minD > 0.0) {
            class_243 diff = hitResult.method_17784().method_1020(Constants.mc.field_1724.method_33571());
            class_2350 side = hitResult.method_17780();
            if (side.method_10166() != class_2350.class_2351.field_11052) {
                double dist;
                double d = dist = side == class_2350.field_11043 || side == class_2350.field_11035 ? diff.field_1350 : diff.field_1352;
                if (Math.abs(dist) < minD) {
                    return false;
                }
            }
        }
        Constants.mc.field_1761.method_2896(Constants.mc.field_1724, class_1268.field_5808, hitResult);
        if (this.swingMode.getValue() == SwingMode.CLIENT) {
            Constants.mc.field_1724.method_6104(class_1268.field_5808);
        } else if (this.swingMode.getValue() == SwingMode.SERVER) {
            MouseHelper.getRightButton().setShowSwings(false);
        }
        return true;
    }

    @Environment(value=EnvType.CLIENT)
    public static enum TechniqueMode {
        NORMAL("Normal"),
        EXPAND("Expand"),
        GODBRIDGE("GodBridge"),
        BREEZILY("Breezily");

        private final String name;

        private TechniqueMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum SameYMode {
        OFF("Off"),
        ON("On"),
        JUMP_KEY("JumpKey"),
        FALLING("Falling"),
        HYPIXEL("Hypixel");

        private final String name;

        private SameYMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum TowerMode {
        NONE("None"),
        MOTION("Motion"),
        PULLDOWN("Pulldown"),
        KARHU("Karhu"),
        VULCAN("Vulcan"),
        HYPIXEL("Hypixel");

        private final String name;

        private TowerMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum RotationTiming {
        NORMAL("Normal"),
        ON_TICK("OnTick"),
        ON_TICK_SNAP("OnTickSnap");

        private final String name;

        private RotationTiming(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum EagleMode {
        NORMAL("Normal"),
        EDGE_DISTANCE("EdgeDistance"),
        SILENT("Silent");

        private final String name;

        private EagleMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum SwingMode {
        CLIENT("Client"),
        SERVER("Server"),
        DO_NOT_HIDE("DoNotHide");

        private final String name;

        private SwingMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static class PlacementTarget {
        final class_2338 placedBlock;
        final class_2338 neighbor;
        final class_2350 direction;

        PlacementTarget(class_2338 placedBlock, class_2338 neighbor, class_2350 direction) {
            this.placedBlock = placedBlock;
            this.neighbor = neighbor;
            this.direction = direction;
        }
    }
}

