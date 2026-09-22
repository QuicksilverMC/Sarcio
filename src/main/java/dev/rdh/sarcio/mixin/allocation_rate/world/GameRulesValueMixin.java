package dev.rdh.sarcio.mixin.allocation_rate.world;

import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.GameRules$Value")
abstract class GameRulesValueMixin {
	@Shadow private String valueString;

	@Inject(method = "setValue(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
	private void sarcio$skipUnchangedWrite(String value, CallbackInfo ci) {
		if (Objects.equals(this.valueString, value)) {
			ci.cancel();
		}
	}
}
