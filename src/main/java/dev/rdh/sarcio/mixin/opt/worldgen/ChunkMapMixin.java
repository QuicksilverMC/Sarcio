package dev.rdh.sarcio.mixin.opt.worldgen;

import dev.rdh.sarcio.util.TerrainPrefetcher;
import net.minecraft.server.ChunkMap;
import net.minecraft.server.entity.living.player.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChunkMap.class)
abstract class ChunkMapMixin {
	@Shadow @Final private ServerWorld world;
	@Shadow private int chunkViewDistance;

	@Inject(method = "addPlayer", at = @At("HEAD"))
	private void sarcio$prefetchOnJoin(ServerPlayerEntity player, CallbackInfo ci) {
		this.sarcio$prefetchAround((int) player.x >> 4, (int) player.z >> 4);
	}

	@Inject(method = "movePlayer", at = @At("HEAD"))
	private void sarcio$prefetchOnMove(ServerPlayerEntity player, CallbackInfo ci) {
		int chunkX = (int) player.x >> 4;
		int chunkZ = (int) player.z >> 4;
		double dx = player.trackedX - player.x;
		double dz = player.trackedZ - player.z;
		if (dx * dx + dz * dz >= 64.0 && (chunkX != (int) player.trackedX >> 4 || chunkZ != (int) player.trackedZ >> 4)) {
			this.sarcio$prefetchAround(chunkX, chunkZ);
		}
	}

	@Inject(method = {"addPlayer", "movePlayer"}, at = @At("RETURN"))
	private void sarcio$endPrefetch(ServerPlayerEntity player, CallbackInfo ci) {
		TerrainPrefetcher.end(this.world);
	}

	@Unique
	private void sarcio$prefetchAround(int chunkX, int chunkZ) {
		int distance = this.chunkViewDistance;
		TerrainPrefetcher.begin(this.world, chunkX - distance, chunkZ - distance, chunkX + distance, chunkZ + distance);
	}
}
