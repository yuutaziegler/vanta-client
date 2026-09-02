/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.client.feature.module.impl.visual.overlay.impl.modulelist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.impl.visual.overlay.IOverlayElement;
import wtf.opal.client.feature.module.impl.visual.overlay.OverlayModule;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.modulelist.ModuleElement;
import wtf.opal.client.feature.module.impl.visual.overlay.impl.modulelist.ToggledSettings;

@Environment(value=EnvType.CLIENT)
public final class ToggledModulesElement
implements IOverlayElement {
    private final ToggledSettings settings;
    private List<ModuleElement> moduleList;
    private List<ModuleElement> visibleList;
    private boolean sortingDirty;

    public ToggledModulesElement(OverlayModule module) {
        this.settings = new ToggledSettings(module);
    }

    public void initialize() {
        Collection<Module> moduleList = OpalClient.getInstance().getModuleRepository().getModules();
        this.moduleList = new ArrayList<ModuleElement>(moduleList.size());
        this.visibleList = new ArrayList<ModuleElement>(moduleList.size());
        moduleList.forEach(m -> this.moduleList.add(new ModuleElement(this.settings, (Module)m)));
        this.markSortingDirty();
    }

    public float getTotalHeight() {
        float height = 0.0f;
        for (ModuleElement element : this.visibleList) {
            height += 12.0f * element.getHeightAnimation().getValue();
        }
        return height * this.settings.getScale();
    }

    public ToggledSettings getSettings() {
        return this.settings;
    }

    public void markSortingDirty() {
        this.sortingDirty = true;
    }

    private void sort() {
        Collections.sort(this.moduleList);
        this.sortingDirty = false;
    }

    @Override
    public void render(class_332 context, float delta, boolean isBloom) {
        this.renderPass(isBloom);
    }

    @Override
    public void renderBlur(class_332 context, float delta) {
    }

    private void renderPass(boolean isBloom) {
        if (this.sortingDirty) {
            this.tick();
            this.sort();
        }
        int size = this.visibleList.size();
        for (int i = 0; i < size; ++i) {
            ModuleElement element = this.visibleList.get(i);
            element.render(i, isBloom);
        }
    }

    @Override
    public void tick() {
        this.visibleList.clear();
        int index = 0;
        for (ModuleElement element : this.moduleList) {
            boolean visible = element.isModuleVisible();
            element.tick(index, visible);
            if (!element.isVisible()) continue;
            this.visibleList.add(element);
            if (!visible) continue;
            ++index;
        }
    }

    @Override
    public boolean isActive() {
        return !Constants.mc.method_53526().method_53536() && this.settings.isEnabled();
    }

    @Override
    public boolean isBloom() {
        return true;
    }
}

