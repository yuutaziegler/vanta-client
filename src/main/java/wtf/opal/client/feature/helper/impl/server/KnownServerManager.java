/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_639
 */
package wtf.opal.client.feature.helper.impl.server;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_639;
import wtf.opal.client.feature.helper.impl.server.KnownServer;
import wtf.opal.client.feature.helper.impl.server.KnownServerIdentifier;
import wtf.opal.client.feature.helper.impl.server.impl.CubecraftServer;
import wtf.opal.client.feature.helper.impl.server.impl.HypixelServer;
import wtf.opal.client.feature.helper.impl.server.impl.proxy.LiquidProxyServer;
import wtf.opal.client.feature.helper.impl.server.impl.proxy.NyaProxyServer;
import wtf.opal.event.EventDispatcher;

@Environment(value=EnvType.CLIENT)
public final class KnownServerManager {
    private KnownServer currentServer;
    private static final KnownServerIdentifier[] SERVER_IDENTIFIERS = new KnownServerIdentifier[]{address -> {
        if (address.method_2954() == 25565 && (KnownServerManager.isAddressOfDomain(address, "hypixel.net", true) || KnownServerManager.isAddressOfDomain(address, "hypixel.io", true) || KnownServerManager.isAddressOfDomain(address, "technoblade.club", true))) {
            return new HypixelServer();
        }
        return null;
    }, address -> {
        if (address.method_2954() == 25565 && (KnownServerManager.isAddressOfDomain(address, "cubecraft.net", false) || KnownServerManager.isAddressOfDomain(address, "play.cubecraft.net", false))) {
            return new CubecraftServer();
        }
        return null;
    }, address -> {
        if (address.method_2954() == 25565 && KnownServerManager.isAddressOfDomain(address, "liquidproxy.net", true)) {
            return new LiquidProxyServer();
        }
        return null;
    }, address -> {
        if (address.method_2954() == 25565 && KnownServerManager.isAddressOfDomain(address, "nyap.buzz", true)) {
            return new NyaProxyServer();
        }
        return null;
    }};

    public void identifyServer(class_639 address) {
        for (KnownServerIdentifier identifier : SERVER_IDENTIFIERS) {
            KnownServer knownServer = identifier.identifyServer(address);
            if (knownServer == null) continue;
            this.currentServer = knownServer;
            EventDispatcher.subscribe(knownServer);
            return;
        }
        this.currentServer = null;
    }

    public KnownServer getCurrentServer() {
        return this.currentServer;
    }

    public void resetServer() {
        this.currentServer = null;
    }

    public void setServer(KnownServer currentServer) {
        if (this.currentServer != currentServer) {
            this.currentServer = currentServer;
            EventDispatcher.subscribe(currentServer);
        }
    }

    private static boolean isAddressOfDomain(class_639 address, String domain, boolean allowSubdomains) {
        String addressStr = address.method_2952().toLowerCase();
        String regex = Pattern.quote(domain) + "(\\.*)$";
        if (allowSubdomains) {
            regex = "^(?:[a-zA-Z0-9-]+\\.)*" + regex;
        }
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(addressStr);
        return matcher.matches();
    }
}

