/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.server.impl.proxy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.server.ProxyServer;

@Environment(value=EnvType.CLIENT)
public final class NyaProxyServer
extends ProxyServer {
    public NyaProxyServer() {
        super("NyaProxy");
    }
}

