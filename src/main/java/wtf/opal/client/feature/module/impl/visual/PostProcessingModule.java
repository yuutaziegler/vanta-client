/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;

@Environment(value=EnvType.CLIENT)
public final class PostProcessingModule
extends Module {
    private final BooleanProperty blur = (BooleanProperty)new BooleanProperty("Enabled", false).id("blurEnabled");
    private final BooleanProperty bloom = (BooleanProperty)new BooleanProperty("Enabled", false).id("bloomEnabled");
    private final NumberProperty blurRadius = (NumberProperty)new NumberProperty("Radius", 7.0, 1.0, 20.0, 1.0).id("blurRadius");
    private final NumberProperty bloomRadius = (NumberProperty)new NumberProperty("Radius", 7.0, 1.0, 20.0, 1.0).id("bloomRadius");

    public PostProcessingModule() {
        super("Post Processing", "Allows you to configure post processing effects.", ModuleCategory.VISUAL);
        this.setEnabled(false);
        this.addProperties(new Property[]{new GroupProperty("Blur", this.blur, this.blurRadius), new GroupProperty("Bloom", this.bloom, this.bloomRadius).hideIf(() -> this.blur.getValue() == false)});
    }

    public boolean isBlur() {
        return this.isEnabled() && this.blur.getValue() != false;
    }

    public boolean isBloom() {
        return this.isEnabled() && this.bloom.getValue() != false && this.isBlur();
    }

    public int getBlurRadius() {
        return ((Double)this.blurRadius.getValue()).intValue();
    }

    public int getBloomRadius() {
        return ((Double)this.bloomRadius.getValue()).intValue();
    }
}

