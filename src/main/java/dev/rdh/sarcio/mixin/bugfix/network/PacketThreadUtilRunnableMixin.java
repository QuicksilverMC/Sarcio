package dev.rdh.sarcio.mixin.bugfix.network;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.network.PacketThreadUtil$1")
public class PacketThreadUtilRunnableMixin {
    @WrapWithCondition(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V"))
    private boolean sarcio$dropPacketsFromClosedConnections(Packet<?> packet, INetHandler handler) {
		return !(handler instanceof NetHandlerPlayClient client) || client.getNetworkManager().isChannelOpen();
	}
}
