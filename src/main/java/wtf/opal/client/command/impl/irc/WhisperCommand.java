/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command.impl.irc;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.command.Command;

@Environment(value=EnvType.CLIENT)
public final class WhisperCommand
extends Command {
    public WhisperCommand() {
        super("whisper", "Allows you to direct message Opal users.", "w", "msg");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(WhisperCommand.argument("user", StringArgumentType.word()).executes(context -> 1));
        builder.then(WhisperCommand.argument("user", StringArgumentType.word()).then(WhisperCommand.argument("message", StringArgumentType.greedyString()).executes(context -> 1)));
    }
}

