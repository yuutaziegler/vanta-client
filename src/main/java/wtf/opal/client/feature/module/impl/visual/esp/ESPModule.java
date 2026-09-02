/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_124
 *  net.minecraft.class_1297
 *  net.minecraft.class_1304
 *  net.minecraft.class_1304$class_1305
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1799
 *  net.minecraft.class_1890
 *  net.minecraft.class_241
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_4604
 *  net.minecraft.class_6880
 *  net.minecraft.class_9274
 *  org.joml.Vector4d
 *  org.lwjgl.nanovg.NanoVG
 */
package wtf.opal.client.feature.module.impl.visual.esp;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_124;
import net.minecraft.class_1297;
import net.minecraft.class_1304;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1799;
import net.minecraft.class_1890;
import net.minecraft.class_241;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_4604;
import net.minecraft.class_6880;
import net.minecraft.class_9274;
import org.joml.Vector4d;
import org.lwjgl.nanovg.NanoVG;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.helper.impl.render.FrustumHelper;
import wtf.opal.client.feature.helper.impl.target.TargetList;
import wtf.opal.client.feature.helper.impl.target.TargetProperty;
import wtf.opal.client.feature.helper.impl.target.impl.TargetLivingEntity;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.visual.esp.ESPSettings;
import wtf.opal.client.feature.module.impl.visual.esp.NameTagElement;
import wtf.opal.client.feature.module.impl.visual.esp.NameTagIcon;
import wtf.opal.client.feature.module.impl.visual.esp.NameTagIconPosition;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.renderer.MinecraftRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.event.impl.render.RenderBloomEvent;
import wtf.opal.event.impl.render.RenderScreenEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.player.BlockUtility;
import wtf.opal.utility.player.PlayerUtility;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.ESPUtility;

