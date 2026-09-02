/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.screen.click.dropdown.panel.property.impl;

import com.ibm.icu.impl.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.renderer.component.ToggleSwitchComponent;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.screen.click.dropdown.panel.property.PropertyPanel;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public final class BooleanPropertyComponent
extends PropertyPanel<BooleanProperty> {
    private final ToggleSwitchComponent toggleSwitch = new ToggleSwitchComponent(property::toggle, property::getValue);

    public BooleanPropertyComponent(BooleanProperty property) {
        super(property);
    }

    @Override
    public void init() {
        this.toggleSwitch.reset();
    }

    @Override
    public void render(class_332 context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        FontRepository.getFont("productsans-medium").drawString(((BooleanProperty)this.getProperty()).getName(), this.x + 5.0f, this.y + 10.5f, 7.0f, -1);
        this.toggleSwitch.setBoxColors((Pair<Integer, Integer>)Pair.of((Object)((Integer)ColorUtility.getClientTheme().first), (Object)-12829636));
        this.toggleSwitch.render(this.x + 88.0f, this.y + 3.8f, 0.85f);
    }

    @Override
    public void mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            this.toggleSwitch.mouseClicked(this.x, this.y, this.width, this.height, mouseX, mouseY);
        }
    }
}

