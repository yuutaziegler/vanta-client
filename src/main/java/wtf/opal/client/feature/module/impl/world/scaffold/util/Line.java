/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_243
 */
package wtf.opal.client.feature.module.impl.world.scaffold.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_243;

@Environment(value=EnvType.CLIENT)
public class Line {
    private final class_243 position;
    private final class_243 direction;

    public Line(class_243 position, class_243 direction) {
        this.position = position;
        this.direction = direction.method_1029();
    }

    public class_243 getPosition() {
        return this.position;
    }

    public class_243 getDirection() {
        return this.direction;
    }

    public class_243 getNearestPointTo(class_243 point) {
        class_243 toPoint = point.method_1020(this.position);
        double projection = toPoint.method_1026(this.direction);
        return this.position.method_1019(this.direction.method_1021(projection));
    }

    public double distanceToPoint(class_243 point) {
        return this.getNearestPointTo(point).method_1022(point);
    }
}

