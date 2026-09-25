package dev.rdh.sarcio.mixin.bugfix.network;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.handler.PacketHandler;
import net.minecraft.network.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.network.PacketUtils$48215440")
public class PacketThreadUtilRunnableMixin {
    @WrapWithCondition(method = "run", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/packet/Packet;handle(Lnet/minecraft/network/handler/PacketHandler;)V"))
    private boolean sarcio$dropPacketsFromClosedConnections(Packet<?> packet, PacketHandler handler) {
		return !(handler instanceof ClientPlayNetworkHandler client) || client.getConnection().isConnected();
	}
}
