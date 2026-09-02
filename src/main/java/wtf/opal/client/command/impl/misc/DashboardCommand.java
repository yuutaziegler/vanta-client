/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_156
 *  net.minecraft.class_2172
 */
package wtf.opal.client.command.impl.misc;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_156;
import net.minecraft.class_2172;
import wtf.opal.client.command.Command;

@Environment(value=EnvType.CLIENT)
public final class DashboardCommand
extends Command {
    public DashboardCommand() {
        super("dashboard", "Opens the Opal dashboard.", "dash");
    }

    @Override
    protected void onCommand(LiteralArgumentBuilder<class_2172> builder) {
        builder.executes(context -> {
            class_156.method_668().method_670("https://opalclient.com/dash");
            return 1;
        });
    }
}

