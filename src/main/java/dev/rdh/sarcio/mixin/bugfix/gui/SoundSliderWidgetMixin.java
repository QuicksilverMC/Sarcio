package dev.rdh.sarcio.mixin.bugfix.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.options.GameOptions;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.client.gui.screen.menu.options.SoundsScreen$SoundSliderWidget")
public class SoundSliderWidgetMixin {
    @WrapWithCondition(method = "renderBackground(Lnet/minecraft/client/Minecraft;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/options/GameOptions;save()V"))
    private boolean sarcio$skipSaveWhileDragging(GameOptions settings) {
        return false;
    }

    @Inject(method = "mouseReleased(II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sound/system/SoundManager;play(Lnet/minecraft/client/sound/instance/SoundInstance;)V"))
    private void sarcio$saveOnRelease(int mouseX, int mouseY, CallbackInfo ci) {
        Minecraft.getInstance().options.save();
    }
}
