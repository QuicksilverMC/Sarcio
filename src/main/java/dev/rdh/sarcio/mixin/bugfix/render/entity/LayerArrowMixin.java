package dev.rdh.sarcio.mixin.bugfix.render.entity;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.renderer.entity.layers.LayerArrow;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LayerArrow.class)
public class LayerArrowMixin {
    @WrapWithCondition(method = "doRenderLayer", at = {@At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderHelper;disableStandardItemLighting()V"), @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderHelper;enableStandardItemLighting()V")})
    private boolean tf$fixArrowLighting() {
        return false;
    }
}
