package dev.rdh.sarcio.mixin.bugfix.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.item.EntityXPOrb;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityXPOrb.class)
public class EntityXPOrbMixin {
    @ModifyExpressionValue(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/EntityPlayer;getEyeHeight()F"))
    private float sarcio$lowerOrbTarget(float original) {
        return original / 2.0F;
    }
}
