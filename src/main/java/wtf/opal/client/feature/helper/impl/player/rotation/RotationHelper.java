/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.player.rotation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.helper.impl.player.rotation.handler.ClientRotationHandler;
import wtf.opal.client.feature.helper.impl.player.rotation.handler.RotationMouseHandler;

@Environment(value=EnvType.CLIENT)
public final class RotationHelper {
    private static final ClientRotationHandler clientHandler = new ClientRotationHandler();
    private static final RotationMouseHandler mouseHandler = new RotationMouseHandler();

    private RotationHelper() {
    }

    public static RotationMouseHandler getHandler() {
        return mouseHandler;
    }

    public static ClientRotationHandler getClientHandler() {
        return clientHandler;
    }
}

