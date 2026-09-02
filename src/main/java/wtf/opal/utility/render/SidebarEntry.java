/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2561
 */
package wtf.opal.utility.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;

@Environment(value=EnvType.CLIENT)
public record SidebarEntry(class_2561 name, class_2561 score, int scoreWidth) {
}

