/*
 * Custom Cape Module - Set any cape from the game by name
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2960;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.StringProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;

@Environment(value=EnvType.CLIENT)
public final class CustomCapeModule extends Module {
    
    private final ModeProperty<CapeMode> mode = new ModeProperty<CapeMode>("Mode", CapeMode.PRESET);
    private final StringProperty customName = new StringProperty("Cape Name", "Minecon 2015");
    private final BooleanProperty glowEffect = new BooleanProperty("Glow Effect", false);
    
    // All available capes in Minecraft
    public static final String[] AVAILABLE_CAPES = {
        "TerentX", "Cobalt", "Migrator", "Minecon 2011", "Minecon 2012",
        "Minecon 2013", "Minecon 2015", "Minecon 2016", "Mojang Studios", "Mojang",
        "Dinnerbone", "Grumm", "Karl", "Semantic", "Paul", "RSS",
        "Labymod", "Lunar Client", "CheatBreaker", "Hyperium", "Sigma",
        "Wynncraft", "Hive", "CubeCraft", "Hypixel", "2b2t"
    };
    
    public CustomCapeModule() {
        super("Custom Cape", "Set any cape from the game or enter a custom name", ModuleCategory.VISUAL);
        this.addProperties(
            this.mode,
            this.customName,
            this.glowEffect
        );
    }

    public CapeMode getMode() {
        return this.mode.getValue();
    }

    public String getCustomName() {
        return this.customName.getValue();
    }

    public boolean isGlowEffect() {
        return this.glowEffect.getValue();
    }

    public class_2960 getCapeIdentifier() {
        String name;
        switch (this.mode.getValue()) {
            case PRESET:
                name = this.customName.getValue().toLowerCase().replace(' ', '-');
                break;
            case CUSTOM:
            default:
                name = this.customName.getValue().toLowerCase().replace(' ', '-');
                break;
        }
        return class_2960.method_60655("terentx", "capes/" + name + ".png");
    }

    @Override
    public String getSuffix() {
        return this.mode.getValue().toString() + " - " + this.customName.getValue();
    }

    @Environment(value=EnvType.CLIENT)
    public static enum CapeMode {
        PRESET("Preset"),
        CUSTOM("Custom Name");

        private final String name;

        private CapeMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}
