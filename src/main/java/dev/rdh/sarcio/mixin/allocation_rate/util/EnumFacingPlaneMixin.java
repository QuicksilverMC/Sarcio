package dev.rdh.sarcio.mixin.allocation_rate.util;

import net.minecraft.util.EnumFacing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EnumFacing.Plane.class)
abstract class EnumFacingPlaneMixin {
	@Unique private static volatile EnumFacing[] sarcio$horizontal;
	@Unique private static volatile EnumFacing[] sarcio$vertical;

	@Redirect(
		method = "iterator",
		at = @At(value = "INVOKE", target = "Lnet/minecraft/util/EnumFacing$Plane;facings()[Lnet/minecraft/util/EnumFacing;")
	)
	private EnumFacing[] reuseFacings(EnumFacing.Plane plane) {
		if (plane == EnumFacing.Plane.HORIZONTAL) {
			if (sarcio$horizontal == null) {
				sarcio$horizontal = plane.facings();
			}
			return sarcio$horizontal;
		}
		if (sarcio$vertical == null) {
			sarcio$vertical = plane.facings();
		}
		return sarcio$vertical;
	}
}
