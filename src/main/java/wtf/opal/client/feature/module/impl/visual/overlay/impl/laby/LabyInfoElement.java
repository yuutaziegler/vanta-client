/*
 * LabyMod / Lunar style info HUD - toggleable lines with
 * Coordinates, FPS, Ping, BPS and Clock.
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.laby;

import java.time.LocalTime;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import net.minecraft.class_640;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.HudSettingsModule;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.renderer.LiquidGlassRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class LabyInfoElement implements IOverlayElement {
    private static final NVGTextRenderer MED_FONT = FontRepository.getFont("inter-medium");
    private static final NVGTextRenderer BOLD_FONT = FontRepository.getFont("inter-bold");

    private final BooleanProperty enabled = new BooleanProperty("Enabled", false);
    private final BooleanProperty showCoordinates = new BooleanProperty("Coordinates", true);
    private final BooleanProperty showFps = new BooleanProperty("FPS", true);
    private final BooleanProperty showPing = new BooleanProperty("Ping", true);
    private final BooleanProperty showBps = new BooleanProperty("Speed (BPS)", false);
    private final BooleanProperty showClock = new BooleanProperty("Clock", true);
    private final ScreenPositionProperty position = new ScreenPositionProperty("Screen Position", 0.02f, 0.30f);

    public LabyInfoElement(OverlayModule module) {
        module.addProperties(new GroupProperty("Info HUD",
            this.enabled,
            new GroupProperty("Lines", this.showCoordinates, this.showFps, this.showPing, this.showBps, this.showClock),
            this.position
        ));
    }

    @Override
    public ScreenPositionProperty getPositionProperty() {
        return this.position;
    }

    @Override
    public boolean isActive() {
        return this.enabled.getValue();
    }

    @Override
    public boolean isBloom() {
        return false;
    }

    private int getPing() {
        try {
            if (Constants.mc.field_1724 == null || Constants.mc.method_1562() == null) {
                return -1;
            }
            class_640 entry = Constants.mc.method_1562().method_2871(Constants.mc.field_1724.method_5667());
            return entry == null ? -1 : entry.method_2959();
        } catch (Throwable t) {
            return -1;
        }
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        if (Constants.mc.field_1724 == null) {
            return;
        }

        // Build the visible lines
        String[] lines = new String[5];
        int count = 0;

        if (this.showCoordinates.getValue()) {
            lines[count] = String.format("XYZ %.0f / %.0f / %.0f",
                Double.valueOf(Constants.mc.field_1724.method_23317()),
                Double.valueOf(Constants.mc.field_1724.method_23318()),
                Double.valueOf(Constants.mc.field_1724.method_23321()));
            ++count;
        }
        if (this.showFps.getValue()) {
            lines[count] = Constants.mc.field_1738 + " FPS";
            ++count;
        }
        if (this.showPing.getValue()) {
            int ping = this.getPing();
            if (ping >= 0) {
                lines[count] = ping + " ms";
                ++count;
            }
        }
        if (this.showBps.getValue()) {
            lines[count] = MoveUtility.getBlocksPerSecond() + " BPS";
            ++count;
        }
        if (this.showClock.getValue()) {
            LocalTime now = LocalTime.now();
            lines[count] = String.format("%02d:%02d:%02d", Integer.valueOf(now.getHour()), Integer.valueOf(now.getMinute()), Integer.valueOf(now.getSecond()));
            ++count;
        }
        if (count == 0) {
            return;
        }

        float lineH = 11.0f;
        float padding = 7.0f;
        float width = 20.0f;
        for (int i = 0; i < count; ++i) {
            width = Math.max(width, MED_FONT.getStringWidth(lines[i], 6.5f) + padding * 2.0f);
        }
        float height = padding * 2.0f + count * lineH + 8.0f;
        this.position.setWidth(width);
        this.position.setHeight(height);

        float x = this.position.getScaledX();
        float y = this.position.getScaledY();
        HudSettingsModule hudSettings = OpalClient.getInstance().getModuleRepository().getModule(HudSettingsModule.class);
        float radius = hudSettings != null && hudSettings.isEnabled() ? hudSettings.getCornerRadius() : 8.0f;
        LiquidGlassRenderer.drawGlassPanel(x, y, width, height, radius);

        BOLD_FONT.drawString("INFO", x + padding, y + padding + 5.0f, 5.5f, 0xFF22AA66);
        for (int i = 0; i < count; ++i) {
            MED_FONT.drawString(lines[i], x + padding, y + padding + 16.0f + i * lineH, 6.5f, -1);
        }
    }
}
