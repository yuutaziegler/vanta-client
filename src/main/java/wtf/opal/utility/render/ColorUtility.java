/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.render;

import com.ibm.icu.impl.Pair;
import java.awt.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.utility.misc.math.MathUtility;
import wtf.opal.utility.render.ClientTheme;

@Environment(value=EnvType.CLIENT)
public final class ColorUtility {
    public static final int MUTED_COLOR = -8355712;

    private ColorUtility() {
    }

    public static Pair<Integer, Integer> getClientTheme() {
        OverlayModule overlayModule = OpalClient.getInstance().getModuleRepository().getModule(OverlayModule.class);
        return ((ClientTheme)((Object)overlayModule.getThemeMode().getValue())).getColors();
    }

    public static int getShadowColor(int color) {
        return (color & 0xFCFCFC) >> 2 | color & 0xFF000000;
    }

    public static int[] hexToRGBA(int hex) {
        int red = hex >> 16 & 0xFF;
        int green = hex >> 8 & 0xFF;
        int blue = hex & 0xFF;
        int alpha = hex >> 24 & 0xFF;
        return new int[]{red, green, blue, alpha};
    }

    public static int[] hexToRGB(int hex) {
        int red = hex >> 16 & 0xFF;
        int green = hex >> 8 & 0xFF;
        int blue = hex & 0xFF;
        return new int[]{red, green, blue};
    }

    public static int rgbToHex(int red, int green, int blue) {
        return red << 16 | green << 8 | blue;
    }

    public static int rgbaToHex(int red, int green, int blue, int alpha) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    public static int darker(int color, float factor) {
        float f = 1.0f - factor;
        int r = (int)((float)(color >> 16 & 0xFF) * f);
        int g = (int)((float)(color >> 8 & 0xFF) * f);
        int b = (int)((float)(color & 0xFF) * f);
        int a = color >> 24 & 0xFF;
        return (r & 0xFF) << 16 | (g & 0xFF) << 8 | b & 0xFF | (a & 0xFF) << 24;
    }

    public static int brighter(int color, float factor) {
        float f = 1.0f / (1.0f - factor);
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        int a = color >> 24 & 0xFF;
        if (r == 0 && g == 0 && b == 0) {
            int grey = (int)(1.0 / (1.0 - (double)factor));
            return (a & 0xFF) << 24 | (grey & 0xFF) << 16 | (grey & 0xFF) << 8 | grey & 0xFF;
        }
        int minBrightness = (int)(1.0 / (1.0 - (double)factor));
        int newR = r > 0 && r < minBrightness ? minBrightness : r;
        int newG = g > 0 && g < minBrightness ? minBrightness : g;
        int newB = b > 0 && b < minBrightness ? minBrightness : b;
        newR = Math.min((int)((float)newR * f), 255);
        newG = Math.min((int)((float)newG * f), 255);
        newB = Math.min((int)((float)newB * f), 255);
        return (a & 0xFF) << 24 | (newR & 0xFF) << 16 | (newG & 0xFF) << 8 | newB & 0xFF;
    }

    public static int applyOpacity(int color, float opacityFactor) {
        opacityFactor = Math.min(1.0f, Math.max(0.0f, opacityFactor));
        int[] colorRGBA = ColorUtility.hexToRGBA(color);
        return ColorUtility.rgbaToHex(colorRGBA[0], colorRGBA[1], colorRGBA[2], (int)(opacityFactor * 255.0f));
    }

    public static int applyOpacity(int color, int opacity) {
        opacity = Math.min(255, Math.max(0, opacity));
        int[] colorRGBA = ColorUtility.hexToRGBA(color);
        return ColorUtility.rgbaToHex(colorRGBA[0], colorRGBA[1], colorRGBA[2], opacity);
    }

    public static int interpolateColors(int color1, int color2, float amount) {
        amount = Math.min(1.0f, Math.max(0.0f, amount));
        int[] color1RGBA = ColorUtility.hexToRGBA(color1);
        int[] color2RGBA = ColorUtility.hexToRGBA(color2);
        int r = (int)MathUtility.interpolate((float)color1RGBA[0], (float)color2RGBA[0], amount);
        int g = (int)MathUtility.interpolate((float)color1RGBA[1], (float)color2RGBA[1], amount);
        int b = (int)MathUtility.interpolate((float)color1RGBA[2], (float)color2RGBA[2], amount);
        int a = (int)MathUtility.interpolate((float)color1RGBA[3], (float)color2RGBA[3], amount);
        return ColorUtility.rgbaToHex(r, g, b, a);
    }

    public static int rainbow(int speed, int index, float saturation, float brightness) {
        int angle = (int)((System.currentTimeMillis() / (long)speed + (long)index) % 360L);
        float hue = (float)angle / 360.0f;
        return Color.HSBtoRGB(hue, saturation, brightness);
    }

    public static int interpolateColorsBackAndForth(int speed, int index, int startColor, int endColor) {
        int angle = (int)((System.currentTimeMillis() / (long)speed - (long)index) % 360L);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return ColorUtility.interpolateColors(startColor, endColor, (float)angle / 360.0f);
    }
}

