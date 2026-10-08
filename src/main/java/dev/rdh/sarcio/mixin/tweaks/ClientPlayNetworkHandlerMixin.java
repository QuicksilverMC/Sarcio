package dev.rdh.sarcio.mixin.tweaks;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.rdh.sarcio.SarcioMod;
import dev.rdh.sarcio.util.LongChatDetector;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.client.options.ServerListEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @WrapWithCondition(method = "handlePlayerRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;openScreen(Lnet/minecraft/client/gui/screen/Screen;)V"))
    private boolean sarcio$skipTerrainScreen(Minecraft instance, Screen screen) {
        return false;
    }

    @Inject(method = "handleLogin", at = @At("TAIL"))
    private void sarcio$detectLongChat(CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        ServerListEntry server = minecraft.getCurrentServerEntry();
        LongChatDetector.detect(SarcioMod.CONFIG.longChat && !minecraft.isIntegratedServerRunning() && server != null ? server.ip : null);
    }
}
