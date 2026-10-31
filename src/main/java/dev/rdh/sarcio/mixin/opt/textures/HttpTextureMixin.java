package dev.rdh.sarcio.mixin.opt.textures;

import dev.rdh.sarcio.Feature;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import net.minecraft.client.render.texture.HttpTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Feature("opt.textures.shared_download_executor")
@Mixin(HttpTexture.class)
abstract class HttpTextureMixin {
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
}
