/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.client.feature.helper.impl.target;

import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.KnownServer;
import wtf.opal.client.feature.helper.impl.target.impl.Target;
import wtf.opal.client.feature.helper.impl.target.impl.TargetLivingEntity;
import wtf.opal.client.feature.helper.impl.target.impl.TargetPlayer;

@Environment(value=EnvType.CLIENT)
public final class TargetList {
    private final Map<Integer, Target<?>> targetMap = new HashMap();

    public void tick() {
        if (Constants.mc.field_1687 == null) {
            this.targetMap.clear();
            return;
        }
        this.removeInvalidTargets();
        this.addTargets();
    }

    private void removeInvalidTargets() {
        Iterator<Target<?>> iterator = this.targetMap.values().iterator();
        while (iterator.hasNext()) {
            Target<?> target = iterator.next();
            Object entity = target.getEntity();
            if (Constants.mc.field_1687.method_62145(entity) && entity.method_5805() && ((class_1309)entity).field_6213 <= 0) continue;
            iterator.remove();
        }
    }

    private void addTargets() {
        KnownServer currentServer = LocalDataWatch.get().getKnownServerManager().getCurrentServer();
        for (class_1297 entity : Constants.mc.field_1687.method_18112()) {
            if (!(entity instanceof class_1309)) continue;
            class_1309 livingEntity = (class_1309)entity;
            if (currentServer != null && !currentServer.isValidTarget(livingEntity)) continue;
            int entityId = entity.method_5628();
            Object target = this.getTarget(entityId, null);
            if (target == null) {
                target = this.createNewTarget(entity);
                this.targetMap.put(entityId, (Target<?>)target);
            }
            ((Target)target).setEntity(entity);
        }
    }

    @NotNull
    private Target<?> createNewTarget(class_1297 entity) {
        class_1297 class_12972 = entity;
        Objects.requireNonNull(class_12972);
        class_1297 class_12973 = class_12972;
        int n = 0;
        return switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{class_1657.class, class_1309.class}, (Object)class_12973, n)) {
            case 0 -> {
                class_1657 playerEntity = (class_1657)class_12973;
                yield new TargetPlayer(playerEntity);
            }
            case 1 -> {
                class_1309 livingEntity = (class_1309)class_12973;
                yield new TargetLivingEntity(livingEntity);
            }
            default -> throw new RuntimeException("This should never happen!");
        };
    }

    public <T extends Target<?>> T getTarget(int entityId, @Nullable Class<T> clazz) {
        Target<?> target = this.targetMap.get(entityId);
        if (clazz == null || clazz.isInstance(target)) {
            return (T)target;
        }
        return null;
    }

    public boolean hasTarget(int entityId) {
        return this.targetMap.containsKey(entityId);
    }

    public <T extends Target<?>> List<T> collectTargets(int flags, @Nullable Class<T> clazzType) {
        Iterator<Target<?>> iterator = this.targetMap.values().iterator();
        ArrayList list = new ArrayList();
        while (iterator.hasNext()) {
            Target<?> target = iterator.next();
            if (clazzType != null && !clazzType.isAssignableFrom(target.getClass()) || !target.isMatchingFlags(flags)) continue;
            list.add(target);
        }
        return list;
    }
}

