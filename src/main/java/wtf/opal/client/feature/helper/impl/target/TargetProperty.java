/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.target;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.target.TargetFlags;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;

@Environment(value=EnvType.CLIENT)
public final class TargetProperty {
    private final MultipleBooleanProperty property;
    private final boolean allowLocalPlayer;

    public TargetProperty(boolean players, boolean allowLocalPlayer, boolean localPlayer, boolean hostile, boolean passive, boolean friendly) {
        this.allowLocalPlayer = allowLocalPlayer;
        BooleanProperty playersProperty = new BooleanProperty("Players", players);
        BooleanProperty localPlayerProperty = (BooleanProperty)new BooleanProperty("Local player", localPlayer).hideIf(() -> playersProperty.getValue() == false || !allowLocalPlayer);
        this.property = new MultipleBooleanProperty("Targets", playersProperty, new BooleanProperty("Hostile", hostile), new BooleanProperty("Passive", passive), new BooleanProperty("Friendly", friendly), localPlayerProperty);
    }

    public MultipleBooleanProperty get() {
        return this.property;
    }

    public int getTargetFlags() {
        return TargetFlags.get(this.property.getProperty("Players").getValue(), this.property.getProperty("Hostile").getValue(), this.property.getProperty("Passive").getValue(), this.property.getProperty("Friendly").getValue());
    }

    public boolean isLocalPlayer() {
        return this.allowLocalPlayer && this.property.getProperty("Local player").getValue() != false;
    }
}

