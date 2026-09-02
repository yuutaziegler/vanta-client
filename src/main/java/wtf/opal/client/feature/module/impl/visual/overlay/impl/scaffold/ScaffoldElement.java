/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1747
 *  net.minecraft.class_1799
 *  net.minecraft.class_332
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.scaffold;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1747;
import net.minecraft.class_1799;
import net.minecraft.class_332;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.HudSettingsModule;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.world.scaffold.LBScaffoldModule;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.renderer.LiquidGlassRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;

@Environment(value=EnvType.CLIENT)
public final class ScaffoldElement
implements IOverlayElement {
    private static final NVGTextRenderer FONT = FontRepository.getFont("productsans-bold");
    private final ScreenPositionProperty positionProperty;
    private final OverlayModule overlayModule;

    public ScaffoldElement(OverlayModule overlayModule) {
        this.overlayModule = overlayModule;
        this.positionProperty = new ScreenPositionProperty("Scaffold Element", 0.5f, 0.5f);
        overlayModule.addProperties(this.positionProperty);
    }

    @Override
    public ScreenPositionProperty getPositionProperty() {
        return this.positionProperty;
    }

    @Override
    public boolean isActive() {
        LBScaffoldModule scaffold = OpalClient.getInstance().getModuleRepository().getModule(LBScaffoldModule.class);
        return this.overlayModule.isEnabled() && scaffold != null && scaffold.isEnabled();
    }

    @Override
    public boolean isBloom() {
        return false;
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        int blockCount = 0;
        class_1799 heldItem = Constants.mc.field_1724.method_6047();
        if (heldItem.method_7909() instanceof class_1747) {
            for (int i = 0; i < 9; ++i) {
                class_1799 stack = Constants.mc.field_1724.method_31548().method_5438(i);
                if (stack == null || stack.method_7909() != heldItem.method_7909()) continue;
                blockCount += stack.method_7947();
            }
        }
        String text = blockCount + " Blocks";
        float textWidth = FONT.getStringWidth(text, 10.0f);
        float width = 40.0f + textWidth;
        float height = 30.0f;
        this.positionProperty.setWidth(width);
        this.positionProperty.setHeight(height);
        float x = this.positionProperty.getScaledX();
        float y = this.positionProperty.getScaledY();
        HudSettingsModule hudSettings = OpalClient.getInstance().getModuleRepository().getModule(HudSettingsModule.class);
        float radius = hudSettings != null && hudSettings.isEnabled() ? hudSettings.getCornerRadius() : 8.0f;
        LiquidGlassRenderer.drawGlassPanel(x, y, width, height, radius);
        if (heldItem.method_7909() instanceof class_1747) {
            context.method_51427(heldItem, (int)x + 8, (int)y + 7);
        }
        FONT.drawString(text, x + 32.0f, y + 15.0f, 10.0f, -1);
    }
}

