/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1839
 *  net.minecraft.class_2244
 *  net.minecraft.class_2246
 *  net.minecraft.class_2248
 *  net.minecraft.class_2281
 *  net.minecraft.class_2350$class_2351
 *  net.minecraft.class_2465
 *  net.minecraft.class_2540
 *  net.minecraft.class_259
 *  net.minecraft.class_2658
 *  net.minecraft.class_2678
 *  net.minecraft.class_2680
 *  net.minecraft.class_2960
 *  net.minecraft.class_3481
 *  net.minecraft.class_8710
 *  net.minecraft.class_8710$class_9154
 *  net.minecraft.class_9139
 */
package wtf.opal.client.feature.module.impl.movement.physics;

import java.lang.runtime.SwitchBootstraps;
import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1839;
import net.minecraft.class_2244;
import net.minecraft.class_2246;
import net.minecraft.class_2248;
import net.minecraft.class_2281;
import net.minecraft.class_2350;
import net.minecraft.class_2465;
import net.minecraft.class_2540;
import net.minecraft.class_259;
import net.minecraft.class_2658;
import net.minecraft.class_2678;
import net.minecraft.class_2680;
import net.minecraft.class_2960;
import net.minecraft.class_3481;
import net.minecraft.class_8710;
import net.minecraft.class_9139;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.timer.TimerHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.movement.physics.NoaPhysics;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.player.interaction.block.BlockBreakCanHarvestEvent;
import wtf.opal.event.impl.game.player.interaction.block.BlockBreakHardnessEvent;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.impl.game.player.movement.step.StepEvent;
import wtf.opal.event.impl.game.world.BlockShapeEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.mixin.ClientPlayerEntityAccessor;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class PhysicsModule
extends Module {
    private final BooleanProperty updateTimer = new BooleanProperty("Update timer", true);
    private final NoaPhysics physics = new NoaPhysics();
    private double jump;

    public PhysicsModule() {
        super("Physics", "Modifies game physics.", ModuleCategory.MOVEMENT);
        this.addProperties(this.updateTimer);
    }

    @Subscribe
    public void onBlockShape(BlockShapeEvent event) {
        class_2680 blockState = event.getBlockState();
        class_2248 block = blockState.method_26204();
        if (block instanceof class_2281 || block instanceof class_2244) {
            event.setVoxelShape(class_259.method_1081((double)0.0, (double)0.0, (double)0.0, (double)1.0, (double)1.0, (double)1.0));
        }
    }

    @Subscribe
    public void onBlockBreakHardness(BlockBreakHardnessEvent event) {
        class_2248 block;
        class_2680 blockState = event.getBlockState();
        class_2248 class_22482 = block = blockState.method_26204();
        Objects.requireNonNull(class_22482);
        class_2248 class_22483 = class_22482;
        int n = 0;
        switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{class_2244.class, class_2465.class}, (Object)class_22483, n)) {
            case 0: {
                class_2244 ignored = (class_2244)class_22483;
                event.setHardness(1.5f);
                break;
            }
            case 1: {
                class_2465 ignored = (class_2465)class_22483;
                event.setHardness(1.0f);
                break;
            }
            default: {
                if (blockState.method_26164(class_3481.field_36265)) {
                    event.setHardness(0.5f);
                    break;
                }
                if (blockState.method_26164(class_3481.field_15481)) {
                    event.setHardness(0.7f);
                    break;
                }
                if (block != class_2246.field_10034) break;
                event.setHardness(1.75f);
            }
        }
    }

    @Subscribe
    public void onBlockBreakCanHarvest(BlockBreakCanHarvestEvent event) {
        class_2680 blockState = event.getBlockState();
        class_2248 block = blockState.method_26204();
        if (block instanceof class_2244) {
            event.setCanHarvest(Constants.mc.field_1724.method_7305(class_2246.field_10340.method_9564()));
        }
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        ClientPlayerEntityAccessor accessor = (ClientPlayerEntityAccessor)Constants.mc.field_1724;
        if (event.getX() != accessor.getLastXClient() || event.getY() != accessor.getLastYClient() || event.getZ() != accessor.getLastZClient()) {
            accessor.setTicksSinceLastPositionPacketSent(20);
        } else {
            accessor.setTicksSinceLastPositionPacketSent(0);
        }
    }

    @Subscribe(priority=3)
    public void onPostMove(PostMoveEvent event) {
        if (Constants.mc.field_1724.method_24828() && this.physics.velocity < 0.0) {
            this.physics.velocity = 0.0;
        }
        if (Constants.mc.field_1724.method_18798().method_10214() == (double)0.42f) {
            this.jump = Math.min(this.jump + 1.0, 3.0);
            this.physics.impulse += 8.0;
        }
        if (LocalDataWatch.get().groundTicks > 5) {
            this.jump = 0.0;
        }
        double speed = !MoveUtility.isMoving() ? 0.0 : (Constants.mc.field_1724.method_6115() && Constants.mc.field_1724.method_6047().method_7976() != class_1839.field_8949 ? 0.06 : 0.26 + 0.025 * this.jump);
        MoveUtility.setSpeed(speed);
        Constants.mc.field_1724.method_18799(Constants.mc.field_1724.method_18798().method_38499(class_2350.class_2351.field_11052, this.physics.getMotionForTick() * 0.03333333333333333));
    }

    @Subscribe(priority=1)
    public void onPreGameTick(PreGameTickEvent event) {
        if (this.updateTimer.getValue().booleanValue()) {
            TimerHelper.getInstance().timer = 1.5f;
        }
    }

    @Override
    protected void onDisable() {
        TimerHelper.getInstance().timer = 1.0f;
    }

    public NoaPhysics getPhysics() {
        return this.physics;
    }

    @Subscribe
    public void onStepHeight(StepEvent event) {
        event.setStepHeight(1.0f);
    }

    /*
     * WARNING - Removed back jump from a try to a catch block - possible behaviour change.
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        float motionY;
        class_8710 payload;
        Object object = event.getPacket();
        if (!(object instanceof class_2658)) {
            if (!(event.getPacket() instanceof class_2678)) return;
            this.physics.impulse = 0.0;
            this.physics.force = 0.0;
            this.physics.velocity = 0.0;
            return;
        }
        class_2658 class_26582 = (class_2658)object;
        try {
            class_8710 class_87102;
            payload = class_87102 = class_26582.comp_1646();
        }
        catch (Throwable throwable) {
            throw new MatchException(throwable.toString(), throwable);
        }
        if (!(payload instanceof ResyncPhysicsPayload)) return;
        object = (ResyncPhysicsPayload)payload;
        {
            float f;
            float motionX = f = ((ResyncPhysicsPayload)object).motionX();
            motionY = f = ((ResyncPhysicsPayload)object).motionY();
            float motionZ = f = ((ResyncPhysicsPayload)object).motionZ();
            this.physics.impulse = 0.0;
            this.physics.force = 0.0;
        }
        this.physics.velocity = motionY;
    }

    @Environment(value=EnvType.CLIENT)
    public record ResyncPhysicsPayload(float motionX, float motionY, float motionZ) implements class_8710
    {
        public static final class_8710.class_9154<ResyncPhysicsPayload> ID = new class_8710.class_9154(class_2960.method_60655((String)"bloxd", (String)"resyncphysics"));
        public static final class_9139<class_2540, ResyncPhysicsPayload> CODEC = class_9139.method_56438((value, buf) -> {}, buf -> {
            float motionX = buf.readFloat();
            float motionY = buf.readFloat();
            float motionZ = buf.readFloat();
            return new ResyncPhysicsPayload(motionX, motionY, motionZ);
        });

        public class_8710.class_9154<? extends class_8710> method_56479() {
            return ID;
        }
    }
}

