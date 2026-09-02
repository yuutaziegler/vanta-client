/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command.impl.misc;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.command.Command;
import wtf.opal.client.feature.module.impl.misc.WebhookLoggerModule;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class WebhookCommand
extends Command {
    public WebhookCommand() {
        super("webhook", "Configure Discord webhook URL for logging", "wh");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        ((LiteralArgumentBuilder)builder.then(WebhookCommand.argument("url", StringArgumentType.greedyString()).executes(context -> {
            String url = StringArgumentType.getString((CommandContext)context, (String)"url");
            if (url.equals("clear") || url.equals("remove") || url.equals("disable")) {
                WebhookLoggerModule.setWebhookUrl("");
                ChatUtility.success("Webhook cleared!");
            } else if (url.startsWith("https://discord.com/api/webhooks/") || url.startsWith("https://discordapp.com/api/webhooks/")) {
                WebhookLoggerModule.setWebhookUrl(url);
                ChatUtility.success("Webhook URL configured! Enable WebhookLogger module to start logging");
            } else {
                ChatUtility.error("Invalid Discord webhook URL!");
                ChatUtility.print("Format: .webhook <discord_webhook_url>");
                ChatUtility.print("To clear: .webhook clear");
            }
            return 1;
        }))).executes(context -> {
            ChatUtility.print("Usage: .webhook <discord_webhook_url>");
            ChatUtility.print("To clear: .webhook clear");
            ChatUtility.print("Get webhook from Discord: Server Settings \u2192 Integrations \u2192 Webhooks");
            return 1;
        });
    }
}

