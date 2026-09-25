package dev.rdh.sarcio.mixin.allocation_rate.util;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.nbt.NbtString;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NbtString.class)
abstract class NbtStringMixin {
	@Unique private String sarcio$asString;

	@Inject(method = "read", at = @At("HEAD"))
	private void sarcio$dropCache(CallbackInfo ci) {
		this.sarcio$asString = null;
	}

	@WrapMethod(method = "toString")
	public String sarcio$toString(Operation<String> original) {
		if (this.sarcio$asString == null) {
			this.sarcio$asString = original.call();
		}

		return this.sarcio$asString;
	}
}
