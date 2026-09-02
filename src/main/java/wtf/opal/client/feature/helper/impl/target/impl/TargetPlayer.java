/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_634
 *  net.minecraft.class_642
 */
package wtf.opal.client.feature.helper.impl.target.impl;

import java.util.Objects;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_634;
import net.minecraft.class_642;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.target.impl.TargetLivingEntity;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class TargetPlayer
extends TargetLivingEntity {
    private boolean strength;

    public TargetPlayer(class_1657 entity) {
        super((class_1309)entity);
    }

    @Override
    public boolean isMatchingFlags(int flags) {
        class_642 serverInfo;
        class_634 networkHandler;
        if (this.isLocal()) {
            return true;
        }
        if (Objects.equals(this.entity.method_5477().method_54160(), "BOT") && (networkHandler = Constants.mc.method_1562()) != null && (serverInfo = networkHandler.method_45734()) != null && serverInfo.field_3761.equals("localhost")) {
            return false;
        }
        if ((flags & 8) == 0 && PlayerUtility.areOnSameTeam((class_1309)Constants.mc.field_1724, this.entity)) {
            return false;
        }
        return (flags & 1) != 0;
    }

    public boolean hasStrength() {
        return this.strength;
    }

    public void setStrength(boolean strength) {
        this.strength = strength;
    }

    @Override
    public boolean isLocal() {
        return this.getEntityId() == Constants.mc.field_1724.method_5628();
    }
}

