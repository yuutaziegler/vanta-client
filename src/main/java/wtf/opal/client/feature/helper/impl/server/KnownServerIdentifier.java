/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_639
 */
package wtf.opal.client.feature.helper.impl.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_639;
import wtf.opal.client.feature.helper.impl.server.KnownServer;

@Environment(value=EnvType.CLIENT)
public interface KnownServerIdentifier {
    public KnownServer identifyServer(class_639 var1);
}

