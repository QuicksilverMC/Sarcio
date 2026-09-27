package dev.rdh.sarcio.mixin.bugfix.render.entity;

import net.minecraft.client.render.entity.ItemSpriteRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemSpriteRenderer.class)
public class ItemSpriteRendererMixin<T extends Entity> {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void sarcio$delayProjectileRendering(T entity, double dx, double dy, double dz, float yaw, float tickDelta, CallbackInfo ci) {
        if (entity.ticks < 2) {
            ci.cancel();
        }
    }
}
