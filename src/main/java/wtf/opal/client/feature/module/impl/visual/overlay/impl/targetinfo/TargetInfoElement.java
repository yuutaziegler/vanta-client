/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  com.mojang.blaze3d.opengl.GlStateManager
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_124
 *  net.minecraft.class_1304
 *  net.minecraft.class_1304$class_1305
 *  net.minecraft.class_1309
 *  net.minecraft.class_1548
 *  net.minecraft.class_1613
 *  net.minecraft.class_1642
 *  net.minecraft.class_1747
 *  net.minecraft.class_1799
 *  net.minecraft.class_1890
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_332
 *  net.minecraft.class_3532
 *  net.minecraft.class_408
 *  net.minecraft.class_4836
 *  net.minecraft.class_5224
 *  net.minecraft.class_5251
 *  net.minecraft.class_6880
 *  net.minecraft.class_742
 *  net.minecraft.class_9274
 *  org.joml.Vector3f
 *  org.lwjgl.nanovg.NVGPaint
 *  org.lwjgl.nanovg.NanoVG
 *  org.lwjgl.nanovg.NanoVGGL3
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.targetinfo;

import com.ibm.icu.impl.Pair;
import com.mojang.blaze3d.opengl.GlStateManager;
import java.awt.Color;
import java.lang.runtime.SwitchBootstraps;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_124;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1548;
import net.minecraft.class_1613;
import net.minecraft.class_1642;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_332;
import net.minecraft.class_3532;
import net.minecraft.class_408;
import net.minecraft.class_4836;
import net.minecraft.class_5224;
import net.minecraft.class_5251;
import net.minecraft.class_6880;
import net.minecraft.class_742;
import net.minecraft.class_9274;
import org.joml.Vector3f;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.impl.combat.killaura.KillAuraModule;
import wtf.opal.client.feature.module.impl.combat.killaura.target.CurrentTarget;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.targetinfo.TargetInfoSettings;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.renderer.MinecraftRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.ESPUtility;
import wtf.opal.utility.render.OrderedTextVisitor;
import wtf.opal.utility.render.animation.Animation;
import wtf.opal.utility.render.animation.Easing;

