package dev.rdh.sarcio.mixin.memory_management.enchantment;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import dev.rdh.sarcio.util.EnchantmentReferenceCleaner;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EnchantmentHelper.class)
abstract class EnchantmentHelperMixin {
	@WrapMethod(method = "applyProtectionWildcard")
	private static void clearHurtIterator(LivingEntity user, Entity attacker, Operation<Void> original) {
		try {
			original.call(user, attacker);
		} finally {
			EnchantmentReferenceCleaner.clearHurtIterator();
		}
	}

	@WrapMethod(method = "applyDamageWildcard")
	private static void clearDamageIterator(LivingEntity user, Entity target, Operation<Void> original) {
		try {
			original.call(user, target);
		} finally {
			EnchantmentReferenceCleaner.clearDamageIterator();
		}
	}
}
