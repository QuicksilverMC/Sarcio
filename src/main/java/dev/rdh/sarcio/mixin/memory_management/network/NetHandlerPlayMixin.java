package dev.rdh.sarcio.mixin.memory_management.network;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Coerce;

import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraft.network.play.server.S3FPacketCustomPayload;

@Mixin({NetHandlerPlayClient.class, NetHandlerPlayServer.class})
public class NetHandlerPlayMixin {
	@WrapMethod(method = {"handleCustomPayload", "processVanilla250Packet"})
	private void releaseCustomPayload(@Coerce Packet<?> packet, Operation<Void> original) {
		original.call(packet);
		PacketBuffer data = switch (packet) {
			case S3FPacketCustomPayload s3f -> s3f.getBufferData();
			case C17PacketCustomPayload c17 -> c17.getBufferData();
			default -> throw new AssertionError("Unsupported packet type");
		};
		if (data != null && data.refCnt() > 0) {
			data.release();
		}
	}
}
