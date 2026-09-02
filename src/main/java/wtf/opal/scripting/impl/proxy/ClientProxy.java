/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.scripting.impl.proxy;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.UnknownModuleException;
import wtf.opal.utility.misc.chat.ChatUtility;

@Environment(value=EnvType.CLIENT)
public class ClientProxy {
    public void print(Object o) {
        ChatUtility.print(o);
    }

    public Module getModule(String ID) {
        try {
            return OpalClient.getInstance().getModuleRepository().getModule(ID);
        }
        catch (UnknownModuleException e) {
            throw new RuntimeException(e);
        }
    }
}

