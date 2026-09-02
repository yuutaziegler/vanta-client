/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_243
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.client.feature.module.impl.combat.killaura.target;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_243;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.target.TargetList;
import wtf.opal.client.feature.helper.impl.target.TargetProperty;
import wtf.opal.client.feature.helper.impl.target.impl.TargetLivingEntity;
import wtf.opal.client.feature.module.impl.combat.killaura.KillAuraSettings;
import wtf.opal.client.feature.module.impl.combat.killaura.target.CurrentTarget;
import wtf.opal.client.feature.module.impl.combat.killaura.target.KillAuraTarget;
import wtf.opal.utility.player.PlayerUtility;
import wtf.opal.utility.player.RaytracedRotation;
import wtf.opal.utility.player.RotationUtility;

@Environment(value=EnvType.CLIENT)
public final class KillAuraTargeting {
    private final KillAuraSettings settings;
    @Nullable
    private CurrentTarget target;
    @Nullable
    private CurrentTarget rotationTarget;
    private double closestDistance = Double.MAX_VALUE;
    @Nullable
    private Map<Integer, KillAuraTarget> targetMap;

    public KillAuraTargeting(KillAuraSettings settings) {
        this.settings = settings;
    }

    public void update() {
        this.closestDistance = Double.MAX_VALUE;
        this.findAttackTarget();
        this.findRotationTarget();
    }

    private void findAttackTarget() {
        double interactionRange = Constants.mc.field_1724.method_55755();
        List<TargetLivingEntity> targets = this.getTargets(interactionRange);
        if (targets == null || targets.isEmpty()) {
            this.target = null;
            return;
        }
        this.target = this.selectTarget(targets, interactionRange, false);
    }

    private void findRotationTarget() {
        if (this.target != null) {
            this.rotationTarget = this.target;
            return;
        }
        double interactionRange = this.settings.getRotationRange();
        List<TargetLivingEntity> targets = this.getTargets(interactionRange);
        if (targets == null || targets.isEmpty()) {
            this.rotationTarget = null;
            return;
        }
        this.rotationTarget = this.selectTarget(targets, interactionRange, true);
    }

    private List<TargetLivingEntity> getTargets(double interactionRange) {
        TargetList targetList = LocalDataWatch.getTargetList();
        if (targetList == null || Constants.mc.field_1724 == null) {
            return null;
        }
        TargetProperty targetProperty = this.settings.getTargetProperty();
        List<TargetLivingEntity> targets = targetList.collectTargets(targetProperty.getTargetFlags(), TargetLivingEntity.class);
        HashMap<Integer, KillAuraTarget> updatedTargetMap = new HashMap<Integer, KillAuraTarget>();
        if (this.targetMap == null) {
            this.targetMap = new HashMap<Integer, KillAuraTarget>();
        }
        Iterator<TargetLivingEntity> iterator = targets.iterator();
        while (iterator.hasNext()) {
            TargetLivingEntity target = iterator.next();
            if (target.isLocal()) {
                iterator.remove();
                continue;
            }
            Object entity = target.getEntity();
            if (entity.method_29504() || !entity.method_5732() || !RotationUtility.isEntityInFOV(entity, this.settings.getFov())) {
                iterator.remove();
                continue;
            }
            if (LocalDataWatch.getFriendList().contains(entity.method_5477().getString().toUpperCase())) {
                iterator.remove();
                continue;
            }
            double distance = PlayerUtility.getDistanceToEntity(entity);
            if (distance < this.closestDistance) {
                this.closestDistance = distance;
            }
            if (distance > interactionRange) {
                iterator.remove();
                continue;
            }
            updatedTargetMap.put(target.getEntityId(), this.getKillAuraTarget(target));
        }
        this.targetMap = updatedTargetMap;
        return targets;
    }

    private CurrentTarget selectTarget(List<TargetLivingEntity> targets, double entityInteractionRange, boolean distanceSorting) {
        targets.sort(Comparator.comparingDouble(t -> {
            double score;
            double d = score = distanceSorting ? t.getEntity().method_5858((class_1297)Constants.mc.field_1724) : (double)t.getFullHealth();
            if (!t.isMatchingFlags(1)) {
                return score * 2.0;
            }
            return score;
        }));
        List<CurrentTarget> targetList = this.convertTargets(targets, entityInteractionRange);
        if (!distanceSorting && this.settings.getMode() == KillAuraSettings.Mode.SWITCH && targetList.size() > 1) {
            targetList.sort(Comparator.comparingDouble(t -> -((double)t.getKillAuraTarget().getTarget().getFullHealth() * 25.0 + (double)(t.getKillAuraTarget().getLastAttackData() == null ? 0L : t.getKillAuraTarget().getLastAttackData().getTime()))));
        }
        if (targetList.isEmpty()) {
            return null;
        }
        return targetList.getFirst();
    }

    @NotNull
    private List<CurrentTarget> convertTargets(List<TargetLivingEntity> targets, double entityInteractionRange) {
        class_243 eyePos = Constants.mc.field_1724.method_33571();
        ArrayList<CurrentTarget> targetList = new ArrayList<CurrentTarget>();
        for (TargetLivingEntity target : targets) {
            class_243 closestVector;
            Object entity = target.getEntity();
            RaytracedRotation tickedRotation = RotationUtility.getRotationFromRaycastedEntity(entity, closestVector = PlayerUtility.getClosestVectorToBoundingBox(eyePos, entity), entityInteractionRange);
            if (tickedRotation == null) continue;
            targetList.add(new CurrentTarget(this.getKillAuraTarget(target), tickedRotation));
        }
        return targetList;
    }

    @NotNull
    private KillAuraTarget getKillAuraTarget(TargetLivingEntity target) {
        if (this.targetMap.containsKey(target.getEntityId())) {
            return this.targetMap.get(target.getEntityId());
        }
        KillAuraTarget killAuraTarget = new KillAuraTarget(target);
        this.targetMap.put(target.getEntityId(), killAuraTarget);
        return killAuraTarget;
    }

    public void reset() {
        this.closestDistance = Double.MAX_VALUE;
        this.targetMap = null;
        this.target = null;
    }

    @Nullable
    public CurrentTarget getTarget() {
        return this.target;
    }

    @Nullable
    public CurrentTarget getRotationTarget() {
        return this.rotationTarget;
    }

    public double getClosestDistance() {
        return this.closestDistance;
    }

    public boolean isTargetSelected() {
        return this.getTarget() != null;
    }
}

