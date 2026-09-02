/*
 * Spotify overlay with multiple selectable layouts (Overlay > Spotify > Layout):
 * Compact / Detailed / Wide / Minimal / Vertical.
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.spotify;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.visual.HudSettingsModule;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.property.impl.ScreenPositionProperty;
import wtf.opal.client.renderer.LiquidGlassRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.repository.ImageRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;

@Environment(value=EnvType.CLIENT)
public final class SpotifyElement implements IOverlayElement {
    private static final NVGTextRenderer BOLD_FONT = FontRepository.getFont("productsans-bold");
    private static final NVGTextRenderer MEDIUM_FONT = FontRepository.getFont("productsans-medium");
    private static final int ACCENT = 0xFF1DB954;
    private final SpotifySettings settings;
    private static volatile String currentSong = null;
    private static volatile String currentArtist = null;
    private static volatile float playbackProgress = 0.0f;
    private static long animationStart = System.currentTimeMillis();
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
                } catch (Exception e) {
                    currentSong = null;
                    currentArtist = null;
                }
                try {
                    Thread.sleep(3000L);
                } catch (InterruptedException e) {
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

    private String songName() {
        return currentSong == null ? "Not Playing" : (currentSong.length() > 25 ? currentSong.substring(0, 22) + "..." : currentSong);
    }

    private String artistName() {
        if (currentArtist != null && !currentArtist.isEmpty()) {
            return currentArtist.length() > 24 ? currentArtist.substring(0, 21) + "..." : currentArtist;
        }
        return currentSong == null ? "Waiting for Spotify..." : "";
    }

    private void drawIcon(float x, float y, float size) {
        NVGImageRenderer logo = ImageRepository.getImage("logo.png");
        if (logo != null) {
            logo.drawImage(x, y, size, size);
        } else {
            NVGRenderer.roundedRect(x, y, size, size, size / 4.0f, -14829228);
        }
    }

    private void drawProgressBar(float x, float y, float width, float height) {
        if (!this.settings.isShowProgressBar()) {
            return;
        }
        NVGRenderer.roundedRect(x, y, width, height, height / 2.0f, 0x25FFFFFF);
        float progress = Math.max(0.0f, Math.min(1.0f, playbackProgress));
        if (progress > 0.01f) {
            NVGRenderer.roundedRect(x, y, width * progress, height, height / 2.0f, ACCENT);
        } else if (this.settings.isAnimatedProgress() && currentSong != null) {
            // Indeterminate: a segment sliding back and forth
            double t = (System.currentTimeMillis() - animationStart) % 2400L / 2400.0;
            float phase = (float)(t < 0.5 ? t * 2.0 : 2.0 - t * 2.0);
            float segment = width * 0.3f;
            NVGRenderer.roundedRect(x + (width - segment) * phase, y, segment, height, height / 2.0f, ACCENT);
        }
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        ScreenPositionProperty pos = this.settings.getScreenPosition();
        String song = this.songName();
        String artist = this.artistName();
        boolean showArt = this.settings.isShowAlbumArt();
        HudSettingsModule hudSettings = OpalClient.getInstance().getModuleRepository().getModule(HudSettingsModule.class);
        float radius = hudSettings != null && hudSettings.isEnabled() ? hudSettings.getCornerRadius() : 12.0f;
        float padding = 8.0f;

        switch ((SpotifySettings.SpotifyLayout)this.settings.getLayout()) {
            case MINIMAL: {
                String line = currentSong == null ? song : (artist.isEmpty() ? song : song + " \u00b7 " + artist);
                float textW = Math.max(60.0f, MEDIUM_FONT.getStringWidth(line, 6.5f));
                float width = padding * 2.0f + textW;
                float height = 18.0f;
                pos.setWidth(width);
                pos.setHeight(height);
                float x = pos.getScaledX();
                float y = pos.getScaledY();
                LiquidGlassRenderer.drawGlassPanel(x, y, width, height, radius);
                MEDIUM_FONT.drawString(line, x + padding, y + 11.5f, 6.5f, -1);
                return;
            }
            case VERTICAL: {
                float iconSize = 30.0f;
                float width = 130.0f;
                float height = padding * 2.0f + (showArt ? iconSize + 6.0f : 0.0f) + 22.0f + (this.settings.isShowProgressBar() ? 8.0f : 0.0f);
                pos.setWidth(width);
                pos.setHeight(height);
                float x = pos.getScaledX();
                float y = pos.getScaledY();
                LiquidGlassRenderer.drawGlassPanel(x, y, width, height, radius);
                float cursorY = y + padding;
                if (showArt) {
                    this.drawIcon(x + width / 2.0f - iconSize / 2.0f, cursorY, iconSize);
                    cursorY += iconSize + 6.0f;
                }
                float songW = MEDIUM_FONT.getStringWidth(song, 7.0f);
                BOLD_FONT.drawString(song, x + width / 2.0f - songW / 2.0f, cursorY + 7.0f, 7.0f, -1);
                float artistW = MEDIUM_FONT.getStringWidth(artist, 6.0f);
                MEDIUM_FONT.drawString(artist, x + width / 2.0f - artistW / 2.0f, cursorY + 17.0f, 6.0f, -4473925);
                cursorY += 22.0f;
                if (this.settings.isShowProgressBar()) {
                    this.drawProgressBar(x + padding, cursorY + 2.0f, width - padding * 2.0f, 2.5f);
                }
                return;
            }
            case WIDE: {
                float iconSize = 22.0f;
                String line = artist.isEmpty() ? song : song + "  \u2022  " + artist;
                float textW = Math.max(80.0f, MEDIUM_FONT.getStringWidth(line, 7.0f));
                float width = padding * 3.0f + (showArt ? iconSize + padding : 0.0f) + textW;
                float height = padding * 2.0f + iconSize + (this.settings.isShowProgressBar() ? 5.0f : 0.0f);
                pos.setWidth(width);
                pos.setHeight(height);
                float x = pos.getScaledX();
                float y = pos.getScaledY();
                LiquidGlassRenderer.drawGlassPanel(x, y, width, height, radius);
                float cursorX = x + padding;
                if (showArt) {
                    this.drawIcon(cursorX, y + padding, iconSize);
                    cursorX += iconSize + padding;
                }
                MEDIUM_FONT.drawString(line, cursorX, y + padding + iconSize / 2.0f + 2.5f, 7.0f, -1);
                if (this.settings.isShowProgressBar()) {
                    this.drawProgressBar(x + padding, y + height - padding + 1.0f, width - padding * 2.0f, 2.5f);
                }
                return;
            }
            case DETAILED: {
                float iconSize = 34.0f;
                float textWidth = Math.max(MEDIUM_FONT.getStringWidth(song, 7.5f), MEDIUM_FONT.getStringWidth(artist, 6.5f));
                textWidth = Math.max(textWidth, 90.0f);
                float width = padding * 3.0f + (showArt ? iconSize + padding : 0.0f) + textWidth;
                float height = padding * 2.0f + iconSize + (this.settings.isShowProgressBar() ? 8.0f : 0.0f) + 8.0f;
                pos.setWidth(width);
                pos.setHeight(height);
                float x = pos.getScaledX();
                float y = pos.getScaledY();
                LiquidGlassRenderer.drawGlassPanel(x, y, width, height, radius);
                float cursorX = x + padding;
                if (showArt) {
                    this.drawIcon(cursorX, y + padding, iconSize);
                    cursorX += iconSize + padding;
                }
                MEDIUM_FONT.drawString("SPOTIFY", cursorX, y + padding + 1.0f, 4.5f, ACCENT);
                BOLD_FONT.drawString(song, cursorX, y + padding + 14.0f, 7.5f, -1);
                MEDIUM_FONT.drawString(artist, cursorX, y + padding + 26.0f, 6.5f, -4473925);
                if (this.settings.isShowProgressBar()) {
                    this.drawProgressBar(x + padding, y + height - padding - 2.0f, width - padding * 2.0f, 3.0f);
                }
                return;
            }
            case COMPACT:
            default: {
                float iconSize = 26.0f;
                float textWidth = Math.max(MEDIUM_FONT.getStringWidth(song, 7.0f), MEDIUM_FONT.getStringWidth(artist, 6.0f));
                textWidth = Math.max(textWidth, 80.0f);
                float width = padding * 3.0f + (showArt ? iconSize + padding : 0.0f) + textWidth;
                float height = padding * 2.0f + iconSize + 6.0f;
                pos.setWidth(width);
                pos.setHeight(height);
                float x = pos.getScaledX();
                float y = pos.getScaledY();
                LiquidGlassRenderer.drawGlassPanel(x, y, width, height, radius);
                float cursorX = x + padding;
                if (showArt) {
                    this.drawIcon(cursorX, y + padding, iconSize);
                    cursorX += iconSize + padding;
                }
                BOLD_FONT.drawString(song, cursorX, y + padding + 9.0f, 7.0f, -1);
                MEDIUM_FONT.drawString(artist, cursorX, y + padding + 20.0f, 6.0f, -4473925);
                float barY = y + height - 5.0f;
                this.drawProgressBar(x + padding, barY, width - padding * 2.0f, 2.5f);
            }
        }
    }
}
