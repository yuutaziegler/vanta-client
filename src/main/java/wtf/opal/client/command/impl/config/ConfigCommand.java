/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command.impl.config;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.command.Command;
import wtf.opal.client.command.arguments.ConfigArgumentType;
import wtf.opal.utility.data.SaveUtility;

@Environment(value=EnvType.CLIENT)
public final class ConfigCommand
extends Command {
    public ConfigCommand() {
        super("Failed to initialize repository:", "Interacts with configs.", "c");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(ConfigCommand.literal("save").then(ConfigCommand.argument("config_name", ConfigArgumentType.create()).executes(context -> {
            String configName = ((String)context.getArgument("config_name", String.class)).toLowerCase();
            SaveUtility.saveConfig(configName);
            return 1;
        })));
        builder.then(ConfigCommand.literal("list").executes(context -> 1));
        builder.then(ConfigCommand.literal("load").then(ConfigCommand.argument("config_name", ConfigArgumentType.create()).executes(context -> {
            String configName = ((String)context.getArgument("config_name", String.class)).toLowerCase();
            return 1;
        })));
        builder.then(ConfigCommand.literal("delete").then(ConfigCommand.argument("config_name", ConfigArgumentType.create()).executes(context -> {
            String configName = ((String)context.getArgument("config_name", String.class)).toLowerCase();
            return 1;
        })));
    }
}

