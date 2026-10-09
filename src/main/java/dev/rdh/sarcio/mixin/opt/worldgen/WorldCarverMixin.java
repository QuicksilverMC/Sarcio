package dev.rdh.sarcio.mixin.opt.worldgen;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.rdh.sarcio.util.TerrainPrefetcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.carver.CaveWorldCarver;
import net.minecraft.world.gen.carver.RavineWorldCarver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin({CaveWorldCarver.class, RavineWorldCarver.class})
abstract class WorldCarverMixin {
	@WrapOperation(method = {"carveTunnel", "carveRavine"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getBiome(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/world/biome/Biome;"))
	private Biome sarcio$prefetchedBiome(World world, BlockPos pos, Operation<Biome> original) {
		Biome[][] prefetched = TerrainPrefetcher.BIOMES.get();
		return prefetched != null ? prefetched[1][pos.getX() & 15 | (pos.getZ() & 15) << 4] : original.call(world, pos);
	}
}
