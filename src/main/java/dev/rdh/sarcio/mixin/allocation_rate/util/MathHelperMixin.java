package dev.rdh.sarcio.mixin.allocation_rate.util;

import dev.rdh.sarcio.util.CompactSineLUT;
import net.minecraft.util.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MathHelper.class)
abstract class MathHelperMixin {
	@Shadow @Final @Mutable private static float[] SIN_TABLE;

	@Inject(method = "<clinit>", at = @At("RETURN"))
	private static void sarcio$buildCompactTable(CallbackInfo ci) {
		CompactSineLUT.init(SIN_TABLE);
		SIN_TABLE = null;
	}

	/**
	 * @author rdh
	 * @reason use the compact table
	 */
	@Overwrite
	public static float sin(float value) {
		return CompactSineLUT.sin(value);
	}

	/**
	 * @author rdh
	 * @reason use the compact table
	 */
	@Overwrite
	public static float cos(float value) {
		return CompactSineLUT.cos(value);
	}
}
