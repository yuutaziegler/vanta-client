/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual.esp;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.impl.visual.esp.NameTagIconPosition;

@Environment(value=EnvType.CLIENT)
record NameTagIcon(String unicode, NameTagIconPosition position, float horizontalOffset) {
    NameTagIcon(String unicode) {
        this(unicode, NameTagIconPosition.RIGHT, 0.5f);
    }

    NameTagIcon(String unicode, float horizontalOffset) {
        this(unicode, NameTagIconPosition.RIGHT, horizontalOffset);
    }

    NameTagIcon(String unicode, NameTagIconPosition position) {
        this(unicode, position, 0.5f);
    }
}

