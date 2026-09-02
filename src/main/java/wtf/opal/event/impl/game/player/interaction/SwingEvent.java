/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1268
 */
package wtf.opal.event.impl.game.player.interaction;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1268;

@Environment(value=EnvType.CLIENT)
public record SwingEvent(class_1268 hand) {
}

