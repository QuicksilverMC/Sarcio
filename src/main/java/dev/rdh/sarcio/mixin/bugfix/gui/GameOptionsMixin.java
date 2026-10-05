package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.Window;
import com.google.common.util.concurrent.ListenableFuture;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Shadow
    @Final
    private static String[] GUI_SCALE_SETTINGS;

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

    @WrapOperation(method = "set(Lnet/minecraft/client/options/GameOptions$Option;I)V", at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;guiScale:I", opcode = Opcodes.PUTFIELD))
    private void sarcio$cycleGuiScale(GameOptions options, int masked, Operation<Void> original, @Local(argsOnly = true) int value) {
        int current = options.guiScale;
        int max;
        try {
            options.guiScale = 0;
            max = new Window(Minecraft.getInstance()).getScale();
        } finally {
            options.guiScale = current;
        }
        original.call(options, Math.floorMod(Math.min(current, max) + value, max + 1));
    }

    @Inject(method = "translateIntegerValue", at = @At("HEAD"), cancellable = true)
    private static void sarcio$numericGuiScale(String[] translationKeys, int value, CallbackInfoReturnable<String> cir) {
        if (translationKeys == GUI_SCALE_SETTINGS && value > 0) {
            cir.setReturnValue(Integer.toString(value));
        }
    }
}
