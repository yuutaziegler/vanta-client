/*
 * Custom Cape Module
 *  - Preset:  any cape bundled with the client (by display name)
 *  - Custom:  any cape asset name (terentx:capes/<name>.png)
 *  - Player:  any cape/skin from anywhere in the game, fetched from Mojang by username
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import wtf.opal.client.feature.helper.impl.skin.SkinFetcher;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.StringProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;

@Environment(value=EnvType.CLIENT)
public final class CustomCapeModule extends Module {

    private final ModeProperty<CapeMode> mode = new ModeProperty<CapeMode>("Mode", CapeMode.PRESET);
    private final StringProperty capeName = new StringProperty("Cape Name", "terentx");
    private final StringProperty playerName = new StringProperty("Player Name", "");
    private final BooleanProperty applyPlayerSkin = new BooleanProperty("Also Apply Skin", true);
    private final BooleanProperty glowEffect = new BooleanProperty("Glow Effect", false);

    /** All capes bundled with the client (assets/terentx/capes/<name>.png). */
    public static final String[] AVAILABLE_CAPES = {
        "terentx", "cobalt", "migrator", "mojang", "mojang_studios",
        "minecon_2011", "minecon_2012", "minecon_2013", "minecon_2015", "minecon_2016",
        "senoe", "prismarine", "edge", "firefox", "billyk_",
        "exhibition_1", "exhibition_2", "ketamine_1", "ketamine_2"
    };

    public CustomCapeModule() {
        super("Custom Cape", "Wear any cape from the game - pick a bundled cape, type any name, or steal any player's cape/skin by username", ModuleCategory.VISUAL);
        this.addProperties(
            this.mode,
            new GroupProperty("Bundled / Custom", this.capeName),
            new GroupProperty("From Player", this.playerName, this.applyPlayerSkin),
            this.glowEffect
        );
    }

    public CapeMode getMode() {
        return this.mode.getValue();
    }

    public boolean isGlowEffect() {
        return this.glowEffect.getValue();
    }

    private static String slug(String name) {
        if (name == null) {
            return "";
        }
        return name.trim().toLowerCase().replace(' ', '_');
    }

    /** Cape texture to override with, or null to keep the current one. */
    public class_2960 getCapeOverride() {
        if (!this.isEnabled()) {
            return null;
        }
        switch ((CapeMode)this.mode.getValue()) {
            case PRESET:
            case CUSTOM: {
                String name = slug(this.capeName.getValue());
                if (name.isEmpty()) {
                    return null;
                }
                return class_2960.method_60655("terentx", "capes/" + name + ".png");
            }
            case PLAYER: {
                String name = this.playerName.getValue();
                if (name == null || name.trim().isEmpty()) {
                    return null;
                }
                SkinFetcher.request(name);
                return SkinFetcher.getCapeTexture(name);
            }
        }
        return null;
    }

    /** Skin texture to override with, or null to keep the current one. */
    public class_2960 getSkinOverride() {
        if (!this.isEnabled() || !this.applyPlayerSkin.getValue()) {
            return null;
        }
        if (this.mode.getValue() != CapeMode.PLAYER) {
            return null;
        }
        String name = this.playerName.getValue();
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        SkinFetcher.request(name);
        return SkinFetcher.getSkinTexture(name);
    }

    @Override
    public String getSuffix() {
        switch ((CapeMode)this.mode.getValue()) {
            case PLAYER:
                return "Player - " + this.playerName.getValue();
            default:
                return this.mode.getValue().toString() + " - " + this.capeName.getValue();
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum CapeMode {
        PRESET("Preset"),
        CUSTOM("Custom Name"),
        PLAYER("From Player");

        private final String name;

        private CapeMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}
