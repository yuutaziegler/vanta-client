/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.utility.misc.INameable;

@Environment(value=EnvType.CLIENT)
public enum ModuleCategory implements INameable
{
    COMBAT("Combat", "\ue9e0"),
    MOVEMENT("Movement", "\ue566"),
    VISUAL("Visual", "\ue8f4"),
    WORLD("World", "\ue80b"),
    UTILITY("Utility", "\uea3c"),
    PERFORMANCE("Performance", "\ue8b8"),
    HUD("HUD", "\ue871"),
    HACKED("Hacked", "\ue868"),
    EXPLOIT("Exploit", "\ue868"),
    MISC("Misc", "\ue90f");

    private final String name;
    private final String icon;
    private int moduleIndex;
    public static final ModuleCategory[] VALUES;

    private ModuleCategory(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setModuleIndex(int moduleIndex) {
        this.moduleIndex = moduleIndex;
    }

    public int getModuleIndex() {
        return this.moduleIndex;
    }

    static {
        VALUES = ModuleCategory.values();
    }
}

