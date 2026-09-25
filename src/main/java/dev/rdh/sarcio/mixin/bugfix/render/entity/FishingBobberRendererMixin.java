package dev.rdh.sarcio.mixin.bugfix.render.entity;

import net.minecraft.client.render.entity.FishingBobberRenderer;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.entity.FishingBobberEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FishingBobberRenderer.class)
public class FishingBobberRendererMixin {
    @Inject(method = "render(Lnet/minecraft/entity/FishingBobberEntity;DDDFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/GlStateManager;translatef(FFF)V"))
    private void sarcio$restoreAlpha(FishingBobberEntity entity, double x, double y, double z, float entityYaw, float partialTicks, CallbackInfo ci) {
        GlStateManager.enableAlphaTest();
    }
}
