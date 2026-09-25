package dev.rdh.sarcio.mixin.core;

import net.minecraft.client.TickTimer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TickTimer.class)
abstract class TickTimerMixin {
	@Shadow private double tickTimeCorrection;
	@Shadow private long cumTickTime;

	@Inject(method = "advance", at = @At("HEAD"))
	private void disableClockSynchronization(CallbackInfo ci) {
		this.tickTimeCorrection = 1.0;
		this.cumTickTime = 0L;
	}

	@ModifyVariable(method = "advance", at = @At(value = "STORE", ordinal = 0), ordinal = 0)
	private double useNanosecondClock(double seconds) {
		return System.nanoTime() / 1.0E9;
	}
}
