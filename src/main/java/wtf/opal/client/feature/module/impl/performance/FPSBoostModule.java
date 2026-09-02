/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.performance;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class FPSBoostModule
extends Module {
    private final BooleanProperty reduceFire = new BooleanProperty("Reduce Fire", true);
    private final BooleanProperty reduceSkybox = new BooleanProperty("Reduce Skybox", false);
    private final BooleanProperty reduceParticles = new BooleanProperty("Reduce Particles", true);
    private final BooleanProperty fastMath = new BooleanProperty("Fast Math", true);
    private final BooleanProperty reduceFoliage = new BooleanProperty("Reduce Foliage", false);
    private final BooleanProperty cullParticles = new BooleanProperty("Cull Particles", true);

    public FPSBoostModule() {
        super("FPS Boost", "Optimizes game for better performance", ModuleCategory.PERFORMANCE);
        this.addProperties(new GroupProperty("Rendering", this.reduceFire, this.reduceSkybox, this.reduceParticles), new GroupProperty("Performance", this.fastMath, this.reduceFoliage, this.cullParticles));
    }

    @Subscribe
    public void onTick(PreGameTickEvent event) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
    }

    public boolean shouldReduceFire() {
        return this.isEnabled() && this.reduceFire.getValue() != false;
    }

    public boolean shouldReduceSkybox() {
        return this.isEnabled() && this.reduceSkybox.getValue() != false;
    }

    public boolean shouldReduceParticles() {
        return this.isEnabled() && this.reduceParticles.getValue() != false;
    }

    public boolean shouldUseFastMath() {
        return this.isEnabled() && this.fastMath.getValue() != false;
    }

    public boolean shouldReduceFoliage() {
        return this.isEnabled() && this.reduceFoliage.getValue() != false;
    }

    public boolean shouldCullParticles() {
        return this.isEnabled() && this.cullParticles.getValue() != false;
    }
}

