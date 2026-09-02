/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class Scroller {
    private final Animation animation = new Animation(Easing.EASE_OUT_EXPO, 250L);
    private float value;

    public Animation getAnimation() {
        return this.animation;
    }

    public void onScroll(float maxOffset) {
        this.value = Math.min(0.0f, Math.max(-maxOffset, this.value));
        this.animation.run(this.value);
    }

    public void addScroll(double verticalScroll, float maxOffset) {
        this.value += (float)(verticalScroll * 50.0);
        this.value = Math.max(-maxOffset, this.value);
        this.value = Math.min(0.0f, this.value);
        this.animation.run(this.value);
    }
}

