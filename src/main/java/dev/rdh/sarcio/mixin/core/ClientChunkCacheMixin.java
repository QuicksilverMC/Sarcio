package dev.rdh.sarcio.mixin.core;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.world.chunk.ClientChunkCache;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ClientChunkCache.class)
abstract class ClientChunkCacheMixin {
	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 1))
	private long sarcio$skipPerChunkClock() {
		return 0;
	}

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;tick(Z)V"))
	private void sarcio$tickWithinBudget(WorldChunk chunk, boolean overBudget, @Local long tickStart) {
		chunk.tick(((ChunkGapLightingAccessor) chunk).sarcio$isGapLightingUpdated()
				&& System.currentTimeMillis() - tickStart > 5L);
	}
}
