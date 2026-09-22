package dev.rdh.sarcio.mixin.allocation_rate.util;

import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
	@Unique private String sarcio$displayName;

	@Inject(method = "getDisplayName", at = @At("HEAD"), cancellable = true)
	private void sarcio$displayNameCacheHit(CallbackInfoReturnable<String> cir) {
		if (this.sarcio$displayName != null) {
			cir.setReturnValue(this.sarcio$displayName);
		}
	}

	@Inject(method = "getDisplayName", at = @At("RETURN"))
	private void sarcio$displayNameCacheStore(CallbackInfoReturnable<String> cir) {
		this.sarcio$displayName = cir.getReturnValue();
	}

	@SuppressWarnings("InvalidInjectorMethodSignature") // this works because mixin will pass null as ci
	@Inject(method = {
			"setStackDisplayName", "clearCustomName", "setTagCompound", "setTagInfo", "setItemDamage"
	}, at = @At("HEAD"))
	private void sarcio$dropName(CallbackInfo ci) {
		this.sarcio$displayName = null;
	}
}
