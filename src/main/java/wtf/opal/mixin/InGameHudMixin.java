/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1041
 *  net.minecraft.class_1657
 *  net.minecraft.class_1661
 *  net.minecraft.class_1799
 *  net.minecraft.class_1819
 *  net.minecraft.class_2561
 *  net.minecraft.class_266
 *  net.minecraft.class_268
 *  net.minecraft.class_269
 *  net.minecraft.class_270
 *  net.minecraft.class_329
 *  net.minecraft.class_332
 *  net.minecraft.class_5250
 *  net.minecraft.class_5348
 *  net.minecraft.class_9011
 *  net.minecraft.class_9022
 *  net.minecraft.class_9025
 *  net.minecraft.class_9779
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Overwrite
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.ModifyArg
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Comparator;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1041;
import net.minecraft.class_1657;
import net.minecraft.class_1661;
import net.minecraft.class_1799;
import net.minecraft.class_1819;
import net.minecraft.class_2561;
import net.minecraft.class_266;
import net.minecraft.class_268;
import net.minecraft.class_269;
import net.minecraft.class_270;
import net.minecraft.class_329;
import net.minecraft.class_332;
import net.minecraft.class_5250;
import net.minecraft.class_5348;
import net.minecraft.class_9011;
import net.minecraft.class_9022;
import net.minecraft.class_9025;
import net.minecraft.class_9779;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;
import wtf.opal.client.feature.module.impl.visual.MotionBlurModule;
import wtf.opal.client.feature.module.impl.visual.PostProcessingModule;
import wtf.opal.client.feature.module.impl.visual.StreamerModeModule;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.repository.ModuleRepository;
import wtf.opal.client.renderer.MinecraftRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.shader.ShaderFramebuffer;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.render.RenderScreenEvent;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.SidebarEntry;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_329.class})
public abstract class InGameHudMixin {
    @Final
    @Shadow
    private static Comparator<class_9011> field_47550;
    @Unique
    private float sbRectX;
    @Unique
    private float sbRectY;
    @Unique
    private float sbRectWidth;
    @Unique
    private float sbRectHeight;

