/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.utility.misc.math;

import java.util.Random;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class RandomUtility {
    public static final Random RANDOM = new Random();
    public static double JOIN_RANDOM = RANDOM.nextDouble();

    private RandomUtility() {
    }

    public static int getRandomInt(int min, int max) {
        return min == max ? min : min + RANDOM.nextInt(max - min);
    }

    public static boolean chance(int percentChance) {
        return RANDOM.nextInt(100) < percentChance;
    }

    public static int getRandomInt(int bound) {
        return RANDOM.nextInt(bound);
    }

    public static double getRandomDouble(double min, double max) {
        return RandomUtility.getRandomDouble(min, max, RANDOM.nextDouble());
    }

    public static float getRandomFloat(float min, float max) {
        return RandomUtility.getRandomFloat(min, max, RANDOM.nextFloat());
    }

    public static float getRandomFloat(float min, float max, float rand) {
        return min == max ? min : min + (max - min) * rand;
    }

    public static double getRandomDouble(double min, double max, double rand) {
        return min == max ? min : min + (max - min) * rand;
    }

    public static double getJoinRandomDouble(double min, double max) {
        return RandomUtility.getRandomDouble(min, max, JOIN_RANDOM);
    }

    public static void resetJoinRandom() {
        JOIN_RANDOM = RANDOM.nextDouble();
    }
}

