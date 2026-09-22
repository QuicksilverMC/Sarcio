package dev.rdh.sarcio.mixin.bugfix.network;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.network.NetHandlerPlayClient;
import org.spongepowered.asm.mixin.Mixin;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.IChatComponent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetHandlerPlayClient.class)
public class NetHandlerPlayClientMixin {
    @WrapWithCondition(
        method = "handleUpdateSign",
        slice = @Slice(from = @At(value = "CONSTANT", args = "stringValue=Unable to locate sign at ")),
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityPlayerSP;addChatMessage(Lnet/minecraft/util/IChatComponent;)V")
    )
    private boolean sarcio$hideSignDebugMessage(EntityPlayerSP player, IChatComponent message) {
        return false;
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void sarcio$clearTitles(CallbackInfo ci) {
        GuiIngame ingameGUI = Minecraft.getMinecraft().ingameGUI;
        ingameGUI.displayTitle("", "", -1, -1, -1);
        ingameGUI.setDefaultTitlesTimes();
    }
}
