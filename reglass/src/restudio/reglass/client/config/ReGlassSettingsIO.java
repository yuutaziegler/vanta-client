/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  net.fabricmc.loader.api.FabricLoader
 */
package restudio.reglass.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import net.fabricmc.loader.api.FabricLoader;
import restudio.reglass.client.api.ReGlassConfig;

public final class ReGlassSettingsIO {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Path configPath() {
        return FabricLoader.getInstance().getConfigDir().resolve("reglass.json");
    }

    public static void loadIntoMemory() {
        try {
            Path p = ReGlassSettingsIO.configPath();
            if (!Files.exists(p, new LinkOption[0])) {
                ReGlassSettingsIO.saveFromMemory();
                return;
            }
            try (BufferedReader r = Files.newBufferedReader(p);){
                Data d = (Data)GSON.fromJson((Reader)r, Data.class);
                if (d == null) {
                    return;
                }
                ReGlassSettingsIO.apply(d);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    public static void saveFromMemory() {
        try {
            Path p = ReGlassSettingsIO.configPath();
            Data d = ReGlassSettingsIO.snapshot();
            try (BufferedWriter w = Files.newBufferedWriter(p, new OpenOption[0]);){
                GSON.toJson((Object)d, (Appendable)w);
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    private static Data snapshot() {
        ReGlassConfig c = ReGlassConfig.INSTANCE;
        Data d = new Data();
        d.features_enableRedesign = c.features.enableRedesign;
        d.features_buttons = c.features.buttons;
        d.features_sliders = c.features.sliders;
        d.features_hotbar = c.features.hotbar;
        d.features_cancelScreenDarkening = c.features.cancelScreenDarkening;
        d.features_pixelatedGrid = c.features.pixelatedGrid;
        d.tintColor = c.defaultTintColor;
        d.tintAlpha = c.defaultTintAlpha;
        d.smoothing = c.defaultSmoothing;
        d.blurRadius = c.defaultBlurRadius;
        d.shadowExpand = c.defaultShadowExpand;
        d.shadowFactor = c.defaultShadowFactor;
        d.shadowOffsetX = c.defaultShadowOffsetX;
        d.shadowOffsetY = c.defaultShadowOffsetY;
        d.shadowColor = c.defaultShadowColor;
        d.shadowColorAlpha = c.defaultShadowColorAlpha;
        d.refThickness = c.defaultRefThickness;
        d.refFactor = c.defaultRefFactor;
        d.refDispersion = c.defaultRefDispersion;
        d.refFresnelRange = c.defaultRefFresnelRange;
        d.refFresnelHardness = c.defaultRefFresnelHardness;
        d.refFresnelFactor = c.defaultRefFresnelFactor;
        d.glareRange = c.defaultGlareRange;
        d.glareHardness = c.defaultGlareHardness;
        d.glareConvergence = c.defaultGlareConvergence;
        d.glareOppositeFactor = c.defaultGlareOppositeFactor;
        d.glareFactor = c.defaultGlareFactor;
        d.glareAngleRad = c.defaultGlareAngleRad;
        d.pixelatedGridSize = c.pixelatedGridSize;
        d.hoverScalePx = c.hoverScalePx;
        d.focusScalePx = c.focusScalePx;
        d.focusBorderWidthPx = c.focusBorderWidthPx;
        d.focusBorderIntensity = c.focusBorderIntensity;
        d.focusBorderSpeed = c.focusBorderSpeed;
        return d;
    }

    public static void apply(Data d) {
        ReGlassConfig c = ReGlassConfig.INSTANCE;
        c.features.enableRedesign = d.features_enableRedesign;
        c.features.buttons = d.features_buttons;
        c.features.sliders = d.features_sliders;
        c.features.hotbar = d.features_hotbar;
        c.features.cancelScreenDarkening = d.features_cancelScreenDarkening;
        c.features.pixelatedGrid = d.features_pixelatedGrid;
        c.defaultTintColor = d.tintColor;
        c.defaultTintAlpha = d.tintAlpha;
        c.defaultSmoothing = d.smoothing;
        c.defaultBlurRadius = d.blurRadius;
        c.defaultShadowExpand = d.shadowExpand;
        c.defaultShadowFactor = d.shadowFactor;
        c.defaultShadowOffsetX = d.shadowOffsetX;
        c.defaultShadowOffsetY = d.shadowOffsetY;
        c.defaultShadowColor = d.shadowColor;
        c.defaultShadowColorAlpha = d.shadowColorAlpha;
        c.defaultRefThickness = d.refThickness;
        c.defaultRefFactor = d.refFactor;
        c.defaultRefDispersion = d.refDispersion;
        c.defaultRefFresnelRange = d.refFresnelRange;
        c.defaultRefFresnelHardness = d.refFresnelHardness;
        c.defaultRefFresnelFactor = d.refFresnelFactor;
        c.defaultGlareRange = d.glareRange;
        c.defaultGlareHardness = d.glareHardness;
        c.defaultGlareConvergence = d.glareConvergence;
        c.defaultGlareOppositeFactor = d.glareOppositeFactor;
        c.defaultGlareFactor = d.glareFactor;
        c.defaultGlareAngleRad = d.glareAngleRad;
        c.pixelatedGridSize = d.pixelatedGridSize;
        c.hoverScalePx = d.hoverScalePx;
        c.focusScalePx = d.focusScalePx;
        c.focusBorderWidthPx = d.focusBorderWidthPx;
        c.focusBorderIntensity = d.focusBorderIntensity;
        c.focusBorderSpeed = d.focusBorderSpeed;
    }

    public static class Data {
        public boolean features_enableRedesign = true;
        public boolean features_buttons = true;
        public boolean features_sliders = true;
        public boolean features_hotbar = true;
        public boolean features_cancelScreenDarkening = true;
        public boolean features_pixelatedGrid = false;
        public int tintColor = 0;
        public float tintAlpha = 0.0f;
        public float smoothing = 0.003f;
        public int blurRadius = 3;
        public float shadowExpand = 25.0f;
        public float shadowFactor = 0.15f;
        public float shadowOffsetX = 0.0f;
        public float shadowOffsetY = 2.0f;
        public int shadowColor = 0;
        public float shadowColorAlpha = 1.0f;
        public float refThickness = 20.0f;
        public float refFactor = 1.4f;
        public float refDispersion = 7.0f;
        public float refFresnelRange = 30.0f;
        public float refFresnelHardness = 20.0f;
        public float refFresnelFactor = 20.0f;
        public float glareRange = 30.0f;
        public float glareHardness = 20.0f;
        public float glareConvergence = 50.0f;
        public float glareOppositeFactor = 80.0f;
        public float glareFactor = 90.0f;
        public float glareAngleRad = -0.7853982f;
        public float pixelatedGridSize = 8.0f;
        public float hoverScalePx = 1.5f;
        public float focusScalePx = 2.5f;
        public float focusBorderWidthPx = 2.0f;
        public float focusBorderIntensity = 0.75f;
        public float focusBorderSpeed = 1.6f;
    }
}

