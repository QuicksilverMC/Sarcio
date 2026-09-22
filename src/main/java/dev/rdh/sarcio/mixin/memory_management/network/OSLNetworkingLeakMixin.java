package dev.rdh.sarcio.mixin.memory_management.network;

import net.ornithemc.osl.core.api.util.NamespacedIdentifier;
import net.ornithemc.osl.networking.api.PacketBuffer;
import net.ornithemc.osl.networking.impl.client.ClientPlayNetworkingImpl;
import net.ornithemc.osl.networking.impl.server.ServerPlayNetworkingImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = {ClientPlayNetworkingImpl.class, ServerPlayNetworkingImpl.class}, remap = false)
public class OSLNetworkingLeakMixin {
	@Inject(method = "handlePayload", at = @At("RETURN"))
	private static void releaseHandledPayload(
			NamespacedIdentifier channel,
			@Coerce Object listener,
			@Coerce Object ctx,
			PacketBuffer data,
			CallbackInfo ci
	) {
		if (data.refCnt() > 0) {
			data.release();
		}
	}
}
