package dev.rdh.sarcio.mixin.bugfix.render.entity;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.client.render.entity.layer.ItemInHandLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Feature("bugfix.render.child_held_item")
@Mixin(ItemInHandLayer.class)
public class ItemInHandLayerMixin {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/GlStateManager;translatef(FFF)V", ordinal = 0), index = 1)
    private float sarcio$fixChildItemOffset(float y) {
        return 0.75F;
    }

    @WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/platform/GlStateManager;rotatef(FFFF)V", ordinal = 0))
    private boolean sarcio$removeChildItemRotation(float angle, float x, float y, float z) {
        return false;
    }
}
