package dev.rdh.sarcio.mixin.core;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ChunkProviderClient.class)
abstract class ChunkProviderClientMixin {
	@Redirect(method = "unloadQueuedChunks", at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 1))
	private long sarcio$skipPerChunkClock() {
		return 0;
	}

	@Redirect(method = "unloadQueuedChunks", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/Chunk;m_32497923(Z)V"))
	private void sarcio$tickWithinBudget(Chunk chunk, boolean overBudget, @Local long tickStart) {
		chunk.m_32497923(((ChunkGapLightingAccessor) chunk).sarcio$isGapLightingUpdated()
				&& System.currentTimeMillis() - tickStart > 5L);
	}
}