@Environment(value=EnvType.CLIENT)
public final class ESPModule
extends Module {
    private final ESPSettings settings = new ESPSettings(this);
    private static final NVGTextRenderer NAMETAG_FONT = FontRepository.getFont("productsans-bold");
    private static final NVGTextRenderer ICON_FONT = FontRepository.getFont("materialicons-regular");
    private static final DecimalFormat HEALTH_DF = new DecimalFormat("0.#");
    private static final float NAMETAG_FONT_SIZE = 5.0f;

    public ESPModule() {
        super("ESP", "Extra sensory perception.", ModuleCategory.VISUAL);
    }

    @Subscribe
    public void onRenderScreen(RenderScreenEvent event) {
        this.render(event.drawContext(), event.tickDelta());
    }

    @Subscribe
    public void onBloomRender(RenderBloomEvent event) {
        if (this.settings.getBloom()) {
            this.render(event.drawContext(), event.tickDelta());
        }
    }

    private void render(class_332 drawContext, float tickDelta) {
        class_4604 frustum = FrustumHelper.get();
        if (frustum == null) {
            return;
        }
        TargetList targetList = LocalDataWatch.getTargetList();
        if (targetList == null || Constants.mc.field_1724 == null) {
            return;
        }
        TargetProperty targetProperty = this.settings.getTargetProperty();
        List<TargetLivingEntity> targets = targetList.collectTargets(targetProperty.getTargetFlags(), TargetLivingEntity.class);
        for (TargetLivingEntity target : targets) {
            Object entity;
            if (target.isLocal() && (!targetProperty.isLocalPlayer() || Constants.mc.field_1690.method_31044().method_31034()) || !frustum.method_23093((entity = target.getEntity()).method_5829())) continue;
            this.renderBoxIn2D(drawContext, (class_1309)entity, tickDelta);
        }
    }

    private void renderBoxIn2D(class_332 drawContext, class_1309 entity, float tickDelta) {
        Vector4d projection = ESPUtility.getEntityPositionsOn2D(entity, tickDelta);
        float x = (float)projection.x;
        float y = (float)projection.y;
        float w = (float)projection.z;
        float h = (float)projection.w;
        float thickness = 0.5f;
        NanoVG.nvgShapeAntiAlias((long)Constants.VG, (boolean)false);
        if (this.settings.getBox()) {
            this.renderFullBox(x, y, w, h, 0.5f, ColorUtility.applyOpacity(entity.method_22861(), 1.0f));
        }
        if (this.settings.getHealthBar()) {
            this.renderHealthBar(x, y, w, h, 0.5f, entity.method_6032() / entity.method_6063());
        }
        NanoVG.nvgShapeAntiAlias((long)Constants.VG, (boolean)true);
        if (this.settings.areNameTagsEnabled()) {
            this.renderNameTag(drawContext, entity, x, y, w);
        }
    }

    private void renderHealthBar(float x, float y, float w, float h, float thickness, float healthValue) {
        if (this.settings.getHealthBarStroke()) {
            NVGRenderer.rectStroke(x - thickness * 2.0f - 0.5f - (this.settings.getBox() && this.settings.getBoxStroke() ? 0.5f : 0.0f), y + (h - h * healthValue), thickness, h * healthValue, thickness, -16711936, -16777216);
        } else {
            NVGRenderer.rect(x - thickness - 0.5f - (this.settings.getBox() && this.settings.getBoxStroke() ? 0.5f : 0.0f), y + (h - h * healthValue), thickness, h * healthValue, -16711936);
        }
    }

    private void renderFullBox(float x, float y, float w, float h, float thickness, int color) {
        if (!this.settings.getBoxStroke()) {
            NVGRenderer.rectOutline(x, y, w, h, thickness, color);
        } else {
            NVGRenderer.rectOutlineStroke(x, y, w, h, thickness, thickness * 3.0f, color, -16777216);
        }
    }

    private void renderNameTag(class_332 drawContext, class_1309 entity, float x, float y, float w) {
        class_1657 player;
        MultipleBooleanProperty indicators = this.settings.getNameTagIndicators();
        MultipleBooleanProperty elements = this.settings.getNameTagElements();
        ArrayList<NameTagElement> elementList = new ArrayList<NameTagElement>();
        if (indicators.getProperty("Strength").getValue().booleanValue() && LocalDataWatch.get().getStrengthedPlayerList().contains(entity.method_5477().getString())) {
            elementList.add(new NameTagElement(new NameTagIcon("\uefe4", 0.25f), -65536));
        }
        if (indicators.getProperty("Sneaking").getValue().booleanValue() && entity.method_18276()) {
            elementList.add(new NameTagElement(new NameTagIcon("\uf19f"), -43691));
        }
        if (indicators.getProperty("Invisible").getValue().booleanValue() && entity.method_5767()) {
            elementList.add(new NameTagElement(new NameTagIcon("\ue8f5", 0.3f), -5592406));
        }
        if (indicators.getProperty("Blocking").getValue().booleanValue() && entity instanceof class_1657 && (BlockUtility.isBlockUseState(player = (class_1657)entity) || BlockUtility.isForceBlockUseState(player) || player == Constants.mc.field_1724 && BlockUtility.isNoSlowBlockingState())) {
            elementList.add(new NameTagElement(new NameTagIcon("\ue1d5", 0.15f), -12472451));
        }
        if (elements.getProperty("Distance").getValue().booleanValue() && entity != Constants.mc.field_1724) {
            NameTagIcon distanceIcon = new NameTagIcon("\ue55c", NameTagIconPosition.RIGHT);
            elementList.add(new NameTagElement(distanceIcon, String.valueOf((int)Math.floor(entity.method_5739((class_1297)Constants.mc.field_1724))), -5592406));
        }
        if (elements.getProperty("Name").getValue().booleanValue()) {
            int color = -1;
            String name = String.valueOf(class_124.field_1068) + PlayerUtility.getFormattedEntityName(entity);
            elementList.add(new NameTagElement(name, color));
        }
        if (elements.getProperty("Health").getValue().booleanValue()) {
            NameTagIcon redHeartIcon = new NameTagIcon(String.valueOf(class_124.field_1061) + "\ue87d", NameTagIconPosition.RIGHT);
            elementList.add(new NameTagElement(redHeartIcon, HEALTH_DF.format(entity.method_6032()), -1));
            if (entity.method_6067() > 0.0f) {
                NameTagIcon normalHeartIcon = new NameTagIcon("\ue87d", NameTagIconPosition.RIGHT);
                elementList.add(new NameTagElement(normalHeartIcon, HEALTH_DF.format(entity.method_6067()), -15801));
            }
        }
        this.renderNameTagElements(elementList, this.calculateStartingPosition(elementList, x, y, w));
        if (elements.getProperty("Equipment").getValue().booleanValue()) {
            this.renderEquipment(drawContext, entity, x, y, w, !elementList.isEmpty());
        }
    }

    private void renderEquipment(class_332 drawContext, class_1309 entity, float x, float y, float w, boolean hasNametagElements) {
        class_1799 mainHandStack;
        ArrayList<class_1799> equipment = new ArrayList<class_1799>();
        for (class_1304 equipmentSlot : class_9274.field_49224) {
            class_1799 stack;
            if (equipmentSlot.method_5925() != class_1304.class_1305.field_6178 || (stack = entity.method_6118(equipmentSlot)).method_7960()) continue;
            equipment.add(stack);
        }
        class_1799 class_17992 = mainHandStack = entity == Constants.mc.field_1724 && BlockUtility.isNoSlowBlockingState() ? SlotHelper.getInstance().getMainHandStack(Constants.mc.field_1724) : entity.method_6047();
        if (!mainHandStack.method_7960()) {
            equipment.add(mainHandStack);
        }
        float scale = 0.65f;
        float stackTextScale = 0.6f;
        MinecraftRenderer.addToQueue(() -> {
            for (int i = 0; i < equipment.size(); ++i) {
                class_1799 stack = (class_1799)equipment.get(i);
                float stackX = x + w / 2.0f - (float)equipment.size() * 0.65f * 8.0f + (float)(equipment.size() - i - 1) * 0.65f * 16.0f;
                drawContext.method_51448().pushMatrix();
                drawContext.method_51448().translate(stackX, y - (hasNametagElements ? 23.5f : 14.0f));
                drawContext.method_51448().scale(0.65f, 0.65f);
                drawContext.method_51448().scale(0.6f, 0.6f);
                drawContext.method_51448().translate(6.0f, 12.0f);
                drawContext.method_51448().pushMatrix();
                drawContext.method_51448().translate(-6.0f, -12.0f);
                drawContext.method_51448().scale(1.6666666f, 1.6666666f);
                drawContext.method_51427(stack, 0, 0);
                drawContext.method_51448().popMatrix();
                AtomicInteger enchantmentCount = new AtomicInteger();
                class_1890.method_57532((class_1799)stack).method_57539().forEach(entry -> ((class_6880)entry.getKey()).method_40230().ifPresent(key -> {
                    String shortName = ESPUtility.ENCHANTMENT_NAMES.get(key);
                    if (shortName == null) {
                        return;
                    }
                    drawContext.method_51430(Constants.mc.field_1772, class_2561.method_30163((String)(shortName + entry.getIntValue())).method_30937(), 2, 7 + -8 * enchantmentCount.getAndIncrement(), -1, true);
                }));
                drawContext.method_51448().popMatrix();
            }
        });
    }

    private class_241 calculateStartingPosition(List<NameTagElement> elements, float x, float y, float w) {
        float totalWidth = 0.0f;
        for (int i = 0; i < elements.size(); ++i) {
            NameTagElement element = elements.get(i);
            if (element.text() != null) {
                totalWidth += NAMETAG_FONT.getStringWidth(element.text(), 5.0f);
            }
            if (element.icon() != null) {
                totalWidth += ICON_FONT.getStringWidth(element.icon().unicode(), 5.0f);
            }
            if (i >= elements.size() - 1) continue;
            totalWidth += 5.0f;
        }
        float middleX = x + w / 2.0f;
        float startX = middleX - totalWidth / 2.0f;
        return new class_241(startX, y - 4.5f);
    }

    private void renderNameTagElements(List<NameTagElement> elements, class_241 position) {
        float currentX = position.field_1343;
        for (NameTagElement element : elements) {
            boolean hasText = element.text() != null;
            boolean hasIcon = element.icon() != null;
            float elementWidth = hasText ? NAMETAG_FONT.getStringWidth(element.text(), 5.0f) : 0.0f;
            NameTagIcon icon = element.icon();
            float iconWidth = hasIcon ? ICON_FONT.getStringWidth(icon.unicode(), 5.0f) : 0.0f;
            float bgPadding = 2.0f;
            float bgRadius = 2.0f;
            NVGRenderer.roundedRect(currentX - 2.0f, position.field_1342 - 2.0f - 4.5f, elementWidth + iconWidth + 4.0f, 9.0f, 2.0f, NVGRenderer.BLUR_PAINT);
            NVGRenderer.roundedRect(currentX - 2.0f, position.field_1342 - 2.0f - 4.5f, elementWidth + iconWidth + 4.0f, 9.0f, 2.0f, ColorUtility.applyOpacity(-16777216, 0.5f));
            float textX = currentX;
            if (hasIcon && icon.position() == NameTagIconPosition.LEFT) {
                ICON_FONT.drawString(icon.unicode(), currentX + icon.horizontalOffset(), position.field_1342 + 1.0f, 5.0f, element.color());
                textX += iconWidth;
            }
            if (hasText) {
                NAMETAG_FONT.drawString(element.text(), textX, position.field_1342, 5.0f, element.color());
            }
            if (hasIcon && icon.position() == NameTagIconPosition.RIGHT) {
                ICON_FONT.drawString(icon.unicode(), textX + elementWidth + icon.horizontalOffset(), position.field_1342 + 1.0f, 5.0f, element.color());
            }
            currentX += elementWidth + iconWidth + 5.0f;
        }
    }

    public ESPSettings getSettings() {
        return this.settings;
    }
}

