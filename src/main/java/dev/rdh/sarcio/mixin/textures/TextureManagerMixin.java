package dev.rdh.sarcio.mixin.textures;

import java.util.Map;
import net.minecraft.client.render.texture.Texture;
import net.minecraft.client.render.texture.TextureManager;
import net.minecraft.resource.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextureManager.class)
abstract class TextureManagerMixin {
	@Shadow @Final private Map<Identifier, Texture> textures;

	@Inject(method = "close", at = @At("TAIL"))
	private void removeDeletedTexture(Identifier location, CallbackInfo ci) {
		this.textures.remove(location);
	}
}
