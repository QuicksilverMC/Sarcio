package dev.rdh.sarcio.mixin.bugfix.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.world.WorldRenderer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @ModifyExpressionValue(method = "setupRender", at = @At(value = "FIELD", target = "Lnet/minecraft/client/render/world/RenderChunk;bounds:Lnet/minecraft/util/math/Box;", opcode = Opcodes.GETFIELD, ordinal = 0))
    private Box sarcio$renderEntitiesOutsideWorld(Box original, @Local(ordinal = 0) BlockPos blockPos) {
        return original.expanded(0.0D, blockPos.getY() > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY, 0.0D);
    }
}
