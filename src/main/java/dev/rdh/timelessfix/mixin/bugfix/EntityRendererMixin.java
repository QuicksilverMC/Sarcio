package dev.rdh.timelessfix.mixin.bugfix;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumWorldBlockLayer;

import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @WrapOperation(method = "renderWorldPass", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/RenderGlobal;renderBlockLayer(Lnet/minecraft/util/EnumWorldBlockLayer;DILnet/minecraft/entity/Entity;)I", ordinal = 3))
    private int tf$offsetTranslucents(RenderGlobal instance, EnumWorldBlockLayer blockLayerIn, double partialTicks, int pass, Entity entityIn, Operation<Integer> original) {
        GlStateManager.doPolygonOffset(-1.0F, -1.0F);
        GlStateManager.enablePolygonOffset();
        int ret = original.call(instance, blockLayerIn, partialTicks, pass, entityIn);
        GlStateManager.disablePolygonOffset();
        return ret;
    }
}
