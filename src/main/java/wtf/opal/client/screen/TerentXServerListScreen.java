/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_11905
 *  net.minecraft.class_11908
 *  net.minecraft.class_11909
 *  net.minecraft.class_156
 *  net.minecraft.class_2561
 *  net.minecraft.class_310
 *  net.minecraft.class_332
 *  net.minecraft.class_342
 *  net.minecraft.class_364
 *  net.minecraft.class_412
 *  net.minecraft.class_437
 *  net.minecraft.class_639
 *  net.minecraft.class_642
 *  net.minecraft.class_642$class_8678
 */
package wtf.opal.client.screen;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_11905;
import net.minecraft.class_11908;
import net.minecraft.class_11909;
import net.minecraft.class_156;
import net.minecraft.class_2561;
import net.minecraft.class_310;
import net.minecraft.class_332;
import net.minecraft.class_342;
import net.minecraft.class_364;
import net.minecraft.class_412;
import net.minecraft.class_437;
import net.minecraft.class_639;
import net.minecraft.class_642;
import wtf.opal.client.Constants;
import wtf.opal.client.renderer.LiquidGlassRenderer;
import wtf.opal.client.renderer.NVGRenderer;
import wtf.opal.client.renderer.image.NVGImageRenderer;
import wtf.opal.client.renderer.repository.FontRepository;
import wtf.opal.client.renderer.repository.ImageRepository;
import wtf.opal.utility.misc.HoverUtility;
import wtf.opal.utility.render.ColorUtility;

