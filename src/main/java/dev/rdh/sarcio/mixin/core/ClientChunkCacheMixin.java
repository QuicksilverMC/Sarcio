package dev.rdh.sarcio.mixin.core;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.world.chunk.ClientChunkCache;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientChunkCache.class)
abstract class ClientChunkCacheMixin {
	@Shadow private WorldChunk empty;

	@Unique private WorldChunk sarcio$lastChunk;

	@WrapMethod(method = "getChunk(II)Lnet/minecraft/world/chunk/WorldChunk;")
	private WorldChunk sarcio$getChunk(int x, int z, Operation<WorldChunk> original) {
		WorldChunk chunk = this.sarcio$lastChunk;
		if (chunk != null && chunk.chunkX == x && chunk.chunkZ == z) {
			return chunk;
		}

		chunk = original.call(x, z);
		if (chunk != this.empty) {
			this.sarcio$lastChunk = chunk;
		}
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

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 1))
	private long sarcio$skipPerChunkClock(Operation<Long> original) {
		return 0;
	}

	@WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;tick(Z)V"))
	private void sarcio$tickWithinBudget(WorldChunk chunk, boolean overBudget, Operation<Void> original, @Local long tickStart) {
		original.call(chunk, ((ChunkGapLightingAccessor) chunk).sarcio$isGapLightingUpdated()
				&& System.currentTimeMillis() - tickStart > 5L);
	}
}
