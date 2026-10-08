package dev.rdh.sarcio.mixin.bugfix.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Entity.class)
public class EntityLightLevelMixin {
    @ModifyExpressionValue(method = "getLightLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;isChunkLoaded(Lnet/minecraft/util/math/BlockPos;)Z"))
    private boolean sarcio$fixOobEntityBrightness(boolean original) {
        return true;
    }
}
