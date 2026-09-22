package dev.rdh.sarcio.mixin.allocation_rate.util;

import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
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
	public BlockPos offset(EnumFacing facing) {
		return new BlockPos(
			this.getX() + facing.getFrontOffsetX(),
			this.getY() + facing.getFrontOffsetY(),
			this.getZ() + facing.getFrontOffsetZ()
		);
	}
}
