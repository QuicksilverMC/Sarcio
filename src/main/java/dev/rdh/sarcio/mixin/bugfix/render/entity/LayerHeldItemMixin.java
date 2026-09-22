package dev.rdh.sarcio.mixin.bugfix.render.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LayerHeldItem.class)
public class LayerHeldItemMixin {
    @ModifyArg(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;translate(FFF)V", ordinal = 0), index = 1)
    private float sarcio$fixChildItemOffset(float y) {
        return 0.75F;
    }

    @WrapWithCondition(method = "doRenderLayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;rotate(FFFF)V", ordinal = 0))
    private boolean sarcio$removeChildItemRotation(float angle, float x, float y, float z) {
        return false;
    }
}
