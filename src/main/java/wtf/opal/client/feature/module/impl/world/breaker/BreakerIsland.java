/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 *  net.minecraft.class_2248
 *  net.minecraft.class_332
 */
package wtf.opal.client.feature.module.impl.world.breaker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_2248;
import net.minecraft.class_332;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.DynamicIslandElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.IslandTrigger;
import wtf.opal.client.feature.module.impl.world.breaker.BreakerModule;
import wtf.opal.client.renderer.MinecraftRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.duck.ClientPlayerInteractionManagerAccess;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class BreakerIsland
implements IslandTrigger {
    private final BreakerModule parent;
    private Animation breakProgressAnimation;

    public BreakerIsland(BreakerModule parent) {
        this.parent = parent;
    }

    @Override
    public void renderIsland(class_332 context, float posX, float posY, float width, float height, float progress) {
        NVGTextRenderer titleFont = FontRepository.getFont("productsans-bold");
        NVGTextRenderer footerFont = FontRepository.getFont("productsans-medium");
        float titleTextSize = 8.0f;
        float secondaryTextSize = 6.0f;
        if (this.parent.getCurrentTarget() == null) {
            DynamicIslandElement.removeTrigger(this);
            return;
        }
        class_2248 block = Constants.mc.field_1687.method_8320(this.parent.getCurrentTarget().candidate().getPos()).method_26204();
        int color = ColorUtility.applyOpacity(block.method_26403().field_16011, 255);
        float prevGlobalAlpha = NVGRenderer.globalAlpha;
        NVGRenderer.globalAlpha(1.0f);
        NVGRenderer.roundedRect(posX + 5.5f, posY + 4.0f, 17.0f, 17.0f, 8.25f, ColorUtility.applyOpacity(color, 120));
        NVGRenderer.globalAlpha(prevGlobalAlpha);
        MinecraftRenderer.addToQueue(() -> {
            context.method_51448().pushMatrix();
            context.method_51448().translate(posX + 8.0f, posY + 6.5f);
            context.method_51448().scale(0.75f, 0.75f);
            context.method_51423((class_1309)Constants.mc.field_1724, block.method_8389().method_7854(), 0, 0, 0);
            context.method_51448().popMatrix();
        });
        NVGRenderer.roundedRect(posX + 28.0f, posY + 11.5f, 85.0f, 2.5f, 1.5f, ColorUtility.darker(color, 0.55f));
        ClientPlayerInteractionManagerAccess access = (ClientPlayerInteractionManagerAccess)Constants.mc.field_1761;
        float breakProgress = access.opal$currentBreakingProgress();
        float scaledWidth = Math.min(breakProgress, 1.0f) * 85.0f;
        if (this.breakProgressAnimation == null) {
            this.breakProgressAnimation = new Animation(Easing.EASE_OUT_EXPO, 200L);
            this.breakProgressAnimation.setValue(scaledWidth);
        } else {
            this.breakProgressAnimation.run(scaledWidth);
        }
        if (breakProgress > 0.0f) {
            NVGRenderer.roundedRectGradient(posX + 28.0f, posY + 11.5f, this.breakProgressAnimation.getValue(), 2.5f, 1.25f, ColorUtility.darker(color, 0.4f), color, 0.0f);
        }
        titleFont.drawString((int)(breakProgress * 100.0f) + "%", posX + 28.0f + 85.0f + 6.0f, posY + 15.0f, 7.0f, -1);
    }

    public void onDisable() {
        this.breakProgressAnimation = null;
    }

    @Override
    public float getIslandWidth() {
        return 140.0f;
    }

    @Override
    public float getIslandHeight() {
        return 25.0f;
    }

    @Override
    public int getIslandPriority() {
        return 3;
    }
}

