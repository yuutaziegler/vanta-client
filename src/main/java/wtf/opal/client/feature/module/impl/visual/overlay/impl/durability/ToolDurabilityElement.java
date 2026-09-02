/*
 * Tool Durability Element for HUD - Shows durability of held tools/weapons
 * Supports swords, pickaxes, and any item with durability
 * Does NOT get dark when blocking (R-Shift)
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.durability;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1799;
import net.minecraft.class_332;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.HudSettingsModule;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.client.renderer.LiquidGlassRenderer;
import wtf.opal.client.renderer.MinecraftRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;

@Environment(value=EnvType.CLIENT)
public final class ToolDurabilityElement implements IOverlayElement {
    private static final NVGTextRenderer MED_FONT = FontRepository.getFont("inter-medium");
    private static final NVGTextRenderer BOLD_FONT = FontRepository.getFont("inter-bold");
    
    // Main settings
    private final BooleanProperty enabled = new BooleanProperty("Enabled", true);
    private final ModeProperty<DisplayMode> displayMode = new ModeProperty<DisplayMode>("Mode", DisplayMode.PERCENTAGE);
    private final BooleanProperty showItemName = new BooleanProperty("Show Item Name", false);
    
    // Visual settings
    private final BooleanProperty showBar = new BooleanProperty("Show Bar", true);
    private final BooleanProperty showNumbers = new BooleanProperty("Show Numbers", true);
    private final BooleanProperty showIcon = new BooleanProperty("Show Icon", true);
    private final BooleanProperty showBlockCount = new BooleanProperty("Show Block Count", true);
    private final BooleanProperty colorfulBar = new BooleanProperty("Colorful Bar", true);
    
    // Bar settings
    private final NumberProperty barHeight = new NumberProperty("Bar Height", 3.0, 1.0, 8.0, 0.5);
    private final BooleanProperty gradientBar = new BooleanProperty("Gradient Bar", true);
    
    // Position
    private final ScreenPositionProperty position = new ScreenPositionProperty("Screen Position", 0.5f, 0.85f);
    
    // Cached values for smooth animation
    private float animatedPercentage = 1.0f;
    
    public ToolDurabilityElement(OverlayModule module) {
        module.addProperties(new GroupProperty("Tool Durability", 
            this.enabled, 
            this.displayMode,
            new GroupProperty("Visuals", this.showBar, this.showNumbers, this.showIcon, this.showItemName, this.showBlockCount, this.colorfulBar, this.barHeight, this.gradientBar),
            this.position
        ));
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
        if (Constants.mc.field_1724 == null) {
            return;
        }
        
        // Get held item - this works even when blocking (R-Shift)
        class_1799 heldItem = Constants.mc.field_1724.method_6047().method_7960() ? null : Constants.mc.field_1724.method_6047();
        
        if (heldItem == null || !heldItem.method_7963()) {
            return;
        }
        
        int maxDurability = heldItem.method_7936();
        int currentDurability = heldItem.method_7919();
        int remainingDurability = maxDurability - currentDurability;
        float targetPercentage = (float)remainingDurability / (float)maxDurability;
        
        // Smooth animation
        animatedPercentage += (targetPercentage - animatedPercentage) * 0.1f;
        float percentage = animatedPercentage;
        
        // Calculate dimensions
        float padding = 6.0f;
        float iconSize = 18.0f;
        float spacing = 4.0f;
        
        String durabilityText = remainingDurability + "";
        String percentageText = Math.round(percentage * 100) + "%";
        String hitsText = "\u2248" + remainingDurability + " hits";
        String blocksText = "\u2248" + remainingDurability + " blocks";
        float textWidth = Math.max(MED_FONT.getStringWidth(durabilityText, 7.0f), MED_FONT.getStringWidth(percentageText, 7.0f));
        switch ((DisplayMode)this.displayMode.getValue()) {
            case HITS_LEFT:
                textWidth = MED_FONT.getStringWidth(hitsText, 6.5f);
                break;
            case BLOCKS_LEFT:
                textWidth = MED_FONT.getStringWidth(blocksText, 6.5f);
                break;
            default:
                break;
        }
        
        float width = padding * 2.0f;
        if (this.showIcon.getValue()) {
            width += iconSize + spacing;
        }
        if (this.showNumbers.getValue()) {
            width += textWidth;
        }
        
        float height = padding * 2.0f + iconSize;
        if (this.showBar.getValue()) {
            height += 4.0f;
        }
        
        this.position.setWidth(width);
        this.position.setHeight(height);
        
        float x = this.position.getScaledX();
        float y = this.position.getScaledY();
        
        HudSettingsModule hudSettings = OpalClient.getInstance().getModuleRepository().getModule(HudSettingsModule.class);
        float radius = hudSettings != null && hudSettings.isEnabled() ? hudSettings.getCornerRadius() : 8.0f;
        
        // Draw background - always full brightness, even when blocking
        LiquidGlassRenderer.drawGlassPanel(x, y, width, height, radius);
        
        float currentX = x + padding;
        
        // Draw item icon - renders normally regardless of blocking state
        if (this.showIcon.getValue()) {
            final float finalCurrentX = currentX;
            final float finalY = y;
            MinecraftRenderer.addToQueue(() -> {
                context.method_71048();
                context.method_51427(heldItem, (int)finalCurrentX, (int)(finalY + padding));
                context.method_51431(Constants.mc.field_1772, heldItem, (int)finalCurrentX, (int)(finalY + padding));
            });
            currentX += iconSize + spacing;
        }
        
        // Calculate bar dimensions
        float barY = y + height - padding - this.barHeight.getValue().floatValue();
        float barWidth = width - padding * 2.0f - (this.showIcon.getValue() ? iconSize + spacing : 0.0f);
        float barHeightVal = this.barHeight.getValue().floatValue();
        
        // Draw durability bar - full color, no darkening when blocking
        if (this.showBar.getValue()) {
            NVGRenderer.roundedRect(currentX, barY, barWidth, barHeightVal, 2.0f, 0x40FFFFFF);
            
            int barColor = getBarColor(percentage);
            
            if (this.gradientBar.getValue()) {
                // Gradient from current color to red
                int gradientEndColor = 0xFFFF5555; // Red
                NVGRenderer.roundedRectGradient(currentX, barY, barWidth * percentage, barHeightVal, 2.0f, barColor, gradientEndColor, 0.0f);
            } else {
                NVGRenderer.roundedRect(currentX, barY, barWidth * percentage, barHeightVal, 2.0f, barColor);
            }
        }
        
        // Draw numbers
        if (this.showNumbers.getValue()) {
            float textY = y + padding + (iconSize - 7.0f) / 2.0f;
            
            switch ((DisplayMode)this.displayMode.getValue()) {
                case NUMBERS:
                    MED_FONT.drawString(durabilityText, currentX, textY, 7.0f, -1);
                    break;
                case PERCENTAGE:
                    MED_FONT.drawString(percentageText, currentX, textY, 7.0f, getPercentageColor(percentage));
                    break;
                case BOTH:
                    MED_FONT.drawString(durabilityText, currentX, textY - 4.0f, 6.0f, -1);
                    MED_FONT.drawString(percentageText, currentX, textY + 6.0f, 6.0f, getPercentageColor(percentage));
                    break;
                case HITS_LEFT:
                    MED_FONT.drawString(hitsText, currentX, textY, 6.5f, getPercentageColor(percentage));
                    break;
                case BLOCKS_LEFT:
                    MED_FONT.drawString(blocksText, currentX, textY, 6.5f, getPercentageColor(percentage));
                    break;
            }
            // Extra info line: how many blocks this tool can still break
            if (this.showBlockCount.getValue() && this.displayMode.getValue() != DisplayMode.BLOCKS_LEFT && this.displayMode.getValue() != DisplayMode.HITS_LEFT) {
                MED_FONT.drawString("\u2248" + remainingDurability + " blocks left", currentX, textY + 9.0f, 5.0f, 0xFFBBBBBB);
            }
        }
        if (this.showItemName.getValue()) {
            String itemName = heldItem.method_7964().method_54160();
            MED_FONT.drawString(itemName, x + padding, y - 8.0f, 5.5f, 0xFFDDDDDD);
        }
    }
    
    private int getBarColor(float percentage) {
        if (this.colorfulBar.getValue()) {
            if (percentage > 0.5f) {
                return 0xFF55FF55; // Green
            } else if (percentage > 0.25f) {
                return 0xFFFFFF55; // Yellow
            } else if (percentage > 0.1f) {
                return 0xFFFFAA00; // Orange
            } else {
                return 0xFFFF5555; // Red
            }
        } else {
            return 0xFF55FF55; // Default green
        }
    }
    
    private int getPercentageColor(float percentage) {
        if (percentage > 0.5f) {
            return 0xFF55FF55;
        } else if (percentage > 0.25f) {
            return 0xFFFFFF55;
        } else if (percentage > 0.1f) {
            return 0xFFFFAA00;
        } else {
            return 0xFFFF5555;
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum DisplayMode {
        NUMBERS("Numbers"),
        PERCENTAGE("Percentage"),
        BOTH("Both"),
        HITS_LEFT("Hits Left"),
        BLOCKS_LEFT("Blocks Left");

        private final String name;

        private DisplayMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}
