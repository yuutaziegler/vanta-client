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
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.screen.TerentXClientMenuScreen;
import wtf.opal.client.screen.click.dropdown.DropdownClickGUI;
import wtf.opal.client.screen.vanta.VantaClickGUIScreen;
import wtf.opal.event.impl.game.PreGameTickEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class ClickGUIModule
extends Module {
    private final ModeProperty<GUIMode> mode = new ModeProperty<GUIMode>("Style", GUIMode.LIQUID);

    public ClickGUIModule() {
        super("Settings Menu", "Opens the client menu (Right Shift)", ModuleCategory.VISUAL);
        this.addProperties(this.mode);
    }

    @Override
    protected void onEnable() {
        if (Constants.mc.field_1755 == null) {
            this.openGUI();
        }
        this.setEnabled(false);
    }

    @Subscribe
    public void onPreGameTick(PreGameTickEvent event) {
        if (Constants.mc.field_1724 == null) {
            return;
        }
        if (this.isEnabled()) {
            this.openGUI();
            this.setEnabled(false);
        }
    }

    private void openGUI() {
        switch (this.mode.getValue()) {
            case LIQUID: {
                Constants.mc.method_1507((class_437)new VantaClickGUIScreen());
                break;
            }
            case DROPDOWN: {
                Constants.mc.method_1507((class_437)new DropdownClickGUI());
                break;
            }
            case MODERN: {
                Constants.mc.method_1507((class_437)new TerentXClientMenuScreen(null));
                break;
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum GUIMode {
        LIQUID("Liquid Glass (new)"),
        MODERN("Modern TerentX"),
        DROPDOWN("Dropdown");

        private final String name;

        private GUIMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

