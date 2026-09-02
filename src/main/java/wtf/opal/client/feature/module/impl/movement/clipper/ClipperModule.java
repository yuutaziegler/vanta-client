/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1922
 *  net.minecraft.class_2338
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 */
package wtf.opal.client.feature.module.impl.movement.clipper;

import java.util.concurrent.atomic.AtomicReference;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1922;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.movement.clipper.ClipperIsland;
import wtf.opal.client.feature.module.impl.movement.physics.NoaPhysics;
import wtf.opal.client.feature.module.impl.movement.physics.PhysicsModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.DynamicIslandElement;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.press.KeyPressEvent;
import wtf.opal.event.impl.press.MousePressEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class ClipperModule
extends Module {
    private final BooleanProperty vanillaLimit = new BooleanProperty("Limit distance", true);
    private final BooleanProperty upwards = new BooleanProperty("Upwards", true);
    private final ClipperIsland dynamicIsland = new ClipperIsland(this);
    private Double upPos;
    private Double downPos;

    public ClipperModule() {
        super("Clipper", "Gives you the option to clip up or down when available.", ModuleCategory.MOVEMENT);
        this.addProperties(this.vanillaLimit, this.upwards);
    }

    Double getUpPos() {
        return this.upPos;
    }

    Double getDownPos() {
        return this.downPos;
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        this.upPos = null;
        this.downPos = null;
        if (Constants.mc.field_1724 != null) {
            if (this.upwards.getValue().booleanValue()) {
                PhysicsModule physicsModule = OpalClient.getInstance().getModuleRepository().getModule(PhysicsModule.class);
                if (Constants.mc.field_1724.method_24828() || !physicsModule.isEnabled()) {
                    this.searchUpwards();
                }
            }
            this.searchDownwards();
        }
        if (this.upPos == null && this.downPos == null) {
            DynamicIslandElement.removeTrigger(this.dynamicIsland);
        } else {
            DynamicIslandElement.addTrigger(this.dynamicIsland);
        }
    }

    @Subscribe
    public void onKeyPress(KeyPressEvent event) {
        if (Constants.mc.field_1724 == null || Constants.mc.field_1755 != null) {
            return;
        }
        if (event.getInteractionCode() == 265) {
            this.clipUp();
        } else if (event.getInteractionCode() == 264) {
            this.clipDown();
        }
    }

    @Subscribe
    public void onMousePress(MousePressEvent event) {
        if (Constants.mc.field_1724 == null || Constants.mc.field_1755 != null) {
            return;
        }
        if (event.getInteractionCode() == 4) {
            this.clipUp();
        } else if (event.getInteractionCode() == 3) {
            this.clipDown();
        }
    }

    private void clipUp() {
        if (this.upPos != null) {
            this.setPosY(this.upPos);
        }
    }

    private void clipDown() {
        if (this.downPos != null) {
            this.setPosY(this.downPos);
        }
    }

    private void setPosY(double posY) {
        Constants.mc.field_1724.method_33574(Constants.mc.field_1724.method_73189().method_38499(class_2350.class_2351.field_11052, posY));
        Constants.mc.field_1724.method_18799(new class_243(0.0, 0.0, 0.0));
        PhysicsModule physicsModule = OpalClient.getInstance().getModuleRepository().getModule(PhysicsModule.class);
        if (physicsModule.isEnabled()) {
            NoaPhysics physics = physicsModule.getPhysics();
            physics.velocity = 0.0;
        }
    }

    private void searchUpwards() {
        int blocks = 1;
        AtomicReference collision = new AtomicReference();
        while (!(blocks >= 10 && this.vanillaLimit.getValue().booleanValue() || Constants.mc.field_1687.method_31601((int)Constants.mc.field_1724.method_23318() + blocks))) {
            class_265 voxelShape;
            if (!class_2338.method_29715((class_238)Constants.mc.field_1724.method_5829().method_989(0.0, (double)(++blocks), 0.0)).noneMatch(pos -> {
                class_2680 blockState = Constants.mc.field_1687.method_8320(pos);
                class_265 voxelShape = blockState.method_26220((class_1922)Constants.mc.field_1687, pos);
                if (!voxelShape.method_1110()) {
                    collision.set(voxelShape.method_1096((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260()));
                    return true;
                }
                return false;
            }) || (voxelShape = (class_265)collision.get()) == null) continue;
            this.upPos = voxelShape.method_1105(class_2350.class_2351.field_11052);
            break;
        }
    }

    private void searchDownwards() {
        class_238 boundingBox = Constants.mc.field_1724.method_5829().method_989(0.0, -(Constants.mc.field_1724.method_23318() % 1.0), 0.0);
        int blocks = 1;
        boolean found = false;
        boolean air = false;
        AtomicReference collision = new AtomicReference();
        while (!(blocks > 10 && this.vanillaLimit.getValue().booleanValue() || Constants.mc.field_1687.method_31601((int)Constants.mc.field_1724.method_23318() - blocks))) {
            if (class_2338.method_29715((class_238)boundingBox.method_989(0.0, (double)(-(++blocks)), 0.0)).anyMatch(pos -> {
                class_2680 blockState = Constants.mc.field_1687.method_8320(pos);
                class_265 voxelShape = blockState.method_26220((class_1922)Constants.mc.field_1687, pos);
                if (!voxelShape.method_1110()) {
                    collision.set(voxelShape.method_1096((double)pos.method_10263(), (double)pos.method_10264(), (double)pos.method_10260()));
                    return true;
                }
                return false;
            })) {
                if (air) {
                    class_265 voxelShape = (class_265)collision.get();
                    this.downPos = voxelShape.method_1105(class_2350.class_2351.field_11052);
                    break;
                }
                found = true;
                continue;
            }
            if (!found) continue;
            air = true;
        }
    }

    @Override
    protected void onDisable() {
        DynamicIslandElement.removeTrigger(this.dynamicIsland);
        this.upPos = null;
        this.downPos = null;
    }
}

