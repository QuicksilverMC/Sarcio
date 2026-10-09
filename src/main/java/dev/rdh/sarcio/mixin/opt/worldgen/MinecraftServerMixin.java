package dev.rdh.sarcio.mixin.opt.worldgen;

import dev.rdh.sarcio.util.TerrainPrefetcher;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
abstract class MinecraftServerMixin {
	@Shadow public ServerWorld[] worlds;

	@Inject(method = "prepareWorlds", at = @At("HEAD"))
	private void sarcio$prefetchSpawnArea(CallbackInfo ci) {
		BlockPos spawn = this.worlds[0].getSpawnPoint();
		TerrainPrefetcher.begin(this.worlds[0], spawn.getX() - 192 >> 4, spawn.getZ() - 192 >> 4, spawn.getX() + 192 >> 4, spawn.getZ() + 192 >> 4);
	}

	@Inject(method = "prepareWorlds", at = @At("RETURN"))
	private void sarcio$endPrefetch(CallbackInfo ci) {
		TerrainPrefetcher.end(this.worlds[0]);
	}
}
