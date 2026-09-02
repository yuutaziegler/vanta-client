/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10182
 *  net.minecraft.class_2709
 */
package wtf.opal.event.impl.game.player.teleport;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10182;
import net.minecraft.class_2709;

@Environment(value=EnvType.CLIENT)
public record PostTeleportEvent(int teleportId, class_10182 change, Set<class_2709> relatives) {
}

