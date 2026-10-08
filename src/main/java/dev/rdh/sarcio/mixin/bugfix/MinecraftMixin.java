package dev.rdh.sarcio.mixin.bugfix;

import net.minecraft.client.Minecraft;
import net.minecraft.client.Screenshot;
import net.minecraft.client.gui.screen.ProgressScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.render.pipeline.RenderTarget;
import org.lwjgl.input.Keyboard;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    public GameOptions options;

    @Shadow
    private RenderTarget renderTarget;

    @Inject(method = "openScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;lockMouse()V"))
    private void sarcio$reapplyKeybinds(Screen guiScreenIn, CallbackInfo ci) {
        for (KeyBinding keyBinding : KeyBinding.ALL) {
            int keyCode = keyBinding.getKeyCode();
            if (keyCode > 0 && keyCode < Keyboard.KEYBOARD_SIZE) {
                KeyBinding.set(keyCode, Keyboard.isKeyDown(keyCode));
            }
        }
    }

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;inventoryKey:Lnet/minecraft/client/options/KeyBinding;", ordinal = 0, opcode = Opcodes.GETFIELD))
    private void sarcio$handleMouseBoundKeys(CallbackInfo ci) {
        Minecraft mc = (Minecraft) (Object) this;
        if (mc.screen == null) {
            if (this.options.togglePerspectiveKey.consumeClick()) {
                this.options.perspective = (this.options.perspective + 1) % 3;
                if (this.options.perspective < 2) {
                    mc.gameRenderer.updateShader(this.options.perspective == 0 ? mc.getCamera() : null);
                }
                mc.worldRenderer.onViewChanged();
            }
            if (this.options.smoothCameraKey.consumeClick()) {
                this.options.smoothCamera = !this.options.smoothCamera;
            }
        }

        if (this.options.fullscreenKey.getKeyCode() < 0 && this.options.fullscreenKey.consumeClick()) {
            mc.toggleFullscreen();
        }
        if (this.options.screenshotKey.getKeyCode() < 0 && this.options.screenshotKey.consumeClick()) {
            mc.gui.getChat().addMessage(Screenshot.take(mc.gameDir, mc.width, mc.height, this.renderTarget));
        }
    }

    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;openScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private Screen sarcio$showWorkingScreen(Screen guiScreenIn) {
        return new ProgressScreen();
    }

    @Inject(method = "toggleFullscreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/options/GameOptions;fullscreen:Z", opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void sarcio$saveFullscreen(CallbackInfo ci) {
        this.options.save();
    }
}
