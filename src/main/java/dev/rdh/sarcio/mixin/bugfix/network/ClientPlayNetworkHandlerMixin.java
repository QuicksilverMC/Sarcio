package dev.rdh.sarcio.mixin.bugfix.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.living.player.LocalClientPlayerEntity;
import net.minecraft.client.gui.GameGui;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @WrapWithCondition(
        method = "handleSignBlockEntityUpdate",
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=Unable to locate sign at ")),
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;sendMessage(Lnet/minecraft/text/Text;)V")
    )
    private boolean sarcio$hideSignDebugMessage(LocalClientPlayerEntity player, Text message) {
        return false;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void sarcio$clearTitles(CallbackInfo ci) {
        GameGui ingameGUI = Minecraft.getInstance().gui;
        ingameGUI.setTitles("", "", -1, -1, -1);
        ingameGUI.resetTitleTimes();
    }
}
