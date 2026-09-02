/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 *  net.minecraft.class_238
 *  net.minecraft.class_243
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_5498
 */
package wtf.opal.client.feature.module.impl.movement;

import com.ibm.icu.impl.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_5498;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.combat.killaura.KillAuraModule;
import wtf.opal.client.feature.module.impl.movement.speed.SpeedModule;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.render.RenderWorldEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.math.MathUtility;
import wtf.opal.utility.player.PlayerUtility;
import wtf.opal.utility.player.RotationUtility;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public final class TargetStrafeModule
extends Module {
    private final ModeProperty<Mode> mode = new ModeProperty<Mode>("Strafe mode", Mode.CIRCLE);
    private final MultipleBooleanProperty requirements = new MultipleBooleanProperty("Requirements", new BooleanProperty("Jump key", true), new BooleanProperty("Speed module", true));
    private final NumberProperty range = new NumberProperty("Range", 3.0, 0.1f, 6.0, 0.1f);
    private final BooleanProperty showRing = new BooleanProperty("Show ring", true);
    private final BooleanProperty auto3rdPerson = new BooleanProperty("Auto 3rd person", false);
    private static final float RING_SEGMENT_THICKNESS = 0.03f;
    private static final int RING_SEGMENT_COUNT = 12;
    private final RingSegment[] ringSegments = new RingSegment[12];
    private final RingSegment[] innerOutlineRingSegments = new RingSegment[12];
    private final RingSegment[] outerOutlineRingSegments = new RingSegment[12];
    private float prevInnerRadius = -1.0f;
    private boolean left;
    private boolean overFall;
    private boolean colliding;
    private boolean active;
    private boolean returnState;
    private float yaw;

    public TargetStrafeModule() {
        super("Target Strafe", "Makes you go in circles around targets.", ModuleCategory.MOVEMENT);
        this.addProperties(this.mode, this.requirements, this.range, this.showRing, this.auto3rdPerson);
    }

    @Subscribe
    public void onPostMove(PostMoveEvent event) {
        if (!this.shouldRun()) {
            this.active = false;
            if (this.auto3rdPerson.getValue().booleanValue() && !Constants.mc.field_1690.method_31044().method_31034() && this.returnState) {
                Constants.mc.field_1690.method_31043(class_5498.field_26664);
                this.returnState = false;
            }
            return;
        }
        this.active = true;
        class_1309 target = this.getKillAuraTarget();
        if (Constants.mc.field_1724.field_5976) {
            if (!this.colliding) {
                this.left = !this.left;
            }
            this.colliding = true;
        } else {
            this.colliding = false;
        }
        class_238 nextTickBox = Constants.mc.field_1724.method_5829().method_997(Constants.mc.field_1724.method_18798());
        if (PlayerUtility.isAirUntil(target.method_23318() - 3.0, nextTickBox) || PlayerUtility.isOverVoid(nextTickBox)) {
            if (!this.overFall) {
                this.left = !this.left;
            }
            this.overFall = true;
        } else {
            this.overFall = false;
        }
        if (this.auto3rdPerson.getValue().booleanValue() && Constants.mc.field_1690.method_31044().method_31034()) {
            Constants.mc.field_1690.method_31043(class_5498.field_26665);
            this.returnState = true;
        }
        double range = (Double)this.range.getValue() + Math.random() / 50.0;
        float targetYaw = switch (((Mode)((Object)this.mode.getValue())).ordinal()) {
            default -> throw new MatchException(null, null);
            case 0 -> RotationUtility.getRotationFromPosition((class_243)target.method_73189()).field_1343 + (float)(160 * (this.left ? -1 : 1));
            case 1 -> target.method_36454() - 180.0f;
        };
        class_243 positionToMove = new class_243((double)(-class_3532.method_15374((float)((float)Math.toRadians(targetYaw)))) * range + target.method_23317(), target.method_23318(), (double)class_3532.method_15362((float)((float)Math.toRadians(targetYaw))) * range + target.method_23321());
        this.yaw = RotationUtility.getRotationFromPosition((class_243)positionToMove).field_1343;
    }

    private class_1309 getKillAuraTarget() {
        return OpalClient.getInstance().getModuleRepository().getModule(KillAuraModule.class).getTargeting().getTarget().getEntity();
    }

    @Subscribe
    public void onRenderWorld(RenderWorldEvent event) {
        if (!(this.active && this.showRing.getValue().booleanValue() && this.shouldRun())) {
            return;
        }
        class_1309 target = this.getKillAuraTarget();
        class_243 position = MathUtility.interpolate(target, event.tickDelta());
        int blackColor = -16777216;
        class_4587 stack = event.matrixStack();
        Pair<Integer, Integer> colors = ColorUtility.getClientTheme();
        stack.method_22903();
        stack.method_22904(position.field_1352, position.field_1351, position.field_1350);
        this.calculateRingSegments();
        stack.method_22909();
    }

    @Override
    protected void onDisable() {
        this.active = false;
        super.onDisable();
    }

    public boolean isActive() {
        return this.active;
    }

    public float getYaw() {
        return this.yaw;
    }

    private boolean shouldRun() {
        if (this.requirements.getProperty("Jump key").getValue().booleanValue() && !PlayerUtility.isKeyPressed(Constants.mc.field_1690.field_1903)) {
            return false;
        }
        KillAuraModule killAuraModule = OpalClient.getInstance().getModuleRepository().getModule(KillAuraModule.class);
        if (!killAuraModule.isEnabled() || !killAuraModule.getTargeting().isTargetSelected()) {
            return false;
        }
        SpeedModule speedModule = OpalClient.getInstance().getModuleRepository().getModule(SpeedModule.class);
        return this.requirements.getProperty("Speed module").getValue() == false || speedModule.isEnabled();
    }

    private void calculateRingSegments() {
        float innerRadius = ((Double)this.range.getValue()).floatValue() + 1.0f - 0.015f;
        if (this.prevInnerRadius != -1.0f && this.prevInnerRadius == innerRadius) {
            return;
        }
        this.prevInnerRadius = innerRadius;
        float outlineThickness = 0.015f;
        for (int i = 0; i < 12; ++i) {
            float angle = (float)Math.PI * 2 * ((float)i / 12.0f);
            float sin = class_3532.method_15374((float)angle);
            float cos = class_3532.method_15362((float)angle);
            float mainInnerX = innerRadius * sin;
            float mainInnerZ = innerRadius * cos;
            float mainOuterX = (innerRadius + 0.03f) * sin;
            float mainOuterZ = (innerRadius + 0.03f) * cos;
            this.ringSegments[i] = new RingSegment(mainInnerX, mainInnerZ, mainOuterX, mainOuterZ);
            float outlineInnerInnerRadius = innerRadius - 0.015f;
            float innerOutlineInnerX = outlineInnerInnerRadius * sin;
            float innerOutlineInnerZ = outlineInnerInnerRadius * cos;
            float innerOutlineOuterX = innerRadius * sin;
            float innerOutlineOuterZ = innerRadius * cos;
            this.innerOutlineRingSegments[i] = new RingSegment(innerOutlineInnerX, innerOutlineInnerZ, innerOutlineOuterX, innerOutlineOuterZ);
            float outlineOuterInnerRadius = innerRadius + 0.03f;
            float outlineOuterOuterRadius = innerRadius + 0.03f + 0.015f;
            float outerOutlineInnerX = outlineOuterInnerRadius * sin;
            float outerOutlineInnerZ = outlineOuterInnerRadius * cos;
            float outerOutlineOuterX = outlineOuterOuterRadius * sin;
            float outerOutlineOuterZ = outlineOuterOuterRadius * cos;
            this.outerOutlineRingSegments[i] = new RingSegment(outerOutlineInnerX, outerOutlineInnerZ, outerOutlineOuterX, outerOutlineOuterZ);
        }
    }

    @Environment(value=EnvType.CLIENT)
    private static enum Mode {
        CIRCLE("Circle"),
        BEHIND("Behind");

        private final String name;

        private Mode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }

    @Environment(value=EnvType.CLIENT)
    private record RingSegment(float innerX, float innerZ, float outerX, float outerZ) {
    }
}

