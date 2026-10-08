package dev.rdh.sarcio.mixin.worldgen;

import dev.rdh.sarcio.util.TerrainPrefetcher;
import java.util.Random;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkSource;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.gen.chunk.NetherChunkGenerator;
import net.minecraft.world.gen.structure.FortressStructure;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(NetherChunkGenerator.class)
abstract class NetherChunkGeneratorMixin implements TerrainPrefetcher.Prefetchable {
	@Shadow @Final private World world;
	@Shadow @Final private boolean placeStructures;
	@Shadow @Final @Mutable private Random random;
	@Shadow @Final private FortressStructure fortress;

	@Override
	public ChunkSource sarcio$workerCopy() {
		return new NetherChunkGenerator(this.world, false, this.world.getSeed());
	}

	@Override
	public Random sarcio$takeRandom() {
		Random random = this.random;
		this.random = new Random();
		return random;
	}

	@Override
	public void sarcio$finish(WorldChunk chunk, int chunkX, int chunkZ, Random random) {
		this.random = random;
		if (this.placeStructures) {
			this.fortress.place((ChunkSource) this, this.world, chunkX, chunkZ, null);
		}
	}
}
