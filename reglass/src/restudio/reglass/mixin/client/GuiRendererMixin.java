/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  net.minecraft.class_11228
 *  net.minecraft.class_11239
 *  net.minecraft.class_11256
 *  net.minecraft.class_4597$class_4598
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package restudio.reglass.mixin.client;

import com.google.common.collect.ImmutableMap;
import net.minecraft.class_11228;
import net.minecraft.class_11239;
import net.minecraft.class_11256;
import net.minecraft.class_4597;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import restudio.reglass.client.gui.LiquidGlassGuiElementRenderer;
import restudio.reglass.mixin.accessor.GuiRendererAccessor;

@Mixin(value={class_11228.class})
public class GuiRendererMixin {
    @Redirect(method={"<init>"}, at=@At(value="INVOKE", target="Lcom/google/common/collect/ImmutableMap$Builder;buildOrThrow()Lcom/google/common/collect/ImmutableMap;"))
    private ImmutableMap<Class<? extends class_11256>, class_11239<?>> addCustomRenderer(ImmutableMap.Builder<Class<? extends class_11256>, class_11239<?>> builder) {
        class_11228 thisGuiRenderer = (class_11228)this;
        class_4597.class_4598 vertexConsumers = ((GuiRendererAccessor)thisGuiRenderer).getVertexConsumers();
        LiquidGlassGuiElementRenderer liquidGlassRenderer = new LiquidGlassGuiElementRenderer(vertexConsumers);
        builder.put(liquidGlassRenderer.method_70903(), (Object)liquidGlassRenderer);
        return builder.buildOrThrow();
    }
}

