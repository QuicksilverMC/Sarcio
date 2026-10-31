package dev.rdh.sarcio.mixin.bugfix.network;

import dev.rdh.sarcio.Feature;
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
    @Feature("bugfix.network.hide_sign_debug")
    @WrapWithCondition(
        method = "handleSignBlockEntityUpdate",
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=Unable to locate sign at ")),
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/living/player/LocalClientPlayerEntity;sendMessage(Lnet/minecraft/text/Text;)V")
    )
    private boolean sarcio$hideSignDebugMessage(LocalClientPlayerEntity player, Text message) {
        return false;
    }

    @Feature("bugfix.network.clear_titles_on_login")
    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void sarcio$clearTitles(CallbackInfo ci) {
        GameGui ingameGUI = Minecraft.getInstance().gui;
        ingameGUI.setTitles(null, null, -1, -1, -1);
        ingameGUI.resetTitleTimes();
    }
}
