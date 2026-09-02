/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 */
package wtf.opal.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public final class ReleaseInfo {
    public static final ReleaseChannel CHANNEL = ReleaseChannel.DEVELOPMENT;
    public static final String VERSION = "0.1-beta.1";
    public static final String NAME = "TerentX";

    @Environment(value=EnvType.CLIENT)
    public static enum ReleaseChannel {
        PUBLIC("public"),
        BETA("beta"),
        DEVELOPMENT("development");

        private final String name;

        private ReleaseChannel(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

