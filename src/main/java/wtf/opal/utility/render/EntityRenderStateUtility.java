/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10034
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.utility.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10034;
import org.jetbrains.annotations.Nullable;

@Environment(value=EnvType.CLIENT)
public final class EntityRenderStateUtility {
    private static final ThreadLocal<class_10034> HUMAN_RENDER_STATE = ThreadLocal.withInitial(() -> null);

    private EntityRenderStateUtility() {
    }

    @Nullable
    public static class_10034 getHumanRenderState() {
        return HUMAN_RENDER_STATE.get();
    }

    public static void setHumanRenderState(class_10034 state) {
        HUMAN_RENDER_STATE.set(state);
    }

    public static void clearHumanRenderState() {
        HUMAN_RENDER_STATE.remove();
    }
}

