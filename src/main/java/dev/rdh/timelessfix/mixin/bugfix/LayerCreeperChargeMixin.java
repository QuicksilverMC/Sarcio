package dev.rdh.timelessfix.mixin.bugfix;

import net.minecraft.client.renderer.entity.layers.LayerCreeperCharge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LayerCreeperCharge.class)
public class LayerCreeperChargeMixin {
    @ModifyArg(method = "doRenderLayer(Lnet/minecraft/entity/monster/EntityCreeper;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/GlStateManager;depthMask(Z)V", ordinal = 1))
    private boolean tf$fixDepth(boolean flagIn) {
        return true;
    }
}
