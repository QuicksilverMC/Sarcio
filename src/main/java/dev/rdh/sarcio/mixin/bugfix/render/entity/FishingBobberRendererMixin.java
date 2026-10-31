package dev.rdh.sarcio.mixin.bugfix.render.entity;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.render.entity.FishingBobberRenderer;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.entity.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberRenderer.class)
public class FishingBobberRendererMixin {
    @Feature("bugfix.render.delay_close_projectiles")
    @Inject(method = "render(Lnet/minecraft/entity/FishingBobberEntity;DDDFF)V", at = @At("HEAD"), cancellable = true)
    private void sarcio$delayBobberRendering(FishingBobberEntity entity, double dx, double dy, double dz, float yaw, float tickDelta, CallbackInfo ci) {
        if (entity.ticks < 2 && dx * dx + dy * dy + dz * dz < 12.25) {
            ci.cancel();
        }
    }

    @Feature("bugfix.render.projectile_alpha")
    @Inject(method = "render(Lnet/minecraft/entity/FishingBobberEntity;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/GlStateManager;translatef(FFF)V"))
    private void sarcio$restoreAlpha(FishingBobberEntity entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        GlStateManager.enableAlphaTest();
    }
}
