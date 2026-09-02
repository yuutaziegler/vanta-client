/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.CommandSyntaxException
 *  com.mojang.brigadier.exceptions.DynamicCommandExceptionType
 *  com.mojang.brigadier.suggestion.Suggestions
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 *  net.minecraft.class_2561
 *  org.lwjgl.glfw.GLFW
 */
package wtf.opal.client.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import org.lwjgl.glfw.GLFW;

@Environment(value=EnvType.CLIENT)
public final class BindArgumentType
implements ArgumentType<String> {
    private static final BindArgumentType INSTANCE = new BindArgumentType();
    private static final DynamicCommandExceptionType NO_SUCH_BIND = new DynamicCommandExceptionType(name -> class_2561.method_43470((String)("No bind with name " + String.valueOf(name) + " exists.")));
    private final List<String> binds = new ArrayList<String>();
    private static final Collection<String> EXAMPLES = List.of("RIGHT_SHIFT", "G", "MOUSE_0", "CLEAR");

    public static BindArgumentType create() {
        return INSTANCE;
    }

    public static String get(CommandContext<?> context) {
        return (String)context.getArgument("bind", String.class);
    }

    private BindArgumentType() {
        for (Field field : GLFW.class.getDeclaredFields()) {
            if (!field.getName().startsWith("GLFW_KEY_")) continue;
            this.binds.add(field.getName().substring("GLFW_KEY_".length()));
        }
        for (int i = 0; i < 10; ++i) {
            this.binds.add("MOUSE_" + i);
        }
        this.binds.add("CLEAR");
    }

    public String parse(StringReader reader) throws CommandSyntaxException {
        String argument = reader.readString();
        if (!this.binds.contains(argument.toUpperCase())) {
            throw NO_SUCH_BIND.create((Object)argument);
        }
        return argument;
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return class_2172.method_9265(this.binds, (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

