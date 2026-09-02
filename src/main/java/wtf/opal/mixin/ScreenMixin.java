/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2558
 *  net.minecraft.class_2561
 *  net.minecraft.class_2583
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_412
 *  net.minecraft.class_4185
 *  net.minecraft.class_429
 *  net.minecraft.class_433
 *  net.minecraft.class_437
 *  net.minecraft.class_500
 *  net.minecraft.class_526
 *  net.minecraft.class_639
 *  net.minecraft.class_642
 *  net.minecraft.class_642$class_8678
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2558;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_412;
import net.minecraft.class_4185;
import net.minecraft.class_429;
import net.minecraft.class_433;
import net.minecraft.class_437;
import net.minecraft.class_500;
import net.minecraft.class_526;
import net.minecraft.class_639;
import net.minecraft.class_642;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.ImageRepository;
import wtf.opal.client.screen.TerentXClientMenuScreen;
import wtf.opal.client.screen.click.dropdown.DropdownClickGUI;
import wtf.opal.client.screen.hud.HUDEditorScreen;
import wtf.opal.mixin.ScreenAccessor;
import wtf.opal.utility.misc.RunnableClickEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_437.class})
public abstract class ScreenMixin {
    @Unique
    private static final String[] POPULAR_SERVERS = new String[]{"mc.hypixel.net", "eu.minemen.club", "na.minemen.club", "sa.minemen.club", "gommehd.net", "play.pika-network.net", "top.jartex.fun", "play.blocksmc.com", "play.herobrine.org", "play.cubecraft.net", "lunar.gg", "play.mineberry.net", "play.universespvp.net", "pvp.land", "redesky.gg", "play.manacube.com", "mc.invadedlands.net", "loyis.net", "tubnet.gg", "play.hivemc.com", "mc.applecraft.org", "play.snapcraft.net", "purpleprison.org", "play.insanitycraft.net", "play.craftersmc.net", "play.lemoncloud.org", "play.wildwoodpvr.net", "play.vanilla-anarchy.org", "play.earthmc.net", "play.originrealms.com", "mc.advancius.net", "play.performium.net", "play.neocubest.com", "play.mineplex.com", "play.extremecraft.net", "play.grandtheftmc.net", "play.skyblock.net", "minesuperior.com", "play.complexgaming.net", "mc.safari-craft.net"};
    @Unique
    private static final Random RANDOM = new Random();

    @Inject(method={"handleTextClick"}, at={@At(value="HEAD")}, cancellable=true)
    private void onInvalidClickEvent(class_2583 style, CallbackInfoReturnable<Boolean> cir) {
        class_2558 class_25582 = style.method_10970();
        if (class_25582 instanceof RunnableClickEvent) {
            RunnableClickEvent runnableClickEvent = (RunnableClickEvent)class_25582;
            runnableClickEvent.getRunnable().run();
            cir.setReturnValue((Object)true);
        }
    }

    @Inject(method={"init"}, at={@At(value="TAIL")})
    private void onScreenInit(CallbackInfo ci) {
        class_437 screen = (class_437)this;
        if (screen instanceof class_500) {
            class_500 multiplayerScreen = (class_500)screen;
            int btnW = 100;
            int btnH = 20;
            int btnX = screen.field_22789 - btnW - 10;
            int btnY = 10;
            class_4185 randomServerBtn = class_4185.method_46430((class_2561)class_2561.method_43470((String)"\ud83c\udfb2 Random Server"), button -> {
                String randomIp = POPULAR_SERVERS[RANDOM.nextInt(POPULAR_SERVERS.length)];
                class_310 client = class_310.method_1551();
                class_639 address = class_639.method_2950((String)randomIp);
                class_642 info = new class_642("Random: " + randomIp, randomIp, class_642.class_8678.field_45611);
                class_412.method_36877((class_437)multiplayerScreen, (class_310)client, (class_639)address, (class_642)info, (boolean)false, null);
            }).method_46434(btnX, btnY, btnW, btnH).method_46431();
            ((ScreenAccessor)screen).opal$addDrawableChild(randomServerBtn);
        }
    }

    @Inject(method={"renderBackground"}, at={@At(value="HEAD")}, cancellable=true)
    private void onRenderCustomScreenBackground(class_332 context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        class_437 screen = (class_437)this;
        if (screen instanceof TerentXClientMenuScreen || screen instanceof DropdownClickGUI || screen instanceof HUDEditorScreen) {
            ci.cancel();
            return;
        }
        if (screen instanceof class_526 || screen instanceof class_500 || screen instanceof class_429 || screen instanceof class_433) {
            boolean frameStarted = NVGRenderer.beginFrame();
            NVGImageRenderer bgImg = ImageRepository.getImage("image/mmpng.png");
            if (bgImg != null) {
                bgImg.drawImage(0.0f, 0.0f, screen.field_22789, screen.field_22790);
            } else {
                NVGRenderer.roundedRect(0.0f, 0.0f, (float)screen.field_22789, (float)screen.field_22790, 0.0f, -16249574);
            }
            NVGImageRenderer logoMenu = ImageRepository.getImage("logomeniu.png");
            if (logoMenu == null) {
                logoMenu = ImageRepository.getImage("logo.png");
            }
            if (logoMenu != null) {
                float logoW = 100.0f;
                float logoH = 34.0f;
                float logoX = ((float)screen.field_22789 - logoW) / 2.0f;
                float logoY = screen instanceof class_433 ? (float)screen.field_22790 / 4.0f - 24.0f : 10.0f;
                logoMenu.drawImage(logoX, logoY, logoW, logoH);
            }
            if (frameStarted) {
                NVGRenderer.endFrameAndReset(false);
            }
            ci.cancel();
        }
    }
}

