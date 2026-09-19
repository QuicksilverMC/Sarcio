package dev.rdh.sarcio.mixin.bugfix;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
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
}
