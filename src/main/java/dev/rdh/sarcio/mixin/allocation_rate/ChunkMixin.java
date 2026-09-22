package dev.rdh.sarcio.mixin.allocation_rate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Chunk.class)
abstract class ChunkMixin {
	@Shadow @Final private ExtendedBlockStorage[] storageArrays;

	/**
	 * @author rdh
	 * @reason drop the crash report builder so the getter is small enough to inline
	 */
	@Overwrite
	public IBlockState getBlockState(BlockPos pos) {
		int y = pos.getY();
		if (y >= 0 && y >> 4 < this.storageArrays.length) {
			ExtendedBlockStorage storage = this.storageArrays[y >> 4];
			if (storage != null) {
				return storage.get(pos.getX() & 15, y & 15, pos.getZ() & 15);
			}
		}

		return Blocks.air.getDefaultState();
	}
}
