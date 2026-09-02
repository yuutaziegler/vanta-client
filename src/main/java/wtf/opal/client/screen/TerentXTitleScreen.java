/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11908
 *  net.minecraft.class_11909
 *  net.minecraft.class_332
 *  net.minecraft.class_437
 *  net.minecraft.class_442
 *  net.minecraft.class_500
 *  net.minecraft.class_526
 */
package wtf.opal.client.screen;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_332;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_500;
import net.minecraft.class_526;
import wtf.opal.client.Constants;
import wtf.opal.client.renderer.LiquidGlassRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.repository.ImageRepository;
import wtf.opal.client.screen.TerentXOptionsScreen;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public class TerentXTitleScreen
extends class_442 {
    private final List<IconButton> buttons = new ArrayList<IconButton>();
    private int mouseX;
    private int mouseY;
    private float logoScale = 1.0f;
    private boolean resizingLogo = false;

    public TerentXTitleScreen() {
        super(false);
    }

    protected void method_25426() {
        super.method_25426();
        this.method_37067();
        this.buttons.clear();
        int btnSize = 50;
        int spacing = 12;
        int totalWidth = btnSize * 4 + spacing * 3;
        int startX = this.field_22789 / 2 - totalWidth / 2;
        int startY = this.field_22790 / 2 + 40;
        this.buttons.add(new IconButton(startX, startY, btnSize, btnSize, "singleplayer.png", "Singleplayer", () -> Constants.mc.method_1507((class_437)new class_526((class_437)this))));
        this.buttons.add(new IconButton(startX + (btnSize + spacing), startY, btnSize, btnSize, "multiplayer.png", "Multiplayer", () -> Constants.mc.method_1507((class_437)new class_500((class_437)this))));
        this.buttons.add(new IconButton(startX + (btnSize + spacing) * 2, startY, btnSize, btnSize, "settings.png", "Settings", () -> Constants.mc.method_1507((class_437)new TerentXOptionsScreen((class_437)this, Constants.mc.field_1690))));
        this.buttons.add(new IconButton(startX + (btnSize + spacing) * 3, startY, btnSize, btnSize, "quit.png", "Quit", () -> Constants.mc.method_1592()));
    }

    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        NVGImageRenderer logo;
        this.method_25420(context, mouseX, mouseY, delta);
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        NVGRenderer.beginFrame();
        NVGImageRenderer mmBackground = ImageRepository.getImage("image/mmpng.png");
        if (mmBackground != null) {
            mmBackground.drawImage(0.0f, 0.0f, this.field_22789, this.field_22790);
        }
        if ((logo = ImageRepository.getImage("logomeniu.png")) != null) {
            float logoWidth = 250.0f * this.logoScale;
            float logoHeight = 166.0f * this.logoScale;
            float logoY = (float)this.field_22790 / 2.0f - 150.0f;
            logo.drawImage((float)this.field_22789 / 2.0f - logoWidth / 2.0f, logoY, logoWidth, logoHeight);
        }
        if (this.field_22790 > 424) {
            String welcome = "Welcome, " + Constants.mc.method_1548().method_1676();
            float welcomeWidth = FontRepository.getFont("productsans-medium").getStringWidth(welcome, 12.0f);
            FontRepository.getFont("productsans-medium").drawString(welcome, (float)this.field_22789 / 2.0f - welcomeWidth / 2.0f, (float)this.field_22790 - 28.0f, 12.0f, ColorUtility.applyOpacity(-1, 0.9f));
        }
        for (IconButton button : this.buttons) {
            button.render(mouseX, mouseY, delta, 0.0f);
        }
        if (this.resizingLogo) {
            String resizeHint = "Use mouse wheel to resize logo \u2022 Press T again to exit";
            float resizeHintWidth = FontRepository.getFont("productsans-medium").getStringWidth(resizeHint, 11.0f);
            FontRepository.getFont("productsans-medium").drawString(resizeHint, (float)this.field_22789 / 2.0f - resizeHintWidth / 2.0f, 20.0f, 11.0f, ColorUtility.applyOpacity(-256, 0.9f));
        }
        NVGRenderer.endFrame(true);
    }

    public boolean method_25402(class_11909 click, boolean doubled) {
        if (click.method_74245() == 0) {
            for (IconButton btn : this.buttons) {
                if (!btn.isHovered(click.comp_4798(), click.comp_4799(), 0.0f)) continue;
                btn.action.run();
                return true;
            }
        }
        return super.method_25402(click, doubled);
    }

    public boolean method_25404(class_11908 keyInput) {
        if (keyInput.comp_4795() == 84) {
            this.resizingLogo = !this.resizingLogo;
            return true;
        }
        return super.method_25404(keyInput);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.resizingLogo) {
            this.logoScale += (float)verticalAmount * 0.1f;
            this.logoScale = Math.max(0.5f, Math.min(3.0f, this.logoScale));
            return true;
        }
        return super.method_25401(mouseX, mouseY, horizontalAmount, verticalAmount);
    }

    private float getUnlockProgress() {
        return 1.0f;
    }

    public boolean method_25421() {
        return false;
    }

    @Environment(value=EnvType.CLIENT)
    private static class IconButton {
        private final float x;
        private final float y;
        private final float width;
        private final float height;
        private final String iconPath;
        private final String tooltip;
        private final Runnable action;
        private float hoverAnim = 0.0f;
        private float slideInAnim = 0.0f;

        public IconButton(float x, float y, float width, float height, String iconPath, String tooltip, Runnable action) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.iconPath = iconPath;
            this.tooltip = tooltip;
            this.action = action;
        }

        public void render(int mouseX, int mouseY, float delta, float menuY) {
            NVGImageRenderer iconImage;
            boolean hovered = this.isHovered(mouseX, mouseY, menuY);
            this.hoverAnim = hovered ? Math.min(1.0f, this.hoverAnim + 0.15f) : Math.max(0.0f, this.hoverAnim - 0.15f);
            this.slideInAnim = Math.min(1.0f, this.slideInAnim + 0.08f);
            float targetY = this.y + menuY + 10.0f * (1.0f - this.slideInAnim);
            float targetX = this.x;
            float targetWidth = this.width;
            float targetHeight = this.height;
            if (this.hoverAnim > 0.0f) {
                float scale = 1.0f + this.hoverAnim * 0.15f;
                float expandW = (targetWidth * scale - targetWidth) / 2.0f;
                float expandH = (targetHeight * scale - targetHeight) / 2.0f;
                targetX -= expandW;
                targetY -= expandH;
                targetWidth *= scale;
                targetHeight *= scale;
            }
            // Liquid glass button backing
            LiquidGlassRenderer.drawGlassPanel(targetX - 4.0f, targetY - 4.0f, targetWidth + 8.0f, targetHeight + 8.0f, 14.0f);
            if ((iconImage = ImageRepository.getImage("image/" + this.iconPath)) != null) {
                iconImage.drawImage(targetX, targetY, targetWidth, targetHeight);
            }
            if (hovered && this.hoverAnim > 0.5f) {
                float tooltipScale = 11.0f;
                float tooltipWidth = FontRepository.getFont("productsans-medium").getStringWidth(this.tooltip, tooltipScale);
                float tooltipX = targetX + targetWidth / 2.0f - tooltipWidth / 2.0f;
                float tooltipY = targetY - 22.0f;
                float tooltipPadding = 8.0f;
                LiquidGlassRenderer.drawGlassPanel(tooltipX - tooltipPadding, tooltipY - tooltipPadding, tooltipWidth + tooltipPadding * 2.0f, tooltipScale + tooltipPadding * 2.0f, 6.0f);
                NVGRenderer.roundedRect(tooltipX - tooltipPadding, tooltipY - tooltipPadding, tooltipWidth + tooltipPadding * 2.0f, tooltipScale + tooltipPadding * 2.0f, 6.0f, ColorUtility.applyOpacity(-16777216, 0.45f * this.hoverAnim));
                FontRepository.getFont("productsans-medium").drawString(this.tooltip, tooltipX, tooltipY + 7.0f, tooltipScale, ColorUtility.applyOpacity(-1, this.hoverAnim));
            }
        }

        public boolean isHovered(double mouseX, double mouseY, float yOffset) {
            return mouseX >= (double)this.x && mouseX <= (double)(this.x + this.width) && mouseY >= (double)(this.y + yOffset) && mouseY <= (double)(this.y + yOffset + this.height);
        }
    }
}

