package dev.rdh.sarcio.mixin.opt.textures;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(targets = "net.minecraft.client.render.texture.HttpTexture$34366662")
abstract class HttpTextureDownloaderMixin {
	@WrapOperation(method = "run", at = @At(value = "INVOKE", target = "Ljava/net/URL;openConnection(Ljava/net/Proxy;)Ljava/net/URLConnection;"))
	private URLConnection addTimeouts(URL url, Proxy proxy, Operation<URLConnection> original) {
		URLConnection connection = original.call(url, proxy);
		connection.setConnectTimeout(10_000);
		connection.setReadTimeout(10_000);
		return connection;
	}
}
