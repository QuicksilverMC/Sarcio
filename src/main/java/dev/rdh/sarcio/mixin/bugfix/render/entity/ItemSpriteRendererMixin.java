package dev.rdh.sarcio.mixin.bugfix.render.entity;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.render.entity.ItemSpriteRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("bugfix.render.delay_close_projectiles")
@Mixin(ItemSpriteRenderer.class)
public class ItemSpriteRendererMixin<T extends Entity> {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    private void sarcio$delayProjectileRendering(T entity, double dx, double dy, double dz, float yaw, float tickDelta, CallbackInfo ci) {
        if (entity.ticks < 2 && dx * dx + dy * dy + dz * dz < 12.25) {
            ci.cancel();
        }
    }
}
