/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.util.concurrent.AtomicDouble
 *  com.ibm.icu.impl.Pair
 *  com.mojang.blaze3d.opengl.GlStateManager
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1041
 *  net.minecraft.class_1074
 *  net.minecraft.class_10799
 *  net.minecraft.class_1291
 *  net.minecraft.class_1293
 *  net.minecraft.class_243
 *  net.minecraft.class_2960
 *  net.minecraft.class_329
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_640
 *  net.minecraft.class_6880
 *  net.minecraft.class_9848
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.client;

import com.google.common.util.concurrent.AtomicDouble;
import com.ibm.icu.impl.Pair;
import com.mojang.blaze3d.opengl.GlStateManager;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_1074;
import net.minecraft.class_10799;
import net.minecraft.class_1291;
import net.minecraft.class_1293;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_640;
import net.minecraft.class_6880;
import net.minecraft.class_9848;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.client.ClientElementSettings;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.renderer.MinecraftRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.utility.player.MoveUtility;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public final class ClientElements
implements IOverlayElement {
    private static final NVGTextRenderer BOLD_FONT = FontRepository.getFont("productsans-bold");
    private static final NVGTextRenderer REGULAR_FONT = FontRepository.getFont("productsans-regular");
    private static final float FONT_SIZE = 8.0f;
    private static final float FONT_HEIGHT = REGULAR_FONT.getStringHeight("A", 8.0f);
    private final ClientElementSettings settings;

    public ClientElements(OverlayModule module) {
        this.settings = new ClientElementSettings(module);
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        Pair<Integer, Integer> colors = ColorUtility.getClientTheme();
        MultipleBooleanProperty options = this.settings.getOptions();
        float scale = this.settings.getScale();
        class_1041 window = Constants.mc.method_22683();
        float scaledWidth = window.method_4486();
        float scaledHeight = window.method_4502();
        float x = 2.0f;
        NVGRenderer.scale(scale, 2.0f, scaledHeight - 3.0f, 0.0f, 0.0f, () -> {
            float prefixWidth;
            String prefix;
            float y = scaledHeight - 3.0f;
            if (options.getProperty("Memory") != null && options.getProperty("Memory").getValue().booleanValue()) {
                prefix = this.convertCase("RAM ");
                prefixWidth = BOLD_FONT.getStringWidth(prefix, 8.0f);
                long maxMem = Runtime.getRuntime().maxMemory();
                long totalMem = Runtime.getRuntime().totalMemory();
                long freeMem = Runtime.getRuntime().freeMemory();
                long usedMem = (totalMem - freeMem) / 0x100000L;
                long maxMemMB = maxMem / 0x100000L;
                BOLD_FONT.drawGradientStringWithShadow(prefix, 2.0f, y, 8.0f, (Integer)colors.first, (Integer)colors.second);
                REGULAR_FONT.drawStringWithShadow(usedMem + "MB / " + maxMemMB + "MB", prefixWidth + 2.0f, y, 8.0f, -1);
                y -= FONT_HEIGHT;
            }
            if (options.getProperty("Clock") != null && options.getProperty("Clock").getValue().booleanValue()) {
                prefix = this.convertCase("TIME ");
                prefixWidth = BOLD_FONT.getStringWidth(prefix, 8.0f);
                String timeStr = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                BOLD_FONT.drawGradientStringWithShadow(prefix, 2.0f, y, 8.0f, (Integer)colors.first, (Integer)colors.second);
                REGULAR_FONT.drawStringWithShadow(timeStr, prefixWidth + 2.0f, y, 8.0f, -1);
                y -= FONT_HEIGHT;
            }
            if (options.getProperty("Ping") != null && options.getProperty("Ping").getValue().booleanValue()) {
                class_640 entry;
                prefix = this.convertCase("PING ");
                prefixWidth = BOLD_FONT.getStringWidth(prefix, 8.0f);
                int ping = 0;
                if (Constants.mc.method_1562() != null && Constants.mc.field_1724 != null && (entry = Constants.mc.method_1562().method_2871(Constants.mc.field_1724.method_5667())) != null) {
                    ping = entry.method_2959();
                }
                BOLD_FONT.drawGradientStringWithShadow(prefix, 2.0f, y, 8.0f, (Integer)colors.first, (Integer)colors.second);
                REGULAR_FONT.drawStringWithShadow(ping + "ms", prefixWidth + 2.0f, y, 8.0f, -1);
                y -= FONT_HEIGHT;
            }
            if (options.getProperty("FPS").getValue().booleanValue()) {
                prefix = this.convertCase("FPS ");
                prefixWidth = BOLD_FONT.getStringWidth(prefix, 8.0f);
                BOLD_FONT.drawGradientStringWithShadow(prefix, 2.0f, y, 8.0f, (Integer)colors.first, (Integer)colors.second);
                REGULAR_FONT.drawStringWithShadow(String.valueOf(Constants.mc.method_47599()), prefixWidth + 2.0f, y, 8.0f, -1);
                y -= FONT_HEIGHT;
            }
            if (options.getProperty("BPS").getValue().booleanValue()) {
                prefix = this.convertCase("BPS ");
                prefixWidth = BOLD_FONT.getStringWidth(prefix, 8.0f);
                BOLD_FONT.drawGradientStringWithShadow(prefix, 2.0f, y, 8.0f, (Integer)colors.first, (Integer)colors.second);
                REGULAR_FONT.drawStringWithShadow(String.valueOf(MoveUtility.getBlocksPerSecond()), prefixWidth + 2.0f, y, 8.0f, -1);
                y -= FONT_HEIGHT;
            }
            if (options.getProperty("XYZ").getValue().booleanValue()) {
                prefix = this.convertCase("XYZ ");
                prefixWidth = BOLD_FONT.getStringWidth(prefix, 8.0f);
                BOLD_FONT.drawGradientStringWithShadow(prefix, 2.0f, y, 8.0f, (Integer)colors.first, (Integer)colors.second);
                class_243 pos = Constants.mc.field_1724.method_73189();
                REGULAR_FONT.drawStringWithShadow(String.format("%.0f %.0f %.0f", pos.field_1352, pos.field_1351, pos.field_1350), prefixWidth + 2.0f, y, 8.0f, -1);
            }
        });
        x = scaledWidth - 2.0f;
        AtomicDouble y = new AtomicDouble((double)(scaledHeight - 3.0f));
        if (options.getProperty("Status effects").getValue().booleanValue()) {
            int kx = class_9848.method_61317((float)1.0f);
            Constants.mc.field_1724.method_6088().entrySet().stream().sorted((a, b) -> Float.compare(-REGULAR_FONT.getStringWidth(this.getStatusEffectString((class_1293)a.getValue()), 8.0f), -REGULAR_FONT.getStringWidth(this.getStatusEffectString((class_1293)b.getValue()), 8.0f))).forEach(entry -> {
                class_6880 registryEntry = (class_6880)entry.getKey();
                class_1291 effect = (class_1291)registryEntry.comp_349();
                class_1293 instance = (class_1293)entry.getValue();
                String text = this.getStatusEffectString(instance);
                int textWidth = (int)REGULAR_FONT.getStringWidth(text, 8.0f);
                int effectColor = ColorUtility.applyOpacity(effect.method_5556(), 255);
                float effectY = (float)y.getAndAdd((double)(-(FONT_HEIGHT + 0.5f)));
                REGULAR_FONT.drawStringWithShadow(text, x - (float)textWidth - 1.0f, effectY, 8.0f, effectColor);
                MinecraftRenderer.addToQueue(() -> {
                    class_2960 identifier = class_329.method_71644((class_6880)registryEntry);
                    GlStateManager._enableBlend();
                    context.method_52707(class_10799.field_56883, identifier, (int)(x - (float)textWidth - 12.0f), (int)effectY - 7, 9, 9, kx);
                    GlStateManager._disableBlend();
                });
            });
        }
    }

    private String getStatusEffectString(class_1293 instance) {
        String duration = instance.method_48559() ? "**:**" : this.formatTicks(instance.method_5584());
        return this.convertCase(class_1074.method_4662((String)instance.method_5586(), (Object[])new Object[0])) + (String)(instance.method_5578() > 0 ? " " + (instance.method_5578() + 1) : "") + " \u00a77" + duration;
    }

    private String formatTicks(int ticks) {
        int i = class_3532.method_15375((float)((float)ticks / 20.0f));
        int j = i / 60;
        int k = j / 60;
        return k > 0 ? String.format(Locale.ROOT, "%d:%02d:%02d", k, j, i) : String.format(Locale.ROOT, "%d:%02d", j %= 60, i %= 60);
    }

    private String convertCase(String text) {
        return this.settings.isLowercase() ? text.toLowerCase() : text;
    }

    @Override
    public boolean isActive() {
        return !Constants.mc.method_53526().method_53536();
    }

    @Override
    public boolean isBloom() {
        return false;
    }
}

