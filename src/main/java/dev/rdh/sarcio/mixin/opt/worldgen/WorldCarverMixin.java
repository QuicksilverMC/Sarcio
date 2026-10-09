package dev.rdh.sarcio.mixin.opt.worldgen;

import dev.rdh.sarcio.util.TerrainPrefetcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.carver.RavineWorldCarver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({CaveWorldCarver.class, RavineWorldCarver.class})
abstract class WorldCarverMixin {
	@Redirect(method = {"carveTunnel", "carveRavine"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBiome(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome;"))
	private Biome sarcio$prefetchedBiome(World world, BlockPos pos) {
		Biome[][] prefetched = TerrainPrefetcher.BIOMES.get();
		return prefetched != null ? prefetched[1][pos.getX() & 15 | (pos.getZ() & 15) << 4] : world.getBiome(pos);
	}
}
