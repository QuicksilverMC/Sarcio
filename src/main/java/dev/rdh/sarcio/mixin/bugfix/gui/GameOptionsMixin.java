package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import com.google.common.util.concurrent.ListenableFuture;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Inject(method = "isPressed", at = @At("HEAD"), cancellable = true)
    private static void sarcio$ignoreUnicodeKeys(KeyBinding key, CallbackInfoReturnable<Boolean> cir) {
        if (key.getKeyCode() >= 256) {
            cir.setReturnValue(false);
        }
    }

    @WrapOperation(method = "set(Lnet/minecraft/client/options/GameOptions$Option;F)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;submitResourceReload()Lcom/google/common/util/concurrent/ListenableFuture;"))
    private ListenableFuture<?> sarcio$deferResourceRefresh(Minecraft mc, Operation<ListenableFuture<?>> original) {
        return null;
    }
}
