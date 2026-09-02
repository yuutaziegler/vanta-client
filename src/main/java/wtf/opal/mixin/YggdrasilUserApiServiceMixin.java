/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.yggdrasil.YggdrasilUserApiService
 *  com.mojang.authlib.yggdrasil.response.UserAttributesResponse$Privileges
 *  net.fabricmc.api.EnvType
 *  net.fabricmc.api.Environment
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package wtf.opal.mixin;

import com.mojang.authlib.yggdrasil.YggdrasilUserApiService;
import com.mojang.authlib.yggdrasil.response.UserAttributesResponse;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(value=EnvType.CLIENT)
@Mixin(value={YggdrasilUserApiService.class})
public final class YggdrasilUserApiServiceMixin {
    private YggdrasilUserApiServiceMixin() {
    }

    @Redirect(method={"fetchProperties"}, at=@At(value="INVOKE", target="Lcom/mojang/authlib/yggdrasil/response/UserAttributesResponse$Privileges;getTelemetry()Z", remap=false), remap=false, require=0)
    private boolean disableTelemetry(UserAttributesResponse.Privileges instance) {
        return false;
    }
}

