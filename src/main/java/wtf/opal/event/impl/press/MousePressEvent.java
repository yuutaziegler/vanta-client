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
import wtf.opal.event.impl.press.LWJGLInteractionEvent;

@Environment(value=EnvType.CLIENT)
public final class MousePressEvent
extends LWJGLInteractionEvent {
    public MousePressEvent(int mouseKeyCode) {
        super(mouseKeyCode);
    }
}

