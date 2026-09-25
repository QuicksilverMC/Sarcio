package dev.rdh.sarcio.mixin.allocation_rate.util;

import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Direction.Plane.class)
abstract class DirectionPlaneMixin {
	@Unique private static volatile Direction[] sarcio$horizontal;
	@Unique private static volatile Direction[] sarcio$vertical;

	@Redirect(
		method = "iterator",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction$Plane;get()[Lnet/minecraft/util/math/Direction;")
	)
	private Direction[] reuseFacings(Direction.Plane plane) {
		if (plane == Direction.Plane.HORIZONTAL) {
			if (sarcio$horizontal == null) {
				sarcio$horizontal = plane.get();
			}
			return sarcio$horizontal;
		}
		if (sarcio$vertical == null) {
			sarcio$vertical = plane.get();
		}
		return sarcio$vertical;
	}
}
