/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual;

import com.ibm.icu.impl.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public final class ChamsModule
extends Module {
    private final BooleanProperty keepTextures = new BooleanProperty("Keep textures", false);
    private final BooleanProperty colorOverlay = new BooleanProperty("Color overlay", true);

    public ChamsModule() {
        super("Chams", "A form of wall-hack that allows you see players through walls.", ModuleCategory.VISUAL);
        this.addProperties(this.keepTextures, this.colorOverlay);
    }

    public int getRGBAColor() {
        if (!this.colorOverlay.getValue().booleanValue()) {
            return -1;
        }
        Pair<Integer, Integer> colors = ColorUtility.getClientTheme();
        return ColorUtility.interpolateColorsBackAndForth(10, 1, (Integer)colors.first, (Integer)colors.second);
    }

    public boolean isColorOverlay() {
        return this.colorOverlay.getValue();
    }

    public boolean shouldKeepTextures() {
        return this.keepTextures.getValue();
    }
}

