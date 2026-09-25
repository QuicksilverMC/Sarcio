package dev.rdh.sarcio.mixin.memory_management.network;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.network.handler.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.CustomPayloadC2SPacket;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.network.handler.ServerPlayNetworkHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Coerce;

@Mixin({ClientPlayNetworkHandler.class, ServerPlayNetworkHandler.class})
public class NetHandlerPlayMixin {
	@WrapMethod(method = {"handleCustomPayload", "processVanilla250Packet"})
	private void releaseCustomPayload(@Coerce Packet<?> packet, Operation<Void> original) {
		original.call(packet);
		PacketByteBuf data = switch (packet) {
			case CustomPayloadS2CPacket s3f -> s3f.getData();
			case CustomPayloadC2SPacket c17 -> c17.getData();
			default -> throw new AssertionError("Unsupported packet type");
		};
		if (data != null && data.refCnt() > 0) {
			data.release();
		}
	}
}
