/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_3532
 *  net.minecraft.class_4587
 *  net.minecraft.class_7833
 *  org.joml.Quaternionfc
 */
package wtf.opal.client.feature.module.impl.visual;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import org.joml.Quaternionfc;
import wtf.opal.client.feature.module.Module;
import wtf.opal.client.feature.module.ModuleCategory;
import wtf.opal.client.feature.module.property.impl.GroupProperty;
import wtf.opal.client.feature.module.property.impl.bool.BooleanProperty;
import wtf.opal.client.feature.module.property.impl.mode.ModeProperty;
import wtf.opal.client.feature.module.property.impl.number.NumberProperty;
import wtf.opal.utility.player.BlockUtility;

@Environment(value=EnvType.CLIENT)
public final class AnimationsModule
extends Module {
    private final BooleanProperty swordBlocking = new BooleanProperty("Enabled", true);
    private final ModeProperty<BlockMode> blockAnimationMode = (ModeProperty)new ModeProperty<BlockMode>("Block animation", BlockMode.V1_7).hideIf(() -> this.swordBlocking.getValue() == false);
    private final BooleanProperty alwaysHideShield = new BooleanProperty("Always hidden", true);
    private final BooleanProperty hideShieldSlotInHotbar = new BooleanProperty("Hide offhand slot", true);
    private final BooleanProperty oldBackwardsWalking = new BooleanProperty("Old backwards walking", true);
    private final BooleanProperty oldArmorDamageTint = new BooleanProperty("Old armor damage tint", true);
    private final BooleanProperty oldSneaking = new BooleanProperty("Old sneaking", false);
    private final BooleanProperty fixPoseRepeat = new BooleanProperty("Fix pose repeat", true);
    private final NumberProperty mainHandScale = new NumberProperty("Scale", 0.0, -2.0, 2.0, 0.1f);
    private final NumberProperty mainHandX = new NumberProperty("Offset X", 0.0, -2.0, 2.0, 0.1f);
    private final NumberProperty mainHandY = new NumberProperty("Offset Y", 0.0, -2.0, 2.0, 0.1f);
    private final NumberProperty swingSlowdown = new NumberProperty("Swing slowdown", 0.0, 0.0, 5.0, 0.25);
    private final BooleanProperty oldCooldownAnimation = new BooleanProperty("Old cooldown animation", true);
    private final BooleanProperty swingWhileUsing = new BooleanProperty("Visual swing on use", true);
    private final BooleanProperty hideDropSwing = new BooleanProperty("Hide drop swing", false);
    private final BooleanProperty equipOffset = new BooleanProperty("Equip offset", false);

    public AnimationsModule() {
        super("Animations", "Modifies animations within the game.", ModuleCategory.VISUAL);
        this.setEnabled(true);
        this.addProperties(new GroupProperty("Sword blocking", this.swordBlocking, this.blockAnimationMode), new GroupProperty("Shields", this.alwaysHideShield, this.hideShieldSlotInHotbar), new GroupProperty("Player", this.oldBackwardsWalking, this.oldArmorDamageTint, this.oldSneaking, this.fixPoseRepeat), new GroupProperty("Item", this.mainHandScale, this.mainHandX, this.mainHandY, this.swingSlowdown, this.oldCooldownAnimation, this.swingWhileUsing, this.hideDropSwing, this.equipOffset));
    }

    public boolean isHideDropSwing() {
        return this.hideDropSwing.getValue();
    }

    public boolean isOldSneaking() {
        return this.oldSneaking.getValue();
    }

    public boolean isFixPoseRepeat() {
        return this.fixPoseRepeat.getValue();
    }

    public float getSwingSlowdown() {
        return ((Double)this.swingSlowdown.getValue()).floatValue() + 1.0f;
    }

    public boolean isSwordBlocking() {
        return this.swordBlocking.getValue();
    }

    public boolean isEquipOffset() {
        return this.equipOffset.getValue();
    }

    public boolean isOldCooldownAnimation() {
        return this.oldCooldownAnimation.getValue();
    }

    public boolean isOldBackwardsWalking() {
        return this.oldBackwardsWalking.getValue();
    }

    public boolean isOldArmorDamageTint() {
        return this.oldArmorDamageTint.getValue();
    }

    public boolean isHideShield() {
        return this.alwaysHideShield.getValue();
    }

    public boolean isHideShieldSlotInHotbar() {
        return this.hideShieldSlotInHotbar.getValue();
    }

    public float getMainHandScale() {
        return ((Double)this.mainHandScale.getValue()).floatValue();
    }

    public float getMainHandX() {
        return ((Double)this.mainHandX.getValue()).floatValue();
    }

    public float getMainHandY() {
        return ((Double)this.mainHandY.getValue()).floatValue();
    }

    public boolean isSwingWhileUsing() {
        return this.swingWhileUsing.getValue();
    }

    public void applyTransformations(class_4587 matrices, float swingProgress) {
        float convertedProgress = class_3532.method_15374((float)(class_3532.method_15355((float)swingProgress) * (float)Math.PI));
        float f = class_3532.method_15374((float)(swingProgress * swingProgress * (float)Math.PI));
        switch (((BlockMode)((Object)this.blockAnimationMode.getValue())).ordinal()) {
            case 0: {
                BlockUtility.applySwingTransformation(matrices, swingProgress, convertedProgress);
                BlockUtility.applyBlockTransformation(matrices);
                break;
            }
            case 1: {
                BlockUtility.applyBlockTransformation(matrices);
                break;
            }
            case 2: {
                BlockUtility.applyBlockTransformation(matrices);
                matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(f * -30.0f));
                matrices.method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees(convertedProgress * -30.0f));
                break;
            }
            case 3: {
                BlockUtility.applySwingTransformation(matrices, swingProgress, convertedProgress);
                matrices.method_46416(-0.15f, 0.16f, 0.15f);
                matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(-24.0f));
                matrices.method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees(75.0f));
                matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(90.0f));
                break;
            }
            case 4: {
                BlockUtility.applyBlockTransformation(matrices);
                matrices.method_22907((Quaternionfc)class_7833.field_40714.rotationDegrees(0.0f));
                matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(convertedProgress * 42.0f));
                matrices.method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees(-convertedProgress * 22.0f));
                break;
            }
            case 5: {
                BlockUtility.applyBlockTransformation(matrices);
                matrices.method_22907((Quaternionfc)class_7833.field_40714.rotationDegrees(5.0f - convertedProgress * 32.0f));
                matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(0.0f));
                matrices.method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees(0.0f));
                break;
            }
            case 6: {
                matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(45.0f + f * -5.0f));
                matrices.method_22907((Quaternionfc)class_7833.field_40718.rotationDegrees(convertedProgress * -20.0f));
                matrices.method_22907((Quaternionfc)class_7833.field_40714.rotationDegrees(convertedProgress * -40.0f));
                matrices.method_22907((Quaternionfc)class_7833.field_40716.rotationDegrees(-45.0f));
                BlockUtility.applyBlockTransformation(matrices);
            }
        }
    }

    @Environment(value=EnvType.CLIENT)
    public static enum BlockMode {
        V1_7("1.7"),
        V1_8("1.8"),
        RUB("Rub"),
        STELLA("Stella"),
        BOUNCE("Bounce"),
        DIAGONAL("Diagonal"),
        SWANK("Swank");

        private final String name;

        private BlockMode(String name) {
            this.name = name;
        }

        public String toString() {
            return this.name;
        }
    }
}

