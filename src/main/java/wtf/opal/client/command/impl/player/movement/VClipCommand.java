/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.arguments.DoubleArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command.impl.player.movement;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import wtf.opal.client.Constants;
import wtf.opal.client.command.Command;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public final class VClipCommand
extends Command {
    public VClipCommand() {
        super("vclip", "Teleports you up or down.", "v");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(VClipCommand.argument("distance", DoubleArgumentType.doubleArg()).executes(context -> {
            double distance = DoubleArgumentType.getDouble((CommandContext)context, (String)"distance");
            Constants.mc.field_1724.method_5814(Constants.mc.field_1724.method_23317(), Constants.mc.field_1724.method_23318() + distance, Constants.mc.field_1724.method_23321());
            ChatUtility.print("Clipped \u00a7l" + distance + "\u00a77 block" + (distance == 1.0 ? "" : "s") + "!");
            return 1;
        }));
    }
}

