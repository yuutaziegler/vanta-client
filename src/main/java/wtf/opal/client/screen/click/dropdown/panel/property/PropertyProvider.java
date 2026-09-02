/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11908
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click.dropdown.panel.property;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11908;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.property.IPropertyListProvider;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.screen.click.IOpalComponent;
import wtf.opal.client.screen.click.OpalPanelComponent;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;

@Environment(value=EnvType.CLIENT)
public final class PropertyProvider
extends OpalPanelComponent {
    private final List<PropertyPanel<?>> propertyPanelList = new ArrayList();
    private final IPropertyListProvider propertyListProvider;
    private final BooleanSupplier expanded;
    private final BooleanSupplier lastPropertyListProvider;
    private boolean hasProperties;
    private boolean updated;
    private float extraHeight;

    public PropertyProvider(IPropertyListProvider propertyListProvider, BooleanSupplier expanded, BooleanSupplier lastPropertyListProvider) {
        this.propertyListProvider = propertyListProvider;
        this.expanded = expanded;
        this.initProperties();
        this.updateHasProperties();
        this.lastPropertyListProvider = lastPropertyListProvider;
    }

    private void initProperties() {
        for (Property<?> property : this.propertyListProvider.getPropertyList()) {
            PropertyPanel<?> clickGUIComponent = property.createClickGUIComponent();
            if (clickGUIComponent != null) {
                this.propertyPanelList.add(clickGUIComponent);
                continue;
            }
            System.err.println("Unimplemented property: " + property.getClass().getSimpleName());
        }
    }

    public boolean isHasProperties() {
        if (!this.updated) {
            this.updateHasProperties();
            this.updated = true;
        }
        return this.hasProperties;
    }

    private void updateHasProperties() {
        this.hasProperties = this.propertyListProvider.getPropertyList().stream().anyMatch(p -> !p.isHidden());
    }

    private boolean isClosed() {
        return !this.expanded.getAsBoolean();
    }

    @Override
    public void init() {
        if (this.isClosed()) {
            return;
        }
        this.propertyPanelList.forEach(IOpalComponent::init);
    }

    @Override
    public void close() {
        if (this.isClosed()) {
            return;
        }
        this.propertyPanelList.forEach(IOpalComponent::close);
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        int i;
        if (this.isClosed()) {
            return;
        }
        float extraHeight = 0.0f;
        int lastVisibleIndex = -1;
        for (i = this.propertyPanelList.size() - 1; i >= 0; --i) {
            if (this.propertyPanelList.get(i).isHidden()) continue;
            lastVisibleIndex = i;
            break;
        }
        for (i = 0; i < this.propertyPanelList.size(); ++i) {
            PropertyPanel<?> propertyPanel = this.propertyPanelList.get(i);
            if (propertyPanel.isHidden()) continue;
            propertyPanel.setX(this.x);
            propertyPanel.setY(this.y + extraHeight);
            propertyPanel.setWidth(this.width);
            propertyPanel.lastProperty = this.lastPropertyListProvider != null && this.lastPropertyListProvider.getAsBoolean() && i == lastVisibleIndex;
            propertyPanel.render(context, mouseX, mouseY, delta);
            extraHeight += propertyPanel.getHeight();
        }
        this.extraHeight = extraHeight;
    }

    @Override
    public void keyPressed(class_11908 keyInput) {
        if (this.isClosed()) {
            return;
        }
        this.propertyPanelList.forEach(propertyPanel -> propertyPanel.keyPressed(keyInput));
    }

    @Override
    public void charTyped(char chr, int modifiers) {
        if (this.isClosed()) {
            return;
        }
        this.propertyPanelList.forEach(propertyPanel -> propertyPanel.charTyped(chr, modifiers));
    }

    public float getExtraHeight() {
        return this.extraHeight;
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isClosed()) {
            return;
        }
        for (PropertyPanel<?> propertyPanel : this.propertyPanelList) {
            if (propertyPanel.isHidden()) continue;
            propertyPanel.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        if (this.isClosed()) {
            return;
        }
        for (PropertyPanel<?> propertyPanel : this.propertyPanelList) {
            if (propertyPanel.isHidden()) continue;
            propertyPanel.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        }
    }

    @Override
    public void mouseReleased(double mouseX, double mouseY, int button) {
        if (this.isClosed()) {
            return;
        }
        for (PropertyPanel<?> propertyPanel : this.propertyPanelList) {
            if (propertyPanel.isHidden()) continue;
            propertyPanel.mouseReleased(mouseX, mouseY, button);
        }
    }
}

