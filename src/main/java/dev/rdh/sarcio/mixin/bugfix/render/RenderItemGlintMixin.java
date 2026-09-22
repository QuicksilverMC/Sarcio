package dev.rdh.sarcio.mixin.bugfix.render;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderItem.class)
public class RenderItemGlintMixin {
    @Inject(method = "renderItemAndEffectIntoGUI", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/RenderItem;renderItemIntoGUI(Lnet/minecraft/item/ItemStack;II)V"))
    private void sarcio$sizeGlintToItem(ItemStack stack, int xPosition, int yPosition, CallbackInfo ci) {
        GlStateManager.enableDepth();
    }
}
