/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 *  net.minecraft.class_2596
 *  net.minecraft.class_2797
 */
package wtf.opal.client.feature.module.impl.misc;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_2596;
import net.minecraft.class_2797;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.webhook.WebhookHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.impl.game.packet.ReceivePacketEvent;
import wtf.opal.event.impl.game.packet.SendPacketEvent;
import wtf.opal.event.subscriber.Subscribe;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class WebhookLoggerModule
extends Module {
    private final MultipleBooleanProperty events;
    private final BooleanProperty logKills = new BooleanProperty("Log Kills", true);
    private final BooleanProperty logDeaths = new BooleanProperty("Log Deaths", true);
    private final BooleanProperty logServerJoin = new BooleanProperty("Log Server Join", true);
    private final BooleanProperty logServerLeave = new BooleanProperty("Log Server Leave", true);
    private final BooleanProperty logChat = new BooleanProperty("Log Chat", false);
    private final BooleanProperty logDamage = new BooleanProperty("Log Damage", true);
    private final BooleanProperty logItemPickup = new BooleanProperty("Log Item Pickup", false);
    private final BooleanProperty logModuleToggle = new BooleanProperty("Log Module Toggle", true);
    private String lastServer = null;
    private float lastHealth = 20.0f;

    public WebhookLoggerModule() {
        super("WebhookLogger", "Logs server events to Discord webhook. Use '.webhook <url>' to set webhook URL", ModuleCategory.MISC);
        this.events = new MultipleBooleanProperty("Events", this.logKills, this.logDeaths, this.logServerJoin, this.logServerLeave, this.logChat, this.logDamage, this.logItemPickup, this.logModuleToggle);
        this.addProperties(this.events);
    }

    @Override
    protected void onEnable() {
        super.onEnable();
        if (!WebhookHelper.isEnabled()) {
            ChatUtility.error("Webhook URL not set! Use '.webhook <url>' to configure");
        } else {
            ChatUtility.success("Webhook Logger enabled");
        }
        if (Constants.mc.field_1724 != null) {
            this.lastHealth = Constants.mc.field_1724.method_6032();
        }
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        float currentHealth;
        if (Constants.mc.field_1724 == null || Constants.mc.field_1687 == null) {
            return;
        }
        if (this.logServerJoin.getValue().booleanValue() && Constants.mc.method_1558() != null) {
            String currentServer = Constants.mc.method_1558().field_3761;
            if (this.lastServer == null || !this.lastServer.equals(currentServer)) {
                WebhookHelper.logServerJoin(currentServer);
                this.lastServer = currentServer;
            }
        }
        if ((currentHealth = Constants.mc.field_1724.method_6032()) < this.lastHealth) {
            class_1309 attacker;
            float damage = this.lastHealth - currentHealth;
            if (currentHealth <= 0.0f && this.logDeaths.getValue().booleanValue()) {
                class_1309 attacker2 = Constants.mc.field_1724.method_6065();
                String killerName = attacker2 != null ? attacker2.method_5477().getString() : "Unknown";
                WebhookHelper.logDeath(killerName);
            } else if (this.logDamage.getValue().booleanValue() && damage > 0.0f && (attacker = Constants.mc.field_1724.method_6065()) != null) {
                WebhookHelper.logDamage(attacker.method_5477().getString(), damage);
            }
        }
        this.lastHealth = currentHealth;
    }

    @Subscribe
    public void onReceivePacket(ReceivePacketEvent event) {
        if (!WebhookHelper.isEnabled()) {
            return;
        }
        if (!this.logKills.getValue().booleanValue() || Constants.mc.field_1724 != null) {
            // empty if block
        }
    }

    @Subscribe
    public void onSendPacket(SendPacketEvent event) {
        class_2596<?> class_25962;
        if (!WebhookHelper.isEnabled()) {
            return;
        }
        if (this.logChat.getValue().booleanValue() && (class_25962 = event.getPacket()) instanceof class_2797) {
            class_2797 packet = (class_2797)class_25962;
            if (Constants.mc.field_1724 != null) {
                WebhookHelper.logChat(Constants.mc.field_1724.method_5477().getString(), packet.comp_945());
            }
        }
    }

    @Override
    protected void onDisable() {
        if (this.logServerLeave.getValue().booleanValue() && this.lastServer != null && WebhookHelper.isEnabled()) {
            WebhookHelper.logServerLeave(this.lastServer);
        }
        super.onDisable();
    }

    public static void onModuleToggle(String moduleName, boolean enabled) {
        if (WebhookHelper.isEnabled()) {
            WebhookHelper.logModuleToggle(moduleName, enabled);
        }
    }

    public static void setWebhookUrl(String url) {
        WebhookHelper.setWebhookUrl(url);
        if (WebhookHelper.isEnabled()) {
            ChatUtility.success("Webhook URL configured successfully!");
        } else {
            ChatUtility.error("Invalid webhook URL! Must be a Discord webhook URL");
        }
    }
}

