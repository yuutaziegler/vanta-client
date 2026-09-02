/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_124
 *  net.minecraft.class_2583
 *  net.minecraft.class_5224
 */
package wtf.opal.utility.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_124;
import net.minecraft.class_2583;
import net.minecraft.class_5224;

@Environment(value=EnvType.CLIENT)
public final class OrderedTextVisitor
implements class_5224 {
    private final StringBuilder builder = new StringBuilder();
    private class_124 lastFormatting = null;

    public boolean accept(int index, class_2583 style, int codePoint) {
        if (style.method_10984()) {
            this.builder.append(class_124.field_1067);
            this.lastFormatting = class_124.field_1067;
        }
        if (style.method_10973() != null) {
            for (class_124 formatting : class_124.values()) {
                if (!formatting.method_543() || formatting.method_532().intValue() != style.method_10973().method_27716() || formatting == this.lastFormatting) continue;
                this.builder.append(formatting);
                this.lastFormatting = formatting;
                break;
            }
        }
        this.builder.append(new String(Character.toChars(codePoint)));
        return true;
    }

    public String getFormattedString() {
        return this.builder.toString();
    }
}

