/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.hypixel.data.type.GameType
 *  net.hypixel.data.type.ServerType
 *  net.hypixel.modapi.HypixelModAPI
 *  net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket
 *  net.minecraft.class_1309
 *  net.minecraft.class_1531
 *  net.minecraft.class_1646
 *  net.minecraft.class_243
 *  net.minecraft.class_2561
 *  net.minecraft.class_640
 *  net.minecraft.class_745
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.client.feature.helper.impl.server.impl;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hypixel.data.type.GameType;
import net.hypixel.data.type.ServerType;
import net.hypixel.modapi.HypixelModAPI;
import net.hypixel.modapi.packet.impl.clientbound.event.ClientboundLocationPacket;
import net.minecraft.class_1309;
import net.minecraft.class_1531;
import net.minecraft.class_1646;
import net.minecraft.class_243;
import net.minecraft.class_2561;
import net.minecraft.class_640;
import net.minecraft.class_745;
import org.jetbrains.annotations.Nullable;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.KnownServer;
import wtf.opal.event.impl.game.JoinWorldEvent;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class HypixelServer
extends KnownServer {
    private final Set<class_243> armorStands = ConcurrentHashMap.newKeySet();
    private final Set<UUID> bots = ConcurrentHashMap.newKeySet();
    public static final Pattern SERVER_BRAND_PATTERN = Pattern.compile("Hypixel BungeeCord \\(.+\\) <- .+");
    public static final Pattern KILL_MESSAGE_PATTERN = Pattern.compile("(?<username>\\w{1,16}) ?.+(by|of|to|for|with|the|from|was|fighting|against|meet) (?<killer>\\w{1,16})", 2);
    public static final List<Pattern> KARMA_PATTERNS = List.of(Pattern.compile("^ +1st Killer - ?\\[?\\w*\\+*\\]? \\w+ - \\d+(?: Kills?)?$"), Pattern.compile("^ *1st (?:Place ?)?(?:-|:)? ?\\[?\\w*\\+*\\]? \\w+(?: : \\d+| - \\d+(?: Points?)?| - \\d+(?: x .)?| \\(\\w+ .{1,6}\\) - \\d+ Kills?|: \\d+:\\d+| - \\d+ (?:Zombie )?(?:Kills?|Blocks? Destroyed)| - \\[LINK\\])?$"), Pattern.compile("^ +Winn(?:er #1 \\(\\d+ Kills\\): \\w+ \\(\\w+\\)|er(?::| - )(?:Hiders|Seekers|Defenders|Attackers|PLAYERS?|MURDERERS?|Red|Blue|RED|BLU|\\w+)(?: Team)?|ers?: ?\\[?\\w*\\+*\\]? \\w+(?:, ?\\[?\\w*\\+*\\]? \\w+)?|ing Team ?[\\:-] (?:Animals|Hunters|Red|Green|Blue|Yellow|RED|BLU|Survivors|Vampires))$"), Pattern.compile("^ +Alpha Infected: \\w+ \\(\\d+ infections?\\)$"), Pattern.compile("^ +Murderer: \\w+ \\(\\d+ Kills?\\)$"), Pattern.compile("^ +You survived \\d+ rounds!$"), Pattern.compile("^ +(?:UHC|SkyWars|Bridge|Sumo|Classic|OP|MegaWalls|Bow|NoDebuff|Blitz|Combo|Bow Spleef) (?:Duel|Doubles|3v3|4v4|Teams|Deathmatch|2v2v2v2|3v3v3v3)? ?- \\d+:\\d+$"), Pattern.compile("^ +They captured all wools!$"), Pattern.compile("^ +Game over!$"), Pattern.compile("^ +[\\d\\.]+k?/[\\d\\.]+k? \\w+$"), Pattern.compile("^ +(?:Criminal|Cop)s won the game!$"), Pattern.compile("^ +\\[?\\w*\\+*\\]? \\w+ - \\d+ Final Kills$"), Pattern.compile("^ +Zombies - \\d*:?\\d+:\\d+ \\(Round \\d+\\)$"), Pattern.compile("^ +. YOUR STATISTICS .$"), Pattern.compile("^ {36}Winner(s?)$"), Pattern.compile("^ {21}Bridge CTF [a-zA-Z]+ - \\d\\d:\\d\\d$"));

    public HypixelServer() {
        super("Hypixel");
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean isValidTarget(class_1309 livingEntity) {
        UUID uuid;
        String unstyledName;
        ModAPI.Location location = ModAPI.get().getCurrentLocation();
        if (location != null) {
            if (location.serverType() == GameType.REPLAY) return true;
            if (location.serverType() == GameType.SMP) {
                return true;
            }
        }
        if ((unstyledName = livingEntity.method_5477().getString()) == null) return true;
        if (unstyledName.isEmpty()) {
            return true;
        }
        if (livingEntity instanceof class_1531) {
            this.armorStands.add(livingEntity.method_73189());
            return false;
        }
        if (livingEntity.method_5628() == -1234) {
            return false;
        }
        if (livingEntity instanceof class_745) {
            class_745 player = (class_745)livingEntity;
            class_640 playerListEntry = Constants.mc.method_1562().method_2871(player.method_5667());
            if (playerListEntry == null) return false;
            if (playerListEntry.method_2966() == null) {
                return false;
            }
            if (playerListEntry.method_2959() > 1 && player.method_6032() > 14.0f && player.method_6032() < 20.0f && player.method_5767()) {
                return false;
            }
            boolean inLobby = location != null && location.isLobby();
            uuid = player.method_5667();
            if (uuid.version() == 2) {
                if (inLobby) return false;
                if (player.method_6032() == 20.0f && playerListEntry.method_2955() == null) {
                    return false;
                }
            }
            if (!this.bots.contains(uuid)) return true;
            if (!inLobby && !(player.method_6032() > 20.0f)) {
                if (player.method_5767()) return false;
                if (!player.method_24828()) {
                    if (player.field_6012 <= 170) return false;
                }
            }
        } else {
            if (livingEntity.method_5667().version() != 4) {
                List siblings;
                if (livingEntity.method_5767()) {
                    return false;
                }
                if (unstyledName.contains(" ") && !(siblings = livingEntity.method_5477().method_10855()).isEmpty() && ((class_2561)siblings.getFirst()).method_10866().method_10973() != null) {
                    return false;
                }
                class_243 pos = livingEntity.method_73189();
                if (!this.armorStands.stream().anyMatch(armorStand -> armorStand.method_1022(pos) < 2.0)) return true;
                return false;
            }
            if (livingEntity instanceof class_1646) return false;
            return true;
        }
        this.bots.remove(uuid);
        return true;
    }

    @Subscribe
    public void onJoinWorld(JoinWorldEvent event) {
        this.armorStands.clear();
        this.bots.clear();
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        String serverBrand;
        if (Constants.mc.method_1562() != null && (serverBrand = Constants.mc.method_1562().method_52790()) != null && !SERVER_BRAND_PATTERN.matcher(serverBrand).matches()) {
            LocalDataWatch.get().getKnownServerManager().resetServer();
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static class ModAPI {
        private static final ModAPI INSTANCE = new ModAPI();
        @Nullable
        private Location currentLocation;
        @Nullable
        private Location previousLocation;

        public ModAPI() {
            HypixelModAPI.getInstance().subscribeToEventPacket(ClientboundLocationPacket.class);
            HypixelModAPI.getInstance().createHandler(ClientboundLocationPacket.class, this::onLocationReceive);
        }

        public static ModAPI get() {
            return INSTANCE;
        }

        @Nullable
        public Location getCurrentLocation() {
            return this.currentLocation;
        }

        public void setCurrentLocation(@Nullable Location currentLocation) {
            this.currentLocation = currentLocation;
        }

        @Nullable
        public Location getPreviousLocation() {
            return this.previousLocation;
        }

        private void onLocationReceive(ClientboundLocationPacket packet) {
            this.previousLocation = this.currentLocation;
            this.currentLocation = new Location(packet.getServerName(), packet.getServerType().orElse(null), packet.getLobbyName().orElse(null), packet.getMode().orElse(null), packet.getMap().orElse(null));
        }

        @Environment(value=EnvType.CLIENT)
        public record Location(String serverName, @Nullable ServerType serverType, @Nullable String lobbyName, @Nullable String mode, @Nullable String map) {
            public boolean isLobby() {
                return this.lobbyName != null;
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum BedColor {
        RED(28, 0xFF5555),
        GREEN(19, 0x55FF55),
        BLUE(25, 0x5555FF),
        YELLOW(18, 0xFFFF55),
        AQUA(23, 0x55FFFF),
        WHITE(8, 0xFFFFFF),
        PINK(20, 0xFF55FF),
        GRAY(21, 0x555555);

        public final int mapColorId;
        public final int teamColor;

        private BedColor(int mapColorId, int teamColor) {
            this.mapColorId = mapColorId;
            this.teamColor = teamColor;
        }

        @Nullable
        public static BedColor fromTeamColor(int teamColor) {
            for (BedColor color : BedColor.values()) {
                if (color.teamColor != teamColor) continue;
                return color;
            }
            return null;
        }
    }
}

