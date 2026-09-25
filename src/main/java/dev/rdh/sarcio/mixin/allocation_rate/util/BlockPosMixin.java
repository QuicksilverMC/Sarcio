package dev.rdh.sarcio.mixin.allocation_rate.util;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockPos.class)
abstract class BlockPosMixin extends Vec3i {
	private BlockPosMixin() {
		super(0, 0, 0);
	}

	/**
	 * @author rdh
	 * @reason save a few allocations
	 */
	@Overwrite
	public BlockPos up(int n) {
		return n == 0 ? (BlockPos)(Object)this : new BlockPos(this.getX(), this.getY() + n, this.getZ());
	}

	/**
	 * @author rdh
	 * @reason save a few allocations
	 */
	@Overwrite
	public BlockPos down(int n) {
		return n == 0 ? (BlockPos)(Object)this : new BlockPos(this.getX(), this.getY() - n, this.getZ());
	}

	/**
	 * @author rdh
	 * @reason save a few allocations
	 */
	@Overwrite
	public BlockPos north(int n) {
		return n == 0 ? (BlockPos)(Object)this : new BlockPos(this.getX(), this.getY(), this.getZ() - n);
	}

	/**
	 * @author rdh
	 * @reason save a few allocations
	 */
	@Overwrite
	public BlockPos south(int n) {
		return n == 0 ? (BlockPos)(Object)this : new BlockPos(this.getX(), this.getY(), this.getZ() + n);
	}

	/**
	 * @author rdh
	 * @reason save a few allocations
	 */
	@Overwrite
	public BlockPos west(int n) {
		return n == 0 ? (BlockPos)(Object)this : new BlockPos(this.getX() - n, this.getY(), this.getZ());
	}

	/**
	 * @author rdh
	 * @reason save a few allocations
	 */
	@Overwrite
	public BlockPos east(int n) {
		return n == 0 ? (BlockPos)(Object)this : new BlockPos(this.getX() + n, this.getY(), this.getZ());
	}

	/**
	 * @author rdh
	 * @reason save a few allocations
	 */
	@Overwrite
	public BlockPos offset(Direction facing) {
		return new BlockPos(
			this.getX() + facing.getOffsetX(),
			this.getY() + facing.getOffsetY(),
			this.getZ() + facing.getOffsetZ()
		);
	}
}
