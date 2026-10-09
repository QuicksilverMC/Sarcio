package dev.rdh.sarcio.mixin.mem.leak.network;

import net.minecraft.client.Minecraft;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.LoginS2CPacket;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.network.PacketUtils$48215440")
abstract class PacketThreadUtilRunnableMixin {
	@Shadow(remap = false) @Final private Packet<?> f_54564080; // packet, unnamed in feather

	@Inject(method = "run", at = @At("HEAD"), cancellable = true)
	private void dropPacketsWithoutWorld(CallbackInfo ci) {
		if (Minecraft.getInstance().world == null && !(this.f_54564080 instanceof LoginS2CPacket)) {
			ci.cancel();
		}
	}
}
