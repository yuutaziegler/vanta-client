/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_310
 */
package wtf.opal.client;

import java.io.File;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_310;
import wtf.opal.client.renderer.NVGRenderer;

@Environment(value=EnvType.CLIENT)
public final class Constants {
    public static final class_310 mc = class_310.method_1551();
    public static final long VG = NVGRenderer.getContext();
    public static final File DIRECTORY = new File(Constants.mc.field_1697, File.separator + "terentx" + File.separator);
    public static final double FIRST_FALL_MOTION = 0.0784000015258789;
}

