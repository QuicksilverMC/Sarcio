package dev.rdh.sarcio.mixin.allocation_rate.render;

import net.minecraft.client.render.Culler;
import net.minecraft.client.render.FrustumCuller;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
abstract class EntityRendererMixin {
	@Inject(method = "shouldRender", at = @At(value = "NEW", target = "(DDDDDD)Lnet/minecraft/util/math/Box;"), cancellable = true)
	private void a(Entity entity, Culler camera, double cameraX, double cameraY, double cameraZ, CallbackInfoReturnable<Boolean> cir) {
		if(camera instanceof FrustumCuller f) {
			cir.setReturnValue(
					entity.shouldRender(cameraX, cameraY, cameraZ)
							&& (entity.ignoreCameraFrustum
							|| f.isVisible(
							entity.x - 2.0,
							entity.y - 2.0,
							entity.z - 2.0,
							entity.x + 2.0,
							entity.y + 2.0,
							entity.z + 2.0
					))
			);
		}
	}
}
