/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.HashMultimap
 *  com.google.common.collect.Multimap
 *  com.ibm.icu.impl.Pair
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.binding;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.ibm.icu.impl.Pair;
import java.util.Map;
import java.util.Optional;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.binding.IBindable;
import wtf.opal.client.binding.type.InputType;

@Environment(value=EnvType.CLIENT)
public final class BindingService {
    private final Multimap<Pair<Integer, InputType>, IBindable> bindingMap = HashMultimap.create();

    public void register(int code, IBindable bindable, InputType inputType) {
        this.bindingMap.put((Object)Pair.of((Object)code, (Object)((Object)inputType)), (Object)bindable);
    }

    public void clearBindings(IBindable bindable) {
        this.bindingMap.entries().removeIf(entry -> entry.getValue() == bindable);
    }

    public void dispatch(int code, InputType inputType) {
        if (Constants.mc.field_1755 != null) {
            return;
        }
        this.bindingMap.get((Object)Pair.of((Object)code, (Object)((Object)inputType))).forEach(IBindable::onBindingInteraction);
    }

    public Multimap<Pair<Integer, InputType>, IBindable> getBindingMap() {
        return this.bindingMap;
    }

    public Optional<Pair<Integer, InputType>> getKeyFromBindable(IBindable bindable) {
        return this.bindingMap.entries().stream().filter(entry -> ((IBindable)entry.getValue()).equals(bindable)).map(Map.Entry::getKey).findFirst();
    }
}

