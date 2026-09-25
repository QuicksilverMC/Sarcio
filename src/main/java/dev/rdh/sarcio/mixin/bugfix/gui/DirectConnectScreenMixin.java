package dev.rdh.sarcio.mixin.bugfix.gui;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.screen.menu.multiplayer.DirectConnectScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(DirectConnectScreen.class)
public class DirectConnectScreenMixin {
    @ModifyExpressionValue(method = "buttonClicked", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;getText()Ljava/lang/String;"))
    private String sarcio$trimDirectConnectIp(String original) {
        return original.trim();
    }
}
