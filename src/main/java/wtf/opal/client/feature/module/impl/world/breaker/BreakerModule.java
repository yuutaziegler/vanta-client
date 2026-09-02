/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.hypixel.data.type.GameType
 *  net.minecraft.class_1268
 *  net.minecraft.class_1657
 *  net.minecraft.class_1922
 *  net.minecraft.class_2189
 *  net.minecraft.class_2244
 *  net.minecraft.class_2248
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350
 *  net.minecraft.class_239
 *  net.minecraft.class_241
 *  net.minecraft.class_243
 *  net.minecraft.class_2680
 *  net.minecraft.class_3965
 */
package wtf.opal.client.feature.module.impl.world.breaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hypixel.data.type.GameType;
import net.minecraft.class_1268;
import net.minecraft.class_1657;
import net.minecraft.class_1922;
import net.minecraft.class_2189;
import net.minecraft.class_2244;
import net.minecraft.class_2248;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_239;
import net.minecraft.class_241;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.model.impl.InstantRotationModel;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.DynamicIslandElement;
import wtf.opal.client.feature.module.impl.world.breaker.BreakerIsland;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.duck.ClientPlayerInteractionManagerAccess;
import wtf.opal.event.impl.game.PostGameTickEvent;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.player.interaction.CancelBlockBreakingEvent;
import wtf.opal.event.impl.game.player.interaction.VisualSwingEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.PlayerUtility;
import wtf.opal.utility.player.RaycastUtility;
import wtf.opal.utility.player.RotationUtility;

