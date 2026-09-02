/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
 *  net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
 *  net.minecraft.class_11909
 *  net.minecraft.class_124
 *  net.minecraft.class_2561
 *  net.minecraft.class_2960
 *  net.minecraft.class_304
 *  net.minecraft.class_304$class_11900
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_364
 *  net.minecraft.class_3675$class_307
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 */
package restudio.reglass.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.class_11909;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2960;
import net.minecraft.class_304;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_364;
import net.minecraft.class_3675;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import restudio.reglass.client.LiquidGlassWidget;
import restudio.reglass.client.api.WidgetStyle;
import restudio.reglass.client.config.ReGlassSettingsIO;
import restudio.reglass.client.screen.config.ReGlassConfigScreen;

public class ReGlassClient
implements ClientModInitializer {
    private static class_304 playgroundKey;
    private static class_304 configKey;
    public static class_304.class_11900 reGlassCategory;
    public static class_310 minecraftClient;

    public void onInitializeClient() {
        minecraftClient = class_310.method_1551();
        playgroundKey = KeyBindingHelper.registerKeyBinding((class_304)new class_304("ReGlass Playground", class_3675.class_307.field_1668, 72, reGlassCategory));
        configKey = KeyBindingHelper.registerKeyBinding((class_304)new class_304("ReGlass Config", class_3675.class_307.field_1668, 71, reGlassCategory));
        ReGlassSettingsIO.loadIntoMemory();
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (configKey.method_1436()) {
                client.method_1507((class_437)new ReGlassConfigScreen(null));
            }
            if (playgroundKey.method_1436()) {
                client.method_1507((class_437)new PlaygroundScreen());
            }
        });
    }

    static {
        reGlassCategory = new class_304.class_11900(class_2960.method_60655((String)"reglass", (String)"category"));
    }

    public static class PlaygroundScreen
    extends class_437 {
        private boolean blur;

        public PlaygroundScreen() {
            super((class_2561)class_2561.method_43470((String)"ReGlass Playground"));
        }

        protected void method_25426() {
            super.method_25426();
            WidgetStyle customStyle = WidgetStyle.create().tint(class_124.field_1065.method_532(), 0.4f).blurRadius(0).shadow(25.0f, 0.2f, 0.0f, 3.0f).smoothing(0.05f).shadowColor(0, 1.0f);
            this.method_37063((class_364)new LiquidGlassWidget(this.field_22789 / 2 - 75, this.field_22790 / 2 - 25, 150, 50, customStyle).setMoveable(true));
            this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43470((String)"Toggle BG Blur"), b -> {
                this.blur = !this.blur;
            }).method_46434(10, 10, 120, 20).method_46431());
        }

        public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
            context.method_51439(ReGlassClient.minecraftClient.field_1772, (class_2561)class_2561.method_43470((String)"This is a Minecraft Screen"), this.field_22789 / 2 - 70, 10, -1, true);
            super.method_25394(context, mouseX, mouseY, delta);
        }

        public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
            if (this.blur) {
                super.method_25420(context, mouseX, mouseY, delta);
            }
        }

        public boolean method_25402(class_11909 click, boolean isDouble) {
            if (click.method_74245() == 1) {
                ((LiquidGlassWidget)this.method_37063((class_364)new LiquidGlassWidget((int)click.comp_4798() - 50, (int)click.comp_4799() - 50, 100, 100, WidgetStyle.create().smoothing(0.05f)))).setMoveable(true);
                return true;
            }
            return super.method_25402(click, isDouble);
        }
    }
}

