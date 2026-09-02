/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1922
 *  net.minecraft.class_1937
 *  net.minecraft.class_2338
 *  net.minecraft.class_238
 *  net.minecraft.class_2382
 *  net.minecraft.class_243
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_310
 *  net.minecraft.class_3532
 *  net.minecraft.class_638
 *  net.minecraft.class_746
 */
package wtf.opal.client.feature.module.impl.world.scaffold;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1922;
import net.minecraft.class_1937;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_2382;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_310;
import net.minecraft.class_3532;
import net.minecraft.class_638;
import net.minecraft.class_746;
import wtf.opal.client.feature.module.impl.world.scaffold.util.Line;
import wtf.opal.client.feature.module.impl.world.scaffold.util.SupportReference;

@Environment(value=EnvType.CLIENT)
public class ScaffoldMovementPlanner {
    private static final int MAX_LAST_PLACE_BLOCKS = 4;
    private static final float DIRECTION_HYSTERESIS_DEGREES = 30.0f;
    private static final double SUPPORT_SURFACE_EPSILON = 0.001;
    private static final double SUPPORT_OVERLAP_HYSTERESIS = 0.02;
    private final Deque<class_2338> lastPlacedBlocks = new ArrayDeque<class_2338>(4);
    private class_2338 lastPosition = null;
    private SupportReference lastSupportReference = null;
    private float lastDirectionAngle = Float.NaN;
    private static final double[] OFFSETS_TO_TRY = new double[]{0.301, 0.0, -0.301};

