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
	@Shadow public World worldObj;

	@Unique private long sarcio$brightnessTick = Long.MIN_VALUE;
	@Unique private int sarcio$brightnessValue;

	@Inject(method = "getBrightnessForRender", at = @At("HEAD"), cancellable = true)
	private void sarcio$brightnessCacheHit(float partialTicks, CallbackInfoReturnable<Integer> cir) {
		if (this.worldObj != null && this.sarcio$brightnessTick == this.worldObj.getTotalWorldTime()) {
			cir.setReturnValue(this.sarcio$brightnessValue);
		}
	}

	@Inject(method = "getBrightnessForRender", at = @At("RETURN"))
	private void sarcio$brightnessCacheStore(float partialTicks, CallbackInfoReturnable<Integer> cir) {
		if (this.worldObj != null) {
			this.sarcio$brightnessTick = this.worldObj.getTotalWorldTime();
			this.sarcio$brightnessValue = cir.getReturnValue();
		}
	}
}
