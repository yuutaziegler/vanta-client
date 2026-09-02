/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click.dropdown.panel.property;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.screen.click.OpalPanelComponent;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public abstract class PropertyPanel<T extends Property<?>>
extends OpalPanelComponent {
    private final T property;
    protected static final int DEFAULT_HEIGHT = 17;
    protected boolean lastProperty;

    public PropertyPanel(T property) {
        this.property = property;
        this.setHeight(17.0f);
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        if (this.lastProperty) {
            NVGRenderer.roundedRectVarying(this.x, this.y, this.width, this.height, 0.0f, 0.0f, 5.0f, 5.0f, ColorUtility.applyOpacity(-16777216, 0.25f));
        } else {
            NVGRenderer.rect(this.x, this.y, this.width, this.height, ColorUtility.applyOpacity(-16777216, 0.25f));
        }
    }

    public T getProperty() {
        return this.property;
    }

    public boolean isHidden() {
        return ((Property)this.property).isHidden();
    }
}

