/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_3414
 *  net.minecraft.class_3417
 */
package wtf.opal.client.feature.module.impl.visual;

import java.util.Map;
import java.util.function.Supplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3414;
import net.minecraft.class_3417;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.event.impl.game.player.interaction.AttackEvent;
import wtf.opal.event.impl.game.world.PlaySoundEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class AttackEffectsModule
extends Module {
    private final MultipleBooleanProperty particles = new MultipleBooleanProperty("Particles", new BooleanProperty("Critical", false), new BooleanProperty("Sharpness", true));
    private final MultipleBooleanProperty sounds = new MultipleBooleanProperty("Sounds", new BooleanProperty("Critical", false), new BooleanProperty("Knockback", false), new BooleanProperty("Strong", false), new BooleanProperty("Sweep", false), new BooleanProperty("Weak", false), new BooleanProperty("No damage", false));
    private final Map<class_3414, Supplier<Boolean>> soundValues = Map.of(class_3417.field_15016, this.sounds.getProperty("Critical")::getValue, class_3417.field_14999, this.sounds.getProperty("Knockback")::getValue, class_3417.field_14840, this.sounds.getProperty("Strong")::getValue, class_3417.field_14706, this.sounds.getProperty("Sweep")::getValue, class_3417.field_14625, this.sounds.getProperty("Weak")::getValue, class_3417.field_14914, this.sounds.getProperty("No damage")::getValue);

    public AttackEffectsModule() {
        super("Attack Effects", "Adds or changes effects that happen when attacking an entity.", ModuleCategory.VISUAL);
        this.setEnabled(true);
        this.addProperties(this.particles, this.sounds);
    }

    @Subscribe
    public void onAttack(AttackEvent event) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        if (this.particles.getProperty("Critical").getValue().booleanValue()) {
            Constants.mc.field_1724.method_7277(event.getTarget());
        }
        if (this.particles.getProperty("Sharpness").getValue().booleanValue()) {
            Constants.mc.field_1724.method_7304(event.getTarget());
        }
    }

    @Subscribe
    public void onPlaySound(PlaySoundEvent event) {
        Supplier<Boolean> supplier = this.soundValues.get(event.getSoundEvent());
        if (supplier != null && !supplier.get().booleanValue()) {
            event.setCancelled();
        }
    }
}

