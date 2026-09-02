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
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;

@Environment(value=EnvType.CLIENT)
public final class FPSBoostModule
extends Module {
    private final BooleanProperty fastRender = new BooleanProperty("Fast Render", true);
    private final BooleanProperty disableParticles = new BooleanProperty("Disable Particles", true);
    private final BooleanProperty optimizeMath = new BooleanProperty("Optimize Math", true);

    public FPSBoostModule() {
        super("FPS Boost", "Increases client performance through rendering optimizations.", ModuleCategory.VISUAL);
        this.addProperties(this.fastRender, this.disableParticles, this.optimizeMath);
    }
}

