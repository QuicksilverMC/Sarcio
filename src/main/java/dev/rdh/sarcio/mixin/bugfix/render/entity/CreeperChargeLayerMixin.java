package dev.rdh.sarcio.mixin.bugfix.render.entity;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.render.entity.layer.CreeperChargeLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Feature("bugfix.render.creeper_charge_depth")
@Mixin(CreeperChargeLayer.class)
public class CreeperChargeLayerMixin {
    @ModifyArg(method = "render(Lnet/minecraft/entity/living/mob/monster/CreeperEntity;FFFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/GlStateManager;depthMask(Z)V", ordinal = 1))
    private boolean sarcio$fixDepth(boolean flagIn) {
        return true;
    }
}
