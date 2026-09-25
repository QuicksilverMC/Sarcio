package dev.rdh.sarcio.mixin.bugfix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.KeyBinding;
import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Inject(method = "openScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;lockMouse()V"))
    private void sarcio$reapplyKeybinds(Screen guiScreenIn, CallbackInfo ci) {
        for (KeyBinding keyBinding : KeyBinding.ALL) {
            int keyCode = keyBinding.getKeyCode();
            if (keyCode > 0 && keyCode < Keyboard.KEYBOARD_SIZE) {
                KeyBinding.set(keyCode, Keyboard.isKeyDown(keyCode));
            }
        }
    }

    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;openScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private Screen sarcio$showWorkingScreen(Screen guiScreenIn) {
        return new ProgressScreen();
    }
}
