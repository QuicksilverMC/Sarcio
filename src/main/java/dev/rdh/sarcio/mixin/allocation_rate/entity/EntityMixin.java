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

	@Unique private long sarcio$floatBrightnessTick = Long.MIN_VALUE;
	@Unique private float sarcio$floatBrightnessValue;

	@Inject(method = "getBrightness", at = @At("HEAD"), cancellable = true)
	private void sarcio$floatBrightnessCacheHit(float partialTicks, CallbackInfoReturnable<Float> cir) {
		if (this.world != null && this.sarcio$floatBrightnessTick == this.world.getTime()) {
			cir.setReturnValue(this.sarcio$floatBrightnessValue);
		}
	}

	@Inject(method = "getBrightness", at = @At("RETURN"))
	private void sarcio$floatBrightnessCacheStore(float partialTicks, CallbackInfoReturnable<Float> cir) {
		if (this.world != null) {
			this.sarcio$floatBrightnessTick = this.world.getTime();
			this.sarcio$floatBrightnessValue = cir.getReturnValue();
		}
	}
}
