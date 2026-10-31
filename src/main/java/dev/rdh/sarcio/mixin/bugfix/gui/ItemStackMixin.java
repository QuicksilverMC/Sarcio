package dev.rdh.sarcio.mixin.bugfix.gui;

import dev.rdh.sarcio.Feature;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Feature("bugfix.gui.hex_tooltips")
@Mixin(ItemStack.class)
public class ItemStackMixin {
    @WrapOperation(method = "getTooltip", at = @At(value = "INVOKE", target = "Ljava/lang/Integer;toHexString(I)Ljava/lang/String;"))
    private String sarcio$formatHexTooltips(int i, Operation<String> original) {
        return String.format("%06X", i);
    }
}
