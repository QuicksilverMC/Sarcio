package dev.rdh.sarcio.mixin.textures;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import java.awt.image.BufferedImage;
import net.minecraft.client.render.texture.TextureUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TextureUtil.class)
abstract class TextureUtilMixin {
	@ModifyExpressionValue(method = "upload(Ljava/awt/image/BufferedImage;IIZZ)V", at = @At(value = "CONSTANT", args = "intValue=4194304"))
	private static int rightSizeUploadBuffer(int maximumPixels, @Local(argsOnly = true) BufferedImage image) {
		return (int) Math.min(maximumPixels, (long) image.getWidth() * image.getHeight());
	}
}