    public Line getOptimalMovementLine(class_243 inputDirection) {
        class_243 lineAnchor;
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            return null;
        }
        class_243 direction = this.chooseDirection(this.getYawFromDirection(inputDirection));
        SupportReference supportRef = this.findSupportReferenceUnderPlayer();
        if (supportRef == null) {
            return null;
        }
        this.lastSupportReference = supportRef;
        Line lastBlocksLine = this.fitLinesThroughLastPlacedBlocks();
        if (lastBlocksLine != null && !this.divergesTooMuchFromDirection(lastBlocksLine, direction)) {
            lineAnchor = lastBlocksLine.getNearestPointTo(new class_243(player.method_23317(), player.method_23318(), player.method_23321()));
        } else {
            class_2338 pos = supportRef.getBlockPos();
            lineAnchor = new class_243((double)pos.method_10263() + 0.5 + supportRef.getOffsetX(), player.method_23318(), (double)pos.method_10260() + 0.5 + supportRef.getOffsetZ());
        }
        return new Line(new class_243(lineAnchor.field_1352, player.method_23318(), lineAnchor.field_1350), direction);
    }

    private boolean divergesTooMuchFromDirection(Line lastBlocksLine, class_243 direction) {
        return lastBlocksLine.getDirection().method_1026(direction) < 0.5;
    }

    private Line fitLinesThroughLastPlacedBlocks() {
        if (this.lastPlacedBlocks.size() < 2) {
            return null;
        }
        class_2338 last = (class_2338)((ArrayDeque)this.lastPlacedBlocks).getLast();
        class_2338 secondToLast = ((ArrayDeque)this.lastPlacedBlocks).toArray(new class_2338[0])[this.lastPlacedBlocks.size() - 2];
        class_243 lastCenter = class_243.method_24955((class_2382)last);
        class_243 secondToLastCenter = class_243.method_24955((class_2382)secondToLast);
        class_243 avgPos = lastCenter.method_1019(secondToLastCenter).method_1021(0.5);
        class_243 dir = lastCenter.method_1020(secondToLastCenter).method_1029();
        return new Line(avgPos, dir);
    }

    private SupportReference findSupportReferenceUnderPlayer() {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            return null;
        }
        Map<class_2338, SupportCandidate> candidates = this.collectSupportCandidates();
        if (candidates.isEmpty()) {
            this.lastSupportReference = null;
            this.lastPosition = null;
            return null;
        }
        SupportCandidate bestCandidate = Collections.min(candidates.values());
        SupportCandidate chosenCandidate = this.chooseStableSupportCandidate(candidates, bestCandidate);
        this.lastPosition = chosenCandidate.blockPos;
        return new SupportReference(chosenCandidate.blockPos, player.method_23317() - ((double)chosenCandidate.blockPos.method_10263() + 0.5), player.method_23321() - ((double)chosenCandidate.blockPos.method_10260() + 0.5));
    }

    private Map<class_2338, SupportCandidate> collectSupportCandidates() {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        class_638 world = mc.field_1687;
        if (player == null || world == null) {
            return Collections.emptyMap();
        }
        HashMap<class_2338, SupportCandidate> candidates = new HashMap<class_2338, SupportCandidate>();
        for (double xOffset : OFFSETS_TO_TRY) {
            for (double zOffset : OFFSETS_TO_TRY) {
                class_2680 state;
                class_265 shape;
                class_2338 blockPos = class_2338.method_49637((double)(player.method_23317() + xOffset), (double)(player.method_23318() - 1.0), (double)(player.method_23321() + zOffset));
                if (candidates.containsKey(blockPos) || (shape = (state = world.method_8320(blockPos)).method_26220((class_1922)world, blockPos)).method_1110()) continue;
                candidates.put(blockPos, this.createSupportCandidate(blockPos, player, (class_1937)world));
            }
        }
        return candidates;
    }

    private SupportCandidate chooseStableSupportCandidate(Map<class_2338, SupportCandidate> candidates, SupportCandidate bestCandidate) {
        class_2338 lastPlaced = this.lastPlacedBlocks.isEmpty() ? null : (class_2338)((ArrayDeque)this.lastPlacedBlocks).getLast();
        SupportCandidate preferredLastPlaced = candidates.get(lastPlaced);
        SupportCandidate preferredLastPosition = candidates.get(this.lastPosition);
        if (preferredLastPlaced != null && preferredLastPlaced.isStableComparedTo(bestCandidate)) {
            return preferredLastPlaced;
        }
        if (preferredLastPosition != null && preferredLastPosition.isStableComparedTo(bestCandidate)) {
            return preferredLastPosition;
        }
        return bestCandidate;
    }

    private SupportCandidate createSupportCandidate(class_2338 blockPos, class_746 player, class_1937 world) {
        class_238 playerBox = player.method_5829();
        double horizontalDistSqr = new class_243(player.method_23317(), player.method_23318(), player.method_23321()).method_1028((double)blockPos.method_10263() + 0.5, player.method_23318(), (double)blockPos.method_10260() + 0.5);
        return new SupportCandidate(blockPos, 1.0, 0.1, horizontalDistSqr);
    }

    private class_243 chooseDirection(float currentAngle) {
        float newDirectionAngle;
        if (!Float.isNaN(this.lastDirectionAngle) && Math.abs(class_3532.method_15393((float)(currentAngle - this.lastDirectionAngle))) <= 30.0f) {
            return class_243.method_1030((float)0.0f, (float)this.lastDirectionAngle);
        }
        float currentDirection = currentAngle / 180.0f * 4.0f + 4.0f;
        float newDirectionNumber = Math.round(currentDirection);
        this.lastDirectionAngle = newDirectionAngle = class_3532.method_15393((float)((newDirectionNumber - 4.0f) / 4.0f * 180.0f));
        return class_243.method_1030((float)0.0f, (float)newDirectionAngle);
    }

    private float getYawFromDirection(class_243 direction) {
        return (float)Math.toDegrees(Math.atan2(direction.field_1350, direction.field_1352)) - 90.0f;
    }

    public void trackPlacedBlock(class_2338 target) {
        if (!this.lastPlacedBlocks.isEmpty() && target.equals(((ArrayDeque)this.lastPlacedBlocks).getLast())) {
            return;
        }
        while (this.lastPlacedBlocks.size() >= 4) {
            this.lastPlacedBlocks.removeFirst();
        }
        this.lastPlacedBlocks.add(target);
    }

    public void reset() {
        this.lastPosition = null;
        this.lastSupportReference = null;
        this.lastDirectionAngle = Float.NaN;
        this.lastPlacedBlocks.clear();
    }

    public SupportReference getCurrentSupportReference() {
        return this.lastSupportReference;
    }

    @Environment(value=EnvType.CLIENT)
    private static class SupportCandidate
    implements Comparable<SupportCandidate> {
        final class_2338 blockPos;
        final double overlapArea;
        final double surfaceDelta;
        final double horizontalDistanceToPlayerSqr;

        SupportCandidate(class_2338 blockPos, double overlapArea, double surfaceDelta, double horizontalDistSqr) {
            this.blockPos = blockPos;
            this.overlapArea = overlapArea;
            this.surfaceDelta = surfaceDelta;
            this.horizontalDistanceToPlayerSqr = horizontalDistSqr;
        }

        boolean isStableComparedTo(SupportCandidate best) {
            if (this.surfaceDelta > best.surfaceDelta + 0.001) {
                return false;
            }
            return !(this.overlapArea + 0.02 < best.overlapArea);
        }

        @Override
        public int compareTo(SupportCandidate other) {
            if (this.surfaceDelta + 0.001 < other.surfaceDelta) {
                return -1;
            }
            if (other.surfaceDelta + 0.001 < this.surfaceDelta) {
                return 1;
            }
            if (this.overlapArea > other.overlapArea + 0.02) {
                return -1;
            }
            if (this.overlapArea + 0.02 < other.overlapArea) {
                return 1;
            }
            if (this.horizontalDistanceToPlayerSqr < other.horizontalDistanceToPlayerSqr) {
                return -1;
            }
            if (this.horizontalDistanceToPlayerSqr > other.horizontalDistanceToPlayerSqr) {
                return 1;
            }
            return 0;
        }
    }
}

