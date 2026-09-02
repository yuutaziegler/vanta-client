/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 *  net.minecraft.class_640
 *  net.minecraft.class_642
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.preset;

import com.ibm.icu.impl.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import net.minecraft.class_640;
import net.minecraft.class_642;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.ReleaseInfo;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.KnownServer;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.dynamicisland.IslandTrigger;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.repository.ImageRepository;
import wtf.opal.client.renderer.text.NVGTextRenderer;
import wtf.opal.utility.render.ClientTheme;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public class DefaultIsland
implements IslandTrigger {
    private float width;

    @Override
    public void renderIsland(class_332 context, float posX, float posY, float width, float height, float progress) {
        class_642 serverInfo;
        NVGTextRenderer titleFont = FontRepository.getFont("productsans-bold");
        NVGTextRenderer footerFont = FontRepository.getFont("productsans-medium");
        String opalText = "TerentX";
        String releaseType = ReleaseInfo.CHANNEL.toString();
        String releaseVersion = "v0.1-beta.1";
        String serverAddress = "singleplayer";
        Object serverPing = "0 ms";
        if (Constants.mc.method_1562() != null && (serverInfo = Constants.mc.method_1562().method_45734()) != null) {
            KnownServer currentKnownServer = LocalDataWatch.get().getKnownServerManager().getCurrentServer();
            serverAddress = currentKnownServer != null && currentKnownServer.getProxyServer() != null ? currentKnownServer.getProxyServer().getName().toLowerCase() : serverInfo.field_3761.toLowerCase();
            serverAddress = serverAddress.length() > 20 ? serverAddress.substring(0, 17) + "..." : serverAddress;
            long latency = 0L;
            class_640 playerListEntry = Constants.mc.method_1562().method_2871(Constants.mc.method_1548().method_44717());
            if (playerListEntry != null) {
                latency = playerListEntry.method_2959();
            }
            if (latency < 2L) {
                latency = serverInfo.field_3758;
            }
            serverPing = latency + " ms";
        }
        float titleTextSize = 11.5f;
        float secondaryTextSize = 7.0f;
        float footerTextSize = 6.0f;
        float releaseInfoWidth = Math.max(titleFont.getStringWidth(releaseType, 7.0f), footerFont.getStringWidth("v0.1-beta.1", 6.0f));
        this.width = 14.0f + titleFont.getStringWidth("TerentX", 11.5f) + releaseInfoWidth + titleFont.getStringWidth(serverAddress, 7.0f) + 35.0f;
        ClientTheme theme = (ClientTheme)((Object)OpalClient.getInstance().getModuleRepository().getModule(OverlayModule.class).getThemeMode().getValue());
        Pair<Integer, Integer> colors = theme.getColors();
        boolean grayscale = theme != ClientTheme.TERENTX;
        NVGImageRenderer iconRenderer = this.getAppropriateImage(Constants.mc.method_22683().method_4495(), grayscale);
        int xOffset = 10;
        int yOffset = 5;
        String version = "0.1-beta.1";
        int baseYOffset = 5;
        int baseXOffset = 10;
        float dividerHeight = 17.5f;
        float textSpacing = 1.5f;
        int colorIndex = 5;
        if (grayscale) {
            int interpolatedColor = ColorUtility.interpolateColorsBackAndForth(5, 1, (Integer)colors.second, (Integer)colors.first);
            iconRenderer.drawImage(posX + 10.0f - 4.0f, posY + 5.0f + 1.5f, 16.0f, 16.0f, ColorUtility.brighter(interpolatedColor, 0.2f));
        } else {
            iconRenderer.drawImage(posX + 10.0f - 4.0f, posY + 5.0f + 1.5f, 16.0f, 16.0f);
        }
        float textStart = posX + 26.5f - 2.0f;
        titleFont.drawGradientString("TerentX", textStart, posY + 5.0f + 2.5f + 10.0f, 11.5f, (Integer)colors.second, (Integer)colors.first);
        float releaseTypeStart = textStart + titleFont.getStringWidth("TerentX", 11.5f) + 3.3f + 1.0f;
        NVGRenderer.rect(releaseTypeStart, posY + 11.666667f - 2.0f, 0.75f, 10.0f, -8355712);
        titleFont.drawString(releaseType, releaseTypeStart + 1.5f + 2.0f, posY + 13.461539f + 1.0f, 7.0f, -1);
        footerFont.drawString("v0.1-beta.1", releaseTypeStart + 1.5f + 2.0f, posY + 19.5f + 1.0f, 6.0f, -8355712);
        float serverIPStart = releaseTypeStart + 1.5f + releaseInfoWidth + 3.3f;
        NVGRenderer.rect(serverIPStart + 1.0f, posY + 11.666667f - 2.0f, 0.75f, 10.0f, -8355712);
        titleFont.drawString(serverAddress, serverIPStart + 1.5f + 3.0f, posY + 13.461539f + 1.0f, 7.0f, -1);
        footerFont.drawString((String)serverPing, serverIPStart + 1.5f + 3.0f, posY + 19.5f + 1.0f, 6.0f, -8355712);
    }

    private NVGImageRenderer getAppropriateImage(double scaleFactor, boolean grayscale) {
        return ImageRepository.getImage("logo.png");
    }

    @Override
    public float getIslandWidth() {
        return this.width;
    }

    @Override
    public float getIslandHeight() {
        return 28.0f;
    }

    @Override
    public int getIslandPriority() {
        return -5;
    }
}

