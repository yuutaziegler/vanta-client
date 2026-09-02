/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.world.scaffold.features;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class ScaffoldAutoBlockFeature {
    private boolean enabled = true;
    private boolean alwaysHoldBlock = false;
    private int slotResetDelay = 5;
    private int doNotUseBelowCount = 1;

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isAlwaysHoldBlock() {
        return this.alwaysHoldBlock;
    }

    public void setAlwaysHoldBlock(boolean alwaysHoldBlock) {
        this.alwaysHoldBlock = alwaysHoldBlock;
    }

    public int getSlotResetDelay() {
        return this.slotResetDelay;
    }

    public void setSlotResetDelay(int slotResetDelay) {
        this.slotResetDelay = slotResetDelay;
    }

    public int getDoNotUseBelowCount() {
        return this.doNotUseBelowCount;
    }

    public void setDoNotUseBelowCount(int doNotUseBelowCount) {
        this.doNotUseBelowCount = doNotUseBelowCount;
    }
}

