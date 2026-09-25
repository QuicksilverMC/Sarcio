package dev.rdh.sarcio.mixin.bugfix.render;

import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.render.model.block.BakedModel;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.client.resource.model.ModelTransformation;
import net.minecraft.client.resource.model.ModelTransformations;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class RenderItemCullFaceMixin {
    @Shadow
    private boolean shouldCullFrontFace(ModelTransformation itemTranformVec) {
        throw new AssertionError();
    }

    @Inject(method = "renderItemInHand(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/BakedModel;Lnet/minecraft/client/resource/model/ModelTransformations$Type;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemRenderer;renderItem(Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/render/model/block/BakedModel;)V"))
    private void sarcio$cullFrontFaces(ItemStack stack, BakedModel model, ModelTransformations.Type cameraTransformType, CallbackInfo ci) {
        if (this.shouldCullFrontFace(model.getTransformations().get(cameraTransformType))) {
            GlStateManager.cullFace(GL11.GL_FRONT);
        }
    }
}
