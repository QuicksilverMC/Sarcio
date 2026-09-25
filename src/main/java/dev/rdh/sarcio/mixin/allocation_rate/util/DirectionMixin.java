package dev.rdh.sarcio.mixin.allocation_rate.util;

import java.util.Random;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Direction.class)
abstract class DirectionMixin {
	@Shadow @Final private static Direction[] ALL;
	@Shadow @Final private Vec3i normal;
	@Shadow @Final private int opposite;

	/**
	 * @author rdh
	 * @reason reuse direction vector's offsets
	 */
	@Overwrite
	public int getOffsetX() {
		return this.normal.getX();
	}

	/**
	 * @author rdh
	 * @reason reuse direction vector's offsets
	 */
	@Overwrite
	public int getOffsetY() {
		return this.normal.getY();
	}

	/**
	 * @author rdh
	 * @reason reuse direction vector's offsets
	 */
	@Overwrite
	public int getOffsetZ() {
		return this.normal.getZ();
	}

	/**
	 * @author rdh
	 * @reason skip a modulo
	 */
	@Overwrite
	public Direction getOpposite() {
		return ALL[this.opposite];
	}

	/**
	 * @author rdh
	 * @reason save some allocations
	 */
	@Overwrite
	public static Direction pick(Random rand) {
		return ALL[rand.nextInt(ALL.length)];
	}
}
