/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_124
 *  org.jetbrains.annotations.NotNull
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.modulelist;

import com.ibm.icu.impl.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_124;
import org.jetbrains.annotations.NotNull;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.modulelist.ToggledSettings;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class ModuleElement
implements Comparable<ModuleElement> {
    private Animation xAnimation;
    private Animation yAnimation;
    private Animation heightAnimation;
    private final ToggledSettings settings;
    private final Module module;
    private static final NVGTextRenderer FONT = FontRepository.getFont("productsans-medium");
    public static final float OFFSET = 12.0f;
    private String text;
    private float width;
    private float posX;
    private float posY;
    private boolean visible;
    private boolean disabled;

    public ModuleElement(ToggledSettings settings, Module module) {
        this.settings = settings;
        this.module = module;
    }

    public void render(int index, boolean isBloom) {
        this.xAnimation.run(this.posX);
        this.yAnimation.run(this.posY);
        this.heightAnimation.run(this.module.isEnabled() ? 1.0f : 0.0f);
        float scale = this.settings.getScale();
        int scaledWidth = Constants.mc.method_22683().method_4486();
        float posX = this.xAnimation.getValue() + (float)scaledWidth;
        float posY = this.yAnimation.getValue();
        float radius = 1.0f;
        Pair<Integer, Integer> colors = ColorUtility.getClientTheme();
        int color = ColorUtility.interpolateColorsBackAndForth(6, index * 20, (Integer)colors.first, (Integer)colors.second);
        NVGRenderer.rect((posX - (float)scaledWidth - 6.5f) * scale + (float)scaledWidth, posY * scale, (this.width + 6.5f) * scale, 12.0f * scale, NVGRenderer.BLUR_PAINT);
        NVGRenderer.scale(scale, scaledWidth, 0.0f, 0.0f, 0.0f, () -> {
            NVGRenderer.rect(posX - 6.5f, posY, this.width + 6.5f, 12.0f, -2146891511);
            ToggledSettings.BarMode barMode = (ToggledSettings.BarMode)((Object)((Object)this.settings.getBarMode().getValue()));
            if (barMode != ToggledSettings.BarMode.NONE) {
                float xOffset = barMode == ToggledSettings.BarMode.LEFT ? -4.5f : this.width - 2.5f;
                NVGRenderer.roundedRect(posX + xOffset + 0.5f, posY + 2.5f, 1.0f, 8.0f, 1.0f, ColorUtility.getShadowColor(color));
                NVGRenderer.roundedRect(posX + xOffset, posY + 2.0f, 1.0f, 8.0f, 1.0f, color);
            }
            float textOffset = barMode == ToggledSettings.BarMode.LEFT ? 2.0f : (barMode == ToggledSettings.BarMode.NONE ? 3.5f : (barMode == ToggledSettings.BarMode.RIGHT ? 4.25f : 0.0f));
            FONT.drawStringWithShadow(this.text, posX - textOffset, posY + 9.0f, 8.0f, color);
        });
    }

    public void tick(int index, boolean visible) {
        this.updateText();
        this.updateVisibility();
        this.updatePosition(index, visible);
    }

    private void updateText() {
        String name = this.module.getName();
        String suffix = this.module.getSuffix();
        this.text = suffix == null || !this.settings.isShowSuffix() ? name : name + " " + String.valueOf(class_124.field_1080) + suffix;
        if (this.settings.isLowercase()) {
            this.text = this.text.toLowerCase();
        }
        this.width = FONT.getStringWidth(this.text, 8.0f);
    }

    private void updateVisibility() {
        if (!this.isModuleVisible()) {
            if (this.visible) {
                if (this.xAnimation != null && this.xAnimation.isFinished() && this.disabled) {
                    this.xAnimation = null;
                    this.yAnimation = null;
                    this.heightAnimation = null;
                    this.visible = false;
                    return;
                }
                this.disabled = true;
            }
        } else {
            this.disabled = false;
        }
    }

    private void updatePosition(int index, boolean visible) {
        this.posX = this.disabled ? 8.0f : -this.width;
        if (visible) {
            this.visible = true;
            this.posY = (float)index * 12.0f;
            if (this.xAnimation == null) {
                this.xAnimation = new Animation(Easing.EASE_OUT_EXPO, 400L);
                this.xAnimation.setValue(8.0f);
            }
            if (this.yAnimation == null) {
                this.yAnimation = new Animation(Easing.EASE_OUT_EXPO, 600L);
                this.yAnimation.setValue(this.posY);
            }
            if (this.heightAnimation == null) {
                this.heightAnimation = new Animation(Easing.EASE_IN_OUT_CUBIC, 200L);
            }
        }
    }

    public boolean isModuleVisible() {
        return this.module.isVisible() && this.module.isEnabled() && this.settings.getVisibleCategories().getProperty(this.module.getCategory().getName()).getValue() != false;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public Animation getHeightAnimation() {
        return this.heightAnimation;
    }

    @Override
    public int compareTo(@NotNull ModuleElement o) {
        return Float.compare(o.width, this.width);
    }

    public Module getModule() {
        return this.module;
    }
}

