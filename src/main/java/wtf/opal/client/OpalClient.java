/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
 */
package wtf.opal.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import wtf.opal.client.binding.repository.BindRepository;
import wtf.opal.client.command.impl.config.ConfigCommand;
import wtf.opal.client.command.impl.misc.DashboardCommand;
import wtf.opal.client.command.impl.misc.ScriptCommand;
import wtf.opal.client.command.impl.misc.WebhookCommand;
import wtf.opal.client.command.impl.module.BindCommand;
import wtf.opal.client.command.impl.module.ToggleCommand;
import wtf.opal.client.command.impl.player.FriendCommand;
import wtf.opal.client.command.impl.player.UsernameCommand;
import wtf.opal.client.command.impl.player.movement.HClipCommand;
import wtf.opal.client.command.impl.player.movement.VClipCommand;
import wtf.opal.client.command.repository.CommandRepository;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.chat.ChatHelper;
import wtf.opal.client.feature.helper.impl.player.hypixel.TransactionStreamValidator;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.helper.impl.player.swing.SwingDelay;
import wtf.opal.client.feature.helper.impl.player.timer.TimerHelper;
import wtf.opal.client.feature.helper.impl.render.FadingBlockHelper;
import wtf.opal.client.feature.helper.impl.render.ScreenPositionManager;
import wtf.opal.client.feature.module.impl.combat.AttackDelayModule;
import wtf.opal.client.feature.module.impl.combat.AutoClickerModule;
import wtf.opal.client.feature.module.impl.combat.AutoHeadModule;
import wtf.opal.client.feature.module.impl.combat.BlockModule;
import wtf.opal.client.feature.module.impl.combat.PiercingModule;
import wtf.opal.client.feature.module.impl.combat.ReachModule;
import wtf.opal.client.feature.module.impl.combat.criticals.CriticalsModule;
import wtf.opal.client.feature.module.impl.combat.killaura.KillAuraModule;
import wtf.opal.client.feature.module.impl.combat.killaura.LBKillAuraModule;
import wtf.opal.client.feature.module.impl.combat.velocity.VelocityModule;
import wtf.opal.client.feature.module.impl.exploit.ServerCrasherModule;
import wtf.opal.client.feature.module.impl.misc.WebhookLoggerModule;
import wtf.opal.client.feature.module.impl.movement.FastStopModule;
import wtf.opal.client.feature.module.impl.movement.InventoryMoveModule;
import wtf.opal.client.feature.module.impl.movement.JumpCooldownModule;
import wtf.opal.client.feature.module.impl.movement.MovementFixModule;
import wtf.opal.client.feature.module.impl.movement.PhaseModule;
import wtf.opal.client.feature.module.impl.movement.SafeWalkModule;
import wtf.opal.client.feature.module.impl.movement.SpiderModule;
import wtf.opal.client.feature.module.impl.movement.SprintModule;
import wtf.opal.client.feature.module.impl.movement.StrafeModule;
import wtf.opal.client.feature.module.impl.movement.TargetStrafeModule;
import wtf.opal.client.feature.module.impl.movement.clipper.ClipperModule;
import wtf.opal.client.feature.module.impl.movement.flight.FlightModule;
import wtf.opal.client.feature.module.impl.movement.longjump.LongJumpModule;
import wtf.opal.client.feature.module.impl.movement.noslow.NoSlowModule;
import wtf.opal.client.feature.module.impl.movement.physics.PhysicsModule;
import wtf.opal.client.feature.module.impl.movement.speed.SpeedModule;
import wtf.opal.client.feature.module.impl.utility.AntiVoidModule;
import wtf.opal.client.feature.module.impl.utility.AutoChestModule;
import wtf.opal.client.feature.module.impl.utility.AutoHypixelModule;
import wtf.opal.client.feature.module.impl.utility.AutoToolModule;
import wtf.opal.client.feature.module.impl.utility.BlinkModule;
import wtf.opal.client.feature.module.impl.utility.FastUseModule;
import wtf.opal.client.feature.module.impl.utility.NoRotateModule;
import wtf.opal.client.feature.module.impl.utility.PartySpamModule;
import wtf.opal.client.feature.module.impl.utility.SpammerModule;
import wtf.opal.client.feature.module.impl.utility.disabler.DisablerModule;
import wtf.opal.client.feature.module.impl.utility.inventory.AutoArmorModule;
import wtf.opal.client.feature.module.impl.utility.inventory.ChestStealerModule;
import wtf.opal.client.feature.module.impl.utility.inventory.manager.InventoryManagerModule;
import wtf.opal.client.feature.module.impl.utility.nofall.NoFallModule;
import wtf.opal.client.feature.module.impl.visual.AmbienceModule;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;
import wtf.opal.client.feature.module.impl.visual.AttackEffectsModule;
import wtf.opal.client.feature.module.impl.visual.BreakProgressModule;
import wtf.opal.client.feature.module.impl.visual.CapeModule;
import wtf.opal.client.feature.module.impl.visual.ChamsModule;
import wtf.opal.client.feature.module.impl.visual.ClickGUIModule;
import wtf.opal.client.feature.module.impl.visual.FullbrightModule;
import wtf.opal.client.feature.module.impl.visual.HUDEditorModule;
import wtf.opal.client.feature.module.impl.visual.HudSettingsModule;
import wtf.opal.client.feature.module.impl.visual.MotionBlurModule;
import wtf.opal.client.feature.module.impl.visual.NoHurtCameraModule;
import wtf.opal.client.feature.module.impl.visual.PostProcessingModule;
import wtf.opal.client.feature.module.impl.visual.StreamerModeModule;
import wtf.opal.client.feature.module.impl.visual.TabGUIModule;
import wtf.opal.client.feature.module.impl.visual.esp.ESPModule;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.world.FastBreakModule;
import wtf.opal.client.feature.module.impl.world.TimerModule;
import wtf.opal.client.feature.module.impl.world.breaker.BreakerModule;
import wtf.opal.client.feature.module.impl.world.scaffold.LBScaffoldModule;
import wtf.opal.client.feature.module.repository.ModuleRepository;
import wtf.opal.client.notification.NotificationManager;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.client.PostClientInitializationEvent;
import wtf.opal.scripting.repository.ScriptRepository;
import wtf.opal.utility.data.SaveUtility;

