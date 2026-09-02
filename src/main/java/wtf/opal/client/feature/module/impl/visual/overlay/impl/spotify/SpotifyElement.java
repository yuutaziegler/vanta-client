/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2561
 *  net.minecraft.class_327
 *  net.minecraft.class_332
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.spotify;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2561;
import net.minecraft.class_327;
import net.minecraft.class_332;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.HudSettingsModule;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.spotify.SpotifySettings;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.renderer.LiquidGlassRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.repository.ImageRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;

@Environment(value=EnvType.CLIENT)
public final class SpotifyElement
implements IOverlayElement {
    private static final NVGTextRenderer BOLD_FONT = FontRepository.getFont("productsans-bold");
    private static final NVGTextRenderer MEDIUM_FONT = FontRepository.getFont("productsans-medium");
    private static final class_327 MC_FONT = Constants.mc.field_1772;
    private final SpotifySettings settings;
    private static volatile String currentSong = null;
    private static volatile String currentArtist = null;
    private static volatile float playbackProgress = 0.0f;
    private static Thread pollingThread;

    public SpotifyElement(OverlayModule module) {
        this.settings = new SpotifySettings(module);
        this.startPolling();
    }

    private void startPolling() {
        if (pollingThread != null && pollingThread.isAlive()) {
            return;
        }
        pollingThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Process p = new ProcessBuilder("powershell", "-NoProfile", "-Command", "try { Add-Type -AssemblyName System.Runtime.WindowsRuntime; $smg = [Windows.Media.Control.GlobalSystemMediaTransportControlsSessionManager,Windows.Media.Control,ContentType=WindowsRuntime]; $mgr = $smg::RequestAsync().GetAwaiter().GetResult(); $session = $mgr.GetCurrentSession(); if ($session -ne $null) { $info = $session.TryGetMediaPropertiesAsync().GetAwaiter().GetResult(); Write-Output ($info.Title + '|' + $info.Artist) } } catch { Write-Output 'NONE' }").start();
                    String output = new String(p.getInputStream().readAllBytes()).trim();
                    p.waitFor();
                    if (!output.isEmpty() && !output.equals("NONE") && output.contains("|")) {
                        String[] parts = output.split("\\|", 2);
                        currentSong = parts[0].trim();
                        currentArtist = parts.length > 1 ? parts[1].trim() : "";
                    } else {
                        currentSong = null;
                        currentArtist = null;
                    }
                }
                catch (Exception e) {
                    currentSong = null;
                    currentArtist = null;
                }
                try {
                    Thread.sleep(3000L);
                }
                catch (InterruptedException e) {
                    break;
                }
            }
        }, "TerentX-MediaPoller");
        pollingThread.setDaemon(true);
        pollingThread.start();
    }

    @Override
    public ScreenPositionProperty getPositionProperty() {
        return this.settings.getScreenPosition();
    }

    @Override
    public boolean isActive() {
        return this.settings.isEnabled();
    }

    @Override
    public boolean isBloom() {
        return false;
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        String songName;
        ScreenPositionProperty pos = this.settings.getScreenPosition();
        String string = currentSong == null ? "Not Playing" : (songName = currentSong.length() > 25 ? currentSong.substring(0, 22) + "..." : currentSong);
        String artistName = currentArtist != null ? (currentArtist.length() > 22 ? currentArtist.substring(0, 19) + "..." : currentArtist) : (currentSong == null ? "Waiting for Spotify..." : "");
        float padding = 8.0f;
        float iconSize = 26.0f;
        float textWidth = Math.max(MC_FONT.method_1727(songName), MC_FONT.method_1727(artistName));
        textWidth = Math.max(textWidth, 80.0f);
        float width = padding * 3.0f + iconSize + textWidth;
        float height = padding * 2.0f + iconSize + 6.0f;
        pos.setWidth(width);
        pos.setHeight(height);
        float x = pos.getScaledX();
        float y = pos.getScaledY();
        HudSettingsModule hudSettings = OpalClient.getInstance().getModuleRepository().getModule(HudSettingsModule.class);
        float radius = hudSettings != null && hudSettings.isEnabled() ? hudSettings.getCornerRadius() : 12.0f;
        LiquidGlassRenderer.drawGlassPanel(x, y, width, height, radius);
        NVGImageRenderer logo = ImageRepository.getImage("logo.png");
        if (logo != null) {
            logo.drawImage(x + padding, y + padding, iconSize, iconSize);
        } else {
            NVGRenderer.roundedRect(x + padding, y + padding, iconSize, iconSize, 8.0f, -14829228);
        }
        NVGRenderer.endFrame(false);
        context.method_27535(MC_FONT, (class_2561)class_2561.method_43470((String)songName), (int)(x + padding * 2.0f + iconSize), (int)(y + padding + 2.0f), -1);
        context.method_27535(MC_FONT, (class_2561)class_2561.method_43470((String)artistName), (int)(x + padding * 2.0f + iconSize), (int)(y + padding + 14.0f), -4473925);
        NVGRenderer.beginFrame();
        float barY = y + height - 5.0f;
        float barW = width - padding * 2.0f;
        NVGRenderer.roundedRect(x + padding, barY, barW, 2.5f, 1.0f, 0x25FFFFFF);
        float progress = Math.max(0.0f, Math.min(1.0f, playbackProgress));
        if (progress > 0.01f) {
            NVGRenderer.roundedRect(x + padding, barY, barW * progress, 2.5f, 1.0f, -855638017);
        }
    }
}

