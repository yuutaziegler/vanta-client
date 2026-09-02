/*
 * UI Editor Module - Edit UI elements position and size
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_437;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.screen.hud.UIEditorScreen;

@Environment(value=EnvType.CLIENT)
public final class UIEditorModule extends Module {
    
    public UIEditorModule() {
        super("UI Editor", "Edit the position and size of all UI elements including menus", ModuleCategory.VISUAL);
    }

    @Override
    protected void onEnable() {
        Constants.mc.method_1507((class_437)new UIEditorScreen());
        this.setEnabled(false);
    }
}
