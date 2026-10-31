package dev.rdh.sarcio.mixin.mem.alloc.world;

import dev.rdh.sarcio.Feature;
import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("mem.alloc.game_rule_unchanged_write")
@Mixin(targets = "net.minecraft.world.GameRules$Value")
abstract class GameRulesValueMixin {
	@Shadow private String value;

	@Inject(method = "set(Ljava/lang/String;)V", at = @At("HEAD"), cancellable = true)
	private void sarcio$skipUnchangedWrite(String value, CallbackInfo ci) {
		if (Objects.equals(this.value, value)) {
			ci.cancel();
		}
	}
}
