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
import wtf.opal.client.feature.module.impl.visual.esp.NameTagIcon;

@Environment(value=EnvType.CLIENT)
record NameTagElement(NameTagIcon icon, String text, int color) {
    NameTagElement(NameTagIcon icon, int color) {
        this(icon, null, color);
    }

    NameTagElement(String text, int color) {
        this(null, text, color);
    }
}

