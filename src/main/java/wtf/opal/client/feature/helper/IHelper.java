/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.event.subscriber.IEventSubscriber;

@Environment(value=EnvType.CLIENT)
public interface IHelper
extends IEventSubscriber {
}

