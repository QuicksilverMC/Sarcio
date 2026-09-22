package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiScreen.class)
public class GuiScreenMixin {
    @Shadow
    protected Minecraft mc;

    @WrapOperation(method = "handleInput", at = @At(value = "INVOKE", target = "Lorg/lwjgl/input/Keyboard;next()Z"))
    private boolean sarcio$stopKeyboardInputWhenClosed(Operation<Boolean> original) {
        return this.mc.currentScreen == (Object) this && original.call();
    }
}
