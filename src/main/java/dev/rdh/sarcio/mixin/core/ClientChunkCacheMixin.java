package dev.rdh.sarcio.mixin.core;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.world.chunk.ClientChunkCache;
import net.minecraft.util.Long2ObjectHashMap;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientChunkCache.class)
abstract class ClientChunkCacheMixin {
	@Shadow private WorldChunk empty;
	@Shadow private Long2ObjectHashMap<WorldChunk> chunksByPos;

	@Unique private WorldChunk sarcio$lastChunk;

	/**
	 * @author rdh
	 * @reason remember the last chunk, since lookups mostly repeat it
	 */
	@Overwrite
	public WorldChunk getChunk(int x, int z) {
		WorldChunk chunk = this.sarcio$lastChunk;
		if (chunk != null && chunk.chunkX == x && chunk.chunkZ == z) {
			return chunk;
		}

		chunk = this.chunksByPos.get(ChunkPos.toLong(x, z));
		if (chunk == null) {
			return this.empty;
		}
		this.sarcio$lastChunk = chunk;
		return chunk;
	}

	@Inject(method = "loadChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Long2ObjectHashMap;put(JLjava/lang/Object;)V", shift = At.Shift.AFTER))
	private void sarcio$forgetReplacedChunk(int x, int z, CallbackInfoReturnable<WorldChunk> cir) {
		this.sarcio$lastChunk = null;
	}

	@Inject(method = "unloadChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Long2ObjectHashMap;remove(J)Ljava/lang/Object;", shift = At.Shift.AFTER))
	private void sarcio$forgetUnloadedChunk(int x, int z, CallbackInfo ci) {
		this.sarcio$lastChunk = null;
	}

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
