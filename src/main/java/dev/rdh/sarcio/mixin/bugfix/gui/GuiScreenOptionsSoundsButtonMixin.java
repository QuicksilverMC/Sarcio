package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.GuiScreenOptionsSounds$Button")
public class GuiScreenOptionsSoundsButtonMixin {
    @WrapWithCondition(method = "mouseDragged(Lnet/minecraft/client/Minecraft;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/settings/GameSettings;saveOptions()V"))
    private boolean sarcio$skipSaveWhileDragging(GameSettings settings) {
        return false;
    }

    @Inject(method = "mouseReleased(II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/audio/SoundHandler;playSound(Lnet/minecraft/client/audio/ISound;)V"))
    private void sarcio$saveOnRelease(int mouseX, int mouseY, CallbackInfo ci) {
        Minecraft.getMinecraft().gameSettings.saveOptions();
    }
}