@Environment(value=EnvType.CLIENT)
public class TerentXServerListScreen
extends class_437 {
    private final class_437 parent;
    private class_342 serverInput;
    private final Random random = new Random();
    private static final List<ServerEntry> SERVERS = new ArrayList<ServerEntry>();
    private int scrollOffset = 0;
    private static final int ENTRY_HEIGHT = 56;
    private static final int VISIBLE_ENTRIES = 6;
    private long lastClickTime = 0L;
    private long initTime = System.currentTimeMillis();
    private int hoveredEntry = -1;
    private int connectBtnX;
    private int connectBtnY;
    private int connectBtnW = 100;
    private int connectBtnH = 26;
    private int randomBtnX;
    private int randomBtnY;
    private int randomBtnW;
    private int randomBtnH = 26;
    private int backBtnX;
    private int backBtnY;
    private int backBtnW;
    private int backBtnH = 26;
    private int panelX;
    private int panelY;
    private int panelW;
    private int visibleH;

    public TerentXServerListScreen(class_437 parent) {
        super((class_2561)class_2561.method_43470((String)"Select Server"));
        this.parent = parent;
    }

    protected void method_25426() {
        int w = this.field_22789;
        int h = this.field_22790;
        this.panelW = Math.min(460, w - 40);
        this.panelX = w / 2 - this.panelW / 2;
        this.panelY = 40;
        this.visibleH = 384;
        int inputY = this.panelY + this.visibleH + 14;
        int inputW = this.panelW - this.connectBtnW - 8;
        this.serverInput = new class_342(Constants.mc.field_1772, this.panelX, inputY, inputW, 26, (class_2561)class_2561.method_43470((String)"Server IP"));
        this.serverInput.method_47404((class_2561)class_2561.method_43470((String)"Enter server address..."));
        this.method_37063((class_364)this.serverInput);
        this.connectBtnX = this.panelX + inputW + 8;
        this.connectBtnY = inputY;
        this.randomBtnX = this.panelX;
        this.randomBtnY = inputY + 34;
        this.randomBtnW = this.panelW / 2 - 4;
        this.backBtnX = this.panelX + this.panelW / 2 + 4;
        this.backBtnY = inputY + 34;
        this.backBtnW = this.panelW / 2 - 4;
    }

    public void method_25420(class_332 context, int mouseX, int mouseY, float delta) {
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void method_25394(class_332 context, int mouseX, int mouseY, float delta) {
        NVGRenderer.beginFrame();
        NVGImageRenderer bgImg = ImageRepository.getImage("image/mmpng.png");
        if (bgImg != null) {
            bgImg.drawImage(0.0f, 0.0f, this.field_22789, this.field_22790);
        }
        NVGRenderer.endFrame(false);
        int listY = this.panelY + 48;
        this.hoveredEntry = -1;
        int maxEntry = Math.min(this.scrollOffset + 6, SERVERS.size());
        for (int i = this.scrollOffset; i < maxEntry; ++i) {
            int entryX = this.panelX + 8;
            int entryY = listY + (i - this.scrollOffset) * 56;
            int entryW = this.panelW - 16;
            if (!HoverUtility.isHovering(entryX, entryY, entryW, 50.0f, mouseX, mouseY)) continue;
            this.hoveredEntry = i;
        }
        boolean hovConnect = HoverUtility.isHovering(this.connectBtnX, this.connectBtnY, this.connectBtnW, this.connectBtnH, mouseX, mouseY);
        boolean hovRandom = HoverUtility.isHovering(this.randomBtnX, this.randomBtnY, this.randomBtnW, this.randomBtnH, mouseX, mouseY);
        boolean hovBack = HoverUtility.isHovering(this.backBtnX, this.backBtnY, this.backBtnW, this.backBtnH, mouseX, mouseY);
        boolean nvgStarted = NVGRenderer.beginFrame();
        try {
            LiquidGlassRenderer.drawGlassPanel(this.panelX, this.panelY, this.panelW, this.visibleH, 14.0f);
            FontRepository.getFont("productsans-bold").drawGradientString("Select Server", this.panelX + 16, this.panelY + 18, 13.0f, -11890433, -7617281);
            FontRepository.getFont("productsans-medium").drawString(SERVERS.size() + " servers available", this.panelX + 16, this.panelY + 34, 7.5f, ColorUtility.applyOpacity(-1, 0.38f));
            NVGRenderer.roundedRect((float)(this.panelX + 8), (float)(this.panelY + 42), (float)(this.panelW - 16), 1.0f, 0.0f, ColorUtility.applyOpacity(-1, 0.1f));
            for (int i = this.scrollOffset; i < maxEntry; ++i) {
                ServerEntry entry = SERVERS.get(i);
                float entryX = this.panelX + 8;
                float entryW = this.panelW - 16;
                float entryY = listY + (i - this.scrollOffset) * 56;
                boolean hov = this.hoveredEntry == i;
                LiquidGlassRenderer.drawGlassPanel(entryX, entryY, entryW, 50.0f, 10.0f);
                if (hov) {
                    LiquidGlassRenderer.drawGlassHighlight(entryX, entryY, entryW, 50.0f, 10.0f, -11890433, 0.45f);
                }
                FontRepository.getFont("productsans-bold").drawString(entry.name(), entryX + 12.0f, entryY + 15.0f, 10.0f, hov ? -7617281 : -1);
                FontRepository.getFont("productsans-medium").drawString(entry.address(), entryX + 12.0f, entryY + 28.0f, 7.5f, ColorUtility.applyOpacity(-1, 0.55f));
                FontRepository.getFont("productsans-medium").drawString(entry.description(), entryX + 12.0f, entryY + 40.0f, 6.5f, ColorUtility.applyOpacity(-1, 0.28f));
                if (!hov) continue;
                FontRepository.getFont("productsans-bold").drawString("\u203a", entryX + entryW - 18.0f, entryY + 25.0f, 13.0f, -11890433);
            }
            if (this.scrollOffset > 0) {
                FontRepository.getFont("productsans-medium").drawString("\u25b2", (float)this.panelX + (float)this.panelW / 2.0f - 5.0f, this.panelY + 46, 8.0f, ColorUtility.applyOpacity(-1, 0.5f));
            }
            if (maxEntry < SERVERS.size()) {
                FontRepository.getFont("productsans-medium").drawString("\u25bc", (float)this.panelX + (float)this.panelW / 2.0f - 5.0f, this.panelY + this.visibleH - 2, 8.0f, ColorUtility.applyOpacity(-1, 0.5f));
            }
            LiquidGlassRenderer.drawGlassPanel(this.panelX, this.serverInput.method_46427() - 2, this.panelW - this.connectBtnW - 8 + 0, 30.0f, 8.0f);
            LiquidGlassRenderer.drawGlassPanel(this.connectBtnX, this.connectBtnY, this.connectBtnW, this.connectBtnH, 8.0f);
            if (hovConnect) {
                LiquidGlassRenderer.drawGlassHighlight(this.connectBtnX, this.connectBtnY, this.connectBtnW, this.connectBtnH, 8.0f, -11890433, 0.5f);
            }
            FontRepository.getFont("productsans-bold").drawString("Connect", (float)this.connectBtnX + (float)this.connectBtnW / 2.0f - FontRepository.getFont("productsans-bold").getStringWidth("Connect", 9.0f) / 2.0f, this.connectBtnY + 18, 9.0f, hovConnect ? -7617281 : -1);
            LiquidGlassRenderer.drawGlassPanel(this.randomBtnX, this.randomBtnY, this.randomBtnW, this.randomBtnH, 8.0f);
            if (hovRandom) {
                LiquidGlassRenderer.drawGlassHighlight(this.randomBtnX, this.randomBtnY, this.randomBtnW, this.randomBtnH, 8.0f, -11890433, 0.5f);
            }
            FontRepository.getFont("productsans-bold").drawString("\ud83c\udfb2 Random", (float)this.randomBtnX + (float)this.randomBtnW / 2.0f - FontRepository.getFont("productsans-bold").getStringWidth("\ud83c\udfb2 Random", 9.0f) / 2.0f, this.randomBtnY + 18, 9.0f, hovRandom ? -7617281 : -1);
            LiquidGlassRenderer.drawGlassPanel(this.backBtnX, this.backBtnY, this.backBtnW, this.backBtnH, 8.0f);
            if (hovBack) {
                LiquidGlassRenderer.drawGlassHighlight(this.backBtnX, this.backBtnY, this.backBtnW, this.backBtnH, 8.0f, -11890433, 0.5f);
            }
            FontRepository.getFont("productsans-bold").drawString("\u2190 Back", (float)this.backBtnX + (float)this.backBtnW / 2.0f - FontRepository.getFont("productsans-bold").getStringWidth("\u2190 Back", 9.0f) / 2.0f, this.backBtnY + 18, 9.0f, hovBack ? -7617281 : -1);
        }
        finally {
            if (nvgStarted) {
                NVGRenderer.endFrame(true);
            }
        }
        this.serverInput.method_25394(context, mouseX, mouseY, delta);
    }

    public boolean method_25402(class_11909 click, boolean doubled) {
        double mouseX = click.comp_4798();
        double mouseY = click.comp_4799();
        int button = click.method_74245();
        if (button == 0) {
            int listY = this.panelY + 48;
            int maxEntry = Math.min(this.scrollOffset + 6, SERVERS.size());
            for (int i = this.scrollOffset; i < maxEntry; ++i) {
                ServerEntry entry = SERVERS.get(i);
                int entryX = this.panelX + 8;
                int entryY = listY + (i - this.scrollOffset) * 56;
                int entryW = this.panelW - 16;
                if (!HoverUtility.isHovering(entryX, entryY, entryW, 50.0f, mouseX, mouseY)) continue;
                long now = class_156.method_658();
                if (now - this.lastClickTime < 400L) {
                    this.connectToServer(entry.address());
                } else {
                    this.serverInput.method_1852(entry.address());
                }
                this.lastClickTime = now;
                return true;
            }
            if (HoverUtility.isHovering(this.connectBtnX, this.connectBtnY, this.connectBtnW, this.connectBtnH, mouseX, mouseY)) {
                String ip = this.serverInput.method_1882().trim();
                if (!ip.isEmpty()) {
                    this.connectToServer(ip);
                }
                return true;
            }
            if (HoverUtility.isHovering(this.randomBtnX, this.randomBtnY, this.randomBtnW, this.randomBtnH, mouseX, mouseY)) {
                if (!SERVERS.isEmpty()) {
                    this.connectToServer(SERVERS.get(this.random.nextInt(SERVERS.size())).address());
                }
                return true;
            }
            if (HoverUtility.isHovering(this.backBtnX, this.backBtnY, this.backBtnW, this.backBtnH, mouseX, mouseY)) {
                Constants.mc.method_1507(this.parent);
                return true;
            }
        }
        return super.method_25402(click, doubled);
    }

    public boolean method_25401(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        int max = Math.max(0, SERVERS.size() - 6);
        if (verticalAmount < 0.0 && this.scrollOffset < max) {
            ++this.scrollOffset;
        } else if (verticalAmount > 0.0 && this.scrollOffset > 0) {
            --this.scrollOffset;
        }
        return true;
    }

    public boolean method_25404(class_11908 keyInput) {
        if (this.serverInput.method_25370()) {
            if (keyInput.comp_4795() == 257) {
                String ip = this.serverInput.method_1882().trim();
                if (!ip.isEmpty()) {
                    this.connectToServer(ip);
                }
                return true;
            }
            return this.serverInput.method_25404(keyInput);
        }
        return super.method_25404(keyInput);
    }

    public boolean method_25400(class_11905 charInput) {
        if (this.serverInput.method_25370()) {
            return this.serverInput.method_25400(charInput);
        }
        return super.method_25400(charInput);
    }

    private void connectToServer(String address) {
        class_639 serverAddress = class_639.method_2950((String)address);
        class_642 serverInfo = new class_642("TerentX Server", address, class_642.class_8678.field_45611);
        class_412.method_36877((class_437)this, (class_310)Constants.mc, (class_639)serverAddress, (class_642)serverInfo, (boolean)false, null);
    }

    public boolean method_25422() {
        return true;
    }

    static {
        SERVERS.add(new ServerEntry("Hypixel", "mc.hypixel.net", "World's #1 server"));
        SERVERS.add(new ServerEntry("Gamster", "play.gamster.org", "Romanian PvP server"));
        SERVERS.add(new ServerEntry("CubeCraft", "play.cubecraft.net", "Mini-games network"));
        SERVERS.add(new ServerEntry("InvadedLands", "pvp.invadedlands.net", "Competitive PvP"));
        SERVERS.add(new ServerEntry("TimeDeo", "timedeo.net", "Speed bridging practice"));
        SERVERS.add(new ServerEntry("Mineplex", "us.mineplex.com", "Classic mini-games"));
        SERVERS.add(new ServerEntry("GommeHD", "gommehd.net", "German mini-games"));
        SERVERS.add(new ServerEntry("JartexNetwork", "play.jartex.fun", "Mini-games network"));
        SERVERS.add(new ServerEntry("PvPLands", "pvplands.org", "1v1 and ranked PvP"));
        SERVERS.add(new ServerEntry("Velt", "play.veltpvp.com", "HCF & PvP network"));
        SERVERS.add(new ServerEntry("The Hive", "play.hivemc.com", "Popular mini-games"));
        SERVERS.add(new ServerEntry("Mineland", "play.mineland.net", "Ukrainian network"));
        SERVERS.add(new ServerEntry("Lunar Network", "lunar.gg", "Competitive PvP"));
        SERVERS.add(new ServerEntry("OpPrison", "mc.opprison.com", "Prison server"));
        SERVERS.add(new ServerEntry("Bloxd.io", "bloxd.io:25565", "Bloxd.io game"));
    }

    @Environment(value=EnvType.CLIENT)
    private record ServerEntry(String name, String address, String description) {
    }
}

