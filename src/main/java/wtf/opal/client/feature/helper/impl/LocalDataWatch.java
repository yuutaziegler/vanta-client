/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.hypixel.data.type.GameType
 *  net.minecraft.class_1297
 *  net.minecraft.class_1309
 *  net.minecraft.class_243
 *  net.minecraft.class_2596
 *  net.minecraft.class_2708
 *  net.minecraft.class_2743
 *  net.minecraft.class_2824
 *  net.minecraft.class_2846
 *  net.minecraft.class_2848
 *  net.minecraft.class_2868
 *  net.minecraft.class_2885
 *  net.minecraft.class_2886
 *  net.minecraft.class_3545
 *  net.minecraft.class_408
 *  net.minecraft.class_437
 *  net.minecraft.class_7439
 */
package wtf.opal.client.feature.helper.impl;

import java.lang.runtime.SwitchBootstraps;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.hypixel.data.type.GameType;
import net.minecraft.class_1297;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2708;
import net.minecraft.class_2743;
import net.minecraft.class_2824;
import net.minecraft.class_2846;
import net.minecraft.class_2848;
import net.minecraft.class_2868;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import net.minecraft.class_3545;
import net.minecraft.class_408;
import net.minecraft.class_437;
import net.minecraft.class_7439;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.IHelper;
import wtf.opal.client.feature.helper.impl.server.KnownServerManager;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.helper.impl.target.TargetList;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.packet.SendPacketEvent;
import wtf.opal.event.impl.game.player.PlayerCreateEvent;
import wtf.opal.event.impl.game.player.interaction.AttackEvent;
import wtf.opal.event.impl.game.player.movement.PostMoveEvent;
import wtf.opal.event.impl.game.player.movement.step.StepSuccessEvent;
import wtf.opal.event.impl.game.server.ServerConnectEvent;
import wtf.opal.event.impl.game.server.ServerDisconnectEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.mixin.ClientCommonNetworkHandlerAccessor;
import wtf.opal.mixin.PlayerInteractEntityC2SPacketAccessor;
import wtf.opal.utility.misc.chat.ChatUtility;
import wtf.opal.utility.misc.math.RandomUtility;
import wtf.opal.utility.misc.time.Scheduler;
import wtf.opal.utility.misc.time.Stopwatch;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class LocalDataWatch
implements IHelper {
    private final KnownServerManager knownServerManager = new KnownServerManager();
    private TargetList targetList;
    private final List<String> friendList = new CopyOnWriteArrayList<String>();
    private final List<String> strengthedPlayerList = new ArrayList<String>();
    public int airTicks;
    public int groundTicks;
    public int ticksSinceStepped;
    public int ticksSinceTeleport;
    public final Stopwatch velocityStopwatch = new Stopwatch(0L);
    public final class_3545<Integer, class_1309> lastEntityAttack = new class_3545((Object)0, null);
    private class_243 prevVelocity;
    private static LocalDataWatch instance;

    private LocalDataWatch() {
    }

    @Subscribe
    public void onAttack(AttackEvent event) {
        class_1297 class_12972 = event.getTarget();
        if (class_12972 instanceof class_1309) {
            class_1309 livingEntity = (class_1309)class_12972;
            this.lastEntityAttack.method_34964((Object)0);
            this.lastEntityAttack.method_34965((Object)livingEntity);
        }
    }

    @Subscribe
    public void onServerConnect(ServerConnectEvent event) {
        RandomUtility.resetJoinRandom();
        this.knownServerManager.identifyServer(event.getServerAddress());
    }

    @Subscribe
    public void onServerDisconnect(ServerDisconnectEvent event) {
        HypixelServer.ModAPI.get().setCurrentLocation(null);
        this.knownServerManager.resetServer();
        this.targetList = null;
        if (Constants.mc.method_1562() != null) {
            ClientCommonNetworkHandlerAccessor accessor = (ClientCommonNetworkHandlerAccessor)Constants.mc.method_1562();
            accessor.getServerCookies().clear();
        }
    }

    @Subscribe
    public void onPlayerCreate(PlayerCreateEvent event) {
        this.targetList = new TargetList();
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        if (this.targetList != null) {
            this.targetList.tick();
        }
        ++this.ticksSinceStepped;
        ++this.ticksSinceTeleport;
        if (Constants.mc.field_1755 == null && Constants.mc.method_18506() == null && PlayerUtility.isKeyPressed(46)) {
            Constants.mc.method_1507((class_437)new class_408(".", false));
        }
        this.lastEntityAttack.method_34964((Object)((Integer)this.lastEntityAttack.method_15442() + 1));
        if ((Integer)this.lastEntityAttack.method_15442() > 10) {
            this.lastEntityAttack.method_34965(null);
        }
    }

    @Subscribe
    public void onStepSuccess(StepSuccessEvent event) {
        this.ticksSinceStepped = 0;
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        class_2596<?> class_25962 = event.getPacket();
        if (class_25962 instanceof class_7439) {
            class_7439 gameMessage = (class_7439)class_25962;
            String message = gameMessage.comp_763().getString();
            Matcher matcher = HypixelServer.KILL_MESSAGE_PATTERN.matcher(message);
            if (matcher.find()) {
                HypixelServer.ModAPI.Location currentLocation;
                String killer = matcher.group("killer");
                if (this.getKnownServerManager().getCurrentServer() instanceof HypixelServer && (currentLocation = HypixelServer.ModAPI.get().getCurrentLocation()) != null && currentLocation.serverType() == GameType.SKYWARS && currentLocation.mode() != null && !currentLocation.mode().startsWith("mini")) {
                    int strengthTicks = 20 * (currentLocation.mode().startsWith("solo") ? 5 : 2);
                    this.strengthedPlayerList.add(killer);
                    Scheduler.addTask(() -> this.strengthedPlayerList.remove(killer), strengthTicks);
                }
            }
        } else if (event.getPacket() instanceof class_2708) {
            this.ticksSinceTeleport = 0;
        } else {
            class_25962 = event.getPacket();
            if (class_25962 instanceof class_2743) {
                class_2743 packet = (class_2743)class_25962;
                if (Constants.mc.field_1724 != null && packet.method_11818() == Constants.mc.field_1724.method_5628()) {
                    this.velocityStopwatch.reset();
                }
            }
        }
    }

    @Subscribe
    public void onSendPacket(SendPacketEvent event) {
        boolean noSlowDebug = false;
        if (noSlowDebug) {
            class_2596<?> class_25962 = event.getPacket();
            Objects.requireNonNull(class_25962);
            class_2596<?> class_25963 = class_25962;
            int n = 0;
            switch (SwitchBootstraps.typeSwitch("typeSwitch", new Object[]{class_2824.class, class_2886.class, class_2885.class, class_2868.class, class_2848.class, class_2846.class}, class_25963, n)) {
                case 0: {
                    class_2824 interact = (class_2824)class_25963;
                    PlayerInteractEntityC2SPacketAccessor accessor = (PlayerInteractEntityC2SPacketAccessor)interact;
                    ChatUtility.error("ENT_" + String.valueOf(accessor.getType().method_34211()) + Constants.mc.field_1724.field_6012);
                    break;
                }
                case 1: {
                    class_2886 interact = (class_2886)class_25963;
                    ChatUtility.error("ITEM_INTERACT" + Constants.mc.field_1724.field_6012 + " " + String.valueOf(interact.method_12551()) + " " + interact.method_42081());
                    break;
                }
                case 2: {
                    class_2885 interact = (class_2885)class_25963;
                    ChatUtility.error("BLOCK_INTERACT" + Constants.mc.field_1724.field_6012 + " " + String.valueOf(interact.method_12546()) + " " + interact.method_42080());
                    break;
                }
                case 3: {
                    class_2868 slot = (class_2868)class_25963;
                    ChatUtility.error("SLOT" + Constants.mc.field_1724.field_6012 + " " + slot.method_12442());
                    break;
                }
                case 4: {
                    class_2848 command = (class_2848)class_25963;
                    ChatUtility.error("COMMAND" + Constants.mc.field_1724.field_6012 + " " + command.method_12365().name());
                    break;
                }
                case 5: {
                    class_2846 action = (class_2846)class_25963;
                    ChatUtility.error("ACTION" + Constants.mc.field_1724.field_6012 + " " + String.valueOf(action.method_12363()));
                    break;
                }
            }
        }
    }

    @Subscribe(priority=-10)
    public void onPostMoveLow(PostMoveEvent event) {
        this.prevVelocity = Constants.mc.field_1724.method_18798();
    }

    public class_243 getPrevVelocity() {
        return this.prevVelocity;
    }

    public static TargetList getTargetList() {
        return LocalDataWatch.instance.targetList;
    }

    public static List<String> getFriendList() {
        return LocalDataWatch.instance.friendList;
    }

    public List<String> getStrengthedPlayerList() {
        return this.strengthedPlayerList;
    }

    public KnownServerManager getKnownServerManager() {
        return this.knownServerManager;
    }

    public static LocalDataWatch get() {
        return instance;
    }

    public static void setInstance() {
        instance = new LocalDataWatch();
        EventDispatcher.subscribe(instance);
    }
}

