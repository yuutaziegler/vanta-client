/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command.impl.player;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.Constants;
import wtf.opal.client.command.Command;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class UsernameCommand
extends Command {
    public UsernameCommand() {
        super("username", "Copies your username to your clipboard.", "ign");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.executes(context -> {
            Constants.mc.field_1774.method_1455(Constants.mc.field_1724.method_5477().getString());
            ChatUtility.print("Your username has been copied!");
            return 1;
        });
    }
}

