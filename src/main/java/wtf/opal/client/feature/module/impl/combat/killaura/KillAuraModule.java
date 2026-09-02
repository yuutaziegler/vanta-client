/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.base.Predicates
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.hypixel.data.type.GameType
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_1799
 *  net.minecraft.class_239$class_240
 *  net.minecraft.class_243
 *  net.minecraft.class_3489
 *  net.minecraft.class_3966
 *  net.minecraft.class_4597
 *  net.minecraft.class_4597$class_4598
 *  net.minecraft.class_9799
 */
package wtf.opal.client.feature.module.impl.combat.killaura;

import com.google.common.base.Predicates;
import java.util.function.Predicate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hypixel.data.type.GameType;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_1799;
import net.minecraft.class_239;
import net.minecraft.class_243;
import net.minecraft.class_3489;
import net.minecraft.class_3966;
import net.minecraft.class_4597;
import net.minecraft.class_9799;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseButton;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.helper.impl.player.swing.SwingDelay;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.combat.BlockModule;
import wtf.opal.client.feature.module.impl.combat.killaura.KillAuraSettings;
import wtf.opal.client.feature.module.impl.combat.killaura.target.CurrentTarget;
import wtf.opal.client.feature.module.impl.combat.killaura.target.KillAuraTargeting;
import wtf.opal.client.feature.module.impl.combat.velocity.VelocityModule;
import wtf.opal.client.feature.module.impl.combat.velocity.impl.WatchdogVelocity;
import wtf.opal.client.feature.module.impl.world.breaker.BreakerModule;
import wtf.opal.client.feature.module.impl.world.scaffold.LBScaffoldModule;
import wtf.opal.client.feature.module.property.impl.mode.ModuleMode;
import wtf.opal.client.renderer.world.WorldRenderer;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.impl.game.player.movement.PostMovementPacketEvent;
import wtf.opal.event.impl.game.player.movement.PreMovementPacketEvent;
import wtf.opal.event.impl.render.RenderWorldEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.math.MathUtility;
import wtf.opal.utility.misc.math.RandomUtility;
import wtf.opal.utility.player.PlayerUtility;
import wtf.opal.utility.player.RaycastUtility;
import wtf.opal.utility.render.ColorUtility;
import wtf.opal.utility.render.CustomRenderLayers;

