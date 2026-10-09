package dev.rdh.sarcio.mixin.mem.leak.enchantment;

import dev.rdh.sarcio.util.EnchantmentReferenceCleaner;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.enchantment.EnchantmentHelper$ProtectionModifier")
abstract class ProtectionModifierMixin implements EnchantmentReferenceCleaner.Clearable {
	@Shadow public DamageSource source;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void register(CallbackInfo ci) {
		EnchantmentReferenceCleaner.registerProtectionModifier(this);
	}

	@Override
	public void clearReferences() {
		this.source = null;
	}
}
