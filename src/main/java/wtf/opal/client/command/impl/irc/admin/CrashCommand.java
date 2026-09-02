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
package wtf.opal.client.command.impl.irc.admin;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.command.Command;

@Environment(value=EnvType.CLIENT)
public final class CrashCommand
extends Command {
    public CrashCommand() {
        super("crash", "Crashes the specified user.");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(CrashCommand.argument("user", StringArgumentType.word()).executes(context -> 1));
    }
}

