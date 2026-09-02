/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ClassToInstanceMap
 *  com.google.common.collect.ImmutableClassToInstanceMap
 *  com.google.common.collect.ImmutableClassToInstanceMap$Builder
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.module.repository;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import com.google.common.collect.ImmutableMap;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.UnknownModuleException;

@Environment(value=EnvType.CLIENT)
public final class ModuleRepository {
    private final ClassToInstanceMap<Module> classToInstanceMap;
    private final ImmutableMap<String, Module> idToInstanceMap;

    private ModuleRepository(ClassToInstanceMap<Module> classToInstanceMap, ImmutableMap<String, Module> idToInstanceMap) {
        this.classToInstanceMap = classToInstanceMap;
        this.idToInstanceMap = idToInstanceMap;
    }

    public void findModule(String id, Consumer<Module> moduleConsumer, Consumer<UnknownModuleException> exceptionHandler) {
        try {
            moduleConsumer.accept(this.getModule(id));
        }
        catch (UnknownModuleException e) {
            exceptionHandler.accept(e);
        }
    }

    public <T extends Module> T getModule(Class<T> moduleClass) {
        return (T)((Module)this.classToInstanceMap.getInstance(moduleClass));
    }

    public Module getModule(String id) throws UnknownModuleException {
        Module module = (Module)this.idToInstanceMap.get((Object)id);
        if (module == null) {
            throw new UnknownModuleException(id);
        }
        return module;
    }

    public Collection<Module> getModules() {
        return this.classToInstanceMap.values();
    }

    public Collection<Module> getModulesInCategory(ModuleCategory category) {
        return this.classToInstanceMap.values().stream().filter(module -> category.equals(module.getCategory())).collect(Collectors.toList());
    }

    public static ModuleRepository fromModules(Module ... modules) {
        Builder builder = ModuleRepository.builder();
        for (Module module : modules) {
            builder.register(module);
        }
        return builder.build();
    }

    public static Builder builder() {
        return new Builder();
    }

    @Environment(value=EnvType.CLIENT)
    public static class Builder {
        private final ImmutableClassToInstanceMap.Builder<Module> classToInstanceMapBuilder = ImmutableClassToInstanceMap.builder();
        private final ImmutableMap.Builder<String, Module> idToInstanceMapBuilder = ImmutableMap.builder();

        private Builder() {
        }

        public Builder register(Module module) {
            this.classToInstanceMapBuilder.put(module.getClass(), (Object)module);
            this.idToInstanceMapBuilder.put((Object)module.getId(), (Object)module);
            return this;
        }

        public ModuleRepository build() {
            return new ModuleRepository((ClassToInstanceMap<Module>)this.classToInstanceMapBuilder.build(), (ImmutableMap<String, Module>)this.idToInstanceMapBuilder.build());
        }
    }
}

