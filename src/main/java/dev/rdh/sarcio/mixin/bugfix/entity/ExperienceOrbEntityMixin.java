package dev.rdh.sarcio.mixin.bugfix.entity;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.ExperienceOrbEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Feature("bugfix.entity.orb_target_height")
@Mixin(ExperienceOrbEntity.class)
public class ExperienceOrbEntityMixin {
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/living/player/PlayerEntity;getEyeHeight()F"))
    private float sarcio$lowerOrbTarget(float original) {
        return original / 2.0F;
    }
}
