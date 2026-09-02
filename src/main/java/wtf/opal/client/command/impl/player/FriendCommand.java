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
package wtf.opal.client.command.impl.player;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.command.Command;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class FriendCommand
extends Command {
    public FriendCommand() {
        super("friend", "Exempts users from kill aura", "f");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(FriendCommand.literal("add").then(FriendCommand.argument("player", StringArgumentType.word()).executes(context -> {
            String playerName = StringArgumentType.getString((CommandContext)context, (String)"player");
            if (LocalDataWatch.getFriendList().contains(playerName.toUpperCase())) {
                ChatUtility.error(playerName + " is already on your friends list!");
                return 1;
            }
            LocalDataWatch.getFriendList().add(playerName.toUpperCase());
            ChatUtility.print(playerName + " has been added to your friends list!");
            return 1;
        })));
        builder.then(FriendCommand.literal("remove").then(FriendCommand.argument("player", StringArgumentType.word()).executes(context -> {
            String playerName = StringArgumentType.getString((CommandContext)context, (String)"player");
            if (LocalDataWatch.getFriendList().contains(playerName.toUpperCase())) {
                LocalDataWatch.getFriendList().remove(playerName.toUpperCase());
                ChatUtility.print(playerName + " has been removed from your friends list!");
                return 1;
            }
            ChatUtility.error(playerName + " is not on your friends list!");
            return 1;
        })));
    }
}

