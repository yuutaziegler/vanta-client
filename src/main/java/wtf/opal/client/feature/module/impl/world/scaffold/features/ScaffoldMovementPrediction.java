/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_310
 *  net.minecraft.class_638
 *  net.minecraft.class_746
 */
package wtf.opal.client.feature.module.impl.world.scaffold.features;

import java.util.ArrayDeque;
import java.util.Deque;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_310;
import net.minecraft.class_638;
import net.minecraft.class_746;
import wtf.opal.client.feature.module.impl.world.scaffold.ScaffoldMovementPlanner;
import wtf.opal.client.feature.module.impl.world.scaffold.util.Line;
import wtf.opal.client.feature.module.impl.world.scaffold.util.SupportReference;

@Environment(value=EnvType.CLIENT)
public class ScaffoldMovementPrediction {
    private static final int MAX_PLACEMENT_OFFSETS = 4;
    private final Deque<class_243> lastPlacementOffsets = new ArrayDeque<class_243>(5);
    private float bootstrapBackoff = 0.2f;
    private float predictionCutoffDistance = 0.05f;
    private int warmupPlacements = 2;
    private boolean enabled = true;

    public void reset() {
        this.lastPlacementOffsets.clear();
    }

    public void onPlace(Line optimalLine, class_243 lastFallOffPosition, ScaffoldMovementPlanner planner) {
        if (optimalLine == null || !this.enabled || lastFallOffPosition == null) {
            return;
        }
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            return;
        }
        class_243 fallOffPoint = lastFallOffPosition;
        double lineDirAngle = Math.atan2(optimalLine.getDirection().field_1350, optimalLine.getDirection().field_1352);
        class_243 unrotatedOffset = this.yRot(new class_243(player.method_23317(), player.method_23318(), player.method_23321()).method_1020(fallOffPoint), (float)lineDirAngle);
        this.lastPlacementOffsets.addLast(unrotatedOffset);
        if (this.lastPlacementOffsets.size() > 4) {
            this.lastPlacementOffsets.removeFirst();
        }
    }

    public class_243 getAvgPlacementPos() {
        if (this.lastPlacementOffsets.isEmpty()) {
            return null;
        }
        class_243 sum = class_243.field_1353;
        for (class_243 offset : this.lastPlacementOffsets) {
            sum = sum.method_1019(offset);
        }
        return sum.method_1021(1.0 / (double)this.lastPlacementOffsets.size());
    }

    public class_243 getPredictedPlacementPos(Line optimalLine, ScaffoldMovementPlanner planner) {
        if (optimalLine == null || !this.enabled) {
            return null;
        }
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            return null;
        }
        if (this.isCloseToEdge(player, this.predictionCutoffDistance)) {
            return null;
        }
        class_243 fallOffPoint = this.getFallOffPositionOnLine(optimalLine);
        if (fallOffPoint == null) {
            return null;
        }
        class_243 playerPos = new class_243(player.method_23317(), player.method_23318(), player.method_23321());
        class_243 fallOffPointToPlayer = fallOffPoint.method_1020(playerPos);
        class_243 bootstrapPos = this.getBootstrapPlacementPos(fallOffPoint, fallOffPointToPlayer);
        class_243 last = this.getAvgPlacementPos();
        if (last == null) {
            SupportReference ref = planner.getCurrentSupportReference();
            if (ref != null) {
                return bootstrapPos.method_1031(ref.getOffsetX(), 0.0, ref.getOffsetZ());
            }
            return bootstrapPos;
        }
        double lineDirAngle = Math.atan2(optimalLine.getDirection().field_1350, optimalLine.getDirection().field_1352);
        class_243 predictedPos = fallOffPoint.method_1019(this.yRot(last, -((float)lineDirAngle)));
        return bootstrapPos.method_35590(predictedPos, this.getWarmupBlendFactor());
    }

    public class_243 getFallOffPositionOnLine(Line optimalLine) {
        class_243 toLine;
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            return null;
        }
        class_243 nearestPosToPlayer = optimalLine.getNearestPointTo(new class_243(player.method_23317(), player.method_23318(), player.method_23321()));
        class_243 fromLine = nearestPosToPlayer.method_1031(0.0, -0.1, 0.0);
        class_243 edgeCollision = this.findEdgeCollision(fromLine, toLine = fromLine.method_1019(optimalLine.getDirection().method_1021(3.0)));
        if (edgeCollision == null) {
            return null;
        }
        return new class_243(edgeCollision.field_1352, player.method_23318(), edgeCollision.field_1350);
    }

    private class_243 getBootstrapPlacementPos(class_243 fallOffPoint, class_243 fallOffPointToPlayer) {
        if (this.bootstrapBackoff <= 0.0f) {
            return fallOffPoint;
        }
        return fallOffPoint.method_1020(this.withLength(fallOffPointToPlayer, this.bootstrapBackoff));
    }

    private double getWarmupBlendFactor() {
        if (this.warmupPlacements <= 0) {
            return 1.0;
        }
        return Math.min(1.0, (double)this.lastPlacementOffsets.size() / (double)this.warmupPlacements);
    }

    private boolean isCloseToEdge(class_746 player, double distance) {
        class_310 mc = class_310.method_1551();
        class_638 world = mc.field_1687;
        class_238 box = player.method_5829();
        class_238 shrunkBox = box.method_1011(distance);
        return !world.method_18026(shrunkBox.method_989(0.0, -0.5, 0.0));
    }

    private class_243 findEdgeCollision(class_243 from, class_243 to) {
        class_310 mc = class_310.method_1551();
        class_746 player = mc.field_1724;
        if (player == null) {
            return null;
        }
        class_238 box = player.method_5829();
        class_243 direction = to.method_1020(from).method_1029();
        for (double step = 0.0; step <= from.method_1022(to); step += 0.1) {
            class_243 testPos = from.method_1019(direction.method_1021(step));
            class_238 testBox = box.method_997(testPos.method_1020(new class_243(player.method_23317(), player.method_23318(), player.method_23321())));
            if (mc.field_1687.method_18026(testBox.method_989(0.0, -0.5, 0.0))) continue;
            return testPos;
        }
        return null;
    }

    private class_243 yRot(class_243 vec, float angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        return new class_243(vec.field_1352 * cos - vec.field_1350 * sin, vec.field_1351, vec.field_1352 * sin + vec.field_1350 * cos);
    }

    private class_243 withLength(class_243 vec, double length) {
        double currentLength = vec.method_1033();
        if (currentLength == 0.0) {
            return vec;
        }
        return vec.method_1021(length / currentLength);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return this.enabled;
    }
}

