/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.server;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.LocalDataWatch;
import wtf.opal.client.feature.helper.impl.server.KnownServer;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public class ProxyServer
extends KnownServer {
    public ProxyServer(String name) {
        super(name);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        if (Constants.mc.method_1562() == null) {
            return;
        }
        String serverBrand = Constants.mc.method_1562().method_52790();
        if (serverBrand != null && HypixelServer.SERVER_BRAND_PATTERN.matcher(serverBrand).matches()) {
            HypixelServer realServer = new HypixelServer();
            realServer.setProxyServer(this);
            LocalDataWatch.get().getKnownServerManager().setServer(realServer);
        }
    }
}

