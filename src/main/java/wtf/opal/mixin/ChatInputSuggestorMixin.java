/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.llamalad7.mixinextras.sugar.Local
 *  com.mojang.brigadier.ParseResults
 *  com.mojang.brigadier.StringReader
 *  com.mojang.brigadier.suggestion.Suggestions
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_2172
 *  net.minecraft.class_342
 *  net.minecraft.class_4717
 *  org.jetbrains.annotations.Nullable
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package wtf.opal.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.concurrent.CompletableFuture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_2172;
import net.minecraft.class_342;
import net.minecraft.class_4717;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.opal.client.Constants;
import wtf.opal.client.command.repository.CommandRepository;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_4717.class})
public abstract class ChatInputSuggestorMixin {
    @Shadow
    @Nullable
    private ParseResults<class_2172> field_21610;
    @Shadow
    private boolean field_21614;
    @Shadow
    @Final
    private class_342 field_21599;
    @Shadow
    @Nullable
    private CompletableFuture<Suggestions> field_21611;
    @Shadow
    @Nullable
    private // Could not load outer class - annotation placement on inner may be incorrect
    @Nullable class_4717. @Nullable class_464 field_21612;

    @Shadow
    protected abstract void method_23937();

    private ChatInputSuggestorMixin() {
    }

    @Inject(method={"refresh"}, at={@At(value="INVOKE", target="Lcom/mojang/brigadier/StringReader;canRead()Z", remap=false)}, cancellable=true)
    private void onRefresh(CallbackInfo ci, @Local StringReader reader) {
        String prefix = ".";
        int length = prefix.length();
        if (reader.canRead(length) && reader.getString().startsWith(prefix, reader.getCursor())) {
            int cursor;
            reader.setCursor(reader.getCursor() + length);
            if (this.field_21610 == null) {
                if (Constants.mc.method_1562() == null) {
                    ci.cancel();
                    return;
                }
                this.field_21610 = CommandRepository.DISPATCHER.parse(reader, (Object)Constants.mc.method_1562().method_2875());
            }
            if (!((cursor = this.field_21599.method_1881()) < 1 || this.field_21612 != null && this.field_21614)) {
                try {
                    this.field_21611 = CommandRepository.DISPATCHER.getCompletionSuggestions(this.field_21610, cursor);
                }
                catch (Exception e) {
                    this.field_21611 = Suggestions.empty();
                }
                if (this.field_21611 != null) {
                    this.field_21611.thenRun(() -> {
                        if (this.field_21611 != null && this.field_21611.isDone()) {
                            this.method_23937();
                        }
                    });
                }
            }
            ci.cancel();
        }
    }
}

