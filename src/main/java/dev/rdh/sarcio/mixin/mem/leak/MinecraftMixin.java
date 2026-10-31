package dev.rdh.sarcio.mixin.mem.leak;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("mem.leak.map_renderer_textures")
@Mixin(Minecraft.class)
abstract class MinecraftMixin {
	@Shadow public ClientWorld world;
	@Shadow public GameRenderer gameRenderer;

	@Inject(method = "setWorld(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V", at = @At("HEAD"))
	private void clearMapRenderers(ClientWorld world, String message, CallbackInfo ci) {
		if (world != this.world && this.gameRenderer != null) {
			this.gameRenderer.getMapRenderer().clearStateTextures();
		}
	}
}
