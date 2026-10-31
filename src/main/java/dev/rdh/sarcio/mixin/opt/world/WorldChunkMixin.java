package dev.rdh.sarcio.mixin.opt.world;

import dev.rdh.sarcio.Feature;
import net.minecraft.block.Blocks;
import net.minecraft.block.state.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.minecraft.world.chunk.WorldChunkSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Feature("opt.inline_chunk_get_block")
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
