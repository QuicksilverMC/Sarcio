package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;

import com.google.common.util.concurrent.ListenableFuture;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameSettings.class)
public class GameSettingsMixin {
    @Inject(method = "isKeyDown", at = @At("HEAD"), cancellable = true)
    private static void sarcio$ignoreUnicodeKeys(KeyBinding key, CallbackInfoReturnable<Boolean> cir) {
        if (key.getKeyCode() >= 256) {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "setOptionFloatValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;scheduleResourcesRefresh()Lcom/google/common/util/concurrent/ListenableFuture;"))
    private ListenableFuture<?> sarcio$deferResourceRefresh(Minecraft mc, Operation<ListenableFuture<?>> original) {
        return null;
    }
}