@Environment(value=EnvType.CLIENT)
public final class KillAuraModule
extends Module {
    private final KillAuraSettings settings = new KillAuraSettings(this);
    private final KillAuraTargeting targeting = new KillAuraTargeting(this.settings);
    private int attacks;
    private class_3966 hitResult;

    public KillAuraModule() {
        super("KillAura", "Finds and attacks the most relevant nearby entities.", ModuleCategory.COMBAT);
    }

    public KillAuraSettings getSettings() {
        return this.settings;
    }

    @Override
    public String getSuffix() {
        return this.settings.getMode().toString();
    }

    public KillAuraTargeting getTargeting() {
        return this.targeting;
    }

    @Subscribe
    public void onHandleInput(MouseHandleInputEvent event) {
        boolean allowSwingWhenUsing;
        CurrentTarget target = this.targeting.getTarget();
        if (target == null || Constants.mc.field_1765 == null || Constants.mc.field_1765.method_17783() == class_239.class_240.field_1333) {
            double closestDistance;
            if (!this.settings.getCpsProperty().isModernDelay() && (closestDistance = this.targeting.getClosestDistance()) <= this.settings.getSwingRange() && SwingDelay.isSwingAvailable(this.settings.getSwingCpsProperty()) && PlayerUtility.getBlockOver() == null) {
                MouseButton leftButton = MouseHelper.getLeftButton();
                leftButton.setPressed(true, RandomUtility.getRandomInt(2));
                if (this.settings.isHideFakeSwings() && Constants.mc.field_1765.method_17783() != class_239.class_240.field_1331) {
                    leftButton.setShowSwings(false);
                }
                this.settings.getSwingCpsProperty().resetClick();
            }
            return;
        }
        BlockModule blockModule = OpalClient.getInstance().getModuleRepository().getModule(BlockModule.class);
        boolean bl = allowSwingWhenUsing = blockModule.isEnabled() && blockModule.isSwingAllowed();
        if (Constants.mc.field_1724.method_6115() && !allowSwingWhenUsing) {
            return;
        }
        if (this.settings.isOverrideRaycast()) {
            if (this.settings.isTickLookahead() && (this.hitResult == null || this.hitResult.method_17782() != target.getEntity())) {
                return;
            }
            Constants.mc.field_1765 = target.getRotations().hitResult();
        }
        if (Constants.mc.field_1765.method_17783() == class_239.class_240.field_1331) {
            if (this.isAttackSwingAvailable(target)) {
                class_3966 hitResult = (class_3966)Constants.mc.field_1765;
                if (hitResult.method_17782() == target.getEntity()) {
                    MouseHelper.getLeftButton().setPressed();
                    target.getKillAuraTarget().onAttack(this.attacks == 0);
                    this.settings.getCpsProperty().resetClick();
                    SwingDelay.reset();
                    this.attacks = this.attacks > 0 ? --this.attacks : 2;
                }
            } else {
                this.attacks = 0;
            }
        }
    }

    private boolean isAttackSwingAvailable(CurrentTarget target) {
        WatchdogVelocity watchdogVelocity;
        ModuleMode<?> moduleMode;
        VelocityModule velocityModule = OpalClient.getInstance().getModuleRepository().getModule(VelocityModule.class);
        if (target.getKillAuraTarget().isAttackAvailable() || this.attacks > 0 || velocityModule.isEnabled() && (moduleMode = velocityModule.getActiveMode()) instanceof WatchdogVelocity && (watchdogVelocity = (WatchdogVelocity)moduleMode).isSprintReset()) {
            return true;
        }
        return SwingDelay.isSwingAvailable(this.settings.getCpsProperty(), false);
    }

    @Subscribe
    public void onRenderWorld(RenderWorldEvent event) {
        if (!this.targeting.isTargetSelected() || this.targeting.getTarget() == null || !this.settings.getVisuals().getProperty("Box").getValue().booleanValue()) {
            return;
        }
        class_1309 target = this.targeting.getTarget().getEntity();
        class_243 position = MathUtility.interpolate(target, event.tickDelta()).method_1019(Constants.mc.field_1773.method_19418().method_19326()).method_1023(0.25, 0.0, 0.25);
        class_243 dimensions = new class_243((double)target.method_17681(), (double)target.method_17682(), (double)target.method_17681());
        class_4597.class_4598 vcp = class_4597.method_22991((class_9799)new class_9799(1024));
        WorldRenderer rc = new WorldRenderer((class_4597)vcp);
        rc.drawFilledCube(event.matrixStack(), CustomRenderLayers.getPositionColorQuads(true), position, dimensions, ColorUtility.applyOpacity((int)((Integer)ColorUtility.getClientTheme().first), 0.25f));
        vcp.method_22993();
    }

    @Subscribe(priority=2)
    public void onPreGameTick(PreGameTickEvent event) {
        if (!this.shouldRun()) {
            this.targeting.reset();
            return;
        }
        this.targeting.update();
        CurrentTarget target = this.targeting.getRotationTarget();
        if (target == null) {
            return;
        }
        RotationHelper.getHandler().rotate(target.getRotations().rotation(), this.settings.createRotationModel());
    }

    @Subscribe
    public void onPreMovementPacket(PreMovementPacketEvent event) {
        if (!this.settings.isTickLookahead() || this.targeting.getRotationTarget() == null || !this.shouldRun()) {
            return;
        }
        this.targeting.update();
        CurrentTarget target = this.targeting.getRotationTarget();
        if (target == null) {
            return;
        }
        BreakerModule breakerModule = OpalClient.getInstance().getModuleRepository().getModule(BreakerModule.class);
        if (breakerModule.isEnabled() && breakerModule.isBreaking()) {
            return;
        }
        event.setYaw(Constants.mc.field_1724.method_36454());
        event.setPitch(Constants.mc.field_1724.method_36455());
    }

    @Subscribe
    public void onPostMovementPacket(PostMovementPacketEvent event) {
        if (!this.settings.isTickLookahead()) {
            return;
        }
        CurrentTarget target = this.targeting.getTarget();
        Object entityPredicate = target == null ? Predicates.alwaysTrue() : e -> e == target.getEntity();
        this.hitResult = RaycastUtility.raycastEntity(Constants.mc.field_1724.method_55755(), 1.0f, Constants.mc.field_1724.method_36454(), Constants.mc.field_1724.method_36455(), (Predicate<class_1297>)entityPredicate);
    }

    private boolean shouldRun() {
        if (Constants.mc.field_1724 == null) {
            return false;
        }
        if (this.settings.isRequireAttackKey() && !Constants.mc.field_1690.field_1886.method_1434()) {
            return false;
        }
        class_1799 heldItem = SlotHelper.getInstance().getMainHandStack(Constants.mc.field_1724);
        if (this.settings.isRequireWeapon() && !heldItem.method_31573(class_3489.field_42611) && !heldItem.method_31573(class_3489.field_42612) && !heldItem.method_31573(class_3489.field_42614)) {
            return false;
        }
        if (OpalClient.getInstance().getModuleRepository().getModule(LBScaffoldModule.class).isEnabled()) {
            return false;
        }
        if (LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer) {
            HypixelServer.ModAPI.Location currentLocation = HypixelServer.ModAPI.get().getCurrentLocation();
            return currentLocation == null || !currentLocation.isLobby() && currentLocation.serverType() != GameType.REPLAY;
        }
        return true;
    }

    @Override
    protected void onDisable() {
        this.targeting.reset();
        this.hitResult = null;
        this.attacks = 0;
        super.onDisable();
    }
}

