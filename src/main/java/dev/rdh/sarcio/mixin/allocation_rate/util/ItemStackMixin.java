package dev.rdh.sarcio.mixin.allocation_rate.util;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
abstract class ItemStackMixin {
	@Unique private String sarcio$displayName;

	@WrapMethod(method = "getHoverName")
	private String sarcio$cacheDisplayName(Operation<String> original) {
		String name = this.sarcio$displayName;
		if (name == null) {
			this.sarcio$displayName = name = original.call();
		}
		return name;
	}

	@SuppressWarnings("InvalidInjectorMethodSignature") // this works because mixin will pass null as ci
	@Inject(method = {
			"setHoverName", "resetHoverName", "setNbt", "addToNbt", "setDamage"
	}, at = @At("HEAD"))
	private void sarcio$dropName(CallbackInfo ci) {
		this.sarcio$displayName = null;
	}
}
