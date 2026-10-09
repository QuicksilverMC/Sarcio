package dev.rdh.sarcio.mixin.mem.alloc.render;

import java.util.Locale;
import net.minecraft.client.render.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TextRenderer.class)
abstract class TextRendererMixin {
	@Redirect(
		method = "drawLayer(Ljava/lang/String;Z)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/String;toLowerCase(Ljava/util/Locale;)Ljava/lang/String;")
	)
	private String skipWholeStringLowercase(String text, Locale locale) {
		return text;
	}

	@ModifyArg(
		method = "drawLayer(Ljava/lang/String;Z)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/String;indexOf(I)I", ordinal = 0)
	)
	private int findFormattingCode(int code) {
		return Character.toLowerCase((char)code);
	}
}
