package dev.rdh.sarcio.mixin.textures;

import java.awt.image.BufferedImage;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.render.texture.HttpImageProcessor;
import net.minecraft.client.render.texture.HttpTexture;
import net.minecraft.client.resource.manager.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HttpTexture.class)
abstract class HttpTextureMixin {
	@Shadow @Final @Mutable private HttpImageProcessor processor;
	@Shadow private BufferedImage image;
	@Shadow private Thread downloader;
	@Shadow private boolean uploaded;

	@Unique
	private static final AtomicInteger THREAD_NUMBER = new AtomicInteger();
	@Unique
	private static final Executor EXECUTOR = Executors.newFixedThreadPool(4, task -> {
		Thread thread = new Thread(task, "Skin Downloader #" + THREAD_NUMBER.incrementAndGet());
		thread.setDaemon(true);
		thread.setPriority(Thread.MIN_PRIORITY);
		return thread;
	});

	@Redirect(method = "download", at = @At(value = "INVOKE", target = "Ljava/lang/Thread;start()V"))
	private void useSharedExecutor(Thread download) {
		EXECUTOR.execute(download);
	}

	@Inject(method = "setImage", at = @At("TAIL"))
	private void releaseCompletedCallback(CallbackInfo ci) {
		this.processor = null;
	}

	@Inject(method = "upload", at = @At("RETURN"))
	private void releaseUploadedImage(CallbackInfo ci) {
		if (this.uploaded) {
			this.image = null;
			this.downloader = null;
		}
	}

	@Inject(method = "load", at = @At("HEAD"), cancellable = true)
	private void keepUploadedTexture(ResourceManager resourceManager, CallbackInfo ci) {
		if (this.uploaded && this.image == null) {
			ci.cancel();
		}
	}
}
