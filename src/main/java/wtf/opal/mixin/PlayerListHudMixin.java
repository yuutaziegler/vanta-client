/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  net.minecraft.class_124
 *  net.minecraft.class_2561
 *  net.minecraft.class_2564
 *  net.minecraft.class_355
 *  net.minecraft.class_5250
 *  net.minecraft.class_640
 *  net.minecraft.class_7417
 *  net.minecraft.class_8828
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package wtf.opal.mixin;

import java.util.List;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2564;
import net.minecraft.class_355;
import net.minecraft.class_5250;
import net.minecraft.class_640;
import net.minecraft.class_7417;
import net.minecraft.class_8828;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import wtf.opal.client.OpalClient;

@Environment(value=EnvType.CLIENT)
@Mixin(value={class_355.class})
public final class PlayerListHudMixin {
    @Unique
    private static class_2561 GRAY_OPENING_PARENTHESIS;
    @Unique
    private static class_2561 GRAY_CLOSING_PARENTHESIS;
    @Unique
    private static class_2561 EMPTY_TEXT;

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/PlayerListHud;getPlayerName(Lnet/minecraft/client/network/PlayerListEntry;)Lnet/minecraft/text/Text;"))
    private class_2561 redirectPlayerName(class_355 instance, class_640 entry) {
        class_2561 playerNameText = instance.method_1918(entry);
        String user = OpalClient.getInstance().getUser();
        if (user == null) {
            return playerNameText;
        }
        if (GRAY_OPENING_PARENTHESIS == null) {
            GRAY_OPENING_PARENTHESIS = class_2561.method_43470((String)(" " + String.valueOf(class_124.field_1080) + "("));
            GRAY_CLOSING_PARENTHESIS = class_2561.method_43470((String)(String.valueOf(class_124.field_1080) + ")"));
            EMPTY_TEXT = class_2561.method_43473();
        }
        return class_2564.method_37112(List.of(playerNameText, GRAY_OPENING_PARENTHESIS, class_5250.method_43477((class_7417)class_8828.method_54232((String)user)).method_54663(-1), GRAY_CLOSING_PARENTHESIS), (class_2561)EMPTY_TEXT);
    }
}

