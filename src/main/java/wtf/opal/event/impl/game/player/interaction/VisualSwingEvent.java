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
import wtf.opal.event.EventCancellable;

@Environment(value=EnvType.CLIENT)
public final class VisualSwingEvent
extends EventCancellable {
    public final class_1268 hand;

    public VisualSwingEvent(class_1268 hand) {
        this.hand = hand;
    }

    public class_1268 getHand() {
        return this.hand;
    }
}

