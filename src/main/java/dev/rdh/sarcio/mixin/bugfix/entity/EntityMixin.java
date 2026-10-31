package dev.rdh.sarcio.mixin.bugfix.entity;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Feature("bugfix.entity.ground_sprint_particles")
@Mixin(Entity.class)
public class EntityMixin {
    @WrapWithCondition(method = "tickSprintingEffect", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;doSprintingEffect()V"))
    private boolean sarcio$onlyRunningParticlesOnGround(Entity instance) {
        return instance.onGround;
    }
}
