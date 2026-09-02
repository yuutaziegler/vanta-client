/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_304
 */
package wtf.opal.client.feature.helper.impl.player.mouse;

import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_304;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseButton;

@Environment(value=EnvType.CLIENT)
public final class MouseHelper {
    private final Map<class_304, MouseButton> mouseButtonMap = new HashMap<class_304, MouseButton>();
    private final MouseButton leftButton;
    private final MouseButton rightButton;
    private static MouseHelper instance;

    private MouseHelper() {
        this.leftButton = this.register(new MouseButton(Constants.mc.field_1690.field_1886));
        this.rightButton = this.register(new MouseButton(Constants.mc.field_1690.field_1904));
    }

    private <T extends MouseButton> T register(T button) {
        this.mouseButtonMap.put(button.getKeyBinding(), button);
        return button;
    }

    public void tick() {
        this.leftButton.tick();
        this.rightButton.tick();
    }

    public static MouseButton getButtonFromBinding(class_304 binding) {
        return MouseHelper.instance.mouseButtonMap.get(binding);
    }

    public static MouseButton getLeftButton() {
        return MouseHelper.instance.leftButton;
    }

    public static MouseButton getRightButton() {
        return MouseHelper.instance.rightButton;
    }

    public static void setInstance() {
        instance = new MouseHelper();
    }

    public static MouseHelper getInstance() {
        return instance;
    }
}

