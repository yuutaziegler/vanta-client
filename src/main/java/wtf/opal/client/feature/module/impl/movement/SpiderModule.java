/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2350$class_2351
 */
package wtf.opal.client.feature.module.impl.movement;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2350;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.movement.physics.PhysicsModule;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class SpiderModule
extends Module {
    private final NumberProperty speedProperty = (NumberProperty)new NumberProperty("Speed", 0.5, 0.1, 10.0, 0.1).hideIf(this::isBloxd);

    public SpiderModule() {
        super("Spider", "Lets you climb walls.", ModuleCategory.MOVEMENT);
        this.addProperties(this.speedProperty);
    }

    private boolean isBloxd() {
        return OpalClient.getInstance().getModuleRepository().getModule(PhysicsModule.class).isEnabled();
    }

    @Subscribe(priority=5)
    public void onPostMove(PostMoveEvent event) {
        if (Constants.mc.field_1724.field_5976 && !Constants.mc.field_1724.method_6101()) {
            PhysicsModule physicsModule = OpalClient.getInstance().getModuleRepository().getModule(PhysicsModule.class);
            if (physicsModule.isEnabled()) {
                physicsModule.getPhysics().velocity = 8.0;
            } else {
                Constants.mc.field_1724.method_18799(Constants.mc.field_1724.method_18798().method_38499(class_2350.class_2351.field_11052, ((Double)this.speedProperty.getValue()).doubleValue()));
            }
        }
    }
}

