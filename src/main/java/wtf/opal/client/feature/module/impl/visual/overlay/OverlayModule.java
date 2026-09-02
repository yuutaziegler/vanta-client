/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.impl.visual.overlay;

import java.util.ArrayList;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.render.ScaleProperty;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.visual.StreamerModeModule;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.armor.ArmorHUDElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.client.ClientElements;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.client.LogoElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.DynamicIslandElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.keystrokes.KeystrokesElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.modulelist.ToggledModulesElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.notifications.NotificationsElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.scaffold.ScaffoldElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.spotify.SpotifyElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.targetinfo.TargetInfoElement;
import wtf.opal.client.feature.module.property.impl.ColorProperty;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.event.impl.client.PostClientInitializationEvent;
import wtf.opal.event.impl.client.PropertyUpdateEvent;
import wtf.opal.event.impl.game.PostGameTickEvent;
import wtf.opal.event.impl.render.RenderBloomEvent;
import wtf.opal.event.impl.render.RenderScreenEvent;
import wtf.opal.event.impl.render.ResolutionChangeEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.render.ClientTheme;

@Environment(value=EnvType.CLIENT)
public final class OverlayModule
extends Module {
    private final ModeProperty<ClientTheme> themeMode = new ModeProperty<ClientTheme>("Theme", ClientTheme.TERENTX, true);
    public static final ColorProperty primaryColorProperty = new ColorProperty("Primary color", -16777216);
    public static final ColorProperty secondaryColorProperty = new ColorProperty("Secondary color", -16777216);
    private final BooleanProperty statusEffectOverlayEnabled = new BooleanProperty("Enabled", false);
    private final BooleanProperty scoreboardEnabled = new BooleanProperty("Enabled", true);
    private final BooleanProperty scoreboardTextShadow = (BooleanProperty)new BooleanProperty("Text shadow", true).hideIf(() -> this.scoreboardEnabled.getValue() == false);
    private final ScaleProperty scoreboardScale = ScaleProperty.newMinecraftElement();
    private final BooleanProperty bossbarEnabled = new BooleanProperty("Enabled", false);
    private final BooleanProperty dynamicIslandLeftAligned = new BooleanProperty("Left-aligned", false);
    private final List<IOverlayElement> elements = new ArrayList<IOverlayElement>();
    private final TargetInfoElement targetInfo;
    private final ToggledModulesElement toggledModules;
    private final NotificationsElement notifications;

    public OverlayModule() {
        super("Overlay", "Renders the clients display.", ModuleCategory.VISUAL);
        primaryColorProperty.hideIf(() -> !this.themeMode.is(ClientTheme.CUSTOM));
        secondaryColorProperty.hideIf(() -> !this.themeMode.is(ClientTheme.CUSTOM));
        this.setEnabled(true);
        this.addProperties(this.themeMode, primaryColorProperty, secondaryColorProperty, new GroupProperty("Minecraft elements", new GroupProperty("Status effect overlay", this.statusEffectOverlayEnabled), new GroupProperty("Scoreboard", this.scoreboardScale.get(), this.scoreboardEnabled, this.scoreboardTextShadow), new GroupProperty("Bossbar", this.bossbarEnabled)));
        this.targetInfo = this.register(new TargetInfoElement(this));
        this.toggledModules = this.register(new ToggledModulesElement(this));
        this.register(new ClientElements(this));
        this.addProperties(new GroupProperty("Dynamic island", this.dynamicIslandLeftAligned));
        this.notifications = this.register(new NotificationsElement(this));
        this.register(new DynamicIslandElement(this));
        this.register(new SpotifyElement(this));
        this.register(new LogoElement(this));
        this.register(new ArmorHUDElement(this));
        this.register(new KeystrokesElement(this));
        this.register(new ScaffoldElement(this));
    }

    private <T extends IOverlayElement> T register(T element) {
        this.elements.add(element);
        return element;
    }

    @Override
    protected void onDisable() {
        this.elements.forEach(IOverlayElement::onDisable);
    }

    @Override
    protected void onEnable() {
        if (OpalClient.getInstance().isPostInitialization()) {
            this.toggledModules.initialize();
            this.targetInfo.initialize();
        }
    }

    @Subscribe
    public void onPostClientInitialization(PostClientInitializationEvent event) {
        this.toggledModules.initialize();
        this.targetInfo.initialize();
    }

    @Subscribe
    public void onPropertyUpdate(PropertyUpdateEvent event) {
        if (this.toggledModules != null) {
            this.toggledModules.markSortingDirty();
        }
    }

    @Subscribe(priority=-20)
    public void onRenderScreen(RenderScreenEvent event) {
        StreamerModeModule streamerMode = OpalClient.getInstance().getModuleRepository().getModule(StreamerModeModule.class);
        if (streamerMode != null && streamerMode.isEnabled() && streamerMode.isHidingOverlay()) {
            return;
        }
        for (IOverlayElement element : this.elements) {
            if (!element.isActive()) continue;
            element.render(event.drawContext(), event.tickDelta(), false);
        }
    }

    @Subscribe(priority=-20)
    public void onBloomRender(RenderBloomEvent event) {
        StreamerModeModule streamerMode = OpalClient.getInstance().getModuleRepository().getModule(StreamerModeModule.class);
        if (streamerMode != null && streamerMode.isEnabled() && streamerMode.isHidingOverlay()) {
            return;
        }
        for (IOverlayElement element : this.elements) {
            if (!element.isActive() || !element.isBloom()) continue;
            element.render(event.drawContext(), event.tickDelta(), true);
        }
    }

    @Subscribe
    public void onResize(ResolutionChangeEvent event) {
        this.elements.forEach(IOverlayElement::onResize);
    }

    @Subscribe
    public void onPostTick(PostGameTickEvent event) {
        for (IOverlayElement element : this.elements) {
            if (!element.isActive()) continue;
            element.tick();
        }
    }

    public ModeProperty<ClientTheme> getThemeMode() {
        return this.themeMode;
    }

    public ToggledModulesElement getToggledModules() {
        return this.toggledModules;
    }

    public NotificationsElement getNotifications() {
        return this.notifications;
    }

    public boolean isDynamicIslandLeftAligned() {
        return this.dynamicIslandLeftAligned.getValue();
    }

    public boolean isScoreboardTextShadow() {
        return this.scoreboardEnabled.getValue() != false && this.scoreboardTextShadow.getValue() != false;
    }

    public float getScoreboardScale() {
        return this.scoreboardEnabled.getValue() != false ? this.scoreboardScale.getScale() : 1.0f;
    }

    public boolean isBossbarEnabled() {
        return this.bossbarEnabled.getValue();
    }

    public boolean isStatusEffectOverlayEnabled() {
        return this.statusEffectOverlayEnabled.getValue();
    }

    public List<IOverlayElement> getElements() {
        return this.elements;
    }
}

