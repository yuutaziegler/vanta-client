/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command.impl.misc;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.OpalClient;
import wtf.opal.client.command.Command;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class ScriptCommand
extends Command {
    public ScriptCommand() {
        super("script", "Allows you to do actions with your scripts.");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(ScriptCommand.literal("reload").executes(context -> {
            int scriptAmount = OpalClient.getInstance().getScriptRepository().loadScripts();
            ChatUtility.print(scriptAmount + " scripts successfully reloaded!");
            OpalClient.getInstance().getScriptRepository().getScriptList().forEach(script -> {
                if (script.getModule() != null) {
                    script.getModule().setEnabled(true);
                }
            });
            return 1;
        }));
    }
}

