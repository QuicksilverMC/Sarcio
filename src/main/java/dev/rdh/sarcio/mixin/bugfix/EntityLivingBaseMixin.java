package dev.rdh.sarcio.mixin.bugfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;

import com.llamalad7.mixinextras.sugar.Local;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {

	private EntityLivingBaseMixin() { super(null); }

	@ModifyExpressionValue(method = "getLook", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/EntityLivingBase;prevRotationYawHead:F"))
	private float sarcio$usePrevYaw(float original) {
		return this.prevRotationYaw;
	}

	@ModifyExpressionValue(method = "getLook", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/entity/EntityLivingBase;rotationYawHead:F"))
	private float sarcio$useYaw(float original) {
		return this.rotationYaw;
	}

	@Inject(method = "updatePotionEffects", at = @At(value = "INVOKE", target = "Lnet/minecraft/potion/PotionEffect;onUpdate(Lnet/minecraft/entity/EntityLivingBase;)Z"), cancellable = true)
	private void sarcio$skipMissingEffect(CallbackInfo ci, @Local PotionEffect effect) {
		if (effect == null) {
			ci.cancel();
		}
	}
}
