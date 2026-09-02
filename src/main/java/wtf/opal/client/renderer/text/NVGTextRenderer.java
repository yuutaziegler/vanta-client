/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.lwjgl.nanovg.NVGColor
 *  org.lwjgl.nanovg.NanoVG
 *  org.lwjgl.system.MemoryStack
 */
package wtf.opal.client.renderer.text;

import com.google.common.collect.Lists;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryStack;
import wtf.opal.client.Constants;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.utility.misc.system.IOUtility;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public final class NVGTextRenderer {
    private static final int DEFAULT_ALIGNMENT = 3;
    public static boolean blockTextRendering;
    private final String name;
    private final ByteBuffer fontData;
    private static final int[] COLOR_CODES;
    private static final char COLOR_INVOKER = '\u00a7';
    private static final byte[] CHAR_TO_INDEX;

    public NVGTextRenderer(String name, InputStream inputStream) {
        this.name = name;
        this.fontData = IOUtility.ioResourceToByteBuffer(inputStream, 524288);
        if (this.fontData != null) {
            NanoVG.nvgCreateFontMem((long)Constants.VG, (CharSequence)this.name, (ByteBuffer)this.fontData, (boolean)false);
        }
    }

    public List<String> wrapStringToWidth(String text, float width, float size) {
        float i = 0.0f;
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<String> textList = new ArrayList<String>();
        ArrayList copyList = Lists.newArrayList((Object[])new String[]{text});
        for (int j = 0; j < copyList.size() && j < 1024; ++j) {
            String part = (String)copyList.get(j);
            boolean flag = false;
            if (text.contains("\n")) {
                int newlineIndex = text.indexOf(10);
                String s1 = text.substring(newlineIndex + 1);
                text = text.substring(0, newlineIndex + 1);
                copyList.add(j + 1, s1);
                flag = true;
            }
            String s5 = part.endsWith("\n") ? part.substring(0, part.length() - 1) : part;
            float i1 = this.getStringWidth(s5, size);
            String leftOver = s5;
            if (i + i1 > width) {
                String s3;
                Object s2 = this.trimStringToWidth(part, width - i, size);
                String string = s3 = ((String)s2).length() < part.length() ? part.substring(((String)s2).length()) : null;
                if (s3 != null) {
                    int l = ((String)s2).lastIndexOf(32);
                    if (l >= 0 && this.getStringWidth(part.substring(0, l), size) > 0.0f) {
                        s2 = part.substring(0, l);
                        s3 = part.substring(l);
                    } else if (i > 0.0f && !part.contains(" ")) {
                        s2 = "";
                        s3 = part;
                    }
                    if (!s3.isEmpty() && s3.charAt(0) == ' ') {
                        s2 = (String)s2 + " ";
                        s3 = s3.substring(1);
                    }
                    copyList.add(j + 1, s3);
                }
                i1 = this.getStringWidth((String)s2, size);
                leftOver = s2;
                flag = true;
            }
            if (i + i1 <= width) {
                i += i1;
                stringBuilder.append(leftOver);
            } else {
                flag = true;
            }
            if (!flag) continue;
            textList.add(stringBuilder.toString());
            i = 0.0f;
            stringBuilder = new StringBuilder();
        }
        textList.add(stringBuilder.toString());
        return textList;
    }

    public String trimStringToWidth(String text, float width, float size) {
        StringBuilder stringBuilder = new StringBuilder();
        float f = 0.0f;
        int i = 0;
        int j = 1;
        boolean flag = false;
        boolean flag1 = false;
        for (int k = i; k >= 0 && k < text.length() && f < width; k += j) {
            char character = text.charAt(k);
            float stringWidth = this.getStringWidth(String.valueOf(character), size);
            if (character == '\u00a7') {
                stringBuilder.append(character);
                if (k != text.length() - 1) {
                    stringBuilder.append(text.charAt(k + 1));
                }
                ++k;
                continue;
            }
            if (flag) {
                flag = false;
                if (character != 'l' && character != 'L') {
                    if (character == 'r' || character == 'R') {
                        flag1 = false;
                    }
                } else {
                    flag1 = true;
                }
            } else if (stringWidth < 0.0f) {
                flag = true;
            } else {
                f += stringWidth;
                if (flag1) {
                    f += 1.0f;
                }
            }
            if (f > width) break;
            stringBuilder.append(character);
        }
        return stringBuilder.toString();
    }

    public float drawStringWithShadow(String text, float x, float y, float size, int color) {
        this.drawString(text, x + 0.5f, y + 0.5f, size, color, true, 3);
        return this.drawString(text, x, y, size, color, false, 3);
    }

    public float drawString(String text, float x, float y, float size, int color) {
        return this.drawString(text, x, y, size, color, false, 3);
    }

    public void drawGradientString(String text, float x, float y, float size, int color1, int color2, boolean shadow) {
        if (blockTextRendering) {
            return;
        }
        float offset = 0.0f;
        for (int i = 0; i < text.length(); ++i) {
            char c = text.charAt(i);
            String character = String.valueOf(c);
            float characterWidth = this.getStringWidth(character, size);
            int color = ColorUtility.interpolateColorsBackAndForth(10, i * 15, color1, color2);
            if (shadow) {
                this.drawStringWithShadow(character, x + offset, y, size, color);
            } else {
                this.drawString(character, x + offset, y, size, color);
            }
            offset += characterWidth;
        }
    }

    public void drawGradientString(String text, float x, float y, float size, int color1, int color2) {
        this.drawGradientString(text, x, y, size, color1, color2, false);
    }

    public void drawGradientStringWithShadow(String text, float x, float y, float size, int color1, int color2) {
        this.drawGradientString(text, x, y, size, color1, color2, true);
    }

    public float drawString(String text, float x, float y, float size, int color, boolean shadow, int alignment) {
        if (blockTextRendering) {
            return x;
        }
        NanoVG.nvgBeginPath((long)Constants.VG);
        NanoVG.nvgFontFace((long)Constants.VG, (CharSequence)this.name);
        NanoVG.nvgFontSize((long)Constants.VG, (float)size);
        NanoVG.nvgTextAlign((long)Constants.VG, (int)alignment);
        boolean underline = false;
        boolean strikethrough = false;
        NVGRenderer.applyColor(shadow ? ColorUtility.getShadowColor(color) : color, NVGRenderer.NVG_COLOR_1);
        NanoVG.nvgFillColor((long)Constants.VG, (NVGColor)NVGRenderer.NVG_COLOR_1);
        StringBuilder currentSegment = new StringBuilder();
        for (int i = 0; i < text.length(); ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7' && text.length() > i + 1) {
                int index;
                if (!currentSegment.isEmpty()) {
                    this.drawStringSegment(currentSegment.toString(), x, y, size, underline, strikethrough);
                    x += NanoVG.nvgTextBounds((long)Constants.VG, (float)0.0f, (float)0.0f, (CharSequence)currentSegment.toString(), (FloatBuffer)null);
                    currentSegment.setLength(0);
                }
                if ((index = this.getColorCodeCharacter(Character.toLowerCase(text.charAt(i + 1)))) >= 0) {
                    if (index < 16) {
                        NanoVG.nvgFontFace((long)Constants.VG, (CharSequence)this.name);
                        strikethrough = false;
                        underline = false;
                        int colorCode = COLOR_CODES[index];
                        if (shadow) {
                            colorCode = ColorUtility.getShadowColor(colorCode);
                        }
                        NVGRenderer.applyColor(ColorUtility.applyOpacity(colorCode, color >> 24 & 0xFF), NVGRenderer.NVG_COLOR_2);
                        NanoVG.nvgFillColor((long)Constants.VG, (NVGColor)NVGRenderer.NVG_COLOR_2);
                    } else {
                        switch (index) {
                            case 17: {
                                break;
                            }
                            case 18: {
                                strikethrough = true;
                                break;
                            }
                            case 19: {
                                underline = true;
                                break;
                            }
                            case 20: {
                                break;
                            }
                            default: {
                                strikethrough = false;
                                underline = false;
                                NanoVG.nvgFillColor((long)Constants.VG, (NVGColor)NVGRenderer.NVG_COLOR_1);
                            }
                        }
                    }
                }
                ++i;
                continue;
            }
            currentSegment.append(character);
        }
        if (!currentSegment.isEmpty()) {
            this.drawStringSegment(currentSegment.toString(), x, y, size, underline, strikethrough);
            x += NanoVG.nvgTextBounds((long)Constants.VG, (float)0.0f, (float)0.0f, (CharSequence)currentSegment.toString(), (FloatBuffer)null);
        }
        NanoVG.nvgClosePath((long)Constants.VG);
        return x;
    }

    private int getColorCodeCharacter(char lowerCase) {
        return lowerCase < '\u0080' ? CHAR_TO_INDEX[lowerCase] : -1;
    }

    private void drawStringSegment(String segment, float x, float y, float size, boolean underline, boolean strikethrough) {
        NanoVG.nvgText((long)Constants.VG, (float)x, (float)y, (CharSequence)segment);
        float width = NanoVG.nvgTextBounds((long)Constants.VG, (float)0.0f, (float)0.0f, (CharSequence)segment, (FloatBuffer)null);
        if (strikethrough) {
            float strikeY = y - size * 0.25f;
            NanoVG.nvgBeginPath((long)Constants.VG);
            NanoVG.nvgMoveTo((long)Constants.VG, (float)x, (float)strikeY);
            NanoVG.nvgLineTo((long)Constants.VG, (float)(x + width), (float)(strikeY + 0.5f));
            NanoVG.nvgFill((long)Constants.VG);
            NanoVG.nvgClosePath((long)Constants.VG);
        }
        if (underline) {
            NanoVG.nvgBeginPath((long)Constants.VG);
            NanoVG.nvgMoveTo((long)Constants.VG, (float)x, (float)y);
            NanoVG.nvgLineTo((long)Constants.VG, (float)(x + width), (float)(y + 0.5f));
            NanoVG.nvgFill((long)Constants.VG);
            NanoVG.nvgClosePath((long)Constants.VG);
        }
    }

    public float getStringWidth(String text, float size) {
        NanoVG.nvgFontFace((long)Constants.VG, (CharSequence)this.name);
        NanoVG.nvgFontSize((long)Constants.VG, (float)size);
        StringBuilder currentSegment = new StringBuilder();
        float width = 0.0f;
        int length = text.length();
        for (int i = 0; i < length; ++i) {
            char character = text.charAt(i);
            if (character == '\u00a7' && i < length - 1) {
                ++i;
            } else {
                currentSegment.append(character);
            }
            if ((character != '\u00a7' || i >= length - 1) && i != length - 1 || currentSegment.isEmpty()) continue;
            width += NanoVG.nvgTextBounds((long)Constants.VG, (float)0.0f, (float)0.0f, (CharSequence)currentSegment.toString(), (FloatBuffer)null);
            currentSegment.setLength(0);
        }
        return width;
    }

    public float getStringHeight(String text, float size) {
        NanoVG.nvgFontFace((long)Constants.VG, (CharSequence)this.name);
        NanoVG.nvgFontSize((long)Constants.VG, (float)size);
        try (MemoryStack stack = MemoryStack.stackPush();){
            FloatBuffer bounds = stack.mallocFloat(4);
            NanoVG.nvgTextBounds((long)Constants.VG, (float)0.0f, (float)0.0f, (CharSequence)text, (FloatBuffer)bounds);
            float f = bounds.get(3) - bounds.get(1);
            return f;
        }
    }

    private static void initColorCodes() {
        int i;
        for (int i2 = 0; i2 < 32; ++i2) {
            int amplifier = (i2 >> 3 & 1) * 85;
            int red = (i2 >> 2 & 1) * 170 + amplifier;
            int green = (i2 >> 1 & 1) * 170 + amplifier;
            int blue = (i2 & 1) * 170 + amplifier;
            if (i2 == 6) {
                red += 85;
            }
            if (i2 >= 16) {
                red /= 4;
                green /= 4;
                blue /= 4;
            }
            NVGTextRenderer.COLOR_CODES[i2] = (red & 0xFF) << 16 | (green & 0xFF) << 8 | blue & 0xFF;
        }
        String colorCodeCharacters = "0123456789abcdefklmnor";
        for (i = 0; i < 128; ++i) {
            NVGTextRenderer.CHAR_TO_INDEX[i] = -1;
        }
        for (i = 0; i < "0123456789abcdefklmnor".length(); ++i) {
            char c = "0123456789abcdefklmnor".charAt(i);
            NVGTextRenderer.CHAR_TO_INDEX[c] = (byte)i;
            NVGTextRenderer.CHAR_TO_INDEX[Character.toLowerCase((char)c)] = (byte)i;
        }
    }

    static {
        COLOR_CODES = new int[32];
        CHAR_TO_INDEX = new byte[128];
        NVGTextRenderer.initColorCodes();
    }
}

