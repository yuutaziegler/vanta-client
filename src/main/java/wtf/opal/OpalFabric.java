/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ClientModInitializer
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.OpalClient;

@Environment(value=EnvType.CLIENT)
public final class OpalFabric
implements ClientModInitializer {
    public void onInitializeClient() {
        OpalClient.setInstance();
    }
}

