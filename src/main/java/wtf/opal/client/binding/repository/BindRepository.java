/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.lwjgl.glfw.GLFW
 */
package wtf.opal.client.binding.repository;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.lwjgl.glfw.GLFW;
import wtf.opal.client.binding.BindingService;
import wtf.opal.client.binding.type.InputType;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.press.KeyPressEvent;
import wtf.opal.event.impl.press.MousePressEvent;
import wtf.opal.event.subscriber.IEventSubscriber;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class BindRepository
implements IEventSubscriber {
    private final BindingService bindingService = new BindingService();
    private final Map<String, Integer> namedBindingMap = new HashMap<String, Integer>();
    public static final String GLFW_KEY_PREFIX = "GLFW_KEY_";

    public BindRepository() {
        try {
            for (Field field : GLFW.class.getDeclaredFields()) {
                if (!field.getName().startsWith(GLFW_KEY_PREFIX)) continue;
                this.namedBindingMap.put(field.getName().substring(GLFW_KEY_PREFIX.length()), field.getInt(null));
            }
            for (int i = 0; i < 10; ++i) {
                this.namedBindingMap.put("MOUSE_" + i, i);
            }
            this.namedBindingMap.put("CLEAR", -1);
        }
        catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        EventDispatcher.subscribe(this);
    }

    public Map<String, Integer> getNamedBindingMap() {
        return this.namedBindingMap;
    }

    public String getNameFromInteger(int bind) {
        return this.namedBindingMap.entrySet().stream().filter(entry -> (Integer)entry.getValue() == bind).map(Map.Entry::getKey).findFirst().orElse(null);
    }

    public BindingService getBindingService() {
        return this.bindingService;
    }

    @Subscribe
    public void onKeyPress(KeyPressEvent event) {
        this.bindingService.dispatch(event.getInteractionCode(), InputType.KEYBOARD);
    }

    @Subscribe
    public void onMousePress(MousePressEvent event) {
        this.bindingService.dispatch(event.getInteractionCode(), InputType.MOUSE);
    }
}

