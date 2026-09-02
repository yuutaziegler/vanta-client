/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.event.impl.press;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
class LWJGLInteractionEvent {
    private final int interactionCode;

    protected LWJGLInteractionEvent(int interactionCode) {
        this.interactionCode = interactionCode;
    }

    public int getInteractionCode() {
        return this.interactionCode;
    }
}

