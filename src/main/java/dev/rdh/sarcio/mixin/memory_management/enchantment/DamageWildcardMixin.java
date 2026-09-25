package dev.rdh.sarcio.mixin.memory_management.enchantment;

import dev.rdh.sarcio.util.EnchantmentReferenceCleaner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.enchantment.EnchantmentHelper$DamageWildcard")
abstract class DamageWildcardMixin implements EnchantmentReferenceCleaner.Clearable {
	@Shadow public LivingEntity attacker;
	@Shadow public Entity target;

	@Inject(method = "<init>", at = @At("RETURN"))
	private void register(CallbackInfo ci) {
		EnchantmentReferenceCleaner.registerDamageIterator(this);
	}

	@Override
	public void clearReferences() {
		this.attacker = null;
		this.target = null;
	}
}
