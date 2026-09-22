package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.gui.GuiScreenServerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiScreenServerList.class)
public class GuiScreenServerListMixin {
    @ModifyExpressionValue(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiTextField;getText()Ljava/lang/String;"))
    private String sarcio$trimDirectConnectIp(String original) {
        return original.trim();
    }
}
