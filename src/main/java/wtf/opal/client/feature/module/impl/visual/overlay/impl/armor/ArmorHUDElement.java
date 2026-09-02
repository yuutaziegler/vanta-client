/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1304
 *  net.minecraft.class_1304$class_1305
 *  net.minecraft.class_1799
 *  net.minecraft.class_332
 *  net.minecraft.class_9274
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.armor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1304;
import net.minecraft.class_1799;
import net.minecraft.class_332;
import net.minecraft.class_9274;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.renderer.MinecraftRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;

@Environment(value=EnvType.CLIENT)
public final class ArmorHUDElement
implements IOverlayElement {
    private static final NVGTextRenderer FONT = FontRepository.getFont("inter-bold");
    private final BooleanProperty enabled = new BooleanProperty("Enabled", true);
    private final BooleanProperty showDurability = new BooleanProperty("Durability", true);
    private final BooleanProperty showMainHand = new BooleanProperty("Main hand", true);
    private final ScreenPositionProperty position = new ScreenPositionProperty("Screen Position", 0.58f, 0.88f);

    public ArmorHUDElement(OverlayModule module) {
        module.addProperties(new GroupProperty("Armor HUD", this.enabled, this.showDurability, this.showMainHand, this.position));
    }

    @Override
    public ScreenPositionProperty getPositionProperty() {
        return this.position;
    }

    @Override
    public boolean isActive() {
        return this.enabled.getValue();
    }

    @Override
    public boolean isBloom() {
        return false;
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        Iterator main;
        if (Constants.mc.field_1724 == null) {
            return;
        }
        ArrayList items = new ArrayList();
        if (this.showMainHand.getValue().booleanValue() && !(main = Constants.mc.field_1724.method_6047()).method_7960()) {
            items.add(main);
        }
        for (class_1304 slot : class_9274.field_49224) {
            class_1799 stack;
            if (slot.method_5925() != class_1304.class_1305.field_6178 || (stack = Constants.mc.field_1724.method_6118(slot)).method_7960()) continue;
            items.add(stack);
        }
        if (items.isEmpty()) {
            return;
        }
        Collections.reverse(items);
        float itemW = 20.0f;
        float totalW = (float)items.size() * itemW + 6.0f;
        float totalH = 22.0f;
        this.position.setWidth(totalW);
        this.position.setHeight(totalH);
        float x = this.position.getScaledX();
        float y = this.position.getScaledY();
        NVGRenderer.roundedRect(x, y, totalW, totalH, 5.0f, -2013265920);
        for (int i = 0; i < items.size(); ++i) {
            class_1799 item = (class_1799)items.get(i);
            if (!this.showDurability.getValue().booleanValue() || !item.method_7963()) continue;
            int durability = item.method_7936() - item.method_7919();
            float pct = Math.max(0.0f, Math.min(1.0f, (float)durability / (float)item.method_7936()));
            int barCol = pct > 0.5f ? -16718218 : (pct > 0.25f ? -10752 : -59580);
            NVGRenderer.roundedRect(x + 3.0f + (float)i * itemW + 2.0f, y + 18.0f, 14.0f * pct, 1.5f, 0.5f, barCol);
        }
        MinecraftRenderer.addToQueue(() -> {
            context.method_71048();
            for (int i = 0; i < items.size(); ++i) {
                class_1799 item = (class_1799)items.get(i);
                int curX = (int)(x + 3.0f + (float)i * itemW);
                int curY = (int)(y + 2.0f);
                context.method_51427(item, curX, curY);
                context.method_51431(Constants.mc.field_1772, item, curX, curY);
            }
        });
    }
}

