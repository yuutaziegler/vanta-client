/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_243
 */
package wtf.opal.event.impl.game.player.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_243;

@Environment(value=EnvType.CLIENT)
public final class PostMoveEvent {
    private final float speed;
    private final class_243 movementInput;

    public PostMoveEvent(float speed, class_243 movementInput) {
        this.speed = speed;
        this.movementInput = movementInput;
    }

    public float getSpeed() {
        return this.speed;
    }

    public class_243 getMovementInput() {
        return this.movementInput;
    }
}

