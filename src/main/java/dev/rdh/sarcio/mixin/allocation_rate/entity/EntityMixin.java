package dev.rdh.sarcio.mixin.allocation_rate.entity;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
abstract class EntityMixin {
	@Shadow public World world;

	@Unique private long sarcio$brightnessTick = Long.MIN_VALUE;
	@Unique private int sarcio$brightnessValue;

	@Inject(method = "getLightLevel", at = @At("HEAD"), cancellable = true)
	private void sarcio$brightnessCacheHit(float partialTicks, CallbackInfoReturnable<Integer> cir) {
		if (this.world != null && this.sarcio$brightnessTick == this.world.getTime()) {
			cir.setReturnValue(this.sarcio$brightnessValue);
		}
	}

	@Inject(method = "getLightLevel", at = @At("RETURN"))
	private void sarcio$brightnessCacheStore(float partialTicks, CallbackInfoReturnable<Integer> cir) {
		if (this.world != null) {
			this.sarcio$brightnessTick = this.world.getTime();
			this.sarcio$brightnessValue = cir.getReturnValue();
		}
	}
}
