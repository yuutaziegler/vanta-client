/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.property;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.property.Property;

@Environment(value=EnvType.CLIENT)
public interface IPropertyListProvider {
    public List<Property<?>> getPropertyList();
}

