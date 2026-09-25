package dev.rdh.sarcio.mixin.memory_management.network;

import com.llamalad7.mixinextras.sugar.Local;
import io.netty.buffer.ByteBuf;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
abstract class MinecraftServerMixin {
	@Inject(method = "setStatus", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ServerStatus;setFavicon(Ljava/lang/String;)V", shift = At.Shift.AFTER))
	private void releaseFaviconBuffer(CallbackInfo ci, @Local(ordinal = 1) ByteBuf encoded) {
		encoded.release();
	}
}
