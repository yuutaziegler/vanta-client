/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2558
 *  net.minecraft.class_2558$class_10609
 *  net.minecraft.class_2558$class_2559
 *  net.minecraft.class_2561
 */
package wtf.opal.client.feature.module.impl.utility;

import java.util.concurrent.TimeUnit;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2558;
import net.minecraft.class_2561;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.StringProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.client.notification.NotificationType;
import wtf.opal.event.impl.game.chat.ChatReceivedEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.Multithreading;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class AutoHypixelModule
extends Module {
    private final BooleanProperty autoGGEnabled = new BooleanProperty("Enabled", true);
    private final StringProperty autoGGMessage = (StringProperty)new StringProperty("Message", "gg").hideIf(() -> this.autoGGEnabled.getValue() == false);
    private final BooleanProperty autoPlayEnabled = new BooleanProperty("Enabled", true);
    private final NumberProperty autoPlayDelay = (NumberProperty)new NumberProperty("Delay", "s", 2.5, 0.0, 8.0, 0.5).hideIf(() -> this.autoPlayEnabled.getValue() == false);
    private final BooleanProperty autoLeaveOnPlayerBan = new BooleanProperty("Auto leave on ban", false);
    private long lastAutoGGMessage;

    public AutoHypixelModule() {
        super("Auto Hypixel", "Useful features for Hypixel.", ModuleCategory.UTILITY);
        this.addProperties(new GroupProperty("Auto GG", this.autoGGEnabled, this.autoGGMessage), new GroupProperty("Auto Play", this.autoPlayEnabled, this.autoPlayDelay), this.autoLeaveOnPlayerBan);
    }

    @Subscribe
    public void onChatReceived(ChatReceivedEvent event) {
        if (!(LocalDataWatch.get().getKnownServerManager().getCurrentServer() instanceof HypixelServer)) {
            return;
        }
        HypixelServer.ModAPI.Location location = HypixelServer.ModAPI.get().getCurrentLocation();
        if (location == null) {
            return;
        }
        String message = event.getText().getString();
        if (this.autoLeaveOnPlayerBan.getValue().booleanValue() && message.equals("A player has been removed from your game.")) {
            ChatUtility.sendCommand("l");
            OpalClient.getInstance().getNotificationManager().builder(NotificationType.INFO).title(this.getName()).description("A player in your game got banned.").duration(2000).buildAndPublish();
            return;
        }
        if (this.autoGGEnabled.getValue().booleanValue() && System.currentTimeMillis() - this.lastAutoGGMessage > 5000L && HypixelServer.KARMA_PATTERNS.stream().anyMatch(pattern -> pattern.matcher(message).matches())) {
            ChatUtility.sendCommand("ac " + (String)this.autoGGMessage.getValue());
            this.lastAutoGGMessage = System.currentTimeMillis();
        }
        if (this.autoPlayEnabled.getValue().booleanValue()) {
            if (message.equals("Queued! Use the bed to cancel!")) {
                this.scheduleAutoPlay();
            } else if (!location.isLobby()) {
                for (class_2561 sibling : event.getText().method_10855()) {
                    class_2558 clickEvent = sibling.method_10866().method_10970();
                    if (clickEvent == null || clickEvent.method_10845() != class_2558.class_2559.field_11750 || !((class_2558.class_10609)clickEvent).comp_3506().startsWith("/play ")) continue;
                    this.scheduleAutoPlay();
                    break;
                }
            }
        }
    }

    private void scheduleAutoPlay() {
        HypixelServer.ModAPI.Location location = HypixelServer.ModAPI.get().getCurrentLocation();
        if (location == null) {
            return;
        }
        double delay = (Double)this.autoPlayDelay.getValue();
        int delayMs = (int)(delay * 1000.0);
        Multithreading.schedule(() -> ChatUtility.sendCommand("play " + location.mode()), delayMs, TimeUnit.MILLISECONDS);
        OpalClient.getInstance().getNotificationManager().builder(NotificationType.SUCCESS).title(this.getName()).description("Auto Play" + (String)(delay > 0.0 ? " in " + delay + "s" : "") + "!").duration(delayMs + 200).buildAndPublish();
    }
}

