package dev.rdh.sarcio.mixin.allocation_rate.entity;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityOtherPlayerMP.class)
abstract class EntityOtherPlayerMPMixin {
	// the server already sends this stuff for remote players
	@Inject(method = "onLivingUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/entity/EntityOtherPlayerMP;updateArmSwingProgress()V", shift = At.Shift.AFTER), cancellable = true)
	private void sarcio$skipRemoteAnimations(CallbackInfo ci) {
		ci.cancel();
	}
}
