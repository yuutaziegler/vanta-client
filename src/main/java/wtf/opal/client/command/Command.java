/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;

@Environment(value=EnvType.CLIENT)
public abstract class Command {
    private final String name;
    private final String description;
    private final List<String> aliases;

    protected Command(String name, String description, String ... aliases) {
        this.name = name;
        this.description = description;
        this.aliases = List.of(aliases);
    }

    protected Command(String name, String description) {
        this(name, description, new String[0]);
    }

    public final String getName() {
        return this.name;
    }

    public final String getDescription() {
        return this.description;
    }

    public final List<String> getAliases() {
        return this.aliases;
    }

    protected static <T> RequiredArgumentBuilder<class_2172, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument((String)name, type);
    }

    protected static LiteralArgumentBuilder<class_2172> literal(String name) {
        return LiteralArgumentBuilder.literal((String)name);
    }

    public final void registerTo(CommandDispatcher<class_2172> dispatcher) {
        this.register(dispatcher, this.name);
        for (String alias : this.aliases) {
            this.register(dispatcher, alias);
        }
    }

    public final void register(CommandDispatcher<class_2172> dispatcher, String name) {
        LiteralArgumentBuilder builder = LiteralArgumentBuilder.literal((String)name);
        this.onCommand((LiteralArgumentBuilder<class_2172>)builder);
        dispatcher.register(builder);
    }

    protected abstract void onCommand(LiteralArgumentBuilder<class_2172> var1);
}

