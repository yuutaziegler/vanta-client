/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_239$class_240
 */
package wtf.opal.client.feature.module.impl.combat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_239;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.swing.CPSProperty;
import wtf.opal.client.feature.helper.impl.player.swing.SwingDelay;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.combat.BlockModule;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.bool.MultipleBooleanProperty;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.impl.game.player.interaction.AttackDelayEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class AutoClickerModule
extends Module {
    private final MultipleBooleanProperty mouseButtons = new MultipleBooleanProperty("Mouse buttons", new BooleanProperty("Left", true), new BooleanProperty("Right", false));
    private final CPSProperty cpsProperty = new CPSProperty(this);
    private final BooleanProperty requirePressed = new BooleanProperty("Require pressed", true);

    public AutoClickerModule() {
        super("Auto Clicker", "Clicks for you automatically.", ModuleCategory.COMBAT);
        this.addProperties(this.mouseButtons, this.requirePressed);
    }

    @Subscribe
    public void onHandleInput(MouseHandleInputEvent event) {
        boolean allowSwingWhenUsing;
        BlockModule blockModule = OpalClient.getInstance().getModuleRepository().getModule(BlockModule.class);
        boolean bl = allowSwingWhenUsing = blockModule.isEnabled() && blockModule.isSwingAllowed();
        if (Constants.mc.field_1724.method_6115() && !allowSwingWhenUsing) {
            return;
        }
        if (SwingDelay.isSwingAvailable(this.cpsProperty)) {
            if (this.mouseButtons.getProperty("Left").getValue().booleanValue() && Constants.mc.field_1765 != null && Constants.mc.field_1765.method_17783() != class_239.class_240.field_1332 && (!this.requirePressed.getValue().booleanValue() || Constants.mc.field_1690.field_1886.method_1434())) {
                MouseHelper.getLeftButton().setPressed();
            }
            if (this.mouseButtons.getProperty("Right").getValue().booleanValue() && (!this.requirePressed.getValue().booleanValue() || Constants.mc.field_1690.field_1904.method_1434())) {
                MouseHelper.getRightButton().setPressed();
            }
        }
    }

    @Subscribe
    public void onAttackCooldown(AttackDelayEvent event) {
        event.setDelay(0);
    }
}

