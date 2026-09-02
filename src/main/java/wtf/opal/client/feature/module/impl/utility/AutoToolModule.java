/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1799
 *  net.minecraft.class_1922
 *  net.minecraft.class_1934
 *  net.minecraft.class_2244
 *  net.minecraft.class_2246
 *  net.minecraft.class_239
 *  net.minecraft.class_2680
 *  net.minecraft.class_3965
 *  net.minecraft.class_640
 */
package wtf.opal.client.feature.module.impl.utility;

import java.util.stream.IntStream;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1799;
import net.minecraft.class_1922;
import net.minecraft.class_1934;
import net.minecraft.class_2244;
import net.minecraft.class_2246;
import net.minecraft.class_239;
import net.minecraft.class_2680;
import net.minecraft.class_3965;
import net.minecraft.class_640;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.impl.movement.physics.PhysicsModule;
import wtf.opal.event.impl.game.input.MouseHandleInputEvent;
import wtf.opal.event.subscriber.Subscribe;

@Environment(value=EnvType.CLIENT)
public final class AutoToolModule
extends Module {
    public AutoToolModule() {
        super("Auto Tool", "Automatically switches to the best tool in your hotbar.", ModuleCategory.UTILITY);
    }

    @Subscribe
    public void onMouseHandleInput(MouseHandleInputEvent event) {
        class_3965 blockHitResult;
        block7: {
            block6: {
                class_239 class_2392 = Constants.mc.field_1765;
                if (!(class_2392 instanceof class_3965)) break block6;
                blockHitResult = (class_3965)class_2392;
                if (MouseHelper.getLeftButton().isPressed() && !MouseHelper.getRightButton().isPressed() && !Constants.mc.field_1724.method_6115()) break block7;
            }
            return;
        }
        class_640 playerListEntry = Constants.mc.method_1562().method_2871(Constants.mc.method_1548().method_44717());
        if (playerListEntry != null && (playerListEntry.method_2958() == class_1934.field_9220 || playerListEntry.method_2958() == class_1934.field_9219)) {
            return;
        }
        class_2680 blockState = Constants.mc.field_1687.method_8320(blockHitResult.method_17777());
        float hardness = blockState.method_26214((class_1922)Constants.mc.field_1687, blockHitResult.method_17777());
        if (hardness == 0.0f) {
            return;
        }
        int slot = IntStream.range(0, 9).filter(i -> {
            class_1799 itemStack = (class_1799)Constants.mc.field_1724.method_31548().method_67533().get(i);
            class_2680 modifiedBlockState = OpalClient.getInstance().getModuleRepository().getModule(PhysicsModule.class).isEnabled() && blockState.method_26204() instanceof class_2244 ? class_2246.field_10340.method_9564() : blockState;
            return itemStack.method_7924(modifiedBlockState) > 1.0f;
        }).findFirst().orElse(-1);
        if (slot == -1) {
            return;
        }
        SlotHelper.setCurrentItem(slot);
    }
}

