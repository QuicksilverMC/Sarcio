package dev.rdh.sarcio.mixin.memory_management;

import dev.rdh.sarcio.util.ClassInfoManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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

	@Inject(method = "init", at = @At("RETURN"))
	private void clearMixinMetadata(CallbackInfo ci) {
		ClassInfoManager.clear();
	}

	@Redirect(
		method = "setWorld(Lnet/minecraft/client/world/ClientWorld;Ljava/lang/String;)V",
		at = @At(value = "INVOKE", target = "Ljava/lang/System;gc()V")
	)
	private void skipWorldTransitionGc() {
	}
}
