/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10182
 *  net.minecraft.class_241
 *  net.minecraft.class_2709
 */
package wtf.opal.client.feature.module.impl.utility;

import java.util.Set;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10182;
import net.minecraft.class_241;
import net.minecraft.class_2709;
import wtf.opal.client.Constants;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationHelper;
import wtf.opal.client.feature.helper.impl.player.rotation.RotationProperty;
import wtf.opal.client.feature.helper.impl.player.rotation.handler.RotationMouseHandler;
import wtf.opal.client.feature.helper.impl.player.rotation.model.impl.InstantRotationModel;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.Property;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.event.impl.game.player.teleport.PostTeleportEvent;
import wtf.opal.event.impl.game.player.teleport.PreTeleportEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class NoRotateModule
extends Module {
    private final RotationProperty rotationProperty = new RotationProperty(InstantRotationModel.INSTANCE, new Property[0]);
    private final BooleanProperty ignoreTeleports = new BooleanProperty("Ignore teleports", true);
    private class_241 rotation;

    public NoRotateModule() {
        super("No Rotate", "Prevents the server from setting your rotation.", ModuleCategory.UTILITY);
        this.addProperties(this.rotationProperty.get(), this.ignoreTeleports);
    }

    @Subscribe
    public void onPreTeleport(PreTeleportEvent event) {
        class_10182 change;
        if (this.ignoreTeleports.getValue().booleanValue() && (change = event.getChange()).comp_3148().method_1025(Constants.mc.field_1724.method_73189()) >= 100.0) {
            return;
        }
        Set<class_2709> relatives = event.getRelatives();
        if (!relatives.contains(class_2709.field_12397) || !relatives.contains(class_2709.field_12401)) {
            this.rotation = RotationHelper.getClientHandler().getRotation();
        }
    }

    @Subscribe
    public void onPostTeleport(PostTeleportEvent event) {
        if (this.rotation != null) {
            RotationHelper.getClientHandler().setRotation(this.rotation);
            RotationMouseHandler rotationHandler = RotationHelper.getHandler();
            rotationHandler.rotate(this.rotation, this.rotationProperty.createModel());
            class_10182 change = event.change();
            rotationHandler.setTickRotation(new class_241(change.comp_3150(), change.comp_3151()));
            rotationHandler.reverse();
            this.rotation = null;
        }
    }
}

