package dev.rdh.sarcio.mixin.bugfix;

import net.minecraft.client.gui.GuiScreenResourcePacks;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiScreenResourcePacks.class)
public class GuiScreenResourcePacksMixin {
    @ModifyExpressionValue(method = "drawScreen", at = @At(value = "CONSTANT", args = "intValue=77"))
    private int sarcio$moveFolderInfo(int original) {
        return 102;
    }
}
