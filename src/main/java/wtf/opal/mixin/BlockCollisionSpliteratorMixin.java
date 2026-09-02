/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.AbstractIterator
 *  com.llamalad7.mixinextras.injector.ModifyExpressionValue
 *  com.llamalad7.mixinextras.sugar.Local
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2338
 *  net.minecraft.class_2338$class_2339
 *  net.minecraft.class_238
 *  net.minecraft.class_265
 *  net.minecraft.class_2680
 *  net.minecraft.class_5329
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package wtf.opal.mixin;

import com.google.common.collect.AbstractIterator;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2338;
import net.minecraft.class_238;
import net.minecraft.class_265;
import net.minecraft.class_2680;
import net.minecraft.class_5329;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.opal.event.EventDispatcher;
import wtf.opal.event.impl.game.world.BlockShapeEvent;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_5329.class})
public abstract class BlockCollisionSpliteratorMixin<T>
extends AbstractIterator<T> {
    @Shadow
    @Final
    private class_2338.class_2339 field_25172;
    @Shadow
    @Final
    private class_238 field_25169;
    @Shadow
    @Final
    private BiFunction<class_2338.class_2339, class_265, T> field_44787;
    @Unique
    private List<class_265> extraVoxelShapes;

    private BlockCollisionSpliteratorMixin() {
    }

    @ModifyExpressionValue(method={"computeNext"}, at={@At(value="INVOKE", target="Lnet/minecraft/block/ShapeContext;getCollisionShape(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/CollisionView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;")})
    private class_265 hookComputeNextShape(class_265 original, @Local class_2680 blockState) {
        if (this.field_25172 != null) {
            BlockShapeEvent event = new BlockShapeEvent((class_2338)this.field_25172, blockState, original, this.extraVoxelShapes);
            EventDispatcher.dispatch(event);
            return event.getVoxelShape();
        }
        return original;
    }

    @Inject(method={"computeNext"}, at={@At(value="HEAD")}, cancellable=true)
    private void hookComputeNextHead(CallbackInfoReturnable<T> cir) {
        this.handleExtraVoxels(cir);
    }

    @Inject(method={"computeNext"}, at={@At(value="INVOKE", target="Lnet/minecraft/world/BlockCollisionSpliterator;endOfData()Ljava/lang/Object;")}, cancellable=true)
    private void hookComputeNextReturn(CallbackInfoReturnable<T> cir) {
        this.handleExtraVoxels(cir);
    }

    @Unique
    private void handleExtraVoxels(CallbackInfoReturnable<T> cir) {
        if (this.extraVoxelShapes == null) {
            this.extraVoxelShapes = new ArrayList<class_265>();
        }
        if (!this.extraVoxelShapes.isEmpty()) {
            class_265 voxelShape = this.extraVoxelShapes.removeFirst();
            cir.setReturnValue(this.field_44787.apply(this.field_25172, voxelShape));
        }
    }
}

