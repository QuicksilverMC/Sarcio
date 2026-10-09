package dev.rdh.sarcio.mixin.core;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.world.chunk.ClientChunkCache;
import net.minecraft.util.Long2ObjectHashMap;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientChunkCache.class)
abstract class ClientChunkCacheMixin {

	@Unique private WorldChunk sarcio$lastChunk;

	@WrapOperation(method = "getChunk(II)Lnet/minecraft/world/chunk/WorldChunk;", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Long2ObjectHashMap;get(J)Ljava/lang/Object;"))
	private Object sarcio$rememberLastChunk(Long2ObjectHashMap<WorldChunk> chunks, long key, Operation<Object> original, int x, int z) {
		WorldChunk last = this.sarcio$lastChunk;
		if (last != null && last.chunkX == x && last.chunkZ == z) {
			return last;
		}

		Object chunk = original.call(chunks, key);
		if (chunk != null) {
			this.sarcio$lastChunk = (WorldChunk) chunk;
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

	@Redirect(method = "tick", at = @At(value = "INVOKE", target = "Ljava/lang/System;currentTimeMillis()J", ordinal = 1))
	private long sarcio$skipPerChunkClock() {
		return 0;
	}

	@ModifyArg(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;tick(Z)V"))
	private boolean sarcio$tickWithinBudget(boolean overBudget, @Local WorldChunk chunk, @Local long tickStart) {
		return ((ChunkGapLightingAccessor) chunk).sarcio$isGapLightingUpdated()
				&& System.currentTimeMillis() - tickStart > 5L;
	}
}
