package dev.rdh.sarcio.mixin.allocation_rate.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Direction.Plane.class)
abstract class DirectionPlaneMixin {
	@Unique private static volatile Direction[] sarcio$horizontal;
	@Unique private static volatile Direction[] sarcio$vertical;

	@WrapOperation(
		method = "iterator",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction$Plane;get()[Lnet/minecraft/util/math/Direction;")
	)
	private Direction[] reuseFacings(Direction.Plane plane, Operation<Direction[]> original) {
		if (plane == Direction.Plane.HORIZONTAL) {
			if (sarcio$horizontal == null) {
				sarcio$horizontal = original.call(plane);
			}
			return sarcio$horizontal;
		}
		if (sarcio$vertical == null) {
			sarcio$vertical = original.call(plane);
		}
		return sarcio$vertical;
	}
}
