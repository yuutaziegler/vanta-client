/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.IslandTrigger;

@Environment(value=EnvType.CLIENT)
public interface CustomIslandTrigger
extends IslandTrigger {
    public float getIslandX();

    public float getIslandY();
}

