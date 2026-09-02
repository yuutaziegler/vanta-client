/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_241
 *  net.minecraft.class_310
 */
package wtf.opal.client.feature.helper.impl.player.rotation.handler;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_241;
import net.minecraft.class_310;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.handler.ClientRotationHandler;
import wtf.opal.client.feature.helper.impl.player.rotation.model.IRotationModel;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MouseUpdateEvent;
import wtf.opal.event.subscriber.IEventSubscriber;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.RotationUtility;

@Environment(value=EnvType.CLIENT)
public final class RotationMouseHandler
implements IEventSubscriber {
    private IRotationModel rotationModel;
    private class_241 targetRotation;
    private boolean active;
    private boolean forward;
    private class_241 tickRotation;
    private boolean ticked;
    private boolean unlockCursor;

    public RotationMouseHandler() {
        EventDispatcher.subscribe(this);
    }

    @Subscribe
    public void onMouseUpdate(MouseUpdateEvent event) {
        float tickDelta;
        if (this.tickRotation == null || this.targetRotation == null || Constants.mc.field_1724 == null || !this.active) {
            this.ticked = false;
            return;
        }
        if (!this.forward) {
            this.resetToClient();
            if (this.targetRotation == null) {
                this.ticked = false;
                return;
            }
        }
        if (this.ticked) {
            tickDelta = 1.0f;
            this.ticked = false;
        } else {
            tickDelta = class_310.method_1551().method_61966().method_60637(false);
        }
        double sensitivityMultiplier = event.getSensitivityMultiplier();
        class_241 tickedRotation = this.rotationModel.tick(this.tickRotation, this.targetRotation, tickDelta);
        double deltaYaw = tickedRotation.field_1343 - Constants.mc.field_1724.method_36454();
        double cursorDeltaX = RotationUtility.getCursorDelta(deltaYaw, sensitivityMultiplier);
        double deltaPitch = tickedRotation.field_1342 - Constants.mc.field_1724.method_36455();
        double cursorDeltaY = RotationUtility.getCursorDelta(deltaPitch, sensitivityMultiplier);
        if (((Boolean)Constants.mc.field_1690.method_42438().method_41753()).booleanValue()) {
            cursorDeltaY *= -1.0;
        }
        event.setDeltaX(cursorDeltaX);
        event.setDeltaY(cursorDeltaY);
        event.setHandled();
        if (!this.forward && (double)RotationUtility.getRotationDifference(tickedRotation, this.targetRotation) == 0.0) {
            this.rotationModel = null;
            this.targetRotation = null;
            this.active = false;
        }
    }

    @Subscribe(priority=8)
    public void onPreTick(PreGameTickEvent event) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        this.forceTick();
        this.reverse();
        this.setTickRotation(RotationUtility.getRotation());
        this.unlockCursor = false;
    }

    public boolean isUnlockCursor() {
        return this.unlockCursor && this.ticked;
    }

    public void setTickRotation(class_241 tickRotation) {
        this.tickRotation = tickRotation;
    }

    public void reverse() {
        if (this.forward) {
            this.resetToClient();
            this.forward = false;
        }
    }

    private void forceTick() {
        if (this.active) {
            this.ticked = true;
            Constants.mc.field_1729.method_55793();
            this.ticked = false;
        }
    }

    private void resetToClient() {
        ClientRotationHandler clientHandler = RotationHelper.getClientHandler();
        this.targetRotation = clientHandler.getRotation();
    }

    public void rotate(class_241 targetRotation, IRotationModel rotationModel) {
        this.targetRotation = targetRotation;
        this.rotationModel = rotationModel;
        this.forward = true;
        this.active = true;
        this.forceTick();
    }

    public void unlockCursor() {
        this.unlockCursor = true;
    }

    public class_241 getTargetRotation() {
        return this.targetRotation;
    }

    public IRotationModel getRotationModel() {
        return this.rotationModel;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isForward() {
        return this.forward;
    }
}

