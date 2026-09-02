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
 *  net.minecraft.class_3532
 */
package wtf.opal.client.command.impl.player.movement;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import net.minecraft.class_3532;
import wtf.opal.client.Constants;
import wtf.opal.client.command.Command;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.utility.misc.chat.ChatUtility;
import wtf.opal.utility.player.MoveUtility;

@Environment(value=EnvType.CLIENT)
public final class HClipCommand
extends Command {
    public HClipCommand() {
        super("hclip", "Teleports you in the direction you are looking.", "h");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.then(HClipCommand.argument("distance", DoubleArgumentType.doubleArg()).executes(context -> {
            double yaw = MoveUtility.getDirectionRadians(RotationHelper.getClientHandler().getYawOr(Constants.mc.field_1724.method_36454()));
            double distance = DoubleArgumentType.getDouble((CommandContext)context, (String)"distance");
            Constants.mc.field_1724.method_5814(Constants.mc.field_1724.method_23317() - (double)class_3532.method_15374((float)((float)yaw)) * distance, Constants.mc.field_1724.method_23318(), Constants.mc.field_1724.method_23321() + (double)class_3532.method_15362((float)((float)yaw)) * distance);
            ChatUtility.print("Clipped \u00a7l" + distance + "\u00a77 block" + (distance == 1.0 ? "" : "s") + "!");
            return 1;
        }));
    }
}