@Environment(value=EnvType.CLIENT)
public final class OpalClient {
    private final NotificationManager notificationManager = new NotificationManager();
    private final BindRepository bindRepository = new BindRepository();
    private CommandRepository commandRepository;
    private ModuleRepository moduleRepository;
    private ScriptRepository scriptRepository;
    private String user = "BetaUser";
    private boolean postInitialization;
    private static OpalClient instance;

    private OpalClient() {
    }

    public void runPostInitializations() {
        this.runHelperInitializations();
        if (this.moduleRepository == null) {
            this.moduleRepository = ModuleRepository.fromModules(new LBKillAuraModule(), new KillAuraModule(), new BlockModule(), new ReachModule(), new PiercingModule(), new AutoClickerModule(), new AttackDelayModule(), new CriticalsModule(), new VelocityModule(), new AutoHeadModule(), new ClickGUIModule(), new FullbrightModule(), new AnimationsModule(), new OverlayModule(), new ChamsModule(), new ESPModule(), new BreakProgressModule(), new CapeModule(), new AmbienceModule(), new AttackEffectsModule(), new TabGUIModule(), new StreamerModeModule(), new HUDEditorModule(), new HudSettingsModule(), new NoHurtCameraModule(), new PostProcessingModule(), new MotionBlurModule(), new LBScaffoldModule(), new TimerModule(), new BreakerModule(), new FastBreakModule(), new FlightModule(), new SpeedModule(), new JumpCooldownModule(), new SprintModule(), new MovementFixModule(), new NoSlowModule(), new InventoryMoveModule(), new TargetStrafeModule(), new PhaseModule(), new LongJumpModule(), new FastStopModule(), new StrafeModule(), new PhysicsModule(), new SpiderModule(), new ClipperModule(), new SafeWalkModule(), new FastUseModule(), new NoFallModule(), new ChestStealerModule(), new InventoryManagerModule(), new AutoArmorModule(), new DisablerModule(), new AntiVoidModule(), new AutoToolModule(), new AutoChestModule(), new AutoHypixelModule(), new BlinkModule(), new NoRotateModule(), new SpammerModule(), new PartySpamModule(), new ServerCrasherModule(), new WebhookLoggerModule());
        }
        SaveUtility.loadBindings();
        if (this.commandRepository == null) {
            this.commandRepository = CommandRepository.builder().putAll(new ToggleCommand(), new BindCommand(), new ConfigCommand(), new VClipCommand(), new HClipCommand(), new UsernameCommand(), new DashboardCommand(), new WebhookCommand(), new FriendCommand(), new ScriptCommand()).build();
        }
        if (this.scriptRepository == null) {
            this.scriptRepository = new ScriptRepository();
        }
        Runtime.getRuntime().addShutdownHook(new Thread(this::onShutdown));
        this.postInitialization = true;
        EventDispatcher.dispatch(new PostClientInitializationEvent());
        PayloadTypeRegistry.playS2C().register(PhysicsModule.ResyncPhysicsPayload.ID, PhysicsModule.ResyncPhysicsPayload.CODEC);
    }

    private void runHelperInitializations() {
        LocalDataWatch.setInstance();
        MouseHelper.setInstance();
        SwingDelay.setInstance();
        SlotHelper.setInstance();
        ChatHelper.setInstance();
        TimerHelper.setInstance();
        FadingBlockHelper.setInstance();
        ScreenPositionManager.setInstance();
        TransactionStreamValidator.setInstance();
    }

    private void registerFabricEvents() {
    }

    private void onShutdown() {
        this.moduleRepository.getModule(ClickGUIModule.class).setEnabled(false);
        SaveUtility.saveConfig("default");
        SaveUtility.saveBindings();
    }

    public boolean isPostInitialization() {
        return this.postInitialization;
    }

    public ModuleRepository getModuleRepository() {
        return this.moduleRepository;
    }

    public BindRepository getBindRepository() {
        return this.bindRepository;
    }

    public NotificationManager getNotificationManager() {
        return this.notificationManager;
    }

    public ScriptRepository getScriptRepository() {
        return this.scriptRepository;
    }

    public String getUser() {
        return this.user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public static OpalClient getInstance() {
        if (instance == null) {
            instance = new OpalClient();
        }
        return instance;
    }

    public static void setInstance() {
        instance = new OpalClient();
    }
}

