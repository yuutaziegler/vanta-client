/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.arguments.StringArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command.impl.irc.admin;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.command.Command;

@Environment(value=EnvType.CLIENT)
public final class TitleCommand
extends Command {
    public TitleCommand() {
        super("title", "Displays title on specified users screen.");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(TitleCommand.argument("user", StringArgumentType.word()).then(TitleCommand.argument("message", StringArgumentType.string()).then(TitleCommand.argument("fadeInTicks", IntegerArgumentType.integer()).then(TitleCommand.argument("stayTicks", IntegerArgumentType.integer()).then(TitleCommand.argument("fadeOutTicks", IntegerArgumentType.integer()).executes(context -> 1))))));
    }
}

