package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RendererLivingEntity.class)
public class RendererLivingEntityMixin<T extends EntityLivingBase> {
    @Inject(method = "doRender(Lnet/minecraft/entity/EntityLivingBase;DDDFF)V", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/EntityLivingBase;prevRotationPitch:F", ordinal = 0, opcode = Opcodes.GETFIELD))
    private void tf$fixHeadRotation(T entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci, @Local(ordinal = 2) float f, @Local(ordinal = 3) float g, @Local(ordinal = 4) LocalFloatRef h) {
        if (entity.isRiding() && entity.ridingEntity instanceof EntityLivingBase) {
            h.set(g - f);
        }
    }
}
