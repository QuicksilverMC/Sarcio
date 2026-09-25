package dev.rdh.sarcio.mixin.allocation_rate.world;

import net.minecraft.block.Blocks;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.WorldChunkSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(WorldChunk.class)
abstract class WorldChunkMixin {
	@Shadow @Final private WorldChunkSection[] sections;

	/**
	 * @author rdh
	 * @reason drop the crash report builder so the getter is small enough to inline
	 */
	@Overwrite
	public BlockState getBlockState(BlockPos pos) {
		int y = pos.getY();
		if (y >= 0 && y >> 4 < this.sections.length) {
			WorldChunkSection storage = this.sections[y >> 4];
			if (storage != null) {
				return storage.getBlockState(pos.getX() & 15, y & 15, pos.getZ() & 15);
			}
		}

		return Blocks.AIR.defaultState();
	}
}
