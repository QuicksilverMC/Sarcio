package dev.rdh.sarcio.mixin.bugfix.render.entity;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.entity.living.LivingEntity;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity> {
    @Inject(method = "render(Lnet/minecraft/entity/living/LivingEntity;DDDFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/living/LivingEntity;lastPitch:F", ordinal = 0, opcode = Opcodes.GETFIELD))
    private void sarcio$fixHeadRotation(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci, @Local(ordinal = 2) float f, @Local(ordinal = 3) float g, @Local(ordinal = 4) LocalFloatRef h) {
        if (entity.isRiding() && entity.vehicle instanceof LivingEntity) {
            h.set(g - f);
        }
    }
}
