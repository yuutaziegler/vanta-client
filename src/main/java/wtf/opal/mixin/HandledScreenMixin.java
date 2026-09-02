/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_1263
 *  net.minecraft.class_1304
 *  net.minecraft.class_1703
 *  net.minecraft.class_1707
 *  net.minecraft.class_1735
 *  net.minecraft.class_1792
 *  net.minecraft.class_1799
 *  net.minecraft.class_332
 *  net.minecraft.class_3489
 *  net.minecraft.class_437
 *  net.minecraft.class_465
 *  net.minecraft.class_476
 *  net.minecraft.class_6862
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import java.awt.Color;
import java.util.Map;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_1263;
import net.minecraft.class_1304;
import net.minecraft.class_1703;
import net.minecraft.class_1707;
import net.minecraft.class_1735;
import net.minecraft.class_1792;
import net.minecraft.class_1799;
import net.minecraft.class_332;
import net.minecraft.class_3489;
import net.minecraft.class_437;
import net.minecraft.class_465;
import net.minecraft.class_476;
import net.minecraft.class_6862;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.Constants;
import wtf.opal.client.OpalClient;
import wtf.opal.client.feature.module.impl.utility.inventory.ChestStealerModule;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_465.class})
public abstract class HandledScreenMixin<T extends class_1703> {
    @Shadow
    @Final
    protected T field_2797;

    @Inject(method={"drawSlot"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;drawStackOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V")})
    private void hookStackOverlay(class_332 context, class_1735 slot, CallbackInfo ci) {
        class_1799 bestChestAxe;
        class_1799 bestChestPickaxe;
        class_1799 bestChestSword;
        class_437 class_4372 = Constants.mc.field_1755;
        if (!(class_4372 instanceof class_476)) {
            return;
        }
        class_476 container = (class_476)class_4372;
        T t = this.field_2797;
        if (!(t instanceof class_1707)) {
            return;
        }
        class_1707 containerHandler = (class_1707)t;
        if (Constants.mc.field_1724.method_31548() == null || slot.field_7871 == Constants.mc.field_1724.method_31548()) {
            return;
        }
        if (!container.method_25440().getString().toLowerCase().contains("chest")) {
            return;
        }
        class_1799 stack = slot.method_7677();
        class_1263 chestInventory = containerHandler.method_7629();
        ChestStealerModule chestStealer = OpalClient.getInstance().getModuleRepository().getModule(ChestStealerModule.class);
        if (!(chestStealer.isEnabled() && chestStealer.getSmart().getValue().booleanValue() && chestStealer.getHighlight().getValue().booleanValue())) {
            return;
        }
        Map<class_1304, class_1799> bestChestArmor = chestStealer.getBestChestArmor(chestInventory);
        boolean take = chestStealer.shouldTake(stack, bestChestArmor, bestChestSword = chestStealer.getBestChestSword(chestInventory), bestChestPickaxe = chestStealer.getBestChestTool(chestInventory, (class_6862<class_1792>)class_3489.field_42614), bestChestAxe = chestStealer.getBestChestTool(chestInventory, (class_6862<class_1792>)class_3489.field_42612));
        if (take) {
            int color = new Color(200, 200, 200, 50).getRGB();
            context.method_25294(slot.field_7873, slot.field_7872, slot.field_7873 + 16, slot.field_7872 + 16, color);
        }
    }
}

