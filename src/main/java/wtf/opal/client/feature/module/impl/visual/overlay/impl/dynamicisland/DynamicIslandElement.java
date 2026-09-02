/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland;

import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.CustomIslandTrigger;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.IslandTrigger;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.preset.DefaultIsland;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.client.ModuleToggleEvent;
import wtf.opal.event.subscriber.IEventSubscriber;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class DynamicIslandElement
implements IOverlayElement,
IEventSubscriber {
    private static final List<IslandTrigger> ACTIVE_TRIGGERS = Lists.newArrayList((Object[])new IslandTrigger[]{new DefaultIsland()});
    private final OverlayModule module;
    private boolean positioned;
    private static boolean SORTING_DIRTY;
    private final Animation xAnimation = new Animation(Easing.DYNAMIC_ISLAND, 250L);
    private final Animation yAnimation = new Animation(Easing.DYNAMIC_ISLAND, 250L);
    private final Animation widthAnimation = new Animation(Easing.DYNAMIC_ISLAND, 250L);
    private final Animation heightAnimation = new Animation(Easing.DYNAMIC_ISLAND, 250L);

    public DynamicIslandElement(OverlayModule module) {
        this.module = module;
        EventDispatcher.subscribe(this);
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        boolean custom;
        float y;
        float x;
        if (SORTING_DIRTY) {
            this.sort();
        }
        IslandTrigger trigger = this.getDecidingTrigger();
        float width = trigger.getIslandWidth();
        float height = trigger.getIslandHeight();
        if (trigger instanceof CustomIslandTrigger) {
            CustomIslandTrigger customTrigger = (CustomIslandTrigger)trigger;
            x = customTrigger.getIslandX();
            y = customTrigger.getIslandY();
            custom = true;
        } else {
            x = this.module.isDynamicIslandLeftAligned() ? 4.0f : ((float)Constants.mc.method_22683().method_4486() - width) / 2.0f;
            y = this.module.isDynamicIslandLeftAligned() ? 6.0f : 10.0f;
            custom = false;
        }
        this.updateAnimations(x, y, width, height);
        float animatedX = this.xAnimation.getValue();
        float animatedY = this.yAnimation.getValue();
        float animatedWidth = this.widthAnimation.getValue();
        float animatedHeight = this.heightAnimation.getValue();
        float progress = Math.min(1.0f, this.heightAnimation.getProgress());
        Runnable render = () -> trigger.renderIsland(context, animatedX, animatedY, animatedWidth, animatedHeight, progress);
        if (custom) {
            render.run();
        } else {
            this.renderIslandBackground(animatedX, animatedY, animatedWidth, animatedHeight);
            if (!(trigger instanceof DefaultIsland)) {
                NVGRenderer.globalAlpha(progress);
            }
            NVGRenderer.scissor(animatedX, animatedY, animatedWidth, animatedHeight, render);
            NVGRenderer.globalAlpha(1.0f);
        }
    }

    @Override
    public void onResize() {
        this.positioned = false;
    }

    @Subscribe
    public void onModuleToggle(ModuleToggleEvent event) {
        Module module = event.getModule();
        if (module instanceof IslandTrigger) {
            IslandTrigger trigger = (IslandTrigger)((Object)module);
            if (event.isEnabled()) {
                DynamicIslandElement.addTrigger(trigger);
            } else {
                DynamicIslandElement.removeTrigger(trigger);
            }
        }
    }

    public static void addTrigger(IslandTrigger trigger) {
        if (!ACTIVE_TRIGGERS.contains(trigger)) {
            ACTIVE_TRIGGERS.add(trigger);
            SORTING_DIRTY = true;
        }
    }

    public static void removeTrigger(IslandTrigger trigger) {
        if (ACTIVE_TRIGGERS.remove(trigger)) {
            SORTING_DIRTY = true;
        }
    }

    private void sort() {
        Collections.sort(ACTIVE_TRIGGERS);
        SORTING_DIRTY = false;
    }

    private void updateAnimations(float x, float y, float width, float height) {
        if (!this.positioned) {
            this.xAnimation.setValue(x);
            this.yAnimation.setValue(y);
            this.widthAnimation.setValue(width);
            this.heightAnimation.setValue(height);
            this.positioned = true;
        } else {
            this.xAnimation.run(x);
            this.yAnimation.run(y);
            this.widthAnimation.run(width);
            this.heightAnimation.run(height);
        }
    }

    public void renderIslandBackground(float x, float y, float width, float height) {
        NVGRenderer.roundedRect(x + 1.0f, y + 1.0f, width - 2.0f, height - 2.0f, 13.0f, NVGRenderer.BLUR_PAINT);
        NVGRenderer.roundedRect(x + 1.0f, y + 1.0f, width - 2.0f, height - 2.0f, 13.0f, -2146891511);
    }

    public boolean isAnimationFinished() {
        return this.xAnimation.isFinished();
    }

    public float getAnimatedX() {
        return this.xAnimation.getValue();
    }

    public float getAnimatedY() {
        return this.yAnimation.getValue();
    }

    public float getAnimatedWidth() {
        return this.widthAnimation.getValue();
    }

    public float getAnimatedHeight() {
        return this.heightAnimation.getValue();
    }

    @Override
    public boolean isActive() {
        return !(this.getDecidingTrigger() instanceof CustomIslandTrigger);
    }

    private IslandTrigger getDecidingTrigger() {
        return ACTIVE_TRIGGERS.getFirst();
    }

    @Override
    public boolean isBloom() {
        return true;
    }
}

