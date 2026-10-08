package dev.rdh.sarcio.mixin.bugfix.render;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.texture.Stitcher;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Stitcher.class)
public class StitcherMixin {
    @Shadow private int width;
    @Shadow private int height;
    @Shadow @Final private int maxWidth;
    @Shadow @Final private int maxHeight;

    @ModifyExpressionValue(method = "expand", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/render/texture/Stitcher;height:I", ordinal = 5))
    private int sarcio$checkGrowingWidth(int height) {
        return this.width;
    }

    @ModifyExpressionValue(method = "expand", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/render/texture/Stitcher;width:I", ordinal = 5))
    private int sarcio$checkGrowingHeight(int width) {
        return this.height;
    }

    @ModifyExpressionValue(method = "expand", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/render/texture/Stitcher;maxHeight:I", ordinal = 2))
    private int sarcio$checkMaxWidth(int maxHeight) {
        return this.maxWidth;
    }

    @ModifyExpressionValue(method = "expand", at = @At(value = "FIELD", opcode = Opcodes.GETFIELD, target = "Lnet/minecraft/client/render/texture/Stitcher;maxWidth:I", ordinal = 2))
    private int sarcio$checkMaxHeight(int maxWidth) {
        return this.maxHeight;
    }
}
