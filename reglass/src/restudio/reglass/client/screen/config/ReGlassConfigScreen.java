/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.class_2561
 *  net.minecraft.class_332
 *  net.minecraft.class_339
 *  net.minecraft.class_3532
 *  net.minecraft.class_364
 *  net.minecraft.class_4185
 *  net.minecraft.class_437
 *  net.minecraft.class_6382
 */
package restudio.reglass.client.screen.config;

import java.lang.invoke.LambdaMetafactory;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.class_2561;
import net.minecraft.class_332;
import net.minecraft.class_339;
import net.minecraft.class_3532;
import net.minecraft.class_364;
import net.minecraft.class_4185;
import net.minecraft.class_437;
import net.minecraft.class_6382;
import restudio.reglass.client.LiquidGlassWidget;
import restudio.reglass.client.api.ReGlassConfig;
import restudio.reglass.client.api.WidgetStyle;
import restudio.reglass.client.config.ReGlassSettingsIO;
import restudio.reglass.client.ui.MappedSlider;

public class ReGlassConfigScreen
extends class_437 {
    private final class_437 parent;
    private final List<PositionedWidget> positionedWidgets = new ArrayList<PositionedWidget>();
    private LiquidGlassWidget previewCircle;
    private LiquidGlassWidget previewRounded;
    private double scrollPosition;
    private int totalListHeight;

    public ReGlassConfigScreen(class_437 parent) {
        super((class_2561)class_2561.method_43470((String)"ReGlass Configuration"));
        this.parent = parent;
    }

    protected void method_25426() {
        super.method_25426();
        this.positionedWidgets.clear();
        int listWidth = Math.min(300, this.field_22789 / 2 - 20);
        int widgetWidth = listWidth - 20;
        int widgetX = 20;
        int y = 5;
        int gap = 4;
        int widgetHeight = 20;
        ReGlassConfig cfg = ReGlassConfig.INSTANCE;
        this.addTitle("General", widgetX, y, widgetWidth);
        class_4185 enableRedesignButton = class_4185.method_46430((class_2561)this.getEnableRedesignText(), button -> {
            cfg.features.enableRedesign = !cfg.features.enableRedesign;
            button.method_25355(this.getEnableRedesignText());
            this.field_22787.method_1507((class_437)new ReGlassConfigScreen(this.parent));
        }).method_46434(widgetX, y += widgetHeight, widgetWidth, widgetHeight).method_46431();
        this.addPositionedWidget(enableRedesignButton, y);
        class_4185 enableButtonsButton = class_4185.method_46430((class_2561)this.getFeatureText("Buttons", cfg.features.buttons), button -> {
            cfg.features.buttons = !cfg.features.buttons;
            button.method_25355(this.getFeatureText("Buttons", cfg.features.buttons));
        }).method_46434(widgetX, y += widgetHeight + gap, widgetWidth, widgetHeight).method_46431();
        this.addPositionedWidget(enableButtonsButton, (int)y).field_22763 = cfg.features.enableRedesign;
        class_4185 enableSlidersButton = class_4185.method_46430((class_2561)this.getFeatureText("Sliders", cfg.features.sliders), button -> {
            cfg.features.sliders = !cfg.features.sliders;
            button.method_25355(this.getFeatureText("Sliders", cfg.features.sliders));
        }).method_46434(widgetX, y += widgetHeight + gap, widgetWidth, widgetHeight).method_46431();
        this.addPositionedWidget(enableSlidersButton, (int)y).field_22763 = cfg.features.enableRedesign;
        class_4185 enableHotbarButton = class_4185.method_46430((class_2561)this.getFeatureText("Hotbar", cfg.features.hotbar), button -> {
            cfg.features.hotbar = !cfg.features.hotbar;
            button.method_25355(this.getFeatureText("Hotbar", cfg.features.hotbar));
        }).method_46434(widgetX, y += widgetHeight + gap, widgetWidth, widgetHeight).method_46431();
        this.addPositionedWidget(enableHotbarButton, (int)y).field_22763 = cfg.features.enableRedesign;
        class_4185 cancelDarkeningButton = class_4185.method_46430((class_2561)this.getFeatureText("Cancel Screen Darkening", cfg.features.cancelScreenDarkening), button -> {
            cfg.features.cancelScreenDarkening = !cfg.features.cancelScreenDarkening;
            button.method_25355(this.getFeatureText("Cancel Screen Darkening", cfg.features.cancelScreenDarkening));
        }).method_46434(widgetX, y += widgetHeight + gap, widgetWidth, widgetHeight).method_46431();
        this.addPositionedWidget(cancelDarkeningButton, (int)y).field_22763 = cfg.features.enableRedesign;
        this.addTitle("Appearance", widgetX, y += widgetHeight + gap * 2, widgetWidth);
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Tint Alpha"), (double)0.0, (double)1.0, (double)((double)cfg.defaultTintAlpha), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$5(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.intSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Blur Radius"), (int)0, (int)32, (int)cfg.defaultBlurRadius, (Consumer<Integer>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$6(restudio.reglass.client.api.ReGlassConfig java.lang.Integer ), (Ljava/lang/Integer;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Smoothing"), (double)((double)-0.02f), (double)((double)0.02f), (double)((double)cfg.defaultSmoothing), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$7(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addTitle("Shadow", widgetX, y += widgetHeight + gap * 2, widgetWidth);
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Shadow Expand"), (double)0.0, (double)100.0, (double)((double)cfg.defaultShadowExpand), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$8(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Shadow Factor"), (double)0.0, (double)1.0, (double)((double)cfg.defaultShadowFactor), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$9(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Shadow Offset Y"), (double)-10.0, (double)10.0, (double)((double)cfg.defaultShadowOffsetY), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$10(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addTitle("Refraction", widgetX, y += widgetHeight + gap * 2, widgetWidth);
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Refraction Thickness"), (double)1.0, (double)60.0, (double)((double)cfg.defaultRefThickness), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$11(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Refraction Factor"), (double)1.0, (double)2.5, (double)((double)cfg.defaultRefFactor), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$12(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Fresnel Range"), (double)0.0, (double)60.0, (double)((double)cfg.defaultRefFresnelRange), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$13(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Fresnel Hardness"), (double)0.0, (double)100.0, (double)((double)cfg.defaultRefFresnelHardness), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$14(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Fresnel Factor"), (double)0.0, (double)100.0, (double)((double)cfg.defaultRefFresnelFactor), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$15(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addTitle("Glare", widgetX, y += widgetHeight + gap * 2, widgetWidth);
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Glare Range"), (double)0.0, (double)60.0, (double)((double)cfg.defaultGlareRange), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$16(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Glare Factor"), (double)0.0, (double)100.0, (double)((double)cfg.defaultGlareFactor), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$17(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addTitle("Interactions", widgetX, y += widgetHeight + gap * 2, widgetWidth);
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Hover Scale (px)"), (double)0.0, (double)6.0, (double)((double)cfg.hoverScalePx), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$18(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Focus Scale (px)"), (double)0.0, (double)8.0, (double)((double)cfg.focusScalePx), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$19(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Focus Border Width (px)"), (double)0.0, (double)6.0, (double)((double)cfg.focusBorderWidthPx), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$20(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Focus Border Intensity"), (double)0.0, (double)1.0, (double)((double)cfg.focusBorderIntensity), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$21(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Focus Border Speed"), (double)0.0, (double)4.0, (double)((double)cfg.focusBorderSpeed), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$22(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addTitle("Debug", widgetX, y += widgetHeight + gap * 2, widgetWidth);
        class_4185 pixelatedGridButton = class_4185.method_46430((class_2561)this.getFeatureText("Pixelated Grid", cfg.features.pixelatedGrid), button -> {
            cfg.features.pixelatedGrid = !cfg.features.pixelatedGrid;
            button.method_25355(this.getFeatureText("Pixelated Grid", cfg.features.pixelatedGrid));
        }).method_46434(widgetX, y += widgetHeight, widgetWidth, widgetHeight).method_46431();
        this.addPositionedWidget(pixelatedGridButton, (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.floatSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Grid Size"), (double)1.0, (double)32.0, (double)((double)cfg.pixelatedGridSize), (Consumer<Double>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$24(restudio.reglass.client.api.ReGlassConfig java.lang.Double ), (Ljava/lang/Double;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.addSlider((MappedSlider)MappedSlider.intSlider((int)widgetX, (int)(y += widgetHeight + gap), (int)widgetWidth, (int)widgetHeight, (class_2561)class_2561.method_43470((String)"Debug Step"), (int)0, (int)9, (int)Math.round((float)cfg.debugStep), (Consumer<Integer>)LambdaMetafactory.metafactory(null, null, null, (Ljava/lang/Object;)V, lambda$init$25(restudio.reglass.client.api.ReGlassConfig java.lang.Integer ), (Ljava/lang/Integer;)V)((ReGlassConfig)cfg)), (int)y).field_22763 = cfg.features.enableRedesign;
        this.totalListHeight = y += widgetHeight + gap;
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43471((String)"controls.reset"), b -> {
            ReGlassSettingsIO.apply(new ReGlassSettingsIO.Data());
            if (this.field_22787 != null) {
                this.field_22787.method_1507((class_437)new ReGlassConfigScreen(this.parent));
            }
        }).method_46434(this.field_22789 / 2 - 100, this.field_22790 - 28, 98, 20).method_46431());
        this.method_37063((class_364)class_4185.method_46430((class_2561)class_2561.method_43471((String)"gui.done"), b -> this.method_25419()).method_46434(this.field_22789 / 2 + 2, this.field_22790 - 28, 98, 20).method_46431());
        int previewX = this.field_22789 / 2 + 20;
        int previewY = this.field_22790 / 2 - 50;
        WidgetStyle s1 = WidgetStyle.create().tint(0xFFFFFF, Math.min(1.0f, Math.max(0.0f, cfg.defaultTintAlpha))).blurRadius(cfg.defaultBlurRadius).shadow(cfg.defaultShadowExpand, cfg.defaultShadowFactor, cfg.defaultShadowOffsetX, cfg.defaultShadowOffsetY).shadowColor(cfg.defaultShadowColor, cfg.defaultShadowColorAlpha).refractionThickness(cfg.defaultRefThickness).refractionFactor(cfg.defaultRefFactor).refractionDispersion(cfg.defaultRefDispersion).fresnelRange(cfg.defaultRefFresnelRange).fresnelHardness(cfg.defaultRefFresnelHardness).fresnelFactor(cfg.defaultRefFresnelFactor).glareRange(cfg.defaultGlareRange).glareHardness(cfg.defaultGlareHardness).glareConvergence(cfg.defaultGlareConvergence).glareOppositeFactor(cfg.defaultGlareOppositeFactor).glareFactor(cfg.defaultGlareFactor).glareAngleRad(cfg.defaultGlareAngleRad);
        this.previewCircle = (LiquidGlassWidget)this.method_37063((class_364)new LiquidGlassWidget(previewX, previewY, 100, 100, s1).setCornerRadiusPx(50.0f));
        WidgetStyle s2 = WidgetStyle.create().tint(cfg.defaultTintColor, cfg.defaultTintAlpha).blurRadius(cfg.defaultBlurRadius).shadow(cfg.defaultShadowExpand, cfg.defaultShadowFactor, cfg.defaultShadowOffsetX, cfg.defaultShadowOffsetY).shadowColor(cfg.defaultShadowColor, cfg.defaultShadowColorAlpha);
        this.previewRounded = (LiquidGlassWidget)this.method_37063((class_364)new LiquidGlassWidget(previewX + 110, previewY + 20, 140, 60, s2).setCornerRadiusPx(16.0f));
    }

    private class_2561 getEnableRedesignText() {
        return class_2561.method_43470((String)"ReGlass Redesign: ").method_10852((class_2561)(ReGlassConfig.INSTANCE.features.enableRedesign ? class_2561.method_43471((String)"options.on") : class_2561.method_43471((String)"options.off")));
    }

    private class_2561 getFeatureText(String feature, boolean enabled) {
        return class_2561.method_43470((String)(feature + ": ")).method_10852((class_2561)(enabled ? class_2561.method_43471((String)"options.on") : class_2561.method_43471((String)"options.off")));
    }

    private <T extends class_339> T addPositionedWidget(T widget, int y) {
        this.positionedWidgets.add(new PositionedWidget(widget, y));
        return (T)((class_339)this.method_37063((class_364)widget));
    }

    private MappedSlider addSlider(MappedSlider slider, int y) {
        return this.addPositionedWidget(slider, y);
    }

    private void addTitle(String title, int x, int y, int width) {
        this.addPositionedWidget(new TitleWidget(x, y, width, 20, (class_2561)class_2561.method_43470((String)title)), y);
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        this.method_25420(context, mouseX, mouseY, delta);
        int listTop = 32;
        int listBottom = this.field_22790 - 32;
        for (PositionedWidget pw : this.positionedWidgets) {
            pw.widget.method_46419(pw.y() + listTop - (int)this.scrollPosition);
            class_339 class_3392 = pw.widget();
            if (class_3392 instanceof TitleWidget) {
                TitleWidget tw = (TitleWidget)class_3392;
                tw.field_22764 = pw.widget.method_46427() >= listTop && pw.widget.method_46427() + 20 <= listBottom;
                continue;
            }
            pw.widget.field_22764 = pw.widget.method_46427() >= listTop && pw.widget.method_46427() + pw.widget.method_25364() <= listBottom;
        }
        super.method_25394(context, mouseX, mouseY, delta);
        context.method_27534(this.field_22793, class_2561.method_30163((String)"ReGlass Config (Scrollable)"), this.field_22789 / 2, 15, -1);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int listHeight = this.field_22790 - 32 - 32;
        int maxScroll = Math.max(0, this.totalListHeight - listHeight);
        if (maxScroll > 0) {
            this.scrollPosition -= verticalAmount * 10.0;
            this.scrollPosition = class_3532.method_15350((double)this.scrollPosition, (double)0.0, (double)maxScroll);
            return true;
        }
        return false;
    }

    public void method_25419() {
        ReGlassSettingsIO.saveFromMemory();
        if (this.field_22787 != null) {
            this.field_22787.method_1507(this.parent);
        }
    }

    private static /* synthetic */ void lambda$init$25(ReGlassConfig cfg, Integer v) {
        cfg.debugStep = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$24(ReGlassConfig cfg, Double v) {
        cfg.pixelatedGridSize = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$22(ReGlassConfig cfg, Double v) {
        cfg.focusBorderSpeed = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$21(ReGlassConfig cfg, Double v) {
        cfg.focusBorderIntensity = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$20(ReGlassConfig cfg, Double v) {
        cfg.focusBorderWidthPx = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$19(ReGlassConfig cfg, Double v) {
        cfg.focusScalePx = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$18(ReGlassConfig cfg, Double v) {
        cfg.hoverScalePx = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$17(ReGlassConfig cfg, Double v) {
        cfg.defaultGlareFactor = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$16(ReGlassConfig cfg, Double v) {
        cfg.defaultGlareRange = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$15(ReGlassConfig cfg, Double v) {
        cfg.defaultRefFresnelFactor = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$14(ReGlassConfig cfg, Double v) {
        cfg.defaultRefFresnelHardness = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$13(ReGlassConfig cfg, Double v) {
        cfg.defaultRefFresnelRange = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$12(ReGlassConfig cfg, Double v) {
        cfg.defaultRefFactor = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$11(ReGlassConfig cfg, Double v) {
        cfg.defaultRefThickness = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$10(ReGlassConfig cfg, Double v) {
        cfg.defaultShadowOffsetY = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$9(ReGlassConfig cfg, Double v) {
        cfg.defaultShadowFactor = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$8(ReGlassConfig cfg, Double v) {
        cfg.defaultShadowExpand = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$7(ReGlassConfig cfg, Double v) {
        cfg.defaultSmoothing = v.floatValue();
    }

    private static /* synthetic */ void lambda$init$6(ReGlassConfig cfg, Integer v) {
        cfg.defaultBlurRadius = v;
    }

    private static /* synthetic */ void lambda$init$5(ReGlassConfig cfg, Double v) {
        cfg.defaultTintAlpha = v.floatValue();
    }

    private record PositionedWidget(class_339 widget, int y) {
    }

    private class TitleWidget
    extends class_339 {
        public TitleWidget(int x, int y, int width, int height, class_2561 message) {
            super(x, y, width, height, message);
        }

        public void method_48579(class_332 context, int mouseX, int mouseY, float delta) {
            if (this.field_22764) {
                context.method_27534(ReGlassConfigScreen.this.field_22793, this.method_25369(), this.method_46426() + this.method_25368() / 2, this.method_46427() + (this.method_25364() - 8) / 2, 0xFFFFFF);
            }
        }

        public boolean method_25405(double mouseX, double mouseY) {
            return false;
        }

        protected void method_47399(class_6382 builder) {
        }
    }
}

