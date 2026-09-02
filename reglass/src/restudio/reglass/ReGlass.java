/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.ModInitializer
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package restudio.reglass;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReGlass
implements ModInitializer {
    public static final String MOD_ID = "reglass";
    public static final Logger LOGGER = LoggerFactory.getLogger((String)"reglass");

    public void onInitialize() {
        LOGGER.info("Init ReGlass");
    }
}

