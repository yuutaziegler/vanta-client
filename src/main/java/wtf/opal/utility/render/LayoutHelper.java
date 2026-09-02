/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_332
 */
package wtf.opal.utility.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_332;

@Environment(value=EnvType.CLIENT)
public final class LayoutHelper {
    public static float percentWidth(class_332 ctx, float percent) {
        return (float)ctx.method_51421() * percent;
    }

    public static float percentHeight(class_332 ctx, float percent) {
        return (float)ctx.method_51443() * percent;
    }

    public static float centerX(class_332 ctx, float elementWidth) {
        return (float)ctx.method_51421() / 2.0f - elementWidth / 2.0f;
    }

    public static float centerY(class_332 ctx, float elementHeight) {
        return (float)ctx.method_51443() / 2.0f - elementHeight / 2.0f;
    }
}

