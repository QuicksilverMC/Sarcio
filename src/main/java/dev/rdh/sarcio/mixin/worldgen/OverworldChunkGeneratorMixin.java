package dev.rdh.sarcio.mixin.worldgen;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.rdh.sarcio.util.TerrainPrefetcher;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.OverworldChunkGenerator;
import net.minecraft.world.gen.chunk.OverworldGeneratorOptions;
import net.minecraft.world.gen.structure.MineshaftStructure;
import net.minecraft.world.gen.structure.OceanMonumentStructure;
import net.minecraft.world.gen.structure.StrongholdStructure;
import net.minecraft.world.gen.structure.TempleStructure;
import net.minecraft.world.gen.structure.VillageStructure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OverworldChunkGenerator.class)
abstract class OverworldChunkGeneratorMixin implements TerrainPrefetcher.Prefetchable {
	@Shadow private World world;
	@Shadow @Final private boolean placeStructures;
	@Shadow private OverworldGeneratorOptions options;
	@Shadow private MineshaftStructure mineshaft;
	@Shadow private VillageStructure village;
	@Shadow private StrongholdStructure stronghold;
	@Shadow private TempleStructure temple;
	@Shadow private OceanMonumentStructure oceanMonument;

	@WrapOperation(method = "buildTerrain", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/BiomeSource;getNoiseBiomes([Lnet/minecraft/world/biome/Biome;IIII)[Lnet/minecraft/world/biome/Biome;"))
	private Biome[] sarcio$prefetchedNoiseBiomes(BiomeSource source, Biome[] biomes, int x, int z, int sizeX, int sizeZ, Operation<Biome[]> original) {
		Biome[][] prefetched = TerrainPrefetcher.BIOMES.get();
		return prefetched != null ? prefetched[0] : original.call(source, biomes, x, z, sizeX, sizeZ);
	}

	@WrapOperation(method = "getChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/biome/source/BiomeSource;getBiomes([Lnet/minecraft/world/biome/Biome;IIII)[Lnet/minecraft/world/biome/Biome;"))
	private Biome[] sarcio$prefetchedBiomes(BiomeSource source, Biome[] biomes, int x, int z, int sizeX, int sizeZ, Operation<Biome[]> original) {
		Biome[][] prefetched = TerrainPrefetcher.BIOMES.get();
		return prefetched != null ? prefetched[1] : original.call(source, biomes, x, z, sizeX, sizeZ);
	}

	// notifies the world's listeners, so on a worker it is left for TerrainPrefetcher.take
	@WrapWithCondition(method = "getChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/WorldChunk;populateHeightMap()V"))
	private boolean sarcio$deferHeightMap(WorldChunk chunk) {
		return TerrainPrefetcher.BIOMES.get() == null;
	}

	@Override
	public ChunkSource sarcio$workerCopy() {
		return new OverworldChunkGenerator(this.world, this.world.getSeed(), false, this.world.getData().getGeneratorOptions());
	}

	@Override
	public Random sarcio$takeRandom() {
		return null;
	}

	@Override
	public void sarcio$finish(WorldChunk chunk, int chunkX, int chunkZ, Random random) {
		ChunkSource self = (ChunkSource) this;
		if (this.placeStructures && this.options.useMineshafts) {
			this.mineshaft.place(self, this.world, chunkX, chunkZ, null);
		}

		if (this.placeStructures && this.options.useVillages) {
			this.village.place(self, this.world, chunkX, chunkZ, null);
		}

		if (this.placeStructures && this.options.useStrongholds) {
			this.stronghold.place(self, this.world, chunkX, chunkZ, null);
		}

		if (this.placeStructures && this.options.useTemples) {
			this.temple.place(self, this.world, chunkX, chunkZ, null);
		}

		if (this.placeStructures && this.options.useMonuments) {
			this.oceanMonument.place(self, this.world, chunkX, chunkZ, null);
		}

		chunk.populateHeightMap();
	}
}
