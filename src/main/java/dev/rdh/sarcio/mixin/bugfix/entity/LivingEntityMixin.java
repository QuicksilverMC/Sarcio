package dev.rdh.sarcio.mixin.bugfix.entity;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.effect.StatusEffectInstance;
import com.llamalad7.mixinextras.sugar.Local;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

	private LivingEntityMixin() { super(null); }

	@Feature("bugfix.entity.mouse_delay_fix")
	@ModifyExpressionValue(method = "getRotationVector(F)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/living/LivingEntity;lastHeadYaw:F"))
	private float sarcio$usePrevYaw(float original) {
		return this.lastYaw;
	}

	@Feature("bugfix.entity.mouse_delay_fix")
	@ModifyExpressionValue(method = "getRotationVector(F)Lnet/minecraft/util/math/Vec3d;", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/living/LivingEntity;headYaw:F"))
	private float sarcio$useYaw(float original) {
		return this.yaw;
	}

	@Feature("bugfix.entity.missing_status_effect")
	@Inject(method = "tickStatusEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/effect/StatusEffectInstance;tick(Lnet/minecraft/entity/living/LivingEntity;)Z"), cancellable = true)
	private void sarcio$skipMissingEffect(CallbackInfo ci, @Local StatusEffectInstance effect) {
		if (effect == null) {
			ci.cancel();
		}
	}
}
