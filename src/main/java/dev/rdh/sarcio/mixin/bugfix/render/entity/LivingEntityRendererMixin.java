package dev.rdh.sarcio.mixin.bugfix.render.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.living.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @ModifyVariable(method = "render(Lnet/minecraft/entity/living/LivingEntity;DDDFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/living/LivingEntity;lastPitch:F", ordinal = 0, opcode = Opcodes.GETFIELD), ordinal = 4)
    private float sarcio$fixHeadRotation(float h, @Local(argsOnly = true) T entity, @Local(ordinal = 2) float f, @Local(ordinal = 3) float g) {
        return entity.isRiding() && entity.vehicle instanceof LivingEntity ? g - f : h;
    }
}
