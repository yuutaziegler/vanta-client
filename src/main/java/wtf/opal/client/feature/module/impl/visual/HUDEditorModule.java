/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_437
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_437;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.screen.hud.HUDEditorScreen;

@Environment(value=EnvType.CLIENT)
public final class HUDEditorModule
extends Module {
    private final HUDEditorScreen hudEditorScreen = new HUDEditorScreen();

    public HUDEditorModule() {
        super("HUD Editor", "Edit the overlay elements on screen.", ModuleCategory.VISUAL);
    }

    @Override
    protected void onEnable() {
        Constants.mc.method_1507((class_437)this.hudEditorScreen);
        this.setEnabled(false);
    }
}

