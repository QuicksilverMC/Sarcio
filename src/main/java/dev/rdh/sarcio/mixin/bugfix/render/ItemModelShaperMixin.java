package dev.rdh.sarcio.mixin.bugfix.render;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.item.ItemModelShaper;
import net.minecraft.client.render.model.block.BakedModel;
import net.minecraft.client.resource.model.ModelManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemModelShaper.class)
public class ItemModelShaperMixin {
    @Shadow
    @Final
    private ModelManager modelManager;

    @Inject(method = "getModel(Lnet/minecraft/item/ItemStack;)Lnet/minecraft/client/render/model/block/BakedModel;", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), cancellable = true)
    private void sarcio$useMissingModel(ItemStack stack, CallbackInfoReturnable<BakedModel> cir, @Local Item item) {
        if (item == null) {
            cir.setReturnValue(this.modelManager.getMissingModel());
        }
    }
}
