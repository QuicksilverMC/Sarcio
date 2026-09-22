package dev.rdh.sarcio.mixin.bugfix.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityMixin {
    @ModifyExpressionValue(method = "getBrightnessForRender", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isBlockLoaded(Lnet/minecraft/util/BlockPos;)Z"))
    private boolean sarcio$fixOobEntityBrightness(boolean original) {
        return true;
    }

    @WrapWithCondition(method = "spawnRunningParticles", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;createRunningParticles()V"))
    private boolean sarcio$onlyRunningParticlesOnGround(Entity instance) {
        return instance.onGround;
    }
}
