/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.combat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;

@Environment(value=EnvType.CLIENT)
public final class ReachModule
extends Module {
    private final NumberProperty entityInteractionRange = new NumberProperty("Entity interaction range", 3.0, 3.0, 6.0, 0.05);
    private final NumberProperty blockInteractionRange = new NumberProperty("Block interaction range", 4.5, 4.5, 6.0, 0.05);

    public ReachModule() {
        super("Reach", "Allows you to interact or attack further.", ModuleCategory.COMBAT);
        this.addProperties(this.entityInteractionRange, this.blockInteractionRange);
    }

    public double getEntityInteractionRange() {
        return (Double)this.entityInteractionRange.getValue();
    }

    public double getBlockInteractionRange() {
        return (Double)this.blockInteractionRange.getValue();
    }
}

