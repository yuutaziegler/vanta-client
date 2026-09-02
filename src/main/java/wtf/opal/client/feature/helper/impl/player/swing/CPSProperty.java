/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.player.swing;

import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;

@Environment(value=EnvType.CLIENT)
public final class CPSProperty {
    private final BooleanProperty modernDelay;
    private final NumberProperty delay;
    private final GroupProperty groupProperty;
    private long nextClick;

    public CPSProperty(Module parent) {
        this(parent, "CPS", true);
    }

    public CPSProperty(Module parent, String groupName, boolean allowModernDelay) {
        this.modernDelay = allowModernDelay ? new BooleanProperty("Modern delay", false) : null;
        this.delay = (NumberProperty)new NumberProperty("CPS", 10.0, 1.0, 20.0, 1.0).hideIf(this::isModernDelay);
        this.groupProperty = new GroupProperty(groupName, this.modernDelay, this.delay);
        parent.addProperties(this.groupProperty);
    }

    public CPSProperty hideIf(BooleanSupplier hiddenSupplier) {
        this.groupProperty.hideIf(hiddenSupplier);
        return this;
    }

    public boolean isModernDelay() {
        return this.modernDelay != null && this.modernDelay.getValue() != false;
    }

    public int getCPS() {
        return ((Double)this.delay.getValue()).intValue();
    }

    public int getClickDelay() {
        return 1000 / this.getCPS();
    }

    public void resetClick() {
        this.nextClick = this.getClickDelay();
    }

    public long getNextClick() {
        return this.nextClick;
    }
}

