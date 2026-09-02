/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.feature.module.impl.hud;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.event.impl.render.RenderScreenEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class SimpleHUDModule
extends Module {
    public SimpleHUDModule() {
        super("Simple HUD", "Basic HUD information", ModuleCategory.HUD);
    }

    @Subscribe
    public void onRenderScreen(RenderScreenEvent event) {
        if (Constants.mc.field_1724 == null || Constants.mc.field_1687 == null) {
            return;
        }
        class_332 context = event.drawContext();
        int y = 5;
        int fps = Constants.mc.method_47599();
        int fpsColor = fps >= 60 ? -16711936 : (fps >= 30 ? -256 : -65536);
        context.method_25303(Constants.mc.field_1772, "FPS: " + fps, 5, y, fpsColor);
        String coords = String.format("XYZ: %.1f / %.1f / %.1f", Constants.mc.field_1724.method_23317(), Constants.mc.field_1724.method_23318(), Constants.mc.field_1724.method_23321());
        context.method_25303(Constants.mc.field_1772, coords, 5, y += 12, -1);
        String direction = this.getDirection();
        context.method_25303(Constants.mc.field_1772, "Facing: " + direction, 5, y += 12, -1);
    }

    private String getDirection() {
        float yaw = Constants.mc.field_1724.method_36454() % 360.0f;
        if (yaw < 0.0f) {
            yaw += 360.0f;
        }
        if ((double)yaw >= 337.5 || (double)yaw < 22.5) {
            return "South (+Z)";
        }
        if ((double)yaw < 67.5) {
            return "SW";
        }
        if ((double)yaw < 112.5) {
            return "West (-X)";
        }
        if ((double)yaw < 157.5) {
            return "NW";
        }
        if ((double)yaw < 202.5) {
            return "North (-Z)";
        }
        if ((double)yaw < 247.5) {
            return "NE";
        }
        if ((double)yaw < 292.5) {
            return "East (+X)";
        }
        return "SE";
    }
}

