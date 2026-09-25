package dev.rdh.sarcio.mixin.allocation_rate.render;

import java.util.Locale;
import net.minecraft.client.render.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TextRenderer.class)
abstract class TextRendererMixin {
	@Redirect(
		method = "drawLayer(Ljava/lang/String;Z)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/String;toLowerCase(Ljava/util/Locale;)Ljava/lang/String;")
	)
	private String skipWholeStringLowercase(String text, Locale locale) {
		return text;
	}

	@Redirect(
		method = "drawLayer(Ljava/lang/String;Z)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I")
	)
	private int findFormattingCode(String codes, int code) {
		return codes.indexOf(Character.toLowerCase((char)code));
	}
}
