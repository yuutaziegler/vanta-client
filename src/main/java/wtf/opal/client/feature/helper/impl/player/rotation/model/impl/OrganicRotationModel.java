/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_241
 *  net.minecraft.class_3532
 */
package wtf.opal.client.feature.helper.impl.player.rotation.model.impl;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_241;
import net.minecraft.class_3532;
import wtf.opal.client.feature.helper.impl.player.rotation.model.EnumRotationModel;
import wtf.opal.client.feature.helper.impl.player.rotation.model.IRotationModel;
import wtf.opal.utility.misc.math.RandomUtility;
import wtf.opal.utility.player.RotationUtility;

@Environment(value=EnvType.CLIENT)
public final class OrganicRotationModel
implements IRotationModel {
    private final double speed;
    private final double driftIntensity;
    private final double jitterIntensity;
    private final double freqYaw1;
    private final double freqYaw2;
    private final double freqPitch1;
    private final double freqPitch2;
    private final double phaseYaw1;
    private final double phaseYaw2;
    private final double phasePitch1;
    private final double phasePitch2;
    private double timeAccumulator;
    private final Random random;

    public OrganicRotationModel(double speed, double driftIntensity, double jitterIntensity) {
        this.speed = speed;
        this.driftIntensity = driftIntensity;
        this.jitterIntensity = jitterIntensity;
        this.random = new Random(System.nanoTime());
        this.freqYaw1 = this.random.nextDouble() * 0.3 + 0.1;
        this.freqYaw2 = this.random.nextDouble() * 0.5 + 0.5;
        this.freqPitch1 = this.random.nextDouble() * 0.3 + 0.1;
        this.freqPitch2 = this.random.nextDouble() * 0.5 + 0.5;
        this.phaseYaw1 = this.random.nextDouble() * Math.PI * 2.0;
        this.phaseYaw2 = this.random.nextDouble() * Math.PI * 2.0;
        this.phasePitch1 = this.random.nextDouble() * Math.PI * 2.0;
        this.phasePitch2 = this.random.nextDouble() * Math.PI * 2.0;
        this.timeAccumulator = 0.0;
    }

    @Override
    public class_241 tick(class_241 from, class_241 to, float timeDelta) {
        float rawPitch;
        float deltaPitch;
        float rawYaw = class_3532.method_15393((float)(to.field_1343 - from.field_1343));
        float deltaYaw = rawYaw * timeDelta;
        double distance = Math.hypot(deltaYaw, deltaPitch = (rawPitch = to.field_1342 - from.field_1342) * timeDelta);
        if (distance < this.driftIntensity) {
            return new class_241(from.field_1343 + deltaYaw, from.field_1342 + deltaPitch);
        }
        if (distance > 0.0) {
            double ratioYaw = (double)Math.abs(deltaYaw) / distance;
            double ratioPitch = (double)Math.abs(deltaPitch) / distance;
            double maxYaw = this.speed * ratioYaw * (double)timeDelta;
            double maxPitch = this.speed * ratioPitch * (double)timeDelta;
            deltaYaw = class_3532.method_15363((float)deltaYaw, (float)((float)(-maxYaw)), (float)((float)maxYaw));
            deltaPitch = class_3532.method_15363((float)deltaPitch, (float)((float)(-maxPitch)), (float)((float)maxPitch));
        }
        this.timeAccumulator += (double)timeDelta;
        double sinYaw = Math.sin(this.timeAccumulator * this.freqYaw1 + this.phaseYaw1) + RandomUtility.getRandomDouble(0.45, 0.55) * Math.sin(this.timeAccumulator * this.freqYaw2 + this.phaseYaw2);
        double sinPitch = Math.sin(this.timeAccumulator * this.freqPitch1 + this.phasePitch1) + RandomUtility.getRandomDouble(0.45, 0.55) * Math.sin(this.timeAccumulator * this.freqPitch2 + this.phasePitch2);
        double driftYaw = sinYaw * this.driftIntensity * (double)timeDelta;
        double driftPitch = sinPitch * this.driftIntensity * (double)timeDelta;
        double jitterYaw = (this.random.nextDouble() * 2.0 - 1.0) * this.jitterIntensity * (double)timeDelta;
        double jitterPitch = (this.random.nextDouble() * 2.0 - 1.0) * this.jitterIntensity * (double)timeDelta;
        float moveYaw = deltaYaw + (float)driftYaw + (float)jitterYaw;
        float movePitch = deltaPitch + (float)driftPitch + (float)jitterPitch;
        class_241 rotation = new class_241(from.field_1343 + moveYaw, from.field_1342 + movePitch);
        return RotationUtility.patchConstantRotation(rotation, from);
    }

    @Override
    public EnumRotationModel getEnum() {
        return EnumRotationModel.ORGANIC;
    }
}

