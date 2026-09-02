/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 */
package wtf.opal.client.feature.helper.impl.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import wtf.opal.client.feature.helper.IHelper;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.ProxyServer;

@Environment(value=EnvType.CLIENT)
public abstract class KnownServer
implements IHelper {
    private ProxyServer proxyServer;
    private final String name;

    public KnownServer(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public ProxyServer getProxyServer() {
        return this.proxyServer;
    }

    public void setProxyServer(ProxyServer proxyServer) {
        this.proxyServer = proxyServer;
    }

    public boolean isValidTarget(class_1309 livingEntity) {
        return true;
    }

    @Override
    public boolean isHandlingEvents() {
        return this == LocalDataWatch.get().getKnownServerManager().getCurrentServer();
    }
}

