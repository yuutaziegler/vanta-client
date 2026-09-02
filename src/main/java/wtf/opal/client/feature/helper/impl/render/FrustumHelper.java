/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_4604
 *  org.jetbrains.annotations.Nullable
 */
package wtf.opal.client.feature.helper.impl.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_4604;
import org.jetbrains.annotations.Nullable;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.JoinWorldEvent;
import wtf.opal.event.subscriber.IEventSubscriber;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public class FrustumHelper
implements IEventSubscriber {
    @Nullable
    private static class_4604 frustum;
    private static FrustumHelper instance;

    public static void setFrustum(@Nullable class_4604 frustum) {
        FrustumHelper.frustum = frustum;
    }

    public static class_4604 get() {
        return frustum;
    }

    @Subscribe
    public void onDisconnectWorld(JoinWorldEvent event) {
        FrustumHelper.setFrustum(null);
    }

    static {
        instance = new FrustumHelper();
        EventDispatcher.subscribe(instance);
    }
}

