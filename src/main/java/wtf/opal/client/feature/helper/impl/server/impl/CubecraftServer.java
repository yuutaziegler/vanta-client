/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_268
 *  net.minecraft.class_640
 */
package wtf.opal.client.feature.helper.impl.server.impl;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_268;
import net.minecraft.class_640;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.server.KnownServer;

@Environment(value=EnvType.CLIENT)
public final class CubecraftServer
extends KnownServer {
    public CubecraftServer() {
        super("Cubecraft");
    }

    @Override
    public boolean isValidTarget(class_1309 livingEntity) {
        if (livingEntity instanceof class_1657) {
            class_1657 player = (class_1657)livingEntity;
            class_640 playerListEntry = Constants.mc.method_1562().method_2871(player.method_5667());
            if (playerListEntry == null || playerListEntry.method_2966() == null) {
                return false;
            }
            class_268 scoreboardTeam = playerListEntry.method_2955();
            return scoreboardTeam != null && !scoreboardTeam.method_1197().equals(player.method_5477().getString());
        }
        return true;
    }
}

