/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command.repository;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.Constants;
import wtf.opal.client.command.Command;

@Environment(value=EnvType.CLIENT)
public final class CommandRepository {
    public static final CommandDispatcher<class_2172> DISPATCHER = new CommandDispatcher();
    public static final List<Command> COMMANDS = new ArrayList<Command>();

    private CommandRepository(Builder builder) {
        for (Command command : builder.commands) {
            CommandRepository.add(command);
        }
        COMMANDS.sort(Comparator.comparing(Command::getName));
    }

    public static void add(Command command) {
        COMMANDS.removeIf(existing -> existing.getName().equals(command.getName()));
        command.registerTo(DISPATCHER);
        COMMANDS.add(command);
    }

    public static void dispatch(String message) throws CommandSyntaxException {
        DISPATCHER.execute(message, (Object)Constants.mc.method_1562().method_2875());
    }

    public List<Command> getCommands() {
        return COMMANDS;
    }

    public static Builder builder() {
        return new Builder();
    }

    @Environment(value=EnvType.CLIENT)
    public static final class Builder {
        public final List<Command> commands = new ArrayList<Command>();

        public Builder putAll(Command ... commands) {
            Collections.addAll(this.commands, commands);
            return this;
        }

        public CommandRepository build() {
            return new CommandRepository(this);
        }
    }
}

