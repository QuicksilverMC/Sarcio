package dev.rdh.sarcio.mixin.opt.worldgen;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.rdh.sarcio.util.TerrainPrefetcher;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.server.world.chunk.ServerChunkCache;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.storage.ChunkStorage;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkCache.class)
abstract class ServerChunkCacheMixin implements TerrainPrefetcher.Holder {
	@Unique private TerrainPrefetcher sarcio$terrainPrefetcher;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void sarcio$createPrefetcher(ServerWorld world, ChunkStorage storage, ChunkSource generator, CallbackInfo ci) {
		this.sarcio$terrainPrefetcher = TerrainPrefetcher.create((ServerChunkCache) (Object) this, world, storage, generator);
	}

	@WrapOperation(method = "loadChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkSource;getChunk(II)Lnet/minecraft/world/chunk/WorldChunk;"))
	private WorldChunk sarcio$takePrefetched(ChunkSource generator, int chunkX, int chunkZ, Operation<WorldChunk> original) {
		if (this.sarcio$terrainPrefetcher == null) {
			return original.call(generator, chunkX, chunkZ);
		}

		WorldChunk chunk = this.sarcio$terrainPrefetcher.take(chunkX, chunkZ);
		if (chunk != null) {
			return chunk;
		}

		synchronized (TerrainPrefetcher.STATEFUL_BIOMES) {
			return original.call(generator, chunkX, chunkZ);
		}
	}

	@Override
	public TerrainPrefetcher sarcio$terrainPrefetcher() {
		return this.sarcio$terrainPrefetcher;
	}
}
