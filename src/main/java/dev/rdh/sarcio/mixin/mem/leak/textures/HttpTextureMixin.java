package dev.rdh.sarcio.mixin.mem.leak.textures;

import dev.rdh.sarcio.Feature;
import java.awt.image.BufferedImage;
import net.minecraft.client.render.texture.HttpImageProcessor;
import net.minecraft.client.render.texture.HttpTexture;
import net.minecraft.client.resource.manager.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HttpTexture.class)
abstract class HttpTextureMixin {
	@Shadow @Final @Mutable private HttpImageProcessor processor;
	@Shadow private BufferedImage image;
	@Shadow private Thread downloader;
	@Shadow private boolean uploaded;

	@Feature("mem.leak.texture_download_callback")
	@Inject(method = "setImage", at = @At("TAIL"))
	private void releaseCompletedCallback(CallbackInfo ci) {
		this.processor = null;
	}

	@Feature("mem.leak.uploaded_texture_image")
	@Inject(method = "upload", at = @At("RETURN"))
	private void releaseUploadedImage(CallbackInfo ci) {
		if (this.uploaded) {
			this.image = null;
			this.downloader = null;
		}
	}

	@Feature("mem.leak.uploaded_texture_image")
	@Inject(method = "load", at = @At("HEAD"), cancellable = true)
	private void keepUploadedTexture(ResourceManager resourceManager, CallbackInfo ci) {
		if (this.uploaded && this.image == null) {
			ci.cancel();
		}
	}
}
