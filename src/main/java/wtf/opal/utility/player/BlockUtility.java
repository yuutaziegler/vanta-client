/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_10034
 *  net.minecraft.class_10426
 *  net.minecraft.class_1306
 *  net.minecraft.class_1309
 *  net.minecraft.class_1657
 *  net.minecraft.class_1819
 *  net.minecraft.class_1839
 *  net.minecraft.class_3489
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_572$class_573
 *  net.minecraft.class_7833
 *  org.joml.Quaternionfc
 */
package wtf.opal.utility.player;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10034;
import net.minecraft.class_10426;
import net.minecraft.class_1306;
import net.minecraft.class_1309;
import net.minecraft.class_1657;
import net.minecraft.class_1819;
import net.minecraft.class_1839;
import net.minecraft.class_3489;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_572;
import net.minecraft.class_7833;
import org.joml.Quaternionfc;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.helper.impl.player.mouse.MouseHelper;
import wtf.opal.client.feature.helper.impl.player.slot.SlotHelper;
import wtf.opal.client.feature.module.impl.combat.BlockModule;
import wtf.opal.client.feature.module.impl.movement.noslow.NoSlowModule;
import wtf.opal.client.feature.module.impl.visual.AnimationsModule;
import wtf.opal.client.feature.module.repository.ModuleRepository;
import wtf.opal.duck.BipedEntityRenderStateAccess;
import wtf.opal.utility.player.InventoryUtility;
import wtf.opal.utility.player.PlayerUtility;

@Environment(value=EnvType.CLIENT)
public final class BlockUtility {
    private BlockUtility() {
    }

    public static void applyBlockTransformation(class_4587 matrices) {
        matrices.method_46416(-0.15f, 0.16f, 0.15f);
        matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(-18.0f));
        matrices.method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees(82.0f));
        matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(112.0f));
    }

    public static void applySwingTransformation(class_4587 matrices, float swingProgress, float convertedProgress) {
        float f = class_3532.method_15374((float)(swingProgress * swingProgress * (float)Math.PI));
        matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(45.0f + f * -20.0f));
        matrices.method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees(convertedProgress * -20.0f));
        matrices.method_22907((Quaternionfc)class_7833.field_40714.rotationDegrees(convertedProgress * -80.0f));
        matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(-45.0f));
    }

    public static boolean isBlockUseState(class_1657 player) {
        return player.method_6047().method_31573(class_3489.field_42611) && player.method_6047().method_7976().equals((Object)class_1839.field_8949) && player.method_6048() > 0;
    }

    public static boolean isForceBlockUseState(class_1657 player) {
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        return player.method_6047().method_31573(class_3489.field_42611) && player.method_6030().method_7909() instanceof class_1819 && player.method_6048() > 0 && animationsModule.isEnabled() && animationsModule.isSwordBlocking() && !BlockUtility.isBlockUseState(player);
    }

    public static boolean isThirdPersonBlockingState(class_10426 entityState) {
        class_1657 player;
        AnimationsModule animationsModule = OpalClient.getInstance().getModuleRepository().getModule(AnimationsModule.class);
        if (!animationsModule.isEnabled() || !animationsModule.isSwordBlocking()) {
            return false;
        }
        if (!(entityState instanceof class_10034)) {
            return false;
        }
        class_10034 state = (class_10034)entityState;
        class_1309 livingEntity = ((BipedEntityRenderStateAccess)state).opal$getEntity();
        if (state.field_53414 && entityState.field_55303 == class_1306.field_6182 && (entityState.field_55304 == class_572.class_573.field_3409 || entityState.field_55304 == class_572.class_573.field_3406) && livingEntity.method_61420(class_1306.field_6183).method_7909() instanceof class_1819 && livingEntity.method_61420(class_1306.field_6182).method_31573(class_3489.field_42611)) {
            return true;
        }
        if (state.field_53414 && entityState.field_55303 == class_1306.field_6183 && (entityState.field_55306 == class_572.class_573.field_3409 || entityState.field_55306 == class_572.class_573.field_3406) && livingEntity.method_61420(class_1306.field_6182).method_7909() instanceof class_1819 && livingEntity.method_61420(class_1306.field_6183).method_31573(class_3489.field_42611)) {
            return true;
        }
        if (livingEntity == Constants.mc.field_1724 && BlockUtility.isNoSlowBlockingState()) {
            return true;
        }
        return livingEntity instanceof class_1657 && (BlockUtility.isBlockUseState(player = (class_1657)livingEntity) || BlockUtility.isForceBlockUseState(player));
    }

    public static boolean isNoSlowBlockingState() {
        ModuleRepository moduleRepository = OpalClient.getInstance().getModuleRepository();
        AnimationsModule animationsModule = moduleRepository.getModule(AnimationsModule.class);
        NoSlowModule noSlowModule = moduleRepository.getModule(NoSlowModule.class);
        BlockModule blockModule = moduleRepository.getModule(BlockModule.class);
        SlotHelper slotHelper = SlotHelper.getInstance();
        return animationsModule.isEnabled() && animationsModule.isSwordBlocking() && noSlowModule.isEnabled() && noSlowModule.getAction() == NoSlowModule.Action.BLOCKABLE && (MouseHelper.getRightButton().isPressed() || blockModule.isEnabled() && blockModule.isBlocking()) && slotHelper.getMainHandStack(Constants.mc.field_1724).method_31573(class_3489.field_42611) && !InventoryUtility.isBlockInteractable(PlayerUtility.getBlockOver()) && (Constants.mc.field_1724.method_6079().method_7909() instanceof class_1819 || slotHelper.getMainHandStack(Constants.mc.field_1724).method_7976() == class_1839.field_8949);
    }
}

