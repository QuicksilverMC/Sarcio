package dev.rdh.sarcio.mixin.bugfix.render;

import dev.rdh.sarcio.Feature;
import net.minecraft.client.render.entity.ItemRenderer;
import net.minecraft.client.render.platform.GlStateManager;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Feature("bugfix.render.gui_item_glint")
@Mixin(ItemRenderer.class)
public class RenderItemGlintMixin {
    @Inject(method = "renderGuiItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/ItemRenderer;renderGuiItemModel(Lnet/minecraft/item/ItemStack;II)V"))
    private void sarcio$sizeGlintToItem(ItemStack stack, int xPosition, int yPosition, CallbackInfo ci) {
        GlStateManager.enableDepthTest();
    }
}
