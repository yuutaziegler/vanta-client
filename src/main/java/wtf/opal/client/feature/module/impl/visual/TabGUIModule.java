/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual;

import com.ibm.icu.impl.Pair;
import java.util.List;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.event.impl.press.KeyPressEvent;
import wtf.opal.event.impl.render.RenderBloomEvent;
import wtf.opal.event.impl.render.RenderScreenEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public final class TabGUIModule
extends Module {
    private int categoryIndex;
    private boolean categoryExpanded;

    public TabGUIModule() {
        super("Tab GUI", "A display for interacting with client features.", ModuleCategory.VISUAL);
    }

    public void render() {
        NVGTextRenderer font = FontRepository.getFont("productsans-medium");
        Pair<Integer, Integer> colors = ColorUtility.getClientTheme();
        float x = 5.0f;
        float y = 40.0f;
        float width = 75.0f;
        float panelHeight = 16.0f;
        float border = 2.0f;
        this.renderPanel(x, y, width, panelHeight, ModuleCategory.VALUES.length);
        for (int i = 0; i < ModuleCategory.VALUES.length; ++i) {
            float yPos = y + (float)i * panelHeight;
            boolean isCurrent = i == this.categoryIndex;
            this.renderTab(x, yPos, width, panelHeight, i, ModuleCategory.VALUES.length, isCurrent, colors);
            font.drawString(ModuleCategory.VALUES[i].getName(), x + 5.0f, yPos + 11.0f, 8.25f, isCurrent ? -1 : ColorUtility.brighter(-8355712, 0.3f));
        }
        if (this.categoryExpanded) {
            this.renderModules(font, colors, x + width + 7.0f, y, width, panelHeight);
        }
    }

    private void renderPanel(float x, float y, float width, float panelHeight, int itemCount) {
        float height = panelHeight * (float)itemCount;
        float border = 2.0f;
        NVGRenderer.roundedRect(x - border, y - border, width + border * 2.0f, height + border * 2.0f, 5.5f, NVGRenderer.BLUR_PAINT);
        NVGRenderer.roundedRect(x - border, y - border, width + border * 2.0f, height + border * 2.0f, 5.5f, -2146891511);
    }

    private void renderTab(float x, float y, float width, float height, int index, int total, boolean isCurrent, Pair<Integer, Integer> colors) {
        boolean isFirst = index == 0;
        boolean isLast = index == total - 1;
        float radius = 4.0f;
        if (isFirst || isLast) {
            NVGRenderer.roundedRectVarying(x, y, width, height, isFirst ? radius : 0.0f, isFirst ? radius : 0.0f, isLast ? radius : 0.0f, isLast ? radius : 0.0f, NVGRenderer.BLUR_PAINT);
            NVGRenderer.roundedRectVarying(x, y, width, height, isFirst ? radius : 0.0f, isFirst ? radius : 0.0f, isLast ? radius : 0.0f, isLast ? radius : 0.0f, -2146891511);
        } else {
            NVGRenderer.rect(x, y, width, height, NVGRenderer.BLUR_PAINT);
            NVGRenderer.rect(x, y, width, height, -2146891511);
        }
        if (isCurrent) {
            this.renderGradient(x, y, width, height, isFirst, isLast, colors);
        }
    }

    private void renderGradient(float x, float y, float width, float height, boolean isFirst, boolean isLast, Pair<Integer, Integer> colors) {
        int startColor = ColorUtility.applyOpacity((int)((Integer)colors.first), 0.4f);
        int endColor = ColorUtility.applyOpacity((int)((Integer)colors.second), 0.4f);
        if (isFirst || isLast) {
            NVGRenderer.roundedRectVaryingGradient(x, y, width, height, isFirst ? 4.0f : 0.0f, isFirst ? 4.0f : 0.0f, isLast ? 4.0f : 0.0f, isLast ? 4.0f : 0.0f, startColor, endColor, 0.0f);
        } else {
            NVGRenderer.rectGradient(x, y, width, height, startColor, endColor, 0.0f);
        }
    }

    private void renderModules(NVGTextRenderer font, Pair<Integer, Integer> colors, float x, float y, float width, float panelHeight) {
        ModuleCategory category = ModuleCategory.VALUES[this.categoryIndex];
        List<Module> modules = OpalClient.getInstance().getModuleRepository().getModulesInCategory(category).stream().toList();
        if (modules.isEmpty()) {
            return;
        }
        this.renderPanel(x, y, width, panelHeight, modules.size());
        for (int i = 0; i < modules.size(); ++i) {
            float yPos = y + (float)i * panelHeight;
            boolean isCurrent = i == category.getModuleIndex();
            this.renderTab(x, yPos, width, panelHeight, i, modules.size(), isCurrent, colors);
            font.drawString(modules.get(i).getName(), x + 5.0f, yPos + 11.0f, 8.25f, isCurrent ? -1 : ColorUtility.brighter(-8355712, 0.3f));
            if (!isCurrent || !modules.get(i).isExpanded()) continue;
            this.renderProperties(font, colors, x + width + 7.0f, y, panelHeight, modules.get(i));
        }
    }

    private void renderProperties(NVGTextRenderer font, Pair<Integer, Integer> colors, float x, float y, float panelHeight, Module module) {
        List<Property<?>> properties = module.getPropertyList();
        if (properties.isEmpty()) {
            return;
        }
        double maxLength = properties.stream().mapToDouble(p -> font.getStringWidth(p.getName() + ": " + this.getPropertyValue((Property<?>)p), 8.25f)).max().orElse(0.0);
        this.renderPanel(x, y, (float)(maxLength + 12.5), panelHeight, properties.size());
        for (int i = 0; i < properties.size(); ++i) {
            float yPos = y + (float)i * panelHeight;
            boolean isCurrent = i == module.getPropertyIndex();
            this.renderTab(x, yPos, (float)(maxLength + 12.5), panelHeight, i, properties.size(), isCurrent, (Pair<Integer, Integer>)Pair.of((Object)ColorUtility.darker((Integer)colors.first, properties.get(i).isFocused() ? 0.35f : 0.0f), (Object)ColorUtility.darker((Integer)colors.second, properties.get(i).isFocused() ? 0.35f : 0.0f)));
            String propertyName = properties.get(i).getName() + ": ";
            float textX = x + 5.0f;
            font.drawString(propertyName, textX, yPos + 11.0f, 8.25f, isCurrent ? -1 : ColorUtility.brighter(-8355712, 0.3f));
            font.drawString(this.getPropertyValue(properties.get(i)), textX + font.getStringWidth(propertyName, 8.25f), yPos + 11.0f, 8.25f, isCurrent ? -1 : ColorUtility.brighter(-8355712, 0.3f));
        }
    }

    private String getPropertyValue(Property<?> property) {
        if (property instanceof BooleanProperty) {
            BooleanProperty booleanProperty = (BooleanProperty)property;
            return String.valueOf(booleanProperty.getValue());
        }
        if (property instanceof NumberProperty) {
            NumberProperty numberProperty = (NumberProperty)property;
            return String.format("%.3f", numberProperty.getValue()).replaceAll("0+$", "").replaceAll("\\.$", "");
        }
        if (property instanceof ModeProperty) {
            ModeProperty modeProperty = (ModeProperty)property;
            return String.valueOf(modeProperty.getValue());
        }
        if (property instanceof MultipleBooleanProperty) {
            MultipleBooleanProperty multipleBooleanProperty = (MultipleBooleanProperty)property;
            List subProperties = (List)multipleBooleanProperty.getValue();
            int selectedIndex = multipleBooleanProperty.getSubPropertyIndex();
            return subProperties.stream().map(p -> (subProperties.indexOf(p) == selectedIndex ? "**" : "") + p.getName() + ": " + p.getValue() + (subProperties.indexOf(p) == selectedIndex ? "**" : "")).collect(Collectors.joining(", ", "[", "]"));
        }
        return "";
    }

    @Subscribe
    public void onKeyPress(KeyPressEvent event) {
        if (Constants.mc.field_1755 != null) {
            return;
        }
        ModuleCategory category = ModuleCategory.VALUES[this.categoryIndex];
        List<Module> moduleList = OpalClient.getInstance().getModuleRepository().getModulesInCategory(category).stream().toList();
        Module module = moduleList.get(category.getModuleIndex());
        List<Property<?>> propertyList = module.getPropertyList();
        int key = event.getInteractionCode();
        switch (key) {
            case 265: {
                this.handleUpKey(category, module, moduleList, propertyList);
                break;
            }
            case 264: {
                this.handleDownKey(category, module, moduleList, propertyList);
                break;
            }
            case 262: {
                this.handleRightKey(module, propertyList);
                break;
            }
            case 263: {
                this.handleLeftKey(module, propertyList);
                break;
            }
            case 257: {
                this.handleEnterKey(module, propertyList);
                break;
            }
            case 258: {
                this.handleTabKey(module, propertyList);
            }
        }
    }

    private void handleUpKey(ModuleCategory category, Module module, List<Module> moduleList, List<Property<?>> propertyList) {
        if (!this.categoryExpanded) {
            this.categoryIndex = (this.categoryIndex - 1 + ModuleCategory.VALUES.length) % ModuleCategory.VALUES.length;
        } else if (module.isExpanded() && !propertyList.isEmpty()) {
            this.cyclePropertyIndex(module, propertyList, -1);
        } else {
            this.cycleModuleIndex(category, moduleList, -1);
        }
    }

    private void handleDownKey(ModuleCategory category, Module module, List<Module> moduleList, List<Property<?>> propertyList) {
        if (!this.categoryExpanded) {
            this.categoryIndex = (this.categoryIndex + 1) % ModuleCategory.VALUES.length;
        } else if (module.isExpanded() && !propertyList.isEmpty()) {
            this.cyclePropertyIndex(module, propertyList, 1);
        } else {
            this.cycleModuleIndex(category, moduleList, 1);
        }
    }

    private void handleRightKey(Module module, List<Property<?>> propertyList) {
        if (!this.categoryExpanded) {
            this.categoryExpanded = true;
            return;
        }
        if (!propertyList.isEmpty()) {
            Property<?> property = propertyList.get(module.getPropertyIndex());
            if (!property.isFocused()) {
                module.setExpanded(true);
            } else {
                this.modifyProperty(property, true);
            }
        }
    }

    private void handleLeftKey(Module module, List<Property<?>> propertyList) {
        if (this.categoryExpanded && module.isExpanded()) {
            if (!propertyList.isEmpty() && !propertyList.get(module.getPropertyIndex()).isFocused()) {
                module.setExpanded(false);
            } else {
                this.modifyProperty(propertyList.get(module.getPropertyIndex()), false);
            }
        } else {
            this.categoryExpanded = false;
        }
    }

    private void handleEnterKey(Module module, List<Property<?>> propertyList) {
        if (!this.categoryExpanded) {
            return;
        }
        if (!module.isExpanded()) {
            module.toggle();
        } else {
            Property<?> property;
            property.setFocused(!(property = propertyList.get(module.getPropertyIndex())).isFocused());
        }
    }

    private void handleTabKey(Module module, List<Property<?>> propertyList) {
        if (propertyList.isEmpty() || !this.categoryExpanded) {
            return;
        }
        Property<?> property = propertyList.get(module.getPropertyIndex());
        if (property instanceof MultipleBooleanProperty) {
            MultipleBooleanProperty multipleBooleanProperty = (MultipleBooleanProperty)property;
            if (property.isFocused()) {
                multipleBooleanProperty.cycleSubPropertyIndex();
            }
        }
    }

    private void cycleModuleIndex(ModuleCategory category, List<Module> moduleList, int direction) {
        category.setModuleIndex((category.getModuleIndex() + direction + moduleList.size()) % moduleList.size());
    }

    private void cyclePropertyIndex(Module module, List<Property<?>> propertyList, int direction) {
        module.setPropertyIndex((module.getPropertyIndex() + direction + propertyList.size()) % propertyList.size());
        propertyList.get(module.getPropertyIndex()).setFocused(false);
    }

    private void modifyProperty(Property<?> property, boolean increase) {
        MultipleBooleanProperty multipleBooleanProperty;
        BooleanProperty selected;
        if (property instanceof BooleanProperty) {
            BooleanProperty booleanProperty = (BooleanProperty)property;
            booleanProperty.toggle();
        } else if (property instanceof NumberProperty) {
            NumberProperty numberProperty = (NumberProperty)property;
            numberProperty.setValue((Double)numberProperty.getValue() + (increase ? numberProperty.getIncrement() : -numberProperty.getIncrement()));
        } else if (property instanceof ModeProperty) {
            ModeProperty modeProperty = (ModeProperty)property;
            modeProperty.cycle(increase);
        } else if (property instanceof MultipleBooleanProperty && (selected = (multipleBooleanProperty = (MultipleBooleanProperty)property).getSelectedSubProperty()) != null) {
            selected.toggle();
        }
    }

    @Subscribe
    public void onRenderScreen(RenderScreenEvent event) {
        this.render();
    }

    @Subscribe
    public void onBloomRender(RenderBloomEvent event) {
        this.render();
    }
}

