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
 */
package wtf.opal.client.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.UnknownModuleException;

@Environment(value=EnvType.CLIENT)
public final class ModuleArgumentType
implements ArgumentType<Module> {
    private static final ModuleArgumentType INSTANCE = new ModuleArgumentType();
    private static final DynamicCommandExceptionType NO_SUCH_MODULE = new DynamicCommandExceptionType(name -> class_2561.method_43470((String)("Module with name " + String.valueOf(name) + " doesn't exist.")));
    private static final Collection<String> EXAMPLES = OpalClient.getInstance().getModuleRepository().getModules().stream().limit(3L).map(Module::getId).toList();

    public static ModuleArgumentType create() {
        return INSTANCE;
    }

    public static Module get(CommandContext<?> context) {
        return (Module)context.getArgument("module", Module.class);
    }

    private ModuleArgumentType() {
    }

    public Module parse(StringReader reader) throws CommandSyntaxException {
        String argument = reader.readString();
        try {
            return OpalClient.getInstance().getModuleRepository().getModule(argument);
        }
        catch (UnknownModuleException e) {
            throw NO_SUCH_MODULE.create((Object)argument);
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return class_2172.method_9264(OpalClient.getInstance().getModuleRepository().getModules().stream().map(Module::getId), (SuggestionsBuilder)builder);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}

