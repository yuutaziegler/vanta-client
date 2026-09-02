/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2561
 */
package wtf.opal.event.impl.game.chat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;
import wtf.opal.event.EventCancellable;

@Environment(value=EnvType.CLIENT)
public final class ChatReceivedEvent
extends EventCancellable {
    private final class_2561 text;
    private boolean overlay;

    public ChatReceivedEvent(class_2561 text, boolean overlay) {
        this.text = text;
        this.overlay = overlay;
    }

    public class_2561 getText() {
        return this.text;
    }

    public boolean isOverlay() {
        return this.overlay;
    }

    public void setOverlay(boolean overlay) {
        this.overlay = overlay;
    }
}

