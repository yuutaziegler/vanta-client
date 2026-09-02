/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 *  org.jetbrains.annotations.NotNull
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import org.jetbrains.annotations.NotNull;

@Environment(value=EnvType.CLIENT)
public interface IslandTrigger
extends Comparable<IslandTrigger> {
    public void renderIsland(class_332 var1, float var2, float var3, float var4, float var5, float var6);

    public float getIslandWidth();

    public float getIslandHeight();

    default public int getIslandPriority() {
        return 0;
    }

    @Override
    default public int compareTo(@NotNull IslandTrigger o) {
        return Integer.compare(o.getIslandPriority(), this.getIslandPriority());
    }
}