@Environment(value=EnvType.CLIENT)
public final class TargetInfoElement
implements IOverlayElement {
    private static final NVGTextRenderer BOLD_FONT = FontRepository.getFont("productsans-bold");
    private static final NVGTextRenderer MEDIUM_FONT = FontRepository.getFont("productsans-medium");
    private static final NVGTextRenderer ICON_FONT = FontRepository.getFont("materialicons-regular");
    private static final DecimalFormat HEALTH_DF = new DecimalFormat("0.#");
    private final Animation targetAnimation;
    private final Animation healthAnimation;
    private final TargetInfoSettings settings;
    private Target currentTarget;
    private Target lastTarget;

    public TargetInfoElement(OverlayModule module) {
        this.settings = new TargetInfoSettings(module);
        this.targetAnimation = new Animation(Easing.EASE_OUT_EXPO, 200L);
        this.targetAnimation.setValue(1.0f);
        this.healthAnimation = new Animation(Easing.EASE_OUT_EXPO, 1000L);
    }

    public void initialize() {
    }

    @Override
    public ScreenPositionProperty getPositionProperty() {
        return this.settings.getScreenPosition();
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        int targetNameColor;
        Target target = this.getTarget();
        if (target == null) {
            return;
        }
        float scale = this.settings.getScale();
        float targetNameSize = 6.0f;
        float hpSize = 5.0f;
        String targetName = String.valueOf(class_124.field_1068) + target.getFormattedName();
        String user = OpalClient.getInstance().getUser();
        if (user != null) {
            targetName = targetName + " " + String.valueOf(class_124.field_1080) + "(" + String.valueOf(class_124.field_1070) + user + String.valueOf(class_124.field_1080) + ")";
            targetNameColor = -1;
        } else {
            targetNameColor = -1;
        }
        int skinTextureGlId = isBloom ? -1 : this.getSkinTextureGlId(target.entity);
        float padding = 3.0f;
        float headOffset = 22.5f;
        float equipmentWidth = 55.0f;
        ScreenPositionProperty screenPosition = this.settings.getScreenPosition();
        float width = 6.0f + Math.max(50.0f, Math.max(55.0f, BOLD_FONT.getStringWidth(targetName, 6.0f))) + 22.5f + 1.0f;
        float height = 31.5f;
        float x = screenPosition.getScaledX();
        float y = screenPosition.getScaledY();
        screenPosition.setWidth(width * scale);
        screenPosition.setHeight(31.5f * scale);
        float targetAnimationProgress = this.targetAnimation.getValue();
        float healthAnimationProgress = this.healthAnimation.getValue();
        Pair<Integer, Integer> theme = ColorUtility.getClientTheme();
        float trueHealthPercent = class_3532.method_15363((float)((target.entity.method_6032() + target.entity.method_6067()) / (target.entity.method_6063() + target.entity.method_6067())), (float)0.0f, (float)1.0f);
        this.healthAnimation.run(trueHealthPercent);
        String finalTargetName = targetName;
        NVGRenderer.scale(scale, x, y, 0.0f, 0.0f, () -> {
            NVGRenderer.globalAlpha(targetAnimationProgress);
            NVGRenderer.roundedRect(x, y, width, 31.5f, 4.0f, NVGRenderer.BLUR_PAINT);
            NVGRenderer.roundedRect(x, y, width, 31.5f, 4.0f, -2146891511);
            BOLD_FONT.drawString(finalTargetName, x + 3.0f + 22.5f, y + 9.0f, 6.0f, targetNameColor);
            float absorption = target.entity.method_6067();
            float heartWidth = ICON_FONT.getStringWidth("\ue87d", 5.0f);
            String hp = HEALTH_DF.format(target.entity.method_6032() + absorption);
            ICON_FONT.drawString(String.valueOf(absorption > 0.0f ? "" : class_124.field_1061) + "\ue87d", x + width - 7.5f, y + 29.0f, 5.0f, -15801);
            MEDIUM_FONT.drawString(hp, x + width - 3.0f - MEDIUM_FONT.getStringWidth(hp, 5.0f) - heartWidth - 0.25f, y + 28.5f, 5.0f, -1);
            float healthBarWidth = width - 8.25f - MEDIUM_FONT.getStringWidth(hp.length() > 2 ? hp : "88.", 5.0f) - heartWidth;
            NVGRenderer.roundedRect(x + 3.0f - 0.125f, y + 24.75f, healthBarWidth, 4.0f, 1.6666666f, ColorUtility.applyOpacity(ColorUtility.darker((Integer)theme.second, 0.8f), 0.6f));
            if ((double)healthAnimationProgress > 0.01) {
                NVGRenderer.roundedRectGradient(x + 3.0f - 0.125f, y + 24.75f, healthAnimationProgress * healthBarWidth, 4.0f, 1.6666666f, ColorUtility.darker((Integer)theme.first, 0.6f), ColorUtility.darker((Integer)theme.second, 0.6f), 0.0f);
            }
            if ((double)trueHealthPercent > 0.01) {
                NVGRenderer.roundedRectGradient(x + 3.0f - 0.125f, y + 24.75f, trueHealthPercent * healthBarWidth, 4.0f, 1.6666666f, (Integer)theme.first, (Integer)theme.second, 0.0f);
                NVGRenderer.roundedRectGradient(x + 3.0f - 0.125f, y + 24.75f, trueHealthPercent * healthBarWidth, 4.0f, 1.6666666f, 3, ColorUtility.applyOpacity(-16777216, 0.6f), 90.0f);
            }
            if (skinTextureGlId != -1) {
                NanoVG.nvgBeginPath((long)Constants.VG);
                float headX = x + 3.0f + 0.25f;
                float headY = y + 3.0f;
                float headScale = 2.6666667f;
                float size = 19.5f;
                int skinTextureHandle = target.getSkinTextureHandle(skinTextureGlId);
                NanoVG.nvgImagePattern((long)Constants.VG, (float)(headX - 22.199999f), (float)(headY - 22.875f), (float)170.66667f, (float)170.66667f, (float)0.0f, (int)skinTextureHandle, (float)1.0f, (NVGPaint)NVGRenderer.NVG_PAINT);
                if (target.entity.field_6235 > 0) {
                    float damageFactor = (float)target.entity.field_6235 / (float)target.entity.field_6254;
                    float reductionFactor = 0.6f;
                    float r = Math.min(1.0f, 1.0f + 0.39999998f * damageFactor);
                    float g = 1.0f - damageFactor * 0.6f;
                    float b = 1.0f - damageFactor * 0.6f;
                    NVGRenderer.applyColor(new Color(r, g, b).getRGB(), NVGRenderer.NVG_COLOR_1);
                    NVGRenderer.NVG_PAINT.innerColor(NVGRenderer.NVG_COLOR_1);
                }
                NanoVG.nvgFillPaint((long)Constants.VG, (NVGPaint)NVGRenderer.NVG_PAINT);
                NanoVG.nvgRoundedRect((long)Constants.VG, (float)headX, (float)headY, (float)19.5f, (float)19.5f, (float)2.0f);
                NanoVG.nvgFill((long)Constants.VG);
                NanoVG.nvgClosePath((long)Constants.VG);
            }
            ArrayList<class_1799> equipment = new ArrayList<class_1799>();
            for (class_1304 equipmentSlot : class_9274.field_49224) {
                if (equipmentSlot.method_5925() != class_1304.class_1305.field_6178) continue;
                equipment.add(target.entity.method_6118(equipmentSlot));
            }
            equipment.add(target.entity.method_6047());
            Collections.reverse(equipment);
            float stackScale = 0.625f * scale;
            float stackTextScale = 0.6f;
            int equipmentCount = equipment.size();
            for (int i = 0; i < equipmentCount; ++i) {
                float boxX = x + (float)i * 11.5f + 3.0f + 22.5f - 0.5f;
                float boxY = y + 3.0f + 8.5f;
                NVGRenderer.roundedRect(boxX, boxY, 10.5f, 10.5f, 1.0f, ColorUtility.applyOpacity(-16777216, 0.2f));
            }
            NVGRenderer.globalAlpha(1.0f);
            MinecraftRenderer.addToQueue(() -> {
                context.method_71048();
                GlStateManager._enableBlend();
                for (int i = 0; i < equipmentCount; ++i) {
                    float offsetX = (float)i * 11.6f + 3.0f + 22.5f - 0.5f / scale;
                    float offsetY = 11.5f;
                    float stackX = x + offsetX * scale;
                    float stackY = y + 11.5f * scale;
                    context.method_51448().pushMatrix();
                    context.method_51448().translate(stackX, stackY);
                    context.method_51448().scale(stackScale, stackScale);
                    context.method_51448().scale(0.6f, 0.6f);
                    class_1799 stack = (class_1799)equipment.get(i);
                    context.method_51448().pushMatrix();
                    context.method_51448().transform(new Vector3f(-6.0f, -12.0f, -200.0f));
                    context.method_51448().scale(1.6666666f, 1.6666666f);
                    if (stack.method_7909() instanceof class_1747) {
                        if (targetAnimationProgress >= 0.5f) {
                            context.method_51428(stack, 0, 0, -200);
                        }
                    } else {
                        context.method_51428(stack, 0, 0, -200);
                    }
                    context.method_51448().popMatrix();
                    class_1890.method_57532((class_1799)stack).method_57539().forEach(entry -> ((class_6880)entry.getKey()).method_40230().ifPresent(key -> {
                        String shortName = ESPUtility.ENCHANTMENT_NAMES.get(key);
                        if (shortName == null) {
                            return;
                        }
                        context.method_51430(Constants.mc.field_1772, class_2561.method_30163((String)(shortName + entry.getIntValue())).method_30937(), 2, 7, -1, true);
                    }));
                    context.method_51448().popMatrix();
                }
                GlStateManager._disableBlend();
            });
        });
        if (this.currentTarget != null) {
            this.lastTarget = this.currentTarget;
        }
    }

    @Override
    public boolean isActive() {
        return this.settings.isEnabled();
    }

    private Target getTarget() {
        Target activeTarget;
        CurrentTarget killAuraTarget;
        KillAuraModule killAuraModule;
        class_1309 target = (class_1309)LocalDataWatch.get().lastEntityAttack.method_15441();
        if (target != null && !LocalDataWatch.getTargetList().hasTarget(target.method_5628())) {
            target = null;
        }
        if (target == null && (killAuraModule = OpalClient.getInstance().getModuleRepository().getModule(KillAuraModule.class)).isEnabled() && (killAuraTarget = killAuraModule.getTargeting().getTarget()) != null) {
            target = killAuraTarget.getEntity();
        }
        Target preCurrentTarget = this.currentTarget;
        Target preLastTarget = this.lastTarget;
        if (target != null) {
            if (this.currentTarget == null || this.currentTarget.entity.method_5628() != target.method_5628()) {
                this.currentTarget = new Target(target);
            }
        } else if (Constants.mc.field_1755 instanceof class_408) {
            if (this.currentTarget == null || this.currentTarget.entity.method_5628() != Constants.mc.field_1724.method_5628()) {
                this.currentTarget = new Target((class_1309)Constants.mc.field_1724);
            }
        } else {
            this.currentTarget = null;
        }
        if ((activeTarget = this.currentTarget) == null) {
            if (this.targetAnimation.isFinished()) {
                this.lastTarget = null;
            } else {
                activeTarget = this.lastTarget;
                this.targetAnimation.run(0.0f);
            }
        } else {
            this.targetAnimation.setValue(1.0f);
            this.targetAnimation.reset();
        }
        if (activeTarget != null) {
            activeTarget.updateFormattedName();
        }
        if (preCurrentTarget != null && preCurrentTarget.skinTextureHandle != -1 && this.lastTarget == preCurrentTarget && preCurrentTarget != this.currentTarget && preCurrentTarget != activeTarget) {
            NanoVG.nvgDeleteImage((long)Constants.VG, (int)preCurrentTarget.skinTextureHandle);
        } else if (this.currentTarget == null && this.lastTarget != null && this.lastTarget.skinTextureHandle != -1 && this.targetAnimation.getValue() == 0.0f) {
            NanoVG.nvgDeleteImage((long)Constants.VG, (int)preLastTarget.skinTextureHandle);
            this.lastTarget = null;
        }
        return activeTarget;
    }

    private int getSkinTextureGlId(class_1309 entity) {
        class_2960 identifier;
        class_1309 class_13092 = entity;
        Objects.requireNonNull(class_13092);
        class_1309 class_13093 = class_13092;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{class_742.class, class_1613.class, class_1642.class, class_1548.class, class_4836.class}, (Object)class_13093, n)) {
            case 0: {
                class_742 player = (class_742)class_13093;
                class_2960 class_29602 = player.method_52814().comp_1626().comp_3627();
                break;
            }
            case 1: {
                class_1613 ignored = (class_1613)class_13093;
                class_2960 class_29602 = class_2960.method_60656((String)"textures/entity/skeleton/skeleton.png");
                break;
            }
            case 2: {
                class_1642 ignored = (class_1642)class_13093;
                class_2960 class_29602 = class_2960.method_60656((String)"textures/entity/zombie/zombie.png");
                break;
            }
            case 3: {
                class_1548 ignored = (class_1548)class_13093;
                class_2960 class_29602 = class_2960.method_60656((String)"textures/entity/creeper/creeper.png");
                break;
            }
            case 4: {
                class_4836 ignored = (class_4836)class_13093;
                class_2960 class_29602 = class_2960.method_60656((String)"textures/entity/piglin/piglin.png");
                break;
            }
            default: {
                class_2960 class_29602 = identifier = null;
            }
        }
        if (identifier == null) {
            return -1;
        }
        return Integer.parseInt(Constants.mc.method_1531().method_4619(identifier).method_68004().getLabel());
    }

    @Override
    public boolean isBloom() {
        return true;
    }

    @Environment(value=EnvType.CLIENT)
    private static final class Target {
        private final class_1309 entity;
        private String formattedName;
        private int skinTextureHandle = -1;

        private Target(class_1309 entity) {
            this.entity = entity;
        }

        private String getFormattedName() {
            if (this.formattedName != null) {
                return this.formattedName;
            }
            return this.entity.method_5477().getString();
        }

        private void updateFormattedName() {
            if (this.entity.method_5476() == null) {
                return;
            }
            if (this.formattedName != null && LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer && this.entity.method_5476().method_10866().method_10973() == class_5251.method_27718((class_124)class_124.field_1080)) {
                return;
            }
            OrderedTextVisitor visitor = new OrderedTextVisitor();
            this.entity.method_5476().method_30937().accept((class_5224)visitor);
            this.formattedName = visitor.getFormattedString();
        }

        private int getSkinTextureHandle(int skinTextureGlId) {
            if (this.skinTextureHandle != -1) {
                return this.skinTextureHandle;
            }
            this.skinTextureHandle = NanoVGGL3.nvglCreateImageFromHandle((long)Constants.VG, (int)skinTextureGlId, (int)64, (int)64, (int)65536);
            return this.skinTextureHandle;
        }
    }
}

