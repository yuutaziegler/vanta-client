/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command.impl.module;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.command.Command;
import wtf.opal.client.command.arguments.ModuleArgumentType;
import wtf.opal.client.feature.module.Module;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class ToggleCommand
extends Command {
    public ToggleCommand() {
        super("toggle", "Enables or disables specified module.", "t");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(ToggleCommand.argument("module", ModuleArgumentType.create()).executes(context -> {
            Module module = (Module)context.getArgument("module", Module.class);
            module.toggle();
            ChatUtility.print(module.getName() + " has been " + (module.isEnabled() ? "enabled" : "disabled") + "!");
            return 1;
        }));
    }
}

