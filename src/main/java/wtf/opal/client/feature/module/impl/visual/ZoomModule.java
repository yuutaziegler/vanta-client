/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class ZoomModule
extends Module {
    private final NumberProperty zoomLevel = new NumberProperty("Zoom Level", 4.0, 2.0, 10.0, 0.5);
    private final BooleanProperty smooth = new BooleanProperty("Smooth", true);
    private final NumberProperty smoothSpeed = new NumberProperty("Smooth Speed", 0.2, 0.05, 1.0, 0.05);
    private double currentFov = 90.0;
    private double targetFov = 90.0;
    private double originalFov = 90.0;

    public ZoomModule() {
        super("Zoom", "Zoom like OptiFine", ModuleCategory.VISUAL);
        this.addProperties(new GroupProperty("Settings", this.zoomLevel, this.smooth, this.smoothSpeed));
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        if (Constants.mc.field_1690 == null) {
            return;
        }
        this.originalFov = ((Integer)Constants.mc.field_1690.method_41808().method_41753()).intValue();
        this.targetFov = this.originalFov / (Double)this.zoomLevel.getValue();
        if (!this.smooth.getValue().booleanValue()) {
            this.currentFov = this.targetFov;
            Constants.mc.field_1690.method_41808().method_41748((Object)((int)this.currentFov));
        }
    }

    @Override
    protected void onDisable() {
        super.onDisable();
        if (Constants.mc.field_1690 == null) {
            return;
        }
        Constants.mc.field_1690.method_41808().method_41748((Object)((int)this.originalFov));
    }

    @Subscribe
    public void onTick(PreGameTickEvent event) {
        if (Constants.mc.field_1724 == null || Constants.mc.field_1690 == null) {
            return;
        }
        this.targetFov = this.originalFov / (Double)this.zoomLevel.getValue();
        if (this.smooth.getValue().booleanValue()) {
            double speed = (Double)this.smoothSpeed.getValue();
            this.currentFov += (this.targetFov - this.currentFov) * speed;
            Constants.mc.field_1690.method_41808().method_41748((Object)((int)this.currentFov));
        } else {
            Constants.mc.field_1690.method_41808().method_41748((Object)((int)this.targetFov));
        }
    }
}

