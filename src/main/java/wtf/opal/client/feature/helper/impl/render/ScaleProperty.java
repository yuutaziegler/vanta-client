/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client.feature.helper.impl.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;

@Environment(value=EnvType.CLIENT)
public final class ScaleProperty {
    private static final ScaleMode[] MINECRAFT_VALUES = new ScaleMode[]{ScaleMode.AUTO, ScaleMode.SMALL, ScaleMode.NORMAL, null, ScaleMode.LARGE};
    private final ModeProperty<ScaleMode> modeProperty;

    private ScaleProperty(ScaleMode[] values) {
        this.modeProperty = new ModeProperty("Scale", (Enum)ScaleMode.AUTO, (Enum[])values);
    }

    public static ScaleProperty newMinecraftElement() {
        return new ScaleProperty(MINECRAFT_VALUES);
    }

    public static ScaleProperty newNVGElement() {
        return new ScaleProperty(ScaleMode.values());
    }

    public ModeProperty<ScaleMode> get() {
        return this.modeProperty;
    }

    public float getScale() {
        int guiScale = (Integer)Constants.mc.field_1690.method_42474().method_41753();
        return switch (((ScaleMode)((Object)this.modeProperty.getValue())).ordinal()) {
            case 1 -> {
                switch (guiScale) {
                    case 2: {
                        yield 0.5f;
                    }
                    case 3: {
                        yield 0.33333334f;
                    }
                }
                yield 1.0f;
            }
            case 2 -> {
                switch (guiScale) {
                    case 1: {
                        yield 2.0f;
                    }
                    case 3: {
                        yield 0.6666667f;
                    }
                }
                yield 1.0f;
            }
            case 3 -> {
                switch (guiScale) {
                    case 1: {
                        yield 2.25f;
                    }
                    case 2: {
                        yield 1.125f;
                    }
                    case 3: {
                        yield 0.75f;
                    }
                }
                yield 1.0f;
            }
            case 4 -> {
                switch (guiScale) {
                    case 1: {
                        yield 3.0f;
                    }
                    case 2: {
                        yield 1.5f;
                    }
                }
                yield 1.0f;
            }
            default -> 1.0f;
        };
    }

    @Environment(value=EnvType.CLIENT)
    public static enum ScaleMode {
        AUTO("Auto"),
        SMALL("Small (1x)"),
        NORMAL("Normal (2x)"),
        MEDIUM("Medium (2.67x)"),
        LARGE("Large (3x)");

        private final String name;

        private ScaleMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

