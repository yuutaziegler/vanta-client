/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.renderer.component;

import com.ibm.icu.impl.Pair;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.ScreenPosition;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class ToggleSwitchComponent
extends ScreenPosition {
    private Animation toggleAnimation;
    private final Runnable toggleAction;
    private final BooleanSupplier stateSupplier;
    private Pair<Integer, Integer> boxColors = Pair.of((Object)-1, (Object)-8288894);

    public ToggleSwitchComponent(Runnable toggleAction, BooleanSupplier stateSupplier) {
        this.toggleAction = toggleAction;
        this.stateSupplier = stateSupplier;
    }

    public void reset() {
        this.toggleAnimation = null;
    }

    public void render(float x, float y, float scale) {
        this.x = x;
        this.y = y;
        this.width = 20.0f;
        this.height = 10.0f;
        NVGRenderer.scale(scale, x, y, this.width, this.height, () -> {
            float destination;
            float f = destination = this.stateSupplier.getAsBoolean() ? 1.0f : 0.0f;
            if (this.toggleAnimation == null) {
                this.toggleAnimation = new Animation(Easing.DECELERATE, 150L);
                this.toggleAnimation.setValue(destination);
            } else {
                this.toggleAnimation.run(destination);
            }
            int color1 = ColorUtility.interpolateColors((Integer)this.boxColors.second, (Integer)this.boxColors.first, this.toggleAnimation.getValue());
            int color2 = ColorUtility.darker(color1, 0.4f);
            NVGRenderer.roundedRectGradient(x, y, this.width, this.height, this.height / 2.0f, color1, color2, 90.0f);
            NVGRenderer.roundedRectGradient(x + 1.0f + this.toggleAnimation.getValue() * 9.5f, y + 1.0f, this.height - 2.0f, this.height - 2.0f, (this.height - 2.0f) / 2.0f, -1, ColorUtility.darker(-1, 0.1f), 90.0f);
        });
    }

    public void setBoxColors(Pair<Integer, Integer> boxColors) {
        this.boxColors = boxColors;
    }

    public void mouseClicked(double mouseX, double mouseY, float scale) {
        if (HoverUtility.isHovering(this.x, this.y, this.width, this.height, mouseX, mouseY, scale)) {
            this.toggle();
        }
    }

    public void mouseClicked(float x, float y, float width, float height, double mouseX, double mouseY) {
        if (HoverUtility.isHovering(x, y, width, height, mouseX, mouseY)) {
            this.toggle();
        }
    }

    public void toggle() {
        if (this.toggleAction != null) {
            this.toggleAction.run();
        }
    }
}

