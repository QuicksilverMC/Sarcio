package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.BlockFluidRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockFluidRenderer.class)
public class BlockFluidRendererMixin {
    @ModifyExpressionValue(method = "renderFluid", at = @At(value = "CONSTANT", args = "floatValue=0.001F"))
    private float tf$fixFluidGap(float original) {
        return 0.0F;
    }
}
