/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.event.impl.game.input;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class MouseUpdateEvent {
    private double deltaX;
    private double deltaY;
    private final double sensitivityMultiplier;
    private final boolean unlockCursorRun;
    private boolean handled;

    public MouseUpdateEvent(double deltaX, double deltaY, double sensitivityMultiplier, boolean unlockCursorRun) {
        this.sensitivityMultiplier = sensitivityMultiplier;
        this.deltaY = deltaY;
        this.deltaX = deltaX;
        this.unlockCursorRun = unlockCursorRun;
    }

    public double getDeltaX() {
        return this.deltaX;
    }

    public void setDeltaX(double deltaX) {
        this.deltaX = deltaX;
    }

    public double getDeltaY() {
        return this.deltaY;
    }

    public void setDeltaY(double deltaY) {
        this.deltaY = deltaY;
    }

    public double getSensitivityMultiplier() {
        return this.sensitivityMultiplier;
    }

    public boolean isHandled() {
        return this.handled;
    }

    public void setHandled() {
        this.handled = true;
    }

    public boolean isUnlockCursorRun() {
        return this.unlockCursorRun;
    }
}

