/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1297
 */
package wtf.opal.event.impl.game.player.interaction;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1297;

@Environment(value=EnvType.CLIENT)
public final class AttackEvent {
    private final class_1297 target;

    public AttackEvent(class_1297 target) {
        this.target = target;
    }

    public class_1297 getTarget() {
        return this.target;
    }
}

