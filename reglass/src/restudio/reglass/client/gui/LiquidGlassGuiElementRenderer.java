/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_11239
 *  net.minecraft.class_11246
 *  net.minecraft.class_4587
 *  net.minecraft.class_4597$class_4598
 */
package restudio.reglass.client.gui;

import net.minecraft.class_11239;
import net.minecraft.class_11246;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import restudio.reglass.client.LiquidGlassUniforms;
import restudio.reglass.client.gui.LiquidGlassGuiElementRenderState;

public class LiquidGlassGuiElementRenderer
extends class_11239<LiquidGlassGuiElementRenderState> {
    public LiquidGlassGuiElementRenderer(class_4597.class_4598 vertexConsumers) {
        super(vertexConsumers);
    }

    public void render(LiquidGlassGuiElementRenderState element, class_11246 state, int scale) {
        LiquidGlassUniforms.get().addWidget(element);
    }

    public Class<LiquidGlassGuiElementRenderState> method_70903() {
        return LiquidGlassGuiElementRenderState.class;
    }

    protected void render(LiquidGlassGuiElementRenderState element, class_4587 matrices) {
    }

    protected String method_70906() {
        return "liquid_glass_widget";
    }
}

