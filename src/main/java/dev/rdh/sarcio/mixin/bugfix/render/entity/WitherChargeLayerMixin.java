package dev.rdh.sarcio.mixin.bugfix.render.entity;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.render.entity.layer.WitherChargeLayer;
import net.minecraft.client.render.platform.GlStateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("bugfix.render.wither_charge_depth")
@Mixin(WitherChargeLayer.class)
public class WitherChargeLayerMixin {
    @Inject(method = "render(Lnet/minecraft/entity/living/mob/monster/boss/WitherEntity;FFFFFFF)V", at = @At("RETURN"))
    private void sarcio$fixDepth(CallbackInfo ci) {
        GlStateManager.depthMask(true);
    }
}
