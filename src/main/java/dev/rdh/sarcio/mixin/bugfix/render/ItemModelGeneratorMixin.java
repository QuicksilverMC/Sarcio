package dev.rdh.sarcio.mixin.bugfix.render;

import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.model.block.ItemModelGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

/**
 * Fixes holes in item models disabling pixels in textures that contain <=0.1F transparency like OptiFine does.
 */
@Mixin(ItemModelGenerator.class)
public class ItemModelGeneratorMixin {
    @Expression("@(?) == 0")
    @ModifyExpressionValue(method = "isTransparent", at = @At("MIXINEXTRAS:EXPRESSION"))
    private int sarcio$adjustTransparencyAllowed(final int original) {
        return original <= 25 ? 0 : original;
    }
}