    private InGameHudMixin() {
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void render(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        MotionBlurModule motionBlur;
        float tickDelta = tickCounter.method_60637(false);
        this.applyPostProcessing(context, tickDelta);
        class_1041 window = Constants.mc.method_22683();
        int scaledWidth = window.method_4486();
        int scaledHeight = window.method_4502();
        double mouseX = Constants.mc.field_1729.method_1603() * ((double)scaledWidth / (double)window.method_4480());
        double mouseY = Constants.mc.field_1729.method_1604() * ((double)scaledHeight / (double)window.method_4507());
        boolean bloomActive = OpalClient.getInstance().getModuleRepository().getModule(PostProcessingModule.class) != null && OpalClient.getInstance().getModuleRepository().getModule(PostProcessingModule.class).isBloom();
        NVGRenderer.beginFrame();
        if (bloomActive) {
            NVGRenderer.rect(0.0f, 0.0f, (float)scaledWidth, (float)scaledHeight, NVGRenderer.GLOW_PAINT);
        }
        if ((motionBlur = OpalClient.getInstance().getModuleRepository().getModule(MotionBlurModule.class)) != null) {
            motionBlur.renderMotionBlur(context, tickDelta, scaledWidth, scaledHeight);
        }
        this.opal$renderScoreboardRect(false);
        EventDispatcher.dispatch(new RenderScreenEvent(context, tickDelta, mouseX, mouseY));
        NVGRenderer.endFrameAndReset(true);
        if (ShaderFramebuffer.getGlowFramebuffer() != null) {
            RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(ShaderFramebuffer.getGlowFramebuffer().method_30277(), 0, ShaderFramebuffer.getGlowFramebuffer().method_30278(), 1.0);
        }
        MinecraftRenderer.render();
    }

    @Redirect(method={"tick()V"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerInventory;getSelectedStack()Lnet/minecraft/item/ItemStack;"))
    private class_1799 getMainHandStack(class_1661 instance) {
        SlotHelper slotHelper = SlotHelper.getInstance();
        if (slotHelper.isActive() && slotHelper.getSilence() == SlotHelper.Silence.FULL) {
            return slotHelper.getMainHandStack(Constants.mc.field_1724);
        }
        return instance.method_7391();
    }

    @Redirect(method={"renderHotbar"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerInventory;getSelectedSlot()I"))
    private int onRenderHotbarSlot(class_1661 instance) {
        return SlotHelper.getInstance().getSelectedSlot(instance);
    }

    @Unique
    private void applyPostProcessing(class_332 context, float tickDelta) {
    }

    @Redirect(at=@At(value="INVOKE", target="Lnet/minecraft/entity/player/PlayerEntity;getOffHandStack()Lnet/minecraft/item/ItemStack;"), method={"renderHotbar"})
    public class_1799 hideOffhandSlot(class_1657 player) {
        class_1799 realStack = player.method_6079();
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (animationsModule.isEnabled() && animationsModule.isHideShieldSlotInHotbar() && realStack.method_7909() instanceof class_1819 && animationsModule.isHideShield()) {
            return class_1799.field_8037;
        }
        return realStack;
    }

    @ModifyArg(method={"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/scoreboard/ScoreboardObjective;)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;drawText(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/Text;IIIZ)V"), index=5)
    private boolean hookScoreboardTextShadow(boolean shadow) {
        OverlayModule overlayModule = OpalClient.getInstance().getModuleRepository().getModule(OverlayModule.class);
        return overlayModule.isEnabled() && overlayModule.isScoreboardTextShadow();
    }

    @Inject(method={"renderScoreboardSidebar(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V"}, at={@At(value="HEAD")})
    private void resetScoreboardRectDimensions(class_332 context, class_9779 tickCounter, CallbackInfo ci) {
        this.sbRectX = 0.0f;
        this.sbRectY = 0.0f;
        this.sbRectWidth = 0.0f;
        this.sbRectHeight = 0.0f;
    }

    @Overwrite
    private void method_1757(class_332 drawContext, class_266 objective) {
        int i;
        class_269 scoreboard = objective.method_1117();
        class_9022 numberFormat = objective.method_55380((class_9022)class_9025.field_47567);
        SidebarEntry[] sidebarEntrys = (SidebarEntry[])scoreboard.method_1184(objective).stream().filter(score -> !score.method_55385()).sorted(field_47550).limit(15L).map(scoreboardEntry -> {
            class_268 team = scoreboard.method_1164(scoreboardEntry.comp_2127());
            class_2561 textx = scoreboardEntry.method_55387();
            class_5250 text2 = class_268.method_1142((class_270)team, (class_2561)textx);
            class_5250 text3 = scoreboardEntry.method_55386(numberFormat);
            int ix = Constants.mc.field_1772.method_27525((class_5348)text3);
            return new SidebarEntry((class_2561)text2, (class_2561)text3, ix);
        }).toArray(SidebarEntry[]::new);
        class_2561 text = objective.method_1114();
        int j = i = Constants.mc.field_1772.method_27525((class_5348)text);
        int k = Constants.mc.field_1772.method_1727(": ");
        for (SidebarEntry sidebarEntry : sidebarEntrys) {
            j = Math.max(j, Constants.mc.field_1772.method_27525((class_5348)sidebarEntry.name()) + (sidebarEntry.scoreWidth() > 0 ? k + sidebarEntry.scoreWidth() : 0));
        }
        ModuleRepository moduleRepository = OpalClient.getInstance().getModuleRepository();
        OverlayModule overlayModule = moduleRepository.getModule(OverlayModule.class);
        StreamerModeModule streamerModeModule = moduleRepository.getModule(StreamerModeModule.class);
        boolean textShadow = overlayModule.isEnabled() && overlayModule.isScoreboardTextShadow();
        boolean isOnHypixel = LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer;
        boolean hideServerId = isOnHypixel && streamerModeModule.isEnabled() && streamerModeModule.isHidingServerId();
        float moduleListHeight = overlayModule.getToggledModules().getSettings().isOffsetScoreboard() ? overlayModule.getToggledModules().getTotalHeight() : 0.0f;
        float scale = overlayModule.getScoreboardScale();
        int m = sidebarEntrys.length;
        int n = m * 9;
        int o = (int)((float)drawContext.method_51443() / scale / 2.0f + (float)n / 3.0f);
        int q = (int)((float)drawContext.method_51421() / scale - (float)j - 3.0f);
        int r = (int)((float)drawContext.method_51421() / scale - 3.0f + 2.0f);
        int u = o - m * 9;
        if (moduleListHeight != 0.0f && (moduleListHeight + 20.0f) / scale > (float)u) {
            int adjustedHeight = (int)((moduleListHeight + 20.0f) / scale);
            int difference = adjustedHeight - u;
            u = adjustedHeight;
            o += difference;
        }
        drawContext.method_51448().pushMatrix();
        drawContext.method_51448().scale(scale);
        drawContext.method_51439(Constants.mc.field_1772, text, q + j / 2 - i / 2 - 1, u - 9, -1, textShadow);
        for (int v = 0; v < m; ++v) {
            String[] parts;
            SidebarEntry sidebarEntry2 = sidebarEntrys[v];
            int w = u + v * 9;
            class_2561 name = sidebarEntry2.name();
            String nameStr = name.getString();
            if (hideServerId && v == 0 && nameStr.contains("/") && nameStr.contains("  ") && (parts = nameStr.split(" {2}")).length > 1) {
                name = class_2561.method_43470((String)("\u00a77" + parts[0] + "  \u00a78\u00a7k" + parts[1]));
            }
            drawContext.method_51439(Constants.mc.field_1772, name, q - 1, w, -1, textShadow);
            drawContext.method_51439(Constants.mc.field_1772, sidebarEntry2.score(), r - sidebarEntry2.scoreWidth() - 1, w, -1, textShadow);
        }
        drawContext.method_51448().popMatrix();
        this.sbRectX = ((float)(q - 2 - 2) - 0.5f) * scale;
        this.sbRectY = (float)(u - 9 - 1 - 1) * scale;
        this.sbRectWidth = ((float)r - 0.5f) * scale - this.sbRectX;
        this.sbRectHeight = (float)(m * 9 + 13) * scale;
    }

    @Unique
    private void opal$renderScoreboardRect(boolean bloom) {
        if (this.sbRectWidth == 0.0f || this.sbRectHeight == 0.0f) {
            return;
        }
        if (bloom) {
            NVGRenderer.roundedRect(this.sbRectX, this.sbRectY, this.sbRectWidth, this.sbRectHeight, 1.5f, ColorUtility.applyOpacity(-16777216, 0.75f));
        } else {
            NVGRenderer.roundedRect(this.sbRectX, this.sbRectY, this.sbRectWidth, this.sbRectHeight, 1.5f, Constants.mc.field_1690.method_19345(0.5f));
        }
    }

    @Inject(method={"renderStatusEffectOverlay"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderStatusEffectOverlay(CallbackInfo ci) {
        OverlayModule overlayModule = OpalClient.getInstance().getModuleRepository().getModule(OverlayModule.class);
        if (overlayModule.isEnabled() && !overlayModule.isStatusEffectOverlayEnabled()) {
            ci.cancel();
        }
    }
}

