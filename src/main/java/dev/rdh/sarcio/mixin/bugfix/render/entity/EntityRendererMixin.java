package dev.rdh.sarcio.mixin.bugfix.render.entity;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("bugfix.render.fire_blending")
@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @Inject(method = "renderOnFire", at = @At("HEAD"))
    private void sarcio$restoreBlending(Entity entity, double x, double y, double z, float partialTicks, CallbackInfo ci) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        GlStateManager.enableAlphaTest();
    }

    @Inject(method = "renderOnFire", at = @At("RETURN"))
    private void sarcio$resetBlending(CallbackInfo ci) {
        GlStateManager.disableBlend();
    }
}
