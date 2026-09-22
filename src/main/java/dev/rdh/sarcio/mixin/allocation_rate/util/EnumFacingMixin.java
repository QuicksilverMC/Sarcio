package dev.rdh.sarcio.mixin.allocation_rate.util;

import java.util.Random;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(EnumFacing.class)
abstract class EnumFacingMixin {
	@Shadow @Final private static EnumFacing[] VALUES;
	@Shadow @Final private Vec3i directionVec;
	@Shadow @Final private int opposite;

	/**
	 * @author rdh
	 * @reason reuse direction vector's offsets
	 */
	@Overwrite
	public int getFrontOffsetX() {
		return this.directionVec.getX();
	}

	/**
	 * @author rdh
	 * @reason reuse direction vector's offsets
	 */
	@Overwrite
	public int getFrontOffsetY() {
		return this.directionVec.getY();
	}

	/**
	 * @author rdh
	 * @reason reuse direction vector's offsets
	 */
	@Overwrite
	public int getFrontOffsetZ() {
		return this.directionVec.getZ();
	}

	/**
	 * @author rdh
	 * @reason skip a modulo
	 */
	@Overwrite
	public EnumFacing getOpposite() {
		return VALUES[this.opposite];
	}

	/**
	 * @author rdh
	 * @reason save some allocations
	 */
	@Overwrite
	public static EnumFacing random(Random rand) {
		return VALUES[rand.nextInt(VALUES.length)];
	}
}