@Environment(value=EnvType.CLIENT)
public final class BreakerModule
extends Module {
    private static final class_2350[] DIRECTIONS = new class_2350[]{class_2350.field_11036, class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034};
    private final ModeProperty<SwingMode> swingMode = new ModeProperty<SwingMode>("Swing mode", SwingMode.CLIENT);
    private final NumberProperty range = new NumberProperty("Range", 4.5, 0.5, 6.0, 0.5);
    private final BooleanProperty breakSurroundings = new BooleanProperty("Break surroundings", true);
    private BlockTarget currentTarget;
    private class_241 rotation;
    private boolean breaking;
    private boolean cancelVisualSwing;
    private int remainingTicks;
    private int slot;
    private long lastBedBreak;
    private final BreakerIsland breakerIsland = new BreakerIsland(this);

    public BreakerModule() {
        super("Breaker", "Breaks relevant blocks for mini-games.", ModuleCategory.WORLD);
        this.addProperties(this.swingMode, this.range, this.breakSurroundings);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        class_3965 hitResult;
        boolean runIsland = false;
        if (!this.shouldRun()) {
            this.breaking = false;
            return;
        }
        this.updateTargetBlock();
        if (this.currentTarget == null || Constants.mc.field_1687.method_8320(this.currentTarget.candidate.getPos()).method_26204() instanceof class_2189) {
            this.breaking = false;
            return;
        }
        class_2338 blockPos = this.currentTarget.candidate.pos;
        ClientPlayerInteractionManagerAccess access = (ClientPlayerInteractionManagerAccess)Constants.mc.field_1761;
        float breakingDelta = Constants.mc.field_1687.method_8320(blockPos).method_26165((class_1657)Constants.mc.field_1724, (class_1922)Constants.mc.field_1687, blockPos);
        float breakingProgress = access.opal$currentBreakingProgress() + breakingDelta;
        this.rotation = RotationUtility.getRotationFromPosition(blockPos.method_46558());
        double value = breakingProgress + breakingDelta;
        if ((value >= 1.0 || breakingProgress - breakingDelta == 0.0f) && value < Double.MAX_VALUE) {
            RotationHelper.getHandler().rotate(this.rotation, InstantRotationModel.INSTANCE);
            if (this.slot != -1) {
                SlotHelper.setCurrentItem(this.slot).silence(SlotHelper.Silence.NONE);
            }
        }
        if ((hitResult = this.getRaycastHitResult()) == null) {
            return;
        }
        class_2350 direction = hitResult.method_17780();
        if (!this.breaking) {
            boolean success = Constants.mc.field_1761.method_2910(blockPos, direction);
            if (!success) {
                return;
            }
            this.remainingTicks = (int)(Constants.mc.field_1687.method_8320(blockPos).method_26214((class_1922)Constants.mc.field_1687, blockPos) * 20.0f);
            this.breaking = true;
        }
        if (Constants.mc.field_1761.method_2902(blockPos, direction)) {
            MouseHelper.getRightButton().setDisabled();
            MouseHelper.getLeftButton().setDisabled();
            --this.remainingTicks;
            this.cancelVisualSwing = this.swingMode.is(SwingMode.SERVER);
            Constants.mc.field_1724.method_6104(class_1268.field_5808);
            runIsland = true;
        }
        if (runIsland) {
            DynamicIslandElement.addTrigger(this.breakerIsland);
        } else {
            DynamicIslandElement.removeTrigger(this.breakerIsland);
            this.breakerIsland.onDisable();
        }
    }

    @Subscribe
    public void onPostGameTick(PostGameTickEvent event) {
        if (!this.breaking || Constants.mc.field_1724 == null) {
            return;
        }
        this.cancelVisualSwing = this.swingMode.is(SwingMode.SERVER);
        Constants.mc.field_1724.method_6104(class_1268.field_5808);
        if (this.remainingTicks < 0) {
            this.breaking = false;
            if (this.currentTarget != null && Constants.mc.field_1687.method_8320(this.currentTarget.candidate.pos).method_26204() instanceof class_2244) {
                this.lastBedBreak = System.currentTimeMillis();
            }
        }
    }

    public BlockTarget getCurrentTarget() {
        return this.currentTarget;
    }

    @Subscribe
    public void onCancelBlockBreaking(CancelBlockBreakingEvent event) {
        if (this.breaking) {
            event.setCancelled();
        }
    }

    @Subscribe
    public void onVisualSwing(VisualSwingEvent event) {
        if (this.cancelVisualSwing) {
            this.cancelVisualSwing = false;
            event.setCancelled();
        }
    }

    private void updateTargetBlock() {
        this.slot = -1;
        class_243 eyePos = Constants.mc.field_1724.method_33571();
        float range = ((Double)this.range.getValue()).floatValue();
        int fromX = (int)Math.floor(eyePos.field_1352 - (double)range - 1.0);
        int fromY = (int)Math.floor(eyePos.field_1351 - (double)range - 1.0);
        int fromZ = (int)Math.floor(eyePos.field_1350 - (double)range - 1.0);
        int toX = (int)Math.ceil(eyePos.field_1352 + (double)range + 1.0);
        int toY = (int)Math.ceil(eyePos.field_1351 + (double)range + 1.0);
        int toZ = (int)Math.ceil(eyePos.field_1350 + (double)range + 1.0);
        ArrayList<BlockCandidate> targetCandidates = new ArrayList<BlockCandidate>();
        HypixelServer.BedColor ownBedColor = LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer ? HypixelServer.BedColor.fromTeamColor(Constants.mc.field_1724.method_22861()) : null;
        for (int x = fromX; x <= toX; ++x) {
            for (int y = fromY; y <= toY; ++y) {
                for (int z = fromZ; z <= toZ; ++z) {
                    class_2338 blockPos = new class_2338(x, y, z);
                    class_2680 class_26802 = Constants.mc.field_1687.method_8320(blockPos);
                    class_2248 class_22482 = class_26802.method_26204();
                    if (!(class_22482 instanceof class_2244)) continue;
                    class_2244 bedBlock = (class_2244)class_22482;
                    if (ownBedColor != null && ownBedColor.mapColorId == bedBlock.method_9487().method_7794().field_16021) continue;
                    BlockCandidate candidate = new BlockCandidate(blockPos);
                    targetCandidates.add(candidate);
                    BlockCandidate otherBedPartCandidate = candidate.offset(class_2244.method_24163((class_2680)class_26802));
                    targetCandidates.add(otherBedPartCandidate);
                }
            }
        }
        BlockCandidate closestCandidate = targetCandidates.stream().filter(c -> c.distance <= (double)range).min(Comparator.comparingDouble(c -> c.distance)).orElse(null);
        if (closestCandidate == null) {
            this.currentTarget = null;
            return;
        }
        if (!this.breakSurroundings.getValue().booleanValue()) {
            this.setTargetBlock(new BlockTarget(closestCandidate, 0.01));
            return;
        }
        List<Object> adjacentCandidates = Arrays.stream(DIRECTIONS).map(closestCandidate::offset).collect(Collectors.toList());
        class_2680 bedState = Constants.mc.field_1687.method_8320(closestCandidate.pos);
        if (bedState.method_26204() instanceof class_2244) {
            BlockCandidate otherBedPart = closestCandidate.offset(class_2244.method_24163((class_2680)bedState));
            Arrays.stream(DIRECTIONS).map(otherBedPart::offset).forEach(adjacentCandidates::add);
        }
        adjacentCandidates = adjacentCandidates.stream().filter(c -> c.distance <= (double)range).sorted(Comparator.comparingDouble(c -> c.distance)).toList();
        for (BlockCandidate blockCandidate : adjacentCandidates) {
            class_2680 blockState = Constants.mc.field_1687.method_8320(blockCandidate.pos);
            if (!blockState.method_26215() && blockState.method_26227().method_15769()) continue;
            this.setTargetBlock(new BlockTarget(closestCandidate, 0.01));
            return;
        }
        BlockCandidate weakestCandidate = null;
        double d = 3.4028234663852886E38;
        int bestSlot = -1;
        for (BlockCandidate blockCandidate : adjacentCandidates) {
            ClientPlayerInteractionManagerAccess access;
            class_2338 currentBreakingPos;
            class_2680 blockState = Constants.mc.field_1687.method_8320(blockCandidate.pos);
            if (blockState.method_26204() instanceof class_2244) continue;
            double fastestMiningSpeed = SlotHelper.getInstance().getMainHandStack(Constants.mc.field_1724).method_7924(blockState);
            int bestSlotForCandidate = SlotHelper.getInstance().getSelectedSlot(Constants.mc.field_1724.method_31548());
            for (int i = 0; i < 9; ++i) {
                float miningSpeed;
                if (i == SlotHelper.getInstance().getSelectedSlot(Constants.mc.field_1724.method_31548()) || !((double)(miningSpeed = Constants.mc.field_1724.method_31548().method_5438(i).method_7924(blockState)) > fastestMiningSpeed)) continue;
                fastestMiningSpeed = miningSpeed;
                bestSlotForCandidate = i;
            }
            double resistance = Math.max(0.01, (double)blockState.method_26214((class_1922)Constants.mc.field_1687, blockCandidate.pos)) / fastestMiningSpeed;
            if (!this.breaking && (currentBreakingPos = (access = (ClientPlayerInteractionManagerAccess)Constants.mc.field_1761).opal$getCurrentBreakingPos()) != null && currentBreakingPos.equals((Object)blockCandidate.pos)) {
                resistance *= (double)(1.0f - access.opal$currentBreakingProgress());
            }
            if (weakestCandidate != null && !(resistance < d)) continue;
            weakestCandidate = blockCandidate;
            d = resistance;
            bestSlot = bestSlotForCandidate;
        }
        if (weakestCandidate == null) {
            return;
        }
        if (System.currentTimeMillis() - this.lastBedBreak < 500L) {
            this.currentTarget = null;
            return;
        }
        this.slot = bestSlot;
        this.setTargetBlock(new BlockTarget(weakestCandidate, d));
    }

    private void setTargetBlock(BlockTarget newTarget) {
        if (this.shouldUpdateTarget(newTarget)) {
            this.currentTarget = newTarget;
        }
    }

    private boolean shouldUpdateTarget(BlockTarget newTarget) {
        if (this.currentTarget == null) {
            return true;
        }
        class_2680 currentBlockState = Constants.mc.field_1687.method_8320(this.currentTarget.candidate.pos);
        if (currentBlockState.method_26215() || !currentBlockState.method_26227().method_15769()) {
            return true;
        }
        if (this.breakSurroundings.getValue().booleanValue() && currentBlockState.method_26204() instanceof class_2244 && !(Constants.mc.field_1687.method_8320(newTarget.candidate.pos).method_26204() instanceof class_2244)) {
            return true;
        }
        this.currentTarget.candidate.updateDistance();
        if (this.currentTarget.candidate.distance > (double)((Double)this.range.getValue()).floatValue()) {
            return true;
        }
        float breakingProgress = ((ClientPlayerInteractionManagerAccess)Constants.mc.field_1761).opal$currentBreakingProgress();
        double remainingResistance = this.currentTarget.resistance * (double)(1.0f - breakingProgress);
        return !(remainingResistance < newTarget.resistance);
    }

    private class_3965 getRaycastHitResult() {
        if (this.rotation == null) {
            return null;
        }
        class_239 hitResult = RaycastUtility.raycastBlock((double)((Double)this.range.getValue()), 1.0f, false, this.rotation.field_1343, this.rotation.field_1342);
        if (!(hitResult instanceof class_3965)) {
            return null;
        }
        class_3965 blockHitResult = (class_3965)hitResult;
        return blockHitResult;
    }

    private boolean shouldRun() {
        HypixelServer.ModAPI.Location currentLocation;
        if (Constants.mc.field_1724 == null) {
            return false;
        }
        return !(LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer) || (currentLocation = HypixelServer.ModAPI.get().getCurrentLocation()) == null || !currentLocation.isLobby() && currentLocation.serverType() != GameType.REPLAY && !"BEDWARS_PRACTICE".equals(currentLocation.mode());
    }

    public boolean isBreaking() {
        return this.breaking;
    }

    public int getSlot() {
        return this.slot;
    }

    @Environment(value=EnvType.CLIENT)
    private static enum SwingMode {
        CLIENT("Client"),
        SERVER("Server");

        private final String name;

        private SwingMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public record BlockTarget(BlockCandidate candidate, double resistance) {
    }

    @Environment(value=EnvType.CLIENT)
    public static class BlockCandidate {
        private final class_2338 pos;
        private double distance;

        private BlockCandidate(class_2338 pos) {
            this.pos = pos;
            this.updateDistance();
        }

        private BlockCandidate offset(class_2350 direction) {
            return new BlockCandidate(this.pos.method_10093(direction));
        }

        private void updateDistance() {
            this.distance = PlayerUtility.getDistanceToBlock(this.pos);
        }

        public class_2338 getPos() {
            return this.pos;
        }
    }
}

