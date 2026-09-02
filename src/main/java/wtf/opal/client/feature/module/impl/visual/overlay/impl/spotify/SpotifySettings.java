/*
 * Spotify Overlay Settings with multiple layouts
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.spotify;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;

@Environment(value=EnvType.CLIENT)
public class SpotifySettings {
    
    private final BooleanProperty enabled = new BooleanProperty("Enabled", true);
    private final ModeProperty<SpotifyLayout> layout = new ModeProperty<SpotifyLayout>("Layout", SpotifyLayout.COMPACT);
    private final BooleanProperty showProgressBar = new BooleanProperty("Progress Bar", true);
    private final BooleanProperty showAlbumArt = new BooleanProperty("Album Art", true);
    private final BooleanProperty animatedProgress = new BooleanProperty("Animated Progress", true);
    private final NumberProperty progressUpdateInterval = new NumberProperty("Update Interval (ms)", 1000.0, 500.0, 5000.0, 100.0);
    private final ScreenPositionProperty screenPosition = new ScreenPositionProperty("Screen Position", 0.75f, 0.02f);
    
    public SpotifySettings(OverlayModule module) {
        module.addProperties(new GroupProperty("Spotify", 
            this.enabled, 
            this.layout,
            this.showProgressBar,
            this.showAlbumArt,
            this.animatedProgress,
            this.progressUpdateInterval,
            this.screenPosition
        ));
    }

    public boolean isEnabled() {
        return this.enabled.getValue();
    }

    public SpotifyLayout getLayout() {
        return this.layout.getValue();
    }

    public boolean isShowProgressBar() {
        return this.showProgressBar.getValue();
    }

    public boolean isShowAlbumArt() {
        return this.showAlbumArt.getValue();
    }

    public boolean isAnimatedProgress() {
        return this.animatedProgress.getValue();
    }

    public int getProgressUpdateInterval() {
        return this.progressUpdateInterval.getValue().intValue();
    }

    public ScreenPositionProperty getScreenPosition() {
        return this.screenPosition;
    }

    @Environment(value=EnvType.CLIENT)
    public enum SpotifyLayout {
        COMPACT("Compact"),
        DETAILED("Detailed"),
        WIDE("Wide"),
        MINIMAL("Minimal"),
        VERTICAL("Vertical");

        private final String name;

        private SpotifyLayout(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}
